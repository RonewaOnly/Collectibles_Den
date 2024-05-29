package com.example.collectibles_den.Logic

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.collectibles_den.Data.MakeCollection
import com.example.collectibles_den.Data.NoteData
import com.example.collectibles_den.Data.Storyboard_Stories
import com.example.collectibles_den.Data.UserData
import com.example.collectibles_den.collectiblesDenApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class DatabaseViewModel(private val context: Context) : ViewModel() {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    private var _userID = MutableLiveData<String?>()
    val userID: LiveData<String?>
        get() = _userID

    init {
        // Load the userID from SharedPreferences when the ViewModel is created
        _userID.value = sharedPreferences.getString("user_id", null)
    }

    interface LoginValidationCallback {
        fun onUserFound()
        fun onUserNotFound()
        fun onError(error: DatabaseError)
    }

    fun loginValidation(userEmail: String, userPassword: String, callback: LoginValidationCallback) {
        database.child("Users").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var isUserFound = false
                for (loginSnapshot in snapshot.children) {
                    val userDataMap = loginSnapshot.value as? Map<*, *>
                    if (userDataMap != null && userDataMap.containsKey("email") && userDataMap.containsKey("password")) {
                        val email = userDataMap["email"] as String
                        val password = userDataMap["password"] as String
                        if (email == userEmail && password == userPassword) {
                            isUserFound = true
                            val id = userDataMap["id"] as String
                            _userID.value = id
                            saveUserID(id)
                            break
                        }
                    }
                }
                if (isUserFound) {
                    callback.onUserFound()
                } else {
                    callback.onUserNotFound()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback.onError(error)
            }
        })
    }

    private fun saveUserID(id: String) {
        viewModelScope.launch {
            sharedPreferences.edit().putString("user_id", id).apply()
            collectiblesDenApp.setUserID(id)
        }
    }


    fun registrationTaker(firstname: String, lastname: String, email: String, password: String) {
        val key = database.child("Users").push().key
        val registerDetails = UserData(key, firstname, lastname, email, email, password)
        key?.let {
            database.child("Users").child(it).setValue(registerDetails)
                .addOnSuccessListener {
                    Log.d("RegistrationActivity", "Registration Successful")
                }.addOnFailureListener { error ->
                    Log.e("RegistrationActivity", "Registration Failed: $error")
                }
        }
    }
    fun getUser(userId: String, onSuccess: (List<UserData>) -> Unit){
        database.child("Users").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = mutableListOf<UserData>()

                for (usersnapshot in snapshot.children){
                    //get the values
                    val loggedValue = usersnapshot.getValue() as? Map<String,Any>
                    if (loggedValue != null && loggedValue["id"]!= null && userId == loggedValue["id"].toString()){
                        val loggedIn = UserData(
                            id = loggedValue["id"] as? String ?: "",
                            firstname = loggedValue["firstname"] as? String ?: "",
                            lastname = loggedValue["lastname"]as? String ?: "",
                            username = loggedValue["username"] as? String ?: "",
                            email = loggedValue["email"] as? String ?: "",
                            password = loggedValue["passsword"] as? String ?: ""
                        )

                        user.add(loggedIn)
                    }
                }
                onSuccess(user)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("getUser", error.details)

            }

        })
    }
    //The function will be getting and setting collections saved by the user..

    @Suppress("UNCHECKED_CAST")
    fun getCollections(userID: String, onResult: (List<MakeCollection>) -> Unit) {
        database.child("Collections").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val collections = mutableListOf<MakeCollection>()

                for (collectionSnapShot in snapshot.children) {
                    // Retrieve the value from the snapshot
                    val dataMap = collectionSnapShot.getValue() as? Map<String, Any>
                    if (dataMap != null && dataMap["userAssigned"] != null && userID == dataMap["userAssigned"].toString()) {
                        // Convert the dataMap to a MakeCollection object
                        val collection = MakeCollection(
                            makeCollectionID = dataMap["makeCollectionID"] as? String ?: "",
                            makeCollectionName = dataMap["makeCollectionName"] as? String ?: "",
                            makeCollectionDescription = dataMap["makeCollectionDescription"] as? String ?: "",
                            makeCollectionCategory = dataMap["makeCollectionCategory"] as? String ?: "",
                            makeCollectionImages = dataMap["makeCollectionImages"] as? List<String> ?: emptyList(),
                            makeCollectionCameraImages = dataMap["makeCollectionCameraImages"] as? List<String> ?: emptyList(),
                            makeCollectionNotes = dataMap["makeCollectionNotes"] as? List<NoteData> ?: emptyList(),
                            makeCollectionScannedItems = dataMap["makeCollectionScannedItems"] as? List<String> ?: emptyList(),
                            makeCollectionFiles = dataMap["makeCollectionFiles"] as? List<String> ?: emptyList(),
                            userAssigned = dataMap["userAssigned"] as? String ?: "",
                            makeCollectionDate = dataMap["makeCollectionDate"] ?: ServerValue.TIMESTAMP
                        )

                        collections.add(collection)
                    }
                }
                onResult(collections)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("getCollection", error.details)
            }
        })
    }



    fun setCollections(
        collectionName: String,
        collectionDesc: String,
        category: String,
        images: List<Uri?> = emptyList(),
        cameraImages: List<Uri?> = emptyList(),
        notes: List<NoteData> = emptyList(),
        scannedItems: List<Uri?> = emptyList(),
        files: List<Uri?> = emptyList(),
        user: String,
        onSaved: (MakeCollection) -> Unit
    ) {
        val collectionID = database.child("Collections").push().key
        val collection = collectionID?.let {
            MakeCollection(
                it,
                collectionName,
                collectionDesc,
                category,
                images.map { it.toString() }, // Convert Uri to String
                cameraImages.map { it.toString() }, // Convert Uri to String
                notes,
                scannedItems.map { it.toString() }, // Convert Uri to String
                files.map { it.toString() }, // Convert Uri to String
                user
            )
        }

        // Add logging here
        Log.d("SetCollections", "Collection: $collection")

        collectionID?.let {
            database.child("Collections").child(it).setValue(collection)
                .addOnSuccessListener {
                    Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                    collection?.let(onSaved)
                    Log.e("MakeCollectionActivity", "We saved")
                }.addOnFailureListener { error ->
                    Log.e("MakeCollectionActivity", "MakeCollectionActivity Failed: $error")
                }
        }
    }

    fun setStoryboard(
        storyboardLine: Storyboard_Stories.StoryboardLine,
        onSave: (Storyboard_Stories.StoryboardLine) -> Unit
    ){
        val storyKey = database.child("Storyboard").push().key

        val storyboard = storyKey?.let {
            storyboardLine.copy(storyID = it)
        }
        storyKey?.let {
            database.child("Storyboard").child(it).setValue(storyboard)
                .addOnSuccessListener {
                    Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                    storyboard?.let(onSave)
                    Log.e("StoryboardActivity", "We saved")
                }.addOnFailureListener { error ->
                    Log.e("StoryboardActivity", "StoryboardActivity Failed: $error")
                }
        }
    }

    fun getStoryboard(userID: String, onResult: (List<Storyboard_Stories.StoryboardLine>) -> Unit ){
        val databaseRef = FirebaseDatabase.getInstance().getReference("Storyboard")
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val storyboards = mutableListOf<Storyboard_Stories.StoryboardLine>()

                //val storyboards = dataSnapshot.children.mapNotNull { it.getValue(Storyboard_Stories.StoryboardLine::class.java) }
                for(storyboardsnapshot in dataSnapshot.children) {
                    // Retrieve the value from the snapshot
                    val dataMap = storyboardsnapshot.getValue() as? Map<String, Any>
                    if (dataMap != null && dataMap["user"] != null && userID == dataMap["user"].toString()) {
                       val storyboard = Storyboard_Stories.StoryboardLine(
                            storyID = dataMap["storyID"] as? String ?: "",
                           storyName = dataMap["storyName"] as? String ?: "",
                           storyItems = dataMap["storyItems"] as? List<MakeCollection> ?: emptyList(),
                           storyCategory = dataMap["storyCategory"] as? String ?: "",
                           storyDescription = dataMap["storyDescription"] as? String ?: "",
                           storyCovers = dataMap["storyCovers"] as? List<String> ?: emptyList(),
                           showGoalDialog = dataMap["showGoalDialog"] as? Boolean ?: false,
                           goalSet = dataMap["goalSet"] as? Int ?: 0,
                           currentProgress = dataMap["currentProgress"] as? Int ?: 0,
                        )
                        storyboards.add(storyboard)
                    }
                }
                onResult(storyboards)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("StoryboardActivity", "Failed to load storyboards: ${databaseError.message}")
            }
        })
    }
    fun updateStoryboard(userId: String, updatedStoryboardLine: Storyboard_Stories.StoryboardLine, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val storyboardId = updatedStoryboardLine.storyID // Assuming storyboard ID is used as the key
        val storyboardRef = database.child("storyboards").child(userId).child(storyboardId)

        storyboardRef.setValue(updatedStoryboardLine)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Unknown error occurred")
            }
    }

    fun deleteStoryboard(userId: String, storyboardId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val storyboardRef = database.child("storyboards").child(userId).child(storyboardId)

        storyboardRef.removeValue()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Unknown error occurred")
            }
    }
}

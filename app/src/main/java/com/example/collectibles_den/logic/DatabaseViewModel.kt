@file:Suppress("UNCHECKED_CAST")

package com.example.collectibles_den.logic

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.collectibles_den.CollectiblesDenApp
import com.example.collectibles_den.data.MakeCollection
import com.example.collectibles_den.data.Storyboard_Stories
import com.example.collectibles_den.data.UserData
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
            CollectiblesDenApp.setUserID(id)
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

    fun getUser(userId: String, onSuccess: (List<UserData>) -> Unit) {
        database.child("Users").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = mutableListOf<UserData>()

                for (usersnapshot in snapshot.children) {
                    val loggedValue = usersnapshot.value as? Map<String, Any>
                    if (loggedValue != null && loggedValue["id"] != null && userId == loggedValue["id"].toString()) {
                        val loggedIn = UserData(
                            id = loggedValue["id"] as? String ?: "",
                            firstname = loggedValue["firstname"] as? String ?: "",
                            lastname = loggedValue["lastname"] as? String ?: "",
                            username = loggedValue["username"] as? String ?: "",
                            email = loggedValue["email"] as? String ?: "",
                            password = loggedValue["password"] as? String ?: ""
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

    // The function will be getting and setting collections saved by the user.
    @Suppress("UNCHECKED_CAST")
    fun getCollections(userID: String, onResult: (List<MakeCollection>) -> Unit) {
        database.child("Collections").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val collections = mutableListOf<MakeCollection>()

                for (collectionSnapshot in snapshot.children) {
                    val dataMap = collectionSnapshot.value as? Map<String, Any>
                    if (dataMap != null && dataMap["userAssigned"] != null && userID == dataMap["userAssigned"].toString()) {
                        val collection = try {
                            MakeCollection(
                                makeCollectionID = dataMap["makeCollectionID"] as? String ?: "",
                                makeCollectionName = dataMap["makeCollectionName"] as? String ?: "",
                                makeCollectionDescription = dataMap["makeCollectionDescription"] as? String ?: "",
                                makeCollectionCover = dataMap["makeCollectionCover"] as? String ?: "",
                                makeCollectionCategory = dataMap["makeCollectionCategory"] as? String ?: "",
                                makeCollectionImages = dataMap["makeCollectionImages"] as? List<String> ?: emptyList(),
                                makeCollectionCameraImages = dataMap["makeCollectionCameraImages"] as? List<String> ?: emptyList(),
                                makeCollectionNotes = dataMap["makeCollectionNotes"] as? List<String> ?: emptyList(),
                                makeCollectionScannedItems = dataMap["makeCollectionScannedItems"] as? List<String> ?: emptyList(),
                                makeCollectionFiles = dataMap["makeCollectionFiles"] as? List<String> ?: emptyList(),
                                userAssigned = dataMap["userAssigned"] as? String ?: "",
                                makeCollectionDate = dataMap["makeCollectionDate"] ?: ServerValue.TIMESTAMP
                            )
                        } catch (e: Exception) {
                            null
                        }
                        if (collection != null) {
                            collections.add(collection)
                        }
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
        cover: String,
        images: List<Uri?> = emptyList(),
        cameraImages: List<Uri?> = emptyList(),
        notes: List<String> = emptyList(),
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
                cover,
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
    ) {
        val storyKey = database.child("Storyboard").push().key
        if (storyKey == null) {
            Log.e("StoryboardActivity", "Failed to generate a story key")
            return
        }
        val storyboard = storyboardLine.copy(storyID = storyKey)
        Log.d("StoryboardActivity", "Generated Storyboard: $storyboard")

        database.child("Storyboard").child(storyKey).setValue(storyboard)
            .addOnSuccessListener {
                Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                storyboard.let(onSave)
                Log.e("StoryboardActivity", "Storyboard saved successfully")
            }.addOnFailureListener { error ->
                Log.e("StoryboardActivity", "Storyboard saving failed: $error")
            }
    }

    fun getStoryboard(userID: String, onResult: (List<Storyboard_Stories.StoryboardLine>) -> Unit) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("Storyboard")
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val storyboards = mutableListOf<Storyboard_Stories.StoryboardLine>()

                for (storyboardSnapshot in dataSnapshot.children) {
                    val dataMap = storyboardSnapshot.value as? Map<String, Any>
                    if (dataMap != null && dataMap["user"] != null && userID == dataMap["user"].toString()) {
                        val storyboard = Storyboard_Stories.StoryboardLine(
                            storyID = dataMap["storyID"] as? String ?: "",
                            storyName = dataMap["storyName"] as? String ?: "",
                            storyItems = (dataMap["storyItems"] as? List<Map<String, Any>>)?.mapNotNull { item ->
                                try {
                                    MakeCollection(
                                        makeCollectionID = item["makeCollectionID"] as? String ?: "",
                                        makeCollectionName = item["makeCollectionName"] as? String ?: "",
                                        makeCollectionDescription = item["makeCollectionDescription"] as? String ?: "",
                                        makeCollectionCategory = item["makeCollectionCategory"] as? String ?: "",
                                        makeCollectionImages = item["makeCollectionImages"] as? List<String> ?: emptyList(),
                                        makeCollectionCameraImages = item["makeCollectionCameraImages"] as? List<String> ?: emptyList(),
                                        makeCollectionNotes = item["makeCollectionNotes"] as? List<String> ?: emptyList(),
                                        makeCollectionScannedItems = item["makeCollectionScannedItems"] as? List<String> ?: emptyList(),
                                        makeCollectionFiles = item["makeCollectionFiles"] as? List<String> ?: emptyList(),
                                        userAssigned = item["userAssigned"] as? String ?: "",
                                        makeCollectionDate = item["makeCollectionDate"] ?: ServerValue.TIMESTAMP
                                    )
                                } catch (e: Exception) {
                                    null
                                }
                            } ?: emptyList(),
                            storyCategory = dataMap["storyCategory"] as? String ?: "",
                            storyDescription = dataMap["storyDescription"] as? String ?: "",
                            storyCovers = dataMap["storyCovers"] as? List<String> ?: emptyList(),
                            showGoalDialog = dataMap["showGoalDialog"] as? Boolean ?: false,
                            goalSet = dataMap["goalSet"] as? String ?: "",
                            currentProgress = dataMap["currentProgress"] as? String ?: ""
                        )
                        Log.d("StoryboardActivity", "Storyboard fetched: $storyboard")
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

    fun updateStoryboard(
        storyboardId: String,
        updatedStoryboardLine: Storyboard_Stories.StoryboardLine,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val storyboardRef = database.child("Storyboard").child(storyboardId)

        storyboardRef.setValue(updatedStoryboardLine)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Unknown error occurred")
            }
    }

    fun deleteStoryboard(storyboardId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val storyboardRef = database.child("Storyboard").child(storyboardId)

        storyboardRef.removeValue()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Unknown error occurred")
            }
    }

    fun updateGoalSet(storyID: String, newGoalSet: Int) {
        database.child(storyID).child("goalSet").setValue(newGoalSet)
            .addOnSuccessListener {
                // Handle success if needed
            }
            .addOnFailureListener { e ->
                // Handle failure if needed
                throw Exception("Error updating goalSet in database: ${e.message}")
            }
    }
}

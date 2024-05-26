package com.example.collectibles_den

import com.example.collectibles_den.Data.UserData
import com.example.collectibles_den.Data.collectionStorage
import com.example.collectibles_den.Pages.getBeginningOfPastMonth
import java.sql.Timestamp

class DefaultValuesClass {
    //Collection default values
    var recentCollection = listOf(
        collectionStorage(
            collectionID = "laskdk",
            collectionName = "Jocelyn Glasses",
            collectionDescription = listOf("Style life through the eyes","sdkajsoas"),
            collectionImage = R.drawable.jocelyn_morales_mv7kokwzimw_unsplash,
            collectionCategory = "Glasses",
            postedDate = Timestamp(System.currentTimeMillis())
        ),
        collectionStorage(
            collectionID = "7uyb7",
            collectionName = "Sincerely",
            collectionDescription = listOf("Wow clear View","sfafoif"),
            collectionImage = R.drawable.sincerely_media_d05w6_7fapm_unsplash,
            collectionCategory = "Glasses",
            postedDate = Timestamp(System.currentTimeMillis())
        ),
        collectionStorage(
            collectionID = "49djd",
            collectionName = "Voj-tech",
            collectionDescription = listOf("View Iphone","afkoaidjiaks"),
            postedDate = Timestamp(System.currentTimeMillis()),
            collectionImage = R.drawable.vojtech_bruzek_j82gxqnwkss_unsplash,
            collectionCategory = "Phone",

            ),
        collectionStorage(
            collectionID = "al820391j",
            collectionName = "Sora Sagano",
            collectionDescription = listOf("Coder's Dream","akfiuoaij"),
            collectionImage = R.drawable.sora_sagano_wfsap6cixuw_unsplash,
            collectionCategory = "Desktop",
            postedDate = Timestamp(System.currentTimeMillis())
        ),
        collectionStorage(
            collectionID = "89yhj hk",
            collectionName = "Gold Table",
            collectionDescription = listOf("Royal table beauty","dtry76875erdfghjkl;ihugyftyugi"),
            collectionImage = R.drawable.raretable,
            collectionCategory = "Rare Table",
            postedDate = getBeginningOfPastMonth()
        ),
        collectionStorage(
            collectionID = "8ytyuij",
            collectionName = "Brown Table",
            collectionDescription = listOf("Calm feeding Zone","l;opiuyfgihoj;jhtr"),
            collectionImage = R.drawable.rare_table_18th_century_oak_dining_table_57_l1,
            collectionCategory = "Rare Table",
            postedDate = getBeginningOfPastMonth()
        ),
        collectionStorage(
            collectionID = "nbgyu876trdf",
            collectionName = "Old Sit  ",
            collectionDescription = listOf("Old feeling ;","retyu"),
            collectionCategory = "Rare Chair".lowercase(),
            collectionImage = R.drawable.rarechair3,
            postedDate = getBeginningOfPastMonth()
        ),
    )

    var userDummy = listOf(
        UserData(
                id = "43djdk_w",
                firstname = "OneKim",
                lastname = "Lim",
                username = "Tom",
                email = "www.TomStream19@gmail.com",
                password = "password"
        )
    )
}
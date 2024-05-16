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
            collectionName = "kajsnldpaks",
            collectionDescription = listOf("kdoaopdkso","sdkajsoas"),
            collectionCategory = "Glasses",
            postedDate = Timestamp(System.currentTimeMillis())
        ),
        collectionStorage(
            collectionID = "7uyb7",
            collectionName = "sduijosp",
            collectionDescription = listOf("skdifsj","sfafoif"),
            postedDate = Timestamp(System.currentTimeMillis())
        ),
        collectionStorage(
            collectionID = "49djd",
            collectionName = "afoiajp-kdf",
            collectionDescription = listOf("adfkaoisfj","afkoaidjiaks"),
            postedDate = Timestamp(System.currentTimeMillis()),
            collectionCategory = "Phone",

            ),
        collectionStorage(
            collectionID = "al820391j",
            collectionName = "afoaijd",
            collectionDescription = listOf("svodiv","akfiuoaij"),
            postedDate = Timestamp(System.currentTimeMillis())
        ),
        collectionStorage(
            collectionID = "89yhj hk",
            collectionName = "a56yughjjhfoaijd",
            collectionDescription = listOf("kyutr457689yo","dtry76875erdfghjkl;ihugyftyugi"),
            postedDate = getBeginningOfPastMonth()
        ),
        collectionStorage(
            collectionID = "8ytyuij",
            collectionName = "htu86rtdgfhj",
            collectionDescription = listOf("=0ijhiyb","l;opiuyfgihoj;jhtr"),
            postedDate = getBeginningOfPastMonth()
        ),
        collectionStorage(
            collectionID = "nbgyu876trdf",
            collectionName = "8765rdcv",
            collectionDescription = listOf("0987tcvbjkl;","retyu"),
            collectionCategory = "Laptops".lowercase(),
            postedDate = getBeginningOfPastMonth()
        ),
    )

    var userDummy = listOf(
        UserData(
                customerId = "43djdk_w",
                firstname = "OneKim",
                lastname = "Lim",
                username = "Tom",
                email = "www.TomStream19@gmail.com",
                password = "password"
        )
    )
}
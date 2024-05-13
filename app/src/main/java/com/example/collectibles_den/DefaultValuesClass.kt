package com.example.collectibles_den

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
            posted_Date = Timestamp(System.currentTimeMillis())
        ),
        collectionStorage(
            collectionID = "7uyb7",
            collectionName = "sduijosp",
            collectionDescription = listOf("skdifsj","sfafoif"),
            posted_Date = Timestamp(System.currentTimeMillis())
        ),
        collectionStorage(
            collectionID = "49djd",
            collectionName = "afoiajp-kdf",
            collectionDescription = listOf("adfkaoisfj","afkoaidjiaks"),
            posted_Date = Timestamp(System.currentTimeMillis())
        ),
        collectionStorage(
            collectionID = "al820391j",
            collectionName = "afoaijd",
            collectionDescription = listOf("svodiv","akfiuoaij"),
            posted_Date = Timestamp(System.currentTimeMillis())
        ),
        collectionStorage(
            collectionID = "89yhj hk",
            collectionName = "a56yughjjhfoaijd",
            collectionDescription = listOf("kyutr457689yo","dtry76875erdfghjkl;ihugyftyugi"),
            posted_Date = getBeginningOfPastMonth()
        ),
        collectionStorage(
            collectionID = "8ytyuij",
            collectionName = "htu86rtdgfhj",
            collectionDescription = listOf("=0ijhiyb","l;opiuyfgihoj;jhtr"),
            posted_Date = getBeginningOfPastMonth()
        ),
        collectionStorage(
            collectionID = "nbgyu876trdf",
            collectionName = "8765rdcv",
            collectionDescription = listOf("0987tcvbjkl;","retyu"),
            posted_Date = getBeginningOfPastMonth()
        ),
    )
}
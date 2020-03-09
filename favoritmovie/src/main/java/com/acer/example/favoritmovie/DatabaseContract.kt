package com.acer.example.favoritmovie

import android.media.tv.TvContract.AUTHORITY
import android.net.Uri
import android.provider.BaseColumns
import android.service.notification.Condition.SCHEME

object DatabaseContract {

    const val SCHEME = "content"
    const val AUTHORITY = "com.acer.example.katalogfilmsub2"

    internal class FavColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite"
            const val _ID = "_id"
            const val JUDUL = "judul"
            const val DESKRIPSI = "deskripsi"
            const val POSTER = "poster"
            const val KATEGORI = "kategori"
            const val DATABASE_VERSION = 1

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }


    }

}
package com.acer.example.favoritmovie

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie (
    var id: Int,
    var judul: String,
    var deskripsi: String,
    var poster: String
): Parcelable
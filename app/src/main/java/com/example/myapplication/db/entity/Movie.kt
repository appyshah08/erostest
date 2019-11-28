package com.example.myapplication.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity



data class Movie(

        @ColumnInfo(name = "popularity") var popularity: Double,
        @ColumnInfo(name = "vote_count") var vote_count: Int,
        @ColumnInfo(name = "video") var video: Boolean,
        @ColumnInfo(name = "poster_path") var poster_path: String,
        @ColumnInfo(name = "id") var id: Int,
        @ColumnInfo(name = "adult") var adult: Boolean,
        @ColumnInfo(name = "backdrop_path") var backdrop_path: String,
        @ColumnInfo(name = "original_language") var original_language: String,
        @ColumnInfo(name = "original_title") var original_title: String,
        @ColumnInfo(name = "title") var title: String,
        @ColumnInfo(name = "vote_average") var vote_average: Double,
        @ColumnInfo(name = "overview") var overview: String,
        @ColumnInfo(name = "release_date") var release_date: String,
        @ColumnInfo(name = "is_favorite") var is_favorite: Int = 0

)
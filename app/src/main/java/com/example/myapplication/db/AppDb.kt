package com.example.myapplication.db

import android.content.Context
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.db.dao.moviedao
import com.example.myapplication.db.entity.Movie
import com.example.myapplication.model.ResultTopRated

@Database(entities = arrayOf(ResultTopRated::class), version = 1, exportSchema = true)
abstract class AppDb():RoomDatabase() {
    abstract fun moviedao(): moviedao


    companion object {
        @Volatile private var instance: AppDb? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
                AppDb::class.java, "movie-list.db")
                .build()
    }
}
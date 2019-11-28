package com.example.myapplication.db.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.model.ResultTopRated

@Dao
interface moviedao {

    @Insert
    fun insertTopRatedMovie(topRatedMovie: ResultTopRated)

    @Insert
    fun insertAllTopRatedMovie(list:List<ResultTopRated>)

    @Query(value = "Select * from movie")
    fun getAllMovies() : List<ResultTopRated>

    @Query(value = "Select * from movie where isfavorite=1")
    fun getFavoriteMovie() : List<ResultTopRated>

    @Query("UPDATE movie SET isfavorite = :favoriteValue WHERE id = :id")
    fun updateTour(id: Int, favoriteValue: Int): Int

    @Query("DELETE FROM movie")
    fun deleteAll()
}
package com.example.myapplication.fragment


import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room


import com.example.myapplication.R
import com.example.myapplication.adapter.MovieAdapter
import com.example.myapplication.api.ApiInterface
import com.example.myapplication.api.SearchInterface
import com.example.myapplication.common.CommonClass
import com.example.myapplication.db.AppDb
import com.example.myapplication.model.ResponseTopRatedMovie
import com.example.myapplication.model.ResultTopRated
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 */
class FavoriteFragment : Fragment() {

    lateinit var rvTopRatedMovie: RecyclerView
    lateinit var tvNoDataFound: TextView
    lateinit var llProgressBar: LinearLayout
    var movieList: ArrayList<ResultTopRated> = ArrayList<ResultTopRated>()
     var movieAdapter: MovieAdapter? = null
    lateinit var searchComm: SearchInterface
    lateinit var db: AppDb
    var isChangeDone:Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_top_rated, container, false)
        rvTopRatedMovie = view.findViewById(R.id.rvTopRatedMovie)
        tvNoDataFound = view.findViewById(R.id.tvNoDataFound)
        llProgressBar = view.findViewById(R.id.llProgressBar)
        db = Room.databaseBuilder(this.activity!!.applicationContext, AppDb::class.java, "MovieDB")
            .build()


        callFavoriteMovie()
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStop() {
        super.onStop()
        if (llProgressBar.visibility == View.VISIBLE) {
            llProgressBar.visibility = View.GONE
        }
    }

    fun callFilter(query: String) {
        movieAdapter?.filter?.filter(query)
    }

    fun callFavoriteMovie() {
        try {
            llProgressBar.visibility = View.VISIBLE
            val thread = Thread {
                var list: List<ResultTopRated> = db.moviedao().getFavoriteMovie()
                if (list != null && list.size > 0) {
                    movieList.clear()
                    movieList.addAll(list)
                    isChangeDone=true
                }
//                @UiThread {
//                    llProgressBar.visibility = View.GONE
//                    if(isChangeDone) {
//                        if(movieAdapter == null){
//                            movieAdapter = MovieAdapter(this@FavoriteFragment!!, movieList, 1)
//                            var gridLayoutManager: GridLayoutManager =
//                                GridLayoutManager(this@FavoriteFragment.context, 2)
//                            rvTopRatedMovie.adapter = movieAdapter
//                            rvTopRatedMovie.layoutManager = gridLayoutManager
//                        }else {
//                            movieAdapter?.notifyDataSetChanged();
//                            isChangeDone = false
//                        }
//                    }
//                }
                this@FavoriteFragment.activity!!.runOnUiThread(java.lang.Runnable {
                    llProgressBar.visibility = View.GONE
                    if(isChangeDone) {
                        if(movieAdapter == null){
                            movieAdapter = MovieAdapter(this@FavoriteFragment!!, movieList, 1)
                            var gridLayoutManager: GridLayoutManager =
                                GridLayoutManager(this@FavoriteFragment.context, 2)
                            rvTopRatedMovie.adapter = movieAdapter
                            rvTopRatedMovie.layoutManager = gridLayoutManager
                        }else {
                            movieAdapter?.notifyDataSetChanged();
                            isChangeDone = false
                        }
                    }
                    if(movieList.size==0){
                        tvNoDataFound.visibility=View.VISIBLE
                        rvTopRatedMovie.visibility=View.GONE
                    }
                })
            }

            thread.start()


        } catch (e: Exception) {
            Toast.makeText(this@FavoriteFragment.context, e.message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()

    }

    fun onResumeFragment() {
        tvNoDataFound.visibility=View.GONE
        rvTopRatedMovie.visibility=View.VISIBLE
        callFavoriteMovie()
    }
}// Required empty public constructor

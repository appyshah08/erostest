package com.example.myapplication.frag

import android.content.Context
import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myapplication.R
import com.example.myapplication.adapter.MovieAdapter
import com.example.myapplication.api.ApiInterface
import com.example.myapplication.api.SearchInterface
import com.example.myapplication.common.CommonClass
import com.example.myapplication.common.PaginationListener
import com.example.myapplication.common.PaginationListener.Companion.PAGE_START
import com.example.myapplication.db.AppDb
import com.example.myapplication.model.ResponseTopRatedMovie
import com.example.myapplication.model.ResultTopRated
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 */
class TopRatedFragment : Fragment() {

    lateinit var rvTopRatedMovie: RecyclerView
    lateinit var tvNoDataFound: TextView
    lateinit var llProgressBar: LinearLayout
    var movieList: ArrayList<ResultTopRated> = ArrayList<ResultTopRated>()
    lateinit var movieAdapter: MovieAdapter
    lateinit var searchComm:SearchInterface
    lateinit var db: AppDb
    private var currentPage = PAGE_START
    private var isLastPage = false
    private val totalPage = 10
    private var isLoading = false
    internal var itemCount = 0
    lateinit var gridLayoutManager: GridLayoutManager

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        searchComm = activity as SearchInterface
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_top_rated, container, false)
        rvTopRatedMovie = view.findViewById(R.id.rvTopRatedMovie)
        tvNoDataFound = view.findViewById(R.id.tvNoDataFound)
        llProgressBar = view.findViewById(R.id.llProgressBar)
         db= Room.databaseBuilder(this.activity!!.applicationContext,AppDb::class.java,"MovieDB").build()

        callTopRatedMovie()
        return view
    }

    fun callTopRatedMovie() {
        try {
            if (CommonClass.isOnline(this.context)) {
                llProgressBar.visibility = View.VISIBLE

                val call = ApiInterface.create().getTopRatingData(this.resources.getString(R.string.apikey), "en-US", currentPage)
                call.enqueue(object : Callback<ResponseTopRatedMovie> {
                    override fun onFailure(call: Call<ResponseTopRatedMovie>, t: Throwable) {
                        llProgressBar.visibility = View.GONE

                        Toast.makeText(this@TopRatedFragment.context, "Error Occured", Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<ResponseTopRatedMovie>, response: Response<ResponseTopRatedMovie>) {
                        llProgressBar.visibility = View.GONE
                        tvNoDataFound.visibility = View.GONE

                        if (response != null && response.body() != null) {
                            if (response.body()!!.getResults() != null && response.body()!!.getResults()?.size!! >= 1) {
                                rvTopRatedMovie.visibility = View.VISIBLE
                                val thread = Thread{
                                    db.moviedao().deleteAll()
                                    response.body()!!.getResults()?.let { db.moviedao().insertAllTopRatedMovie(it) }
                                }
                                thread.start()
                                movieList.addAll(response.body()!!.getResults()!!)
                                if(::movieAdapter.isInitialized) {
                                    movieAdapter!!.notifyDataSetChanged()
                                }else{
                                    movieAdapter =
                                        MovieAdapter(this@TopRatedFragment!!, movieList, 0)
                                    gridLayoutManager =
                                        GridLayoutManager(this@TopRatedFragment.context, 2)
                                    rvTopRatedMovie.adapter = movieAdapter
                                    rvTopRatedMovie.layoutManager = gridLayoutManager
                                    initScrolling()

                                }

                            } else {
                                tvNoDataFound.visibility = View.VISIBLE
                            }



                        }
                    }

                })

            } else {
                llProgressBar.visibility = View.GONE
                Toast.makeText(this.context, this.resources.getString(R.string.activate_internet), Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this@TopRatedFragment.context, e.message, Toast.LENGTH_LONG).show()
        }
    }

    fun initScrolling(){
        rvTopRatedMovie.addOnScrollListener(object:PaginationListener(gridLayoutManager){
            override val isLastPage: Boolean
                get() = currentPage == totalPage
            override val isLoading: Boolean
                get() = false   //To change initializer of created properties use File | Settings | File Templates.

            override fun loadMoreItems() {
                this@TopRatedFragment.isLoading = true
                currentPage++
                callTopRatedMovieMoreItems()
            }

        })
    }

    fun callTopRatedMovieMoreItems(){
        try {
            if (CommonClass.isOnline(this.context)) {
                movieAdapter!!.addLoading()
                val call = ApiInterface.create().getTopRatingData(this.resources.getString(R.string.apikey), "en-US", currentPage)
                call.enqueue(object : Callback<ResponseTopRatedMovie> {
                    override fun onFailure(call: Call<ResponseTopRatedMovie>, t: Throwable) {


                        Toast.makeText(this@TopRatedFragment.context, "Error Occured", Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<ResponseTopRatedMovie>, response: Response<ResponseTopRatedMovie>) {


                        if (response != null && response.body() != null) {
                            if (response.body()!!.getResults() != null && response.body()!!.getResults()?.size!! >= 1) {
                                rvTopRatedMovie.visibility = View.VISIBLE
                                val thread = Thread{
                                    response.body()!!.getResults()?.let { db.moviedao().insertAllTopRatedMovie(it) }
                                }
                                thread.start()

                                if (currentPage !== PAGE_START) movieAdapter!!.removeLoading()
                                movieAdapter!!.addItems(response.body()!!.getResults()!!)

                                // check weather is last page or not
                                if (currentPage < totalPage) {
                                    movieAdapter!!.addLoading()
                                } else {
                                    isLastPage = true
                                }
                                isLoading = false

                            } else {
                                tvNoDataFound.visibility = View.VISIBLE
                            }



                        }
                    }

                })

            } else {

                Toast.makeText(this.context, this.resources.getString(R.string.activate_internet), Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this@TopRatedFragment.context, e.message, Toast.LENGTH_LONG).show()
        }
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

    fun callFilter(query:String){
        movieAdapter!!.filter.filter(query)
    }

    fun updateFavorite(tag:Int,resultTopRated: ResultTopRated,pos:Int){
        val thread = Thread{
            db.moviedao().updateTour(resultTopRated.getId()!!,tag)
            this@TopRatedFragment.activity!!.runOnUiThread(java.lang.Runnable {
                  resultTopRated.isfavorite=tag
                  movieAdapter!!.getAdapterList().removeAt(pos)
                  movieAdapter!!.getAdapterList().add(pos,resultTopRated)
                  movieAdapter!!.notifyItemChanged(pos)
            })
        }
        thread.start()
    }


}// Required empty public constructor



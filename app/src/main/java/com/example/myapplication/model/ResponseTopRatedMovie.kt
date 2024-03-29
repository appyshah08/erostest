package com.example.myapplication.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Neeta on 11/23/2019.
 */
class ResponseTopRatedMovie {
    @SerializedName("page")
    @Expose
    private var page: Int? = null
    @SerializedName("total_results")
    @Expose
    private var totalResults: Int? = null
    @SerializedName("total_pages")
    @Expose
    private var totalPages: Int? = null
    @SerializedName("results")
    @Expose
    private var results: List<ResultTopRated>? = null

    fun getPage(): Int? {
        return page
    }

    fun setPage(page: Int?) {
        this.page = page
    }

    fun getTotalResults(): Int? {
        return totalResults
    }

    fun setTotalResults(totalResults: Int?) {
        this.totalResults = totalResults
    }

    fun getTotalPages(): Int? {
        return totalPages
    }

    fun setTotalPages(totalPages: Int?) {
        this.totalPages = totalPages
    }

    fun getResults(): List<ResultTopRated>? {
        return results
    }

    fun setResults(results: List<ResultTopRated>) {
        this.results = results
    }

}
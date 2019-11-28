package com.example.myapplication.activity

import android.os.Bundle
import android.widget.RatingBar
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout


import com.example.myapplication.model.ResultTopRated
import com.squareup.picasso.Picasso
import android.graphics.drawable.Drawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.Bitmap
import android.widget.FrameLayout
import com.example.myapplication.R


class DetailActivity : AppCompatActivity() {

    lateinit var constraintlayout: ConstraintLayout
    lateinit var tvMovieTitle: TextView
    lateinit var tvPopularity: TextView
    lateinit var tvOverview: TextView
    lateinit var ratingbar: RatingBar
    lateinit var framelayout:FrameLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        constraintlayout = findViewById(R.id.constraintlayout)
        tvMovieTitle = findViewById(R.id.tvMovieTitle)
        tvPopularity = findViewById(R.id.tvPopularity)
        tvOverview = findViewById(R.id.tvOverview)
        ratingbar = findViewById(R.id.ratingbar)
        framelayout = findViewById(R.id.framelayout)

        initializeValue()
    }

    fun initializeValue() {
        if (intent.extras != null) {
            var result: ResultTopRated = intent.getSerializableExtra("data") as ResultTopRated
            if (result != null) {
                tvMovieTitle.text = result.getTitle()
                tvPopularity.text = result.getPopularity().toString()
                tvOverview.text = result.getOverview()

                ratingbar.rating = result.getVoteAverage().toString().toFloat() / 2
                val url: String =
                    this.resources.getString(com.example.myapplication.R.string.backdropImageUrl) + result.getPosterPath()
                Picasso.with(this).load(url).into(object : com.squareup.picasso.Target {
                    override fun onBitmapFailed(errorDrawable: Drawable?) {
                        TODO("not implemented")
                    }

                    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                        // loaded bitmap is here (bitmap)

                        framelayout.background=BitmapDrawable(bitmap)
                    }

                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

                })
            }
        }
    }
}








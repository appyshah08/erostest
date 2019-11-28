package com.example.myapplication.adapter


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.frag.TopRatedFragment
import com.example.myapplication.model.ResultTopRated
import com.example.myapplication.R
import com.example.myapplication.activity.DetailActivity
import com.squareup.picasso.Picasso


/**
 * Created by Neeta on 11/24/2019.
 */
class MovieAdapter(val ctx: Fragment, var list: ArrayList<ResultTopRated>,var pos:Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1
    var mFilteredList:ArrayList<ResultTopRated> = ArrayList<ResultTopRated>()
    private var isLoaderVisible = false
    var fragmentPos:Int = pos
    init {
        mFilteredList = list
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       // return MovieHolder(LayoutInflater.from(ctx).inflate(com.example.myapplication.R.layout.movie_item_adapter, parent, false), ctx)
        if (viewType === VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item_adapter, parent, false)
            return MovieHolder(view,ctx,mFilteredList)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.progress_bar, parent, false)
            return LoadingViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is MovieHolder) {
            var resultModel: ResultTopRated = mFilteredList.get(position)
            if (resultModel.getTitle() != null && resultModel.getTitle() != "") {
                holder.tvMovieName.text = resultModel.getTitle()
            }
            if (resultModel.getVoteAverage() != null) {
                holder.ratingbar.rating = resultModel.getVoteAverage().toString().toFloat() / 2
            }
            if (resultModel.getPosterPath() != null && resultModel.getPosterPath() != "") {
                val url: String = ctx.resources.getString(R.string.shortImageUrl) + resultModel.getPosterPath()
                Picasso.with(ctx.context).load(url).into(holder.ivPoster)
            }
            if(resultModel.getIs_favorite()==1)
            {
                holder.ivFavorite.background = ctx.resources.getDrawable(R.drawable.heart_filled)
                holder.ivFavorite.tag = 1
            }else{
                holder.ivFavorite.background = ctx.resources.getDrawable(R.drawable.heart_unfilled)
                holder.ivFavorite.tag = 0
            }
        }else if(holder is LoadingViewHolder)
        {
            showLoadingView(holder as LoadingViewHolder, position)
        }


    }

    fun showLoadingView(holder:LoadingViewHolder,pos:Number){

    }

    internal fun getItem(position: Int): ResultTopRated {
        return mFilteredList.get(position)
    }

    override fun getItemCount(): Int {
        return if (mFilteredList == null) 0 else mFilteredList.size
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    public override fun getItemViewType(position: Int): Int {
        return if (isLoaderVisible) {
            if (position == mFilteredList.size - 1) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
        } else {
            VIEW_TYPE_ITEM
        }
    }

    fun getAdapterList():ArrayList<ResultTopRated>{
        return mFilteredList
    }




    inner class MovieHolder(view: View, ctx: Fragment,list:List<ResultTopRated>) : RecyclerView.ViewHolder(view) {
        var ivPoster: ImageView
        val tvMovieName: TextView
        val ivFavorite: ImageView
        var ratingbar: RatingBar
        var llMain:LinearLayout

        init {
            ivPoster = view.findViewById<ImageView>(R.id.ivPoster)
            tvMovieName = view.findViewById<TextView>(R.id.tvMovieName)
            ivFavorite = view.findViewById<ImageView>(R.id.ivFavorite)
            ratingbar = view.findViewById<RatingBar>(R.id.ratingbar)
            llMain = view.findViewById(R.id.llMain)
            llMain.setOnClickListener(object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    var intent = Intent(ctx.activity, DetailActivity::class.java)
                    intent.putExtra("data",mFilteredList.get(adapterPosition))
                    ctx.activity!!.startActivity(intent)
                }

            })
            if(fragmentPos == 1)
            {
                ivFavorite.visibility = View.GONE
            }
            ratingbar.numStars = 5
            ivFavorite.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    if(ivFavorite.tag==null)
                    {
                       changeBack(ivFavorite,0,ctx,list.get(adapterPosition))
                    }else {
                        changeBack(ivFavorite, ivFavorite.tag as Number, ctx,list.get(adapterPosition))
                    }
                }

            })

        }

        fun changeBack(iv: ImageView, tag: Number, ctx: Fragment,resultTopRated: ResultTopRated) {
            if (tag == 1) {

               // ivFavorite.tag = 0
               // ivFavorite.background = ctx.resources.getDrawable(R.drawable.heart_unfilled)
                (ctx as TopRatedFragment).updateFavorite(0,resultTopRated,adapterPosition)
            } else {
                //ivFavorite.tag = 1
                // ivFavorite.background = ctx.resources.getDrawable(R.drawable.heart_filled)
                (ctx as TopRatedFragment).updateFavorite(1,resultTopRated,adapterPosition)
            }
        }

    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var progressBar: ProgressBar

        init {
            progressBar = itemView.findViewById(R.id.progressBar)
        }
    }

    val filter: Filter
        get() = object : Filter() {
            protected override fun performFiltering(charSequence: CharSequence): FilterResults {

                val charString = charSequence.toString()

                if (charString.isEmpty()) {

                    mFilteredList = list
                } else {

                    val filteredList = ArrayList<ResultTopRated>()

                    for (result:ResultTopRated in list) {

                        if (result.getTitle()?.toLowerCase()?.contains(charString.toLowerCase())!! ) {

                            filteredList.add(result)
                        }
                    }

                    mFilteredList = filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = mFilteredList
                return filterResults
            }

            protected override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                mFilteredList = filterResults.values as ArrayList<ResultTopRated>
                notifyDataSetChanged()
            }
        }


    fun addItems(res: List<ResultTopRated>) {
        mFilteredList.addAll(res)
        notifyDataSetChanged()
    }

    fun addLoading() {
        isLoaderVisible = true
        mFilteredList.add(ResultTopRated())
        notifyItemInserted(mFilteredList.size - 1)
    }

    fun removeLoading() {
        isLoaderVisible = false
        val position = mFilteredList.size - 1
        val item = getItem(position)
        if (item != null) {
            mFilteredList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        mFilteredList.clear()
        notifyDataSetChanged()
    }


}
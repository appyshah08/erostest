package com.example.myapplication.activity


import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.viewpager.widget.ViewPager
import com.example.myapplication.api.SearchInterface
import com.example.myapplication.frag.TopRatedFragment
import com.example.myapplication.fragment.FavoriteFragment
import com.example.myapplication.R
import com.example.myapplication.adapter.CustomPagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*


class HomeActivity : AppCompatActivity(),SearchInterface {
    override fun search() {

    }

    lateinit var searchView: SearchView
    lateinit var pagerAapter: CustomPagerAdapter
    lateinit var pager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
             pager = findViewById<View>(R.id.pager) as ViewPager
            val tablayout = findViewById<View>(R.id.tablayout) as TabLayout

            pagerAapter = CustomPagerAdapter(this@HomeActivity, supportFragmentManager)
            pagerAapter.addfragment(TopRatedFragment(), this.resources.getString(R.string.topRated))
            pagerAapter.addfragment(FavoriteFragment(), this.resources.getString(R.string.favorite))

            pager.adapter = pagerAapter
            tablayout.setupWithViewPager(pager)
            pager.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
                override fun onPageScrollStateChanged(state: Int) {

                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {

                }

                override fun onPageSelected(position: Int) {
                   if(position == 1) {
                       (pagerAapter.getFragment(pager.currentItem) as FavoriteFragment).onResumeFragment()
                   }
                }

            })
            tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    pager!!.currentItem = tab!!.position

                }

            })
        }catch (e:Exception){
            e.printStackTrace()
        }

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(com.example.myapplication.R.menu.search_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(com.example.myapplication.R.id.action_search)
                .actionView as SearchView
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(componentName))
        searchView.setMaxWidth(Integer.MAX_VALUE)

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                    if (pager.currentItem == 0) {
                        (pagerAapter.getFragment(pager.currentItem) as TopRatedFragment).callFilter(newText!!)
                    } else {
                        (pagerAapter.getFragment(pager.currentItem) as FavoriteFragment).callFilter(newText!!)
                    }

                return true
            }

        })

        return true
    }




    override  fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.getItemId()


        return if (id == com.example.myapplication.R.id.action_search) {
            true
        } else super.onOptionsItemSelected(item)

    }

    override fun onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true)
            return
        }
        super.onBackPressed()
    }


}

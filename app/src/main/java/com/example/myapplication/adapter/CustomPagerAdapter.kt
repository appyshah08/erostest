package com.example.myapplication.adapter

import android.content.Context

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * Created by Neeta on 11/23/2019.
 */
class CustomPagerAdapter(var context: Context, var fm: FragmentManager): FragmentStatePagerAdapter(fm) {

    var fragmentList:ArrayList<Fragment> = ArrayList<Fragment>()
    var tabTitleList:ArrayList<String> = ArrayList<String>()

    override fun getItem(position: Int): Fragment {
        return fragmentList.get(position)
    }

    override fun getCount(): Int {
      return fragmentList.size
    }

    fun addfragment( frag:Fragment, title:String){
        fragmentList.add(frag)
        tabTitleList.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabTitleList.get(position)
    }

    fun getFragment(position:Int):Fragment{
        return fragmentList.get(position)
    }



}
package com.example.collectibles_den

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class Fragement (fragementManager: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fragementManager, lifecycle ) {
    override fun getItemCount(): Int {

        return 2
    }

    override fun createFragment(position: Int): Fragment {

        return if(position == 0){

            LoginTabFragment()
        }
        else{

            RegisterTabFragment()
        }

    }




}
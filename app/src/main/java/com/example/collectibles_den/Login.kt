package com.example.collectibles_den

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout

class Login : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: Fragement
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)


            tabLayout = findViewById(R.id.tab_layout)
            viewPager2 = findViewById(R.id.viewPager)

            adapter = Fragement(supportFragmentManager, lifecycle)

            tabLayout.addTab(tabLayout.newTab().setText("Login"))
            tabLayout.addTab(tabLayout.newTab().setText("Register"))

            viewPager2.adapter = adapter

            //hiding toolbar
            supportActionBar?.hide()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

                if (tab != null) {
                    viewPager2.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        //
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                tabLayout.selectTab(tabLayout.getTabAt(position))
            }

        })


        }


}
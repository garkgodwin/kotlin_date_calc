package com.example.datecalc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.example.datecalc.fragments.AddOrSubtractDaysFragment
import com.example.datecalc.fragments.DifferenceBetweenDatesFragment
import com.example.datecalc.fragments.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    //fragments and fragment adapters vars
    lateinit var viewPager: ViewPager
    lateinit var tabsContainer: TabLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupTabs()
    }

    private fun initViews(){
        viewPager = findViewById(R.id.vp_fragments_container)
        tabsContainer = findViewById(R.id.tl_container)
    }
    private fun setupTabs(){
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(DifferenceBetweenDatesFragment(), "Difference between dates")
        adapter.addFragment(AddOrSubtractDaysFragment(), "Add or subtract days")
        viewPager.adapter = adapter
        tabsContainer.setupWithViewPager(viewPager)

        tabsContainer.getTabAt(0)!!.text = ("Difference between dates")
        tabsContainer.getTabAt(1)!!.text = ("Add or subtract days")
    }

}
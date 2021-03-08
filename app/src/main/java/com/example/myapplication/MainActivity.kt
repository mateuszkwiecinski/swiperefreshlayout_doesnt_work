package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.delay
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

internal class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edittext)
        val pager = findViewById<ViewPager2>(R.id.pager)
        pager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 2

            override fun createFragment(position: Int) = RefreshableFragment(position)

        }
        val tabs = findViewById<TabLayout>(R.id.tabs)
        TabLayoutMediator(tabs, pager) { tab, position ->
            tab.text = "Tab $position"
        }.attach()
    }
}

class RefreshableFragment(private val position: Int) : Fragment(R.layout.fragment_refreshable) {

    lateinit var swipeRefresh: SwipeRefreshLayout
    lateinit var label: TextView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefresh = view.findViewById(R.id.swipeRefresh)
        label = view.findViewById(R.id.label)

        label.text = "Initial $position"
        swipeRefresh.setOnRefreshListener { action() }
    }

    @OptIn(ExperimentalTime::class)
    fun action() {
        lifecycleScope.launchWhenCreated {
            swipeRefresh.isRefreshing = true
            var left = 10.seconds
            while (left > 0.seconds) {
                left -= 1.seconds
                label.text = "Loading ${left}"
                delay(1.seconds)
            }
            label.text = "Loaded"
            swipeRefresh.isRefreshing = false
        }
    }
}

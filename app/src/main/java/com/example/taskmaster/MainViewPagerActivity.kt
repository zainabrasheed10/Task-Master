package com.example.taskmaster

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainViewPagerActivity : AppCompatActivity() {

    private lateinit var togggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_view_pager)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawerLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        //Slideable menu with navigation drawer
        val drawer = findViewById<DrawerLayout>(R.id.drawerLayout)

        togggle = ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close)
        drawer.addDrawerListener(togggle)
        togggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val navViews = findViewById<NavigationView>(R.id.navView)
        navViews.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.miSetting -> Toast.makeText(applicationContext, "Clicked Settings", Toast.LENGTH_SHORT).show()
            }
            true
        }

        val allTasks = SharedPrefUtils.getTasks(this)

        val viewPager = findViewById<ViewPager2>(R.id.viewPager2)
        viewPager.adapter = ViewPagerAdapter(this, allTasks)

        val tabs = findViewById<TabLayout>(R.id.tabLayout)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "All"
                1 -> "Pending"
                2 -> "Completed"
                else -> ""
            }
        }.attach()
    }

    //functions to implement toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort -> {
                Toast.makeText (this, "Sort clicked", Toast.LENGTH_SHORT).show()
                true
            }

            R.id.action_filter -> {
                Toast.makeText (this, "Filter clicked", Toast.LENGTH_SHORT).show()
                true
            }

            R.id.action_settings -> {
                Toast.makeText (this, "Settings clicked", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
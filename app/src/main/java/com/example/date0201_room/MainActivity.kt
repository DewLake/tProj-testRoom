package com.example.date0201_room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.date0201_room.ui.local.BooksFragment
import com.example.date0201_room.ui.remote.ApiFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<BooksFragment>(R.id.framContainer__MainActivity, "TAG_BooksFragment")
            }
        }

        // Bottom navigation view
        findViewById<BottomNavigationView>(R.id.bottomNavigationView).apply {
            setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        }

    } // end onCreate().

    // Bottom navigation item selected listener
    private val mOnNavigationItemSelectedListener =
            object : BottomNavigationView.OnNavigationItemSelectedListener {
                override fun onNavigationItemSelected(item: MenuItem): Boolean {
                    val fragBooks = BooksFragment()
                    val fragFakeApi = ApiFragment()

                    return when (item.itemId) {
                        R.id.menuItem_navi_home__MenuBtmNavi -> setupCurrentFragment(fragBooks)
                        R.id.menuItem_navi_webApi__MenuBumNavi -> setupCurrentFragment(fragFakeApi)
                        // ↑ return true to display the item as the selected item.
                        else -> false
                    } // end when(itemId).
                } // end onNavigationItemSelected().
            } // end object : listener.

    /** set up current fragment to display */
    private fun setupCurrentFragment(fragment: Fragment): Boolean {
        supportFragmentManager.commit {
            replace(R.id.framContainer__MainActivity, fragment)
        }

        return true
    }

} // end class MainActivity.
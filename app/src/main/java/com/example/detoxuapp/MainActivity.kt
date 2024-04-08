package com.example.detoxuapp


import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationBar : BottomNavigationView
    private lateinit var frameLayout: FrameLayout
    private lateinit var fragmentManager: FragmentManager
    private lateinit var nestedScrollView: NestedScrollView
    val homeFrag = HomeFragment()
    val goalFrag = GoalsFragment()
    val profileFrag = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeAllWidgets()

        fragmentManager = supportFragmentManager


        replaceFragment(homeFrag)

        bottomNavigationBar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.homePage -> replaceFragment(homeFrag)
                R.id.profilePage -> replaceFragment(profileFrag)
                R.id.goalPage -> replaceFragment(goalFrag)
            }
            true
        }

        nestedScrollView.setOnScrollChangeListener{ _,_,scrollY,_,oldScrollY ->
            if (scrollY > oldScrollY && bottomNavigationBar.translationY == 0f) {

                bottomNavigationBar.animate().translationY(bottomNavigationBar.height.toFloat())
            } else if (scrollY < oldScrollY && bottomNavigationBar.translationY > 0f) {

                bottomNavigationBar.animate().translationY(0f)
            }

        }

    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

    private fun initializeAllWidgets() {
        bottomNavigationBar = findViewById(R.id.bottomNav)
        frameLayout = findViewById(R.id.frameLayout)
        nestedScrollView = findViewById(R.id.nestedScrollView)
    }
}
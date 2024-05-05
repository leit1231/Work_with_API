package com.example.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.fragments.AnonsFragment
import com.example.newsapp.fragments.NewsFragment
import com.example.newsapp.fragments.UsersFragment

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, NewsFragment())
            .commit()

        binding.BNV.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_news -> {
                    val newsFragment = NewsFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, newsFragment)
                        .commit()
                    true
                }
                R.id.menu_anons -> {
                    val anonsFragment = AnonsFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, anonsFragment)
                        .commit()
                    true
                }
                R.id.menu_users -> {
                    val userFragment = UsersFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, userFragment)
                        .commit()
                    true
                }
                else -> false
            }
        }
    }
}

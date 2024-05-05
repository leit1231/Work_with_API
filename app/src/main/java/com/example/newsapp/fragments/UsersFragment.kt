package com.example.newsapp.fragments

import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.adapter.UserAdapter
import com.example.newsapp.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UsersFragment : Fragment() {

    lateinit var adapter: UserAdapter
    private lateinit var apiService: ApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_users, container, false)

        adapter = UserAdapter()

        val recView = view.findViewById<RecyclerView>(R.id.recyclerViewUsers)
        recView.layoutManager = LinearLayoutManager(requireContext())
        recView.adapter = adapter

        val retrofit = Retrofit.Builder().baseUrl("https://a9be56f403ee093f.mokky.dev/")
            .addConverterFactory(GsonConverterFactory.create()).build()

        apiService = retrofit.create(ApiService::class.java)

        val svo = view.findViewById<SearchView>(R.id.svo)
        svo.setOnQueryTextListener(object : OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                CoroutineScope(Dispatchers.IO).launch {
                    val id = newText?.toIntOrNull()
                    if (id != null) {
                        val user = apiService.getUserById(id.toString())
                        view?.post {
                            adapter.submitList(listOf(user))
                        }
                    }
                }
                return true
            }
        })

        CoroutineScope(Dispatchers.IO).launch {
            val list = apiService.getAllUsers()
            view?.post {
                adapter.submitList(list)
            }
        }

        return view
    }

}

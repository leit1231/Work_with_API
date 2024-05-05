package com.example.newsapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.NewNews
import com.example.newsapp.R
import com.example.newsapp.adapter.NewsAdapter
import com.example.newsapp.models.News
import com.example.newsapp.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsFragment : Fragment() {

    private val adapter: NewsAdapter by lazy {
        NewsAdapter { news ->
            deleteNews(news)
        }
    }

    private val apiService: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://a9be56f403ee093f.mokky.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_news, container, false)

        val recView = view.findViewById<RecyclerView>(R.id.recyclerViewNews)
        recView.layoutManager = LinearLayoutManager(requireContext())
        recView.adapter = adapter

        val btn_add = view.findViewById<ImageButton>(R.id.btn_add_news)
        btn_add.setOnClickListener{
            val intent = Intent(requireContext(), NewNews::class.java)
            startActivity(intent)
        }

        loadNews()

        return view
    }

    private fun loadNews() {
        coroutineScope.launch {
            try {
                val list = apiService.getAllNews()
                withContext(Dispatchers.Main) {
                    adapter.submitList(list)
                }
            } catch (e: Exception) {
                Log.e("NewsFragment", "Error loading news", e)
            }
        }
    }

    private fun deleteNews(news: News) {
        coroutineScope.launch {
            try {
                apiService.deleteNews(news.id)
                withContext(Dispatchers.Main) {
                    loadNews()
                }
            } catch (e: Exception) {
                Log.e("NewsFragment", "Error deleting news", e)
            }
        }
    }
}

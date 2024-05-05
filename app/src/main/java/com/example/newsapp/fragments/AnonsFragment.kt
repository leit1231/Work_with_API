package com.example.newsapp.fragments


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.NewAnons
import com.example.newsapp.models.Anons
import com.example.newsapp.adapter.AnonsAdapter
import com.example.newsapp.R
import com.example.newsapp.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AnonsFragment : Fragment() {

    lateinit var adapter: AnonsAdapter
    private lateinit var apiService: ApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_anons, container, false)

        adapter = AnonsAdapter { anons ->
            deleteAnons(anons)
        }

        val btn_add = view.findViewById<ImageButton>(R.id.btn_add_anons)
        btn_add.setOnClickListener{
            val intent = Intent(requireContext(), NewAnons::class.java)
            startActivity(intent)
        }

        val recView = view.findViewById<RecyclerView>(R.id.recyclerViewAnons)
        recView.layoutManager = LinearLayoutManager(requireContext())
        recView.adapter = adapter

        val retrofit = Retrofit.Builder().baseUrl("https://a9be56f403ee093f.mokky.dev/")
            .addConverterFactory(GsonConverterFactory.create()).build()

        apiService = retrofit.create(ApiService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val list = apiService.getAllAnons()
            view?.post {
                adapter.submitList(list)
            }
        }
        return view
    }

    private fun loadAnons() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val list = apiService.getAllAnons()
                withContext(Dispatchers.Main) {
                    adapter.submitList(list)
                }
            } catch (e: Exception) {
                // Обработка ошибок при загрузке анонсов
                Log.e("AnonsFragment", "Error loading anons", e)
            }
        }
    }

    private fun deleteAnons(anons: Anons) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                apiService.deleteAnons(anons.id)
                // После успешного удаления анонса, загружаем обновленный список
                loadAnons()
            } catch (e: Exception) {
                // Обработка ошибок при удалении анонса
                Log.e("AnonsFragment", "Error deleting anons", e)
            }
        }
    }

}

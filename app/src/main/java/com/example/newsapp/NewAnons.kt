package com.example.newsapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import com.example.newsapp.databinding.ActivityNewAnonsBinding
import com.example.newsapp.models.Anons
import com.example.newsapp.models.News
import com.example.newsapp.models.User
import com.example.newsapp.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewAnons : AppCompatActivity() {

    private lateinit var binding: ActivityNewAnonsBinding
    private var selectedImageUri: Uri? = null
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewAnonsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiService = createApiService()

        binding.imageViewAd.setOnClickListener {
            pickImageFromGallery()
        }

        binding.buttonCancel.setOnClickListener{
            finish()
        }

        binding.buttonSave.setOnClickListener {
            saveAnons()
        }

    }

    private fun createApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://a9be56f403ee093f.mokky.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            uri?.let {
                selectedImageUri = it
                binding.imageViewAd.setImageURI(it)
            }
        }
    }

    private fun getImagePath(uri: Uri): String? {
        var imagePath: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.use { cursor ->
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                imagePath = cursor.getString(columnIndex)
            }
        }
        return imagePath
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        getContent.launch(intent)
    }

    private fun saveAnons() {
        val title = binding.editTextTitle.text.toString()
        val description = binding.editTextDescription.text.toString()
        val userId = binding.editTextUserID.text.toString().toInt()
        val price = binding.editTextPrice.text.toString()
        val imageUri = selectedImageUri?.let { getImagePath(it) }

        val user = User(userId, "Golovach", "Lena")

        val announcement = Anons(0, user, title, imageUri.toString(), description, price)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.addAnons(announcement)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        finish()
                    }
                } else {
                }
            } catch (e: Exception) {
            }
        }
    }

}
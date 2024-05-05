package com.example.newsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.databinding.NewsLayoutBinding
import com.example.newsapp.models.News

class NewsAdapter(private val onDeleteNewsClick: (News) -> Unit) : ListAdapter<News, NewsAdapter.Holder>(Comparator()) {

    inner class Holder(private val binding: NewsLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.imageDelete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onDeleteNewsClick(getItem(position))
                }
            }
        }

        fun bind(news: News) {
            with(binding) {
                textTitle.text = news.title
                textDescription.text = news.description

                Glide.with(itemView)
                    .load(news.imageUrl)
                    .placeholder(R.drawable.test)
                    .into(imageNews)

                news.user?.let { user ->
                    textAuthor.text = user.fullName
                }
            }
        }
    }


    class Comparator: DiffUtil.ItemCallback<News>(){
        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = NewsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.bind(getItem(position))

    }

}

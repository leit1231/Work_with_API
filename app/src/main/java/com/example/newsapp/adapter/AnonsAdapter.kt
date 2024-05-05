package com.example.newsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.databinding.AnonsLayoutBinding
import com.example.newsapp.models.Anons

class AnonsAdapter(private val onDeleteClickListener: (Anons) -> Unit): ListAdapter<Anons, AnonsAdapter.Holder>(AnonsAdapter.Comparator()) {

    fun deleteAnons(anons: Anons) {
        onDeleteClickListener(anons)
    }

    inner class Holder(view: View): RecyclerView.ViewHolder(view) {
        private val binding = AnonsLayoutBinding.bind(view)

        init {
            binding.imageDelete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val anons = getItem(position)
                    deleteAnons(anons)
                }
            }
        }

        fun bind(anons: Anons)= with(binding){
            textTitle.text = anons.title

            textDescription.text = anons.description

            Glide.with(itemView)
                .load(anons.imageUrl)
                .placeholder(R.drawable.test)
                .into(imageAnons)

            textPrice.text = anons.price

            anons.user?.let { user ->
                textAuthor.text = user.fullName
            }
        }
    }

    class Comparator: DiffUtil.ItemCallback<Anons>(){
        override fun areItemsTheSame(oldItem: Anons, newItem: Anons): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Anons, newItem: Anons): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.anons_layout, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }


}
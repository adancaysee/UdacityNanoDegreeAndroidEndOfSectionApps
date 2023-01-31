package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.ItemAsteroidBinding
import com.udacity.asteroidradar.domain.Asteroid

class AsteroidListAdapter(
    private val itemClickListener: ItemClickListener
) : ListAdapter<Asteroid, AsteroidListAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder private constructor(private val binding: ItemAsteroidBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Asteroid, itemClickListener: ItemClickListener) {
            binding.asteroid = item
            binding.itemClickListener = itemClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemAsteroidBinding.inflate(inflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }


    companion object DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }

    }

    class ItemClickListener(private val onItemClick: (Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = onItemClick(asteroid)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), itemClickListener)
    }
}
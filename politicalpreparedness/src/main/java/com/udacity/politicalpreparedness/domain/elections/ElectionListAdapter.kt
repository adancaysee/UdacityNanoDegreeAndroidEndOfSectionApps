package com.udacity.politicalpreparedness.domain.elections

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.udacity.politicalpreparedness.base.DataBindingViewHolder
import com.udacity.politicalpreparedness.data.domain.Election
import com.udacity.politicalpreparedness.databinding.ItemElectionBinding

class ElectionListAdapter(private val clickListener: ElectionListener) :
    ListAdapter<Election, ElectionListAdapter.ViewHolder>(DiffCallback) {


    class ViewHolder private constructor(private val binding: ItemElectionBinding) :
        DataBindingViewHolder<Election>(binding) {

        fun bind(item: Election, itemClickListener: ElectionListener) {
            binding.clickListener = itemClickListener
            bind(item)
        }

        companion object {

            fun from(parent: ViewGroup): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemElectionBinding.inflate(inflater,parent,false)
                return ViewHolder(binding)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Election>() {

        override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position),clickListener)
    }

    class ElectionListener(val onItemClick: (Election) -> Unit) {
        fun onClick(item: Election) = onItemClick(item)
    }
}
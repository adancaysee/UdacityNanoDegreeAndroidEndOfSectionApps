package com.udacity.politicalpreparedness.domain.representative

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.politicalpreparedness.data.domain.Channel
import com.udacity.politicalpreparedness.data.domain.Representative
import com.udacity.politicalpreparedness.databinding.ItemRepresentativeBinding


class RepresentativeListAdapter(private val clickListener: RepresentativeListener) :
    ListAdapter<Representative, RepresentativeListAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position),clickListener)

    }

    class ViewHolder private constructor(private val binding: ItemRepresentativeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Representative, itemClickListener: RepresentativeListener) {
            binding.item = item
            handleSocialImage(item,itemClickListener)
            binding.clickListener = itemClickListener
            binding.executePendingBindings()
        }

        private fun handleSocialImage(item: Representative, itemClickListener: RepresentativeListener) {
            val channels = item.official.channels
            if (channels != null) {
                val facebookUrl = getFacebookUrl(channels)
                if (!facebookUrl.isNullOrBlank()) {
                    enableLink(binding.facebookImage, facebookUrl,itemClickListener)
                }else {
                    binding.facebookImage.visibility = View.GONE
                }

                val twitterUrl = getTwitterUrl(channels)
                if (!twitterUrl.isNullOrBlank()) {
                    enableLink(binding.twitterImage, twitterUrl,itemClickListener)
                }else {
                    binding.twitterImage.visibility = View.GONE
                }
            }else {
                binding.facebookImage.visibility = View.GONE
                binding.twitterImage.visibility = View.GONE
            }
            val urls = item.official.urls
            if (urls != null){
                enableLink(binding.wwwImage, urls.first(),itemClickListener)
            }else {
                binding.wwwImage.visibility = View.GONE
            }

        }

        private fun getFacebookUrl(channels: List<Channel>): String? {
            return channels.filter { channel -> channel.type == "Facebook" }
                .map { channel -> "https://www.facebook.com/${channel.id}" }
                .firstOrNull()
        }

        private fun getTwitterUrl(channels: List<Channel>): String? {
            return channels.filter { channel -> channel.type == "Twitter" }
                .map { channel -> "https://www.twitter.com/${channel.id}" }
                .firstOrNull()
        }

        private fun enableLink(view: ImageView, url: String, clickListener: RepresentativeListener) {
            view.visibility = View.VISIBLE
            view.setOnClickListener { clickListener.startIntent(url) }
        }

        companion object {

            fun from(parent: ViewGroup): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemRepresentativeBinding.inflate(inflater)
                return ViewHolder(binding)
            }
        }
    }



    companion object DiffCallback : DiffUtil.ItemCallback<Representative>() {

        override fun areItemsTheSame(oldItem: Representative, newItem: Representative): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Representative, newItem: Representative): Boolean {
            return oldItem == newItem
        }
    }

    interface RepresentativeListener {
        fun startIntent(url: String)
    }


}
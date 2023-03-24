package com.udacity.locationreminder.reminderlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.udacity.locationreminder.base.DataBindingViewHolder
import com.udacity.locationreminder.data.domain.Reminder
import com.udacity.locationreminder.databinding.ItemReminderBinding

class RemindersListAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<Reminder, RemindersListAdapter.ViewHolder>(DiffUtilCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindReminder(getItem(position), onClickListener)
    }

    class ViewHolder private constructor(private val binding: ItemReminderBinding) :
        DataBindingViewHolder<Reminder>(binding) {

        fun bindReminder(reminder: Reminder, clickListener: OnClickListener) {
            binding.onClickListener = clickListener
            bind(reminder)
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemReminderBinding.inflate(layoutInflater)
                return ViewHolder(binding)
            }
        }
    }

    companion object DiffUtilCallback : DiffUtil.ItemCallback<Reminder>() {
        override fun areItemsTheSame(oldItem: Reminder, newItem: Reminder): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Reminder, newItem: Reminder): Boolean {
            return oldItem == newItem
        }
    }

    class OnClickListener(val click: (Reminder) -> Unit) {
        fun onItemClick(reminder: Reminder) = click(reminder)
    }


}
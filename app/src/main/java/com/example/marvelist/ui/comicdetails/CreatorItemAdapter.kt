package com.example.marvelist.ui.comicdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelist.R
import com.example.marvelist.data.remote.models.MarvelJson
import com.example.marvelist.databinding.CreatorItemLayoutBinding

/**
 * Item adapter to display information from the list of [MarvelJson.Creator]
 * submitted.
 */
class CreatorItemAdapter(diffUtil: DiffUtil.ItemCallback<MarvelJson.Creator>) :
    ListAdapter<MarvelJson.Creator, CreatorItemAdapter.CreatorViewHolder>(diffUtil) {
    class CreatorViewHolder(private val binding: CreatorItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        /**
         * Assign databinding.
         *
         * @param creatorItem the [MarvelJson.Creator] to assign to the binding.
         */
        fun bind(creatorItem: MarvelJson.Creator) {
            binding.creatorItem = creatorItem
            val roleText =
                binding.roleText.resources.getString(R.string.role_name_text, creatorItem.role)
            binding.roleText.text = roleText
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreatorViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CreatorItemLayoutBinding.inflate(inflater, parent, false)
        return CreatorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CreatorViewHolder, position: Int) {
        val creatorItem = getItem(position)
        holder.bind(creatorItem)
    }
}
package com.example.marvelist.ui.readinglist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelist.data.local.ComicDetail
import com.example.marvelist.databinding.ReadingStatusItemBinding
import com.example.marvelist.ui.readinglist.ReadingListItemAdapter.ReadingStatusViewHolder

/**
 * Adapter for displaying a list of [ComicDetail] items that has been saved from the local database. Uses the
 * [ReadingStatusViewHolder] to display reading progress details for each item.
 */
class ReadingListItemAdapter(diffCallBack: DiffUtil.ItemCallback<ComicDetail>) :
    ListAdapter<ComicDetail, ReadingListItemAdapter.ReadingStatusViewHolder>(diffCallBack) {

    class ReadingStatusViewHolder(private val binding: ReadingStatusItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(comicDetail: ComicDetail) {
            binding.textView.text = comicDetail.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReadingStatusViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewBinding = ReadingStatusItemBinding.inflate(inflater, parent, false)

        return ReadingStatusViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ReadingStatusViewHolder, position: Int) {

    }
}
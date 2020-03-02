package com.example.marvelist.ui.readinglist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.marvelist.databinding.ReadingListFragLayoutBinding
import com.example.marvelist.injection.injector
import com.example.marvelist.injection.viewModel

class ReadingListFragment : Fragment() {
    private lateinit var viewBinding: ReadingListFragLayoutBinding
    private val readingListViewModel by viewModel {
        injector.readingListViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = ReadingListFragLayoutBinding.inflate(inflater, container, false)
        readingListViewModel.savedComics.observe(viewLifecycleOwner, Observer {
            it
        })
        readingListViewModel.getAllSavedComics()
        return viewBinding.root
    }

    /**
     * Cleanup any resources in this callback.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding.unbind()
    }
}
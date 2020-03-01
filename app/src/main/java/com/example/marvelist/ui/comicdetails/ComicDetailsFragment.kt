package com.example.marvelist.ui.comicdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.marvelist.databinding.ComicDetailsFragLayoutBinding
import com.example.marvelist.injection.injector
import com.example.marvelist.injection.viewModel

class ComicDetailsFragment : Fragment() {
    private lateinit var viewBinding: ComicDetailsFragLayoutBinding

    // Provide the BrowserViewModel via injector dagger component.
    private val detailsViewModel by viewModel {
        injector.comicDetailsViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = ComicDetailsFragLayoutBinding.inflate(inflater, container, false)
        setupObservableData()
        displayComicBookDetails()
        return viewBinding.root
    }

    /**
     * Display comic book details.
     */
    private fun displayComicBookDetails() {
        // Retrieve comic book id from bundle
        var comicId = -1
        arguments?.apply {
            comicId = getInt("comicId")
        }
        // Request via API
        detailsViewModel.getComicById(comicId)
    }

    /**
     * Observe data emitted from the ViewModel object.
     */
    private fun setupObservableData() {
        detailsViewModel.comicDetailData.observe(viewLifecycleOwner, Observer {
            viewBinding.comicDetails = it
        })
    }

    /**
     * Cleanup any resources in this callback.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding.unbind()
    }
}
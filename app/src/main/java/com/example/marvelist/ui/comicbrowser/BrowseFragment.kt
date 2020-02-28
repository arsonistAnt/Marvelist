package com.example.marvelist.ui.comicbrowser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.marvelist.databinding.BrowseFragLayoutBinding

class BrowseFragment : Fragment() {
    private lateinit var viewBinding: BrowseFragLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = BrowseFragLayoutBinding.inflate(inflater, container, false)
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
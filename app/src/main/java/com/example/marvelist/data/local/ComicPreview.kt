package com.example.marvelist.data.local

/**
 * Abstract class that contains only relevant information for previewing.
 */
abstract class ComicPreview(
    override var id: Int,
    override var digitalId: Int,
    override var title: String,
    override var comicDescription: String,
    override var thumbnailUrl: String,
    override var issueNo: Int,
    private val format: String
) : ComicBase {

    // TODO: Remove later.
    override fun toString(): String {

        return "Title: $title \nID: $id \nTotal Issues #: $issueNo\n[${format}]"
    }
}
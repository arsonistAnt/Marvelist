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
    override var issueNo: Int
) : ComicBase {

    // TODO: Remove later.
    override fun toString(): String {
        val digitalComic = { id: Int ->
            if (id > 0)
                "\n[DIGITAL]"
            else
                ""
        }
        return "Title: $title \nID: $id \n $issueNo${digitalComic(
            digitalId
        )}"
    }
}
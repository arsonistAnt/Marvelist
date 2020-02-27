package com.example.marvelist.data.local

/**
 * Interface class that defines the base component of a Comic. This class will mostly be used by
 * the RecyclerView adapters.
 */
interface ComicBase {
    val title : String
    val comicDescription : String
    var thumbnailUrl : String
    val issueNo : Int
}
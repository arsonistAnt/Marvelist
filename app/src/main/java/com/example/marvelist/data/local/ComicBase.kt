package com.example.marvelist.data.local

/**
 * Interface class that defines the base component of a Comic. This class will mostly be used by
 * the RecyclerView adapters.
 */
interface ComicBase {
    var id: Int
    var digitalId: Int
    var title: String
    var comicDescription: String
    var thumbnailUrl : String
    var issueNo: Int
}
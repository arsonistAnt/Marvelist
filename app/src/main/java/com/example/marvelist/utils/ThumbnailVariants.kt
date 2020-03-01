package com.example.marvelist.utils

class ThumbnailVariant {
    companion object {
        const val portraitSmall = "/portrait_small"
        const val portraitMedium = "/portrait_medium"
        const val portraitLarge = "/portrait_large"
        const val portraitXLarge = "/portrait_xlarge"
        const val portrait_fantastic = "/portrait_fantastic"
        const val portrait_incredible = "/portrait_incredible"

        /**
         * Construct the portrait thumbnail Url given extension type and size of image.
         *
         * @param url the base thumbnail url.
         * @param extensionType the file type of the image.
         * @param portraitSize the size type of the image. Use sizes defined in the [ThumbnailVariant] class.
         *
         */
        fun constructThumbnailUrl(
            url: String,
            extensionType: String,
            portraitSize: String
        ): String {
            return "$url.$extensionType"
        }
    }
}
package com.example.marvelist.utils

import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

/**
 * Interface that groups classes and modules that perform and handles network responses.
 */
class NetworkResponseHandler @Inject constructor() {
    /**
     * Enum class to keep track of the network responses.
     */
    enum class Status {
        SUCCESS, LOADING, FAILED
    }

    /**
     * Data class to hold network results with the appropriate message and response code given by the HTTP.
     */
    class Response(val message: String = "", val responseCode: Int = -1, val status: Status) {
        companion object {
            /**
             * Gets a http response code and returns the appropriate [NetworkResponseHandler.Status] enum.
             *
             * @param httpResponseCode the HTTP response code (e.g. 200, 403, 400)
             * @return an [NetworkResponseHandler.Status] enum value
             */
            fun getNetworkStatus(httpResponseCode: Int) = when (httpResponseCode) {
                in 200..299 -> Status.SUCCESS
                else -> Status.FAILED
            }
        }
    }

    /**
     * Interface that enforces a class to implement a observable [Response] object.
     */
    interface ResponseCallback {
        val networkResponse: MutableLiveData<Response>
    }

    /**
     * Creates a [Response] object that contains network response information.
     *
     * @param httpResponseCode the http status code that determines what type of message to construct.
     */
    fun createNetworkResult(httpResponseCode: Int): Response {
        // Construct message based on response code.
        val message = when (httpResponseCode) {
            in 200..299 -> "HTTP Request successful."
            else -> "An error has occurred http code: $httpResponseCode"
        }
        // Get status Network status based on response code.
        val networkStatusEnum = Response.getNetworkStatus(httpResponseCode)
        return Response(message, httpResponseCode, networkStatusEnum)
    }
}
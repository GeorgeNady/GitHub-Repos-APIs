package com.george.copticorphanstask.network

import timber.log.Timber

data class Resource<T>(
    val success: Status,
    val data: T? = null,
    val message: String? = null
) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING,
        FAILURE;

        fun isSuccess() = this == SUCCESS
        fun isError() = this == ERROR
        fun isLoading() = this == LOADING
        fun isFailure() = this == FAILURE
    }

    companion object {

        private const val TAG = "Resource"

        fun <T> success(data: T) = Resource(Status.SUCCESS, data, null)

        fun <T> error(message: String, data: T? = null) = Resource(Status.ERROR, data, message)

        fun <T> loading() = Resource(Status.LOADING, null)

        fun <T> failed(message: String, data: T? = null) = Resource(Status.FAILURE, data, message)

    }

    /**
     * ## How to use
     *  x.handler(
     *  mLoading = {},
     *  mError = {},
     *  mFailed = {},
     * ) {
     *    TODO("success scope")
     * }
    */
    fun handler(
        mLoading: (() -> Unit)? = null,
        mError: ((String) -> Unit)? = null,
        mFailed: ((String) -> Unit)? = null,
        mSuccess: (T) -> Unit,
    ) {
        when (this.success) {
            Status.LOADING -> {
                mLoading?.let { mLoading() }
                Timber.d("$TAG >>> LOADING")
            }
            Status.ERROR -> {
                mError?.let { mError(message!!) }
                Timber.d("$TAG >>> ERROR $message")
            }
            Status.FAILURE -> {
                mFailed?.let { mFailed(message!!) }
                Timber.d("$TAG >>> FAILURE $message")
            }
            Status.SUCCESS -> {
                mSuccess(data!!)
                Timber.d("$TAG >>> SUCCESS $data")
            }
        }
    }

}

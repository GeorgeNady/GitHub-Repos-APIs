package com.george.copticorphanstask.network

import android.os.Build
import androidx.annotation.RequiresApi
import com.george.copticorphanstask.util.InternetConnectionUtils.hasInternetConnection
import com.google.gson.Gson
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

abstract class BaseDataSource {

    companion object {
        const val TAG = "BaseDataSource"
    }

    // info : safe api call
    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Resource<T> {
        try {

            if (hasInternetConnection()) {

                val response = apiCall()

                if (response.isSuccessful) {
                    val body = response.body()
                    Timber.d("safeApiCall: body >>> $body")

                    if (body != null) {
                        return Resource.success(body)
                    }

                } else {

                    val gson = Gson().fromJson(response.errorBody()?.charStream(), ErrorBody::class.java)
                    return Resource.error(gson.message)

                }

                return Resource.failed("Something went wrong, try again")


            } else {

                return Resource.failed("No Internet Connection")

            }


        } catch (t: Throwable) {

            return when (t) {
                is IOException -> Resource.failed("Network Failure")
                else -> {
                    Timber.e("Conversion Error " + t.stackTraceToString())
                    Resource.failed("Conversion Error")
                }
            }

        }
    }

    // info : safe api call paging
    suspend fun <T> safeApiCallPaging(pagingLogic:(T) -> Resource<T>, apiCall: suspend () -> Response<T>): Resource<T> {
        try {

            if (hasInternetConnection()) {

                val response = apiCall()

                if (response.isSuccessful) {

                    val headers = response.headers()
                    val links = headers["Link"]
                    Timber.i("links $links")

                    response.body()?.let { resultRes ->
                        return pagingLogic(resultRes)
                        // return Resource.success(cashedResponse ?: resultRes)
                    }

                } else {

                    val gson = Gson().fromJson(response.errorBody()?.charStream(), ErrorBody::class.java)
                    return Resource.error(gson.message)

                }

                return Resource.failed("Something went wrong, try again")


            } else {

                return Resource.failed("No Internet Connection")

            }


        } catch (t: Throwable) {

            return when (t) {
                is IOException -> Resource.failed("Network Failure")
                else -> {
                    Timber.e("Conversion Error ${t.stackTraceToString()}")
                    Resource.failed("Conversion Error")
                }
            }

        }
    }

    // info: Error Body Model
    private data class ErrorBody(
        val message: String,
    )

}
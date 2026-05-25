package com.example.data.util

import com.example.data.dto.ErrorResponseDto
import com.example.data.network.ApiException
import com.example.data.network.NetworkException
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException

val json = Json { ignoreUnknownKeys = true }

suspend inline fun <T> runCatchingApi(crossinline block: suspend () -> T): Result<T> = try {
    Result.success(block())
} catch (e: HttpException) {
    val errorBody = try {
        e.response()?.errorBody()?.string()?.let {
            json.decodeFromString<ErrorResponseDto>(it)
        }
    } catch (parseException: Exception) {
        null
    }
    Result.failure(ApiException(code = e.code(), errorMessage = e.message(), errorBody = errorBody, cause = e))
} catch (e: IOException) {
    Result.failure(NetworkException(e))
} catch (e: Exception) {
    Result.failure(e)
}

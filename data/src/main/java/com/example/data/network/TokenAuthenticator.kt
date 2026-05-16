package com.example.data.network

import com.example.data.local.TokenStore
import com.example.data.remote.AuthApi
import com.example.data.dto.RefreshRequestDto
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

// Sadece HTTP 401'lerde çalış, Refresh akışı sürdür.
class TokenAuthenticator(
    private val tokenStore: TokenStore,
    private val refreshApiProvider: () -> AuthApi
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        // İsteğin tekrar tekrar buraya düşmesi -> refresh olsa bile 401 gelebilir
        if (response.priorResponseCount() >= 1) return null

        val refreshToken = tokenStore.refreshTokenBlocking() ?: return null

        return synchronized(this) {
            // Bu blokta birden fazla istek aynı anda 401 alırsa kuyruğa girer..
            val currentToken = tokenStore.accessTokenBlocking()
            val requestToken = response.request.header("Authorization")?.removePrefix("Bearer ")

            // Eğer token zaten yenilenmişse (başka bir thread tarafından), yeni token ile tekrar dene.
            if (currentToken != requestToken) {
                return@synchronized response.request.newBuilder()
                    .header("Authorization", "Bearer $currentToken")
                    .build()
            }

            // Token yenileme isteği at
            try {
                val refreshResponse = refreshApiProvider().refreshBlocking(RefreshRequestDto(refreshToken))
                tokenStore.saveBlocking(refreshResponse.accessToken, refreshResponse.refreshToken)
                
                response.request.newBuilder()
                    .header("Authorization", "Bearer ${refreshResponse.accessToken}")
                    .build()
            } catch (e: Exception) {
                tokenStore.clearBlocking()
                null
            }
        }
    }

    private fun Response.priorResponseCount(): Int {
        var count = 0
        var prior = priorResponse
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }
}

// Retrofit call'u blocking yapmak için helper (Authenticator thread'de çalıştığı için sorun olmaz)
private fun AuthApi.refreshBlocking(body: RefreshRequestDto) = kotlinx.coroutines.runBlocking {
    refresh(body)
}

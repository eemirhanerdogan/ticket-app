package com.example.data.di

import com.example.core.domain.auth.AuthRepository
import com.example.core.domain.event.EventRepository
import com.example.core.domain.TicketRepository
import com.example.data.local.TokenStore
import com.example.data.network.AuthInterceptor
import com.example.data.network.TokenAuthenticator
import com.example.data.remote.AuthApi
import com.example.data.remote.EventApi
import com.example.data.remote.TicketApi
import com.example.data.repository.AuthRepositoryImpl
import com.example.data.repository.EventRepositoryImpl
import com.example.data.repository.TicketRepositoryImpl
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

private const val BASE_URL = "https://tickets-api.halitkalayci.com/"
private val REFRESH_CLIENT = named("REFRESH_CLIENT")
private val REFRESH_RETROFIT = named("REFRESH_RETROFIT")
private val REFRESH_API = named("REFRESH_API")

val dataModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
            explicitNulls = false
            isLenient = true
        }
    }

    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single { TokenStore(context = get()) }

    single { AuthInterceptor(tokenStore = get()) }

    single(REFRESH_CLIENT) {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single(REFRESH_RETROFIT) {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get(REFRESH_CLIENT))
            .addConverterFactory(get<Json>().asConverterFactory("application/json".toMediaType()))
            .build()
    }

    single(REFRESH_API) {
        get<Retrofit>(REFRESH_RETROFIT).create(AuthApi::class.java)
    }

    single {
        TokenAuthenticator(
            tokenStore = get(),
            refreshApiProvider = { get(REFRESH_API) }
        )
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<AuthInterceptor>())
            .addInterceptor(get<HttpLoggingInterceptor>())
            .authenticator(get<TokenAuthenticator>())
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get<OkHttpClient>())
            .addConverterFactory(get<Json>().asConverterFactory("application/json".toMediaType()))
            .build()
    }

    single { get<Retrofit>().create(AuthApi::class.java) }
    single { get<Retrofit>().create(EventApi::class.java) }
    single { get<Retrofit>().create(TicketApi::class.java) }

    single<AuthRepository> {
        AuthRepositoryImpl(
            authApi = get(),
            tokenStore = get()
        )
    }

    single<EventRepository> {
        EventRepositoryImpl(eventApi = get())
    }

    single<TicketRepository> {
        TicketRepositoryImpl(ticketApi = get())
    }
}

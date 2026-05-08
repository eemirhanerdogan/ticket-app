package com.example.ticketapp.di

import com.example.core.domain.AuthRepository
import com.example.data.remote.AuthApi
import com.example.data.repository.AuthRepositoryImpl
import com.example.ticketapp.ui.login.LoginViewModel
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

val appModule = module {
    single {
        val json = Json { ignoreUnknownKeys = true }
        Retrofit.Builder()
            .baseUrl("https://example.com/") // Dummy base URL
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    single<AuthApi> { get<Retrofit>().create(AuthApi::class.java) }

    single<AuthRepository> { AuthRepositoryImpl(get()) }

    viewModelOf(::LoginViewModel)
}

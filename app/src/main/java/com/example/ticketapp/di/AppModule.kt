package com.example.ticketapp.di

import com.example.ticketapp.viewmodel.HomeViewModel
import com.example.ticketapp.viewmodel.LoginViewModel
import com.example.ticketapp.viewmodel.RegisterViewModel
import com.example.ticketapp.viewmodel.TicketDetailViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // ViewModels
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { TicketDetailViewModel(get()) }
}

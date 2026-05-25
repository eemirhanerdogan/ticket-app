package com.example.ticketapp.di

import com.example.ticketapp.viewmodel.HomeViewModel
import com.example.ticketapp.viewmodel.LoginViewModel
import com.example.ticketapp.viewmodel.RegisterViewModel
import com.example.ticketapp.viewmodel.TicketDetailViewModel
import com.example.ticketapp.viewmodel.EventDetailViewModel
import com.example.ticketapp.viewmodel.MyTicketsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // ViewModels
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { TicketDetailViewModel(get()) }
    viewModel { EventDetailViewModel(get(), get()) }
    viewModel { MyTicketsViewModel(get()) }
}

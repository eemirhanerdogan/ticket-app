package com.example.ticketapp

import android.app.Application
import com.example.data.di.dataModule
import com.example.ticketapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

// Uygulama başladığında Actvitiylerden önce oluşturulur.
// Singleton (Tek bir instance olarak memoryde kalır)
// Uygulama kapanana kadar yok edilmez..
class TicketAppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@TicketAppApplication)
            modules(
                dataModule,
                appModule
            )
        }
    }
}

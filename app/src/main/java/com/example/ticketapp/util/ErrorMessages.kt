package com.example.ticketapp.util

import com.example.data.network.ApiException
import com.example.data.network.NetworkException

fun Throwable.toUserMessage(): String {
    return when (this) {
        is ApiException -> {
            when (errorBody?.error) {
                "email_taken" -> "Bu email zaten kayıtlı"
                "capacity_exceeded" -> "Stok yetersiz, yenile"
                "already_paid" -> "Bu satın alma zaten ödenmiş"
                "not_purchase_owner" -> "Bu satın alma size ait değil"
                "invalid_token" -> "Oturum süreniz doldu. Lütfen tekrar giriş yapın."
                else -> when (code) {
                    401 -> "Oturum süreniz doldu. Lütfen tekrar giriş yapın."
                    409 -> "İşlem çakışması oluştu."
                    in 500..599 -> "Sunucu şu anda cevap veremiyor"
                    else -> message ?: "Beklenmeyen bir hata oluştu"
                }
            }
        }
        is NetworkException -> "İnternet bağlantısı yok"
        else -> message ?: "Beklenmeyen bir hata oluştu"
    }
}

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
                "not_assigned" -> "Bu görevli bu etkinliğe atanmadı."
                "ticket_not_found" -> "Bilet bulunamadı."
                "ticket_already_used" -> "Bu bilet daha önce kullanılmış olabilir."
                else -> when (code) {
                    401 -> "Oturum süreniz doldu. Lütfen tekrar giriş yapın."
                    403 -> "Bu işlem için yetkiniz yok."
                    404 -> "Bilet bulunamadı."
                    409 -> "Bu bilet daha önce kullanılmış olabilir."
                    in 500..599 -> "Sunucu şu anda cevap veremiyor"
                    else -> message ?: "Beklenmeyen bir hata oluştu"
                }
            }
        }
        is NetworkException -> "İnternet bağlantısı yok"
        else -> message ?: "Beklenmeyen bir hata oluştu"
    }
}

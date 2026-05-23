package com.example.core.domain.auth

enum class UserRole {
    USER, STAFF, ADMIN;

    companion object {
        fun fromApi(value: String?): UserRole = when (value?.uppercase()) {
            "ADMIN" -> UserRole.ADMIN
            "STAFF" -> UserRole.STAFF
            else -> UserRole.USER
        }
    }
}

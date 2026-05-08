package com.example.core.domain

data class User(
    val id: String,
    val email: String,
    val role: UserRole
)

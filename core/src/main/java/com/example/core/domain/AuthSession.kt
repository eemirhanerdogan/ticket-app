package com.example.core.domain

data class AuthSession(val user: User, val accessToken: String, val refreshToken: String)

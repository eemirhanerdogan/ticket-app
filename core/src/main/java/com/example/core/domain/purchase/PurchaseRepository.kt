package com.example.core.domain.purchase

interface PurchaseRepository {
    suspend fun createPurchase(items: List<PurchaseItemRequest>): Result<Purchase>
    suspend fun pay(id: String): Result<Purchase>
    suspend fun getPurchase(id: String): Result<Purchase>
}

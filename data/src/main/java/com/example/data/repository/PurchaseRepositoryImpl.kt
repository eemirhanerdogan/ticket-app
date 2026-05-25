package com.example.data.repository

import com.example.core.domain.purchase.Purchase
import com.example.core.domain.purchase.PurchaseItemRequest
import com.example.core.domain.purchase.PurchaseRepository
import com.example.data.dto.purchase.CreatePurchaseRequestDto
import com.example.data.dto.purchase.PurchaseItemRequestDto
import com.example.data.mapper.toDomain
import com.example.data.remote.PurchaseApi
import com.example.data.util.runCatchingApi

class PurchaseRepositoryImpl(
    private val purchaseApi: PurchaseApi
) : PurchaseRepository {
    override suspend fun createPurchase(items: List<PurchaseItemRequest>): Result<Purchase> = runCatchingApi {
        val dto = CreatePurchaseRequestDto(
            items = items.map { PurchaseItemRequestDto(it.ticketTypeId, it.quantity) }
        )
        purchaseApi.createPurchase(dto).toDomain()
    }

    override suspend fun pay(id: String): Result<Purchase> = runCatchingApi {
        purchaseApi.pay(id).toDomain()
    }

    override suspend fun getPurchase(id: String): Result<Purchase> = runCatchingApi {
        purchaseApi.getPurchase(id).toDomain()
    }
}

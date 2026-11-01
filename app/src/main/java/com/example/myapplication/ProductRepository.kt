package com.example.myapplication

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface ProductRepository {
    fun getProducts(): Flow<List<Product>>
    suspend fun getCategories(): List<String>
}

class LocalProductRepository : ProductRepository {
    override fun getProducts(): Flow<List<Product>> = flow {
        delay(300)
        emit(sampleProducts)
    }

    override suspend fun getCategories(): List<String> {
        return listOf("Все") + sampleProducts.map { it.category }.distinct()
    }
}

package com.example.androidbazar.data

import android.content.Context

class ProductsRepository (
    private val assetsProductsDataSource: AssetsProductsDataSource
) {
    fun getAll(): List<Item> {
        return assetsProductsDataSource.readProducts()
            .map { product -> product.toItem() }
    }

    fun getItem(id: Int): Item {
        return assetsProductsDataSource.getProductById(id).toItem()
    }

    fun getItemsByCategory(category: String): List<Item> {
        return assetsProductsDataSource.getProductsByCategory(category)
            .map { product -> product.toItem() }
    }

    /**
     * 'create' es la factoría para crear el singleton del repositorio para toda la app
     * */
    companion object {
        fun create(context: Context): ProductsRepository {
            return ProductsRepository(
                AssetsProductsDataSource(context)
            )
        }
    }
}

/**
 * El objeto 'Item' es el que se utilizara en toda la logica de negocio
 * */
data class Item (
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Int,
    val brand: String?,
    val category: String,
    val thumbnail: String,
    val images: List<String>
)

/**
 * Con esta funcion de extension, se convierte el objeto del DataSource en un 'Item'
 * */

fun ProductsDataSource.Product.toItem(): Item {
    return Item(
        id = this.id,
        title = this.title,
        description = this.description,
        price = this.price,
        discountPercentage = this.discountPercentage,
        rating = this.rating,
        stock = this.stock,
        brand = this.brand,
        category = this.category,
        thumbnail = this.thumbnail,
        images = this.images
    )
}

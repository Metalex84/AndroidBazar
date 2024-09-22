package com.example.androidbazar.data

/**
 * Esta clase sirve para leer el JSON con la estructura tal cual como viene
 * */

class ProductsDataSource {

    data class Bazar (
        val bazar: List<Product>
    )

    data class Product (
        val id: Int,
        val title: String,
        val description: String,
        val price: Float,
        val discountPercentage: Float,
        val rating: Float,
        val stock: Int,
        val brand: String,
        val category: String,
        val thumbnail: String,
        val images: List<String>
    )
}
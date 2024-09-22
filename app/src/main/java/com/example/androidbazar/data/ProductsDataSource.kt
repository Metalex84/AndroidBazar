package com.example.androidbazar.data

/**
 * Esta clase sirve para leer el JSON con la estructura tal cual como viene
 * */

class ProductsDataSource {

    data class Products(
        val products: List<Product>
    )

    data class Product(
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
}
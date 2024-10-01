package com.example.androidbazar.data

import android.content.Context
import com.example.androidbazar.R
import com.example.androidbazar.data.ProductsDataSource.Product
import com.example.androidbazar.data.ProductsDataSource.Products
import com.google.gson.Gson

class AssetsProductsDataSource(private val context: Context) {

    /**
     * Funcion que lee el JSON y devuelve todos los productos del bazar
     * */
    fun readProducts(): List<Product> {
        val filename = context.getString(R.string.json_filename)
        val jsonString = context.assets.open(filename).bufferedReader().use { it.readText() }
        val products = Gson().fromJson(jsonString, Products::class.java)
        return products.products
    }

    /**
     * Funcion que devuelve un producto filtrando por su ID
     * */
    fun getProductById(id: Int): Product {
        val products = readProducts()
        val product = products.filter { it.id == id }
        return product[0]
    }

    /**
     * Funcion que devuelve una lista de items filtrando por categoria
     * */
    fun getProductsByCategory(category: String): List<Product> {
        val products = readProducts()
        val itemsList = products.filter { it.category == category }
        return itemsList
    }
}
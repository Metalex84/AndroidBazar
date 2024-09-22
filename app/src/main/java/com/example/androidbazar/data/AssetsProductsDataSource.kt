package com.example.androidbazar.data

import android.content.Context
import com.example.androidbazar.R
import com.example.androidbazar.data.ProductsDataSource.Product
import com.example.androidbazar.data.ProductsDataSource.Bazar
import com.google.gson.Gson

class AssetsProductsDataSource(private val context: Context) {

    /**
     * Funcion que lee el JSON y devuelve todos los productos del bazar
     * */
    fun readProducts(): List<Product> {
        val filename = context.getString(R.string.json_filename)
        val jsonString = context.assets.open(filename).bufferedReader().use { it.readText() }
        val productList = Gson().fromJson(jsonString, Bazar::class.java)
        return productList.bazar
    }

    /**
     * Funcion que devuelve un producto filtrando por su ID
     * */
    fun getProductById(id: Int): Product {
        val products = readProducts()
        val product = products.filter { it.id == id }
        return product[0]
    }
}
package com.catzo.posterjoin.ListOfGoods

import java.io.Serializable

data class ProductModel (
        private var id: Int,
        var product_name: String,
        var menu_category_id: Int,
        var workshop: String,
        var weight_flag: String,
        var color: String,
        var different_spots_prices: Int,
        var modifications: Int,
        var barcode: String,
        var product_code: String,
        var price: Int,
        var photo: String):Serializable
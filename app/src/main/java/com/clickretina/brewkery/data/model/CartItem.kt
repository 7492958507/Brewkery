package com.clickretina.brewkery.data.model

import java.util.UUID

data class CartItem(
    val cartItemId: String = UUID.randomUUID().toString(),
    val menuItemId: Int,
    val name: String,
    val imageUrl: String,
    val selectedSize: SizeOption,
    val selectedMilk: MilkOption?,
    val selectedSugar: String,
    val basePrice: Double,
    val unitPrice: Double,
    val quantity: Int
) {
    val lineTotal: Double
        get() = unitPrice * quantity
}

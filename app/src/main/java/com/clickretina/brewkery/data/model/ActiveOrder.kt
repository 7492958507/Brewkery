package com.clickretina.brewkery.data.model

data class ActiveOrder(
    val ticketId: String,
    val itemCount: Int,
    val totalAmount: Double,
    val waitTime: String = "20 - 30 minutes",
    val status: String = "PREPARING",
    val statusMessage: String = "Barista accepted your order!",
    val placedTimestamp: Long = System.currentTimeMillis()
)

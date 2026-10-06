package com.clickretina.brewkery.data.model

import com.google.gson.annotations.SerializedName

data class MenuResponse(
    @SerializedName("meta")
    val meta: StoreMeta = StoreMeta(),
    @SerializedName("categories")
    val categories: List<Category> = emptyList(),
    @SerializedName("items")
    val items: List<MenuItem> = emptyList()
)

data class StoreMeta(
    @SerializedName("app")
    val app: String = "Brewkery",
    @SerializedName("version")
    val version: String = "1.0.0",
    @SerializedName("tagline")
    val tagline: String = "Artisanal Coffee & Fresh Oven Bakes",
    @SerializedName("currency")
    val currency: String = "USD",
    @SerializedName("currency_symbol")
    val currencySymbol: String = "$",
    @SerializedName("delivery_fee")
    val deliveryFee: Double = 2.50,
    @SerializedName("tax_rate_percent")
    val taxRatePercent: Double = 8.0,
    @SerializedName("estimated_delivery_time")
    val estimatedDeliveryTime: String = "20 - 30 mins"
)

data class Category(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("icon")
    val icon: String = "",
    @SerializedName("item_count")
    val itemCount: Int = 0
)

data class MenuItem(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("category_id")
    val categoryId: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("tagline")
    val tagline: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("base_price")
    val basePrice: Double = 0.0,
    @SerializedName("rating")
    val rating: Double = 0.0,
    @SerializedName("review_count")
    val reviewCount: Int = 0,
    @SerializedName("prep_time")
    val prepTime: String = "",
    @SerializedName("calories")
    val calories: Int = 0,
    @SerializedName("image_url")
    val imageUrl: String = "",
    @SerializedName("badge")
    val badge: String = "",
    @SerializedName("ingredients")
    val ingredients: List<String> = emptyList(),
    @SerializedName("customizations")
    val customizations: Customizations = Customizations()
)

data class Customizations(
    @SerializedName("sizes")
    val sizes: List<SizeOption> = emptyList(),
    @SerializedName("sugar_levels")
    val sugarLevels: List<String> = emptyList(),
    @SerializedName("milk_options")
    val milkOptions: List<MilkOption> = emptyList()
)

data class SizeOption(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("label")
    val label: String = "",
    @SerializedName("extra_price")
    val extraPrice: Double = 0.0
)

data class MilkOption(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("extra_price")
    val extraPrice: Double = 0.0
)

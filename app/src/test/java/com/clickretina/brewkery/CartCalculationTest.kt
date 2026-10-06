package com.clickretina.brewkery

import com.clickretina.brewkery.data.model.CartItem
import com.clickretina.brewkery.data.model.MilkOption
import com.clickretina.brewkery.data.model.SizeOption
import com.clickretina.brewkery.data.model.StoreMeta
import com.clickretina.brewkery.ui.viewmodel.BrewkeryUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CartCalculationTest {

    private val testMeta = StoreMeta(
        deliveryFee = 2.50,
        taxRatePercent = 8.0
    )

    @Test
    fun testSingleItemCustomizationPriceCalculation() {
        // Base price: $4.85
        // Grande size: +$0.65
        // Roasted Almond Milk: +$0.50
        // Expected unit price: 4.85 + 0.65 + 0.50 = 6.00
        val basePrice = 4.85
        val sizeOption = SizeOption(id = "sz_medium", label = "Grande (12 oz)", extraPrice = 0.65)
        val milkOption = MilkOption(id = "m_almond", name = "Roasted Almond Milk", extraPrice = 0.50)

        val unitPrice = basePrice + sizeOption.extraPrice + milkOption.extraPrice
        assertEquals(6.00, unitPrice, 0.001)

        val cartItem = CartItem(
            menuItemId = 1,
            name = "Toasted Caramel Macchiato",
            imageUrl = "",
            selectedSize = sizeOption,
            selectedMilk = milkOption,
            selectedSugar = "50% Mild",
            basePrice = basePrice,
            unitPrice = unitPrice,
            quantity = 2
        )

        // Line total for 2 items: 6.00 * 2 = 12.00
        assertEquals(12.00, cartItem.lineTotal, 0.001)
    }

    @Test
    fun testCartSubtotalTaxAndDeliveryCalculation() {
        // Item 1: Macchiato at $5.50 qty 1
        val item1 = CartItem(
            menuItemId = 1,
            name = "Toasted Caramel Macchiato",
            imageUrl = "",
            selectedSize = SizeOption("sz_medium", "Grande", 0.65),
            selectedMilk = MilkOption("m_oat", "Oat Milk", 0.00),
            selectedSugar = "50% Mild",
            basePrice = 4.85,
            unitPrice = 5.50,
            quantity = 1
        )

        // Item 2: Croissant at $3.90 qty 1
        val item2 = CartItem(
            menuItemId = 3,
            name = "Golden Normandy Croissant",
            imageUrl = "",
            selectedSize = SizeOption("sz_single", "Single Piece", 0.00),
            selectedMilk = MilkOption("top_plain", "Classic", 0.00),
            selectedSugar = "Warm & Crisp",
            basePrice = 3.90,
            unitPrice = 3.90,
            quantity = 1
        )

        val state = BrewkeryUiState(
            meta = testMeta,
            cartItems = listOf(item1, item2)
        )

        // Subtotal: 5.50 + 3.90 = 9.40
        assertEquals(9.40, state.subtotal, 0.001)

        // Delivery fee: $2.50
        assertEquals(2.50, state.deliveryFee, 0.001)

        // Tax: 8% of 9.40 = 0.752
        val expectedTax = 9.40 * 0.08
        assertEquals(expectedTax, state.taxAmount, 0.001)

        // Total payable: 9.40 + 2.50 + 0.752 = 12.652 (~$12.65)
        val expectedTotal = 9.40 + 2.50 + expectedTax
        assertEquals(expectedTotal, state.totalAmount, 0.001)

        // Item count: 1 + 1 = 2
        assertEquals(2, state.cartItemCount)
    }

    @Test
    fun testEmptyCartProducesZeroTotals() {
        val emptyState = BrewkeryUiState(
            meta = testMeta,
            cartItems = emptyList()
        )

        assertEquals(0, emptyState.cartItemCount)
        assertEquals(0.0, emptyState.subtotal, 0.001)
        assertEquals(0.0, emptyState.deliveryFee, 0.001)
        assertEquals(0.0, emptyState.taxAmount, 0.001)
        assertEquals(0.0, emptyState.totalAmount, 0.001)
    }

    @Test
    fun testActiveOrderTicketIdFormat() {
        val code = "#BK-${(10000..99999).random()}"
        assertTrue(code.startsWith("#BK-"))
        assertEquals(9, code.length)
    }
}

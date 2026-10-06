package com.clickretina.brewkery.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clickretina.brewkery.data.model.ActiveOrder
import com.clickretina.brewkery.data.model.CartItem
import com.clickretina.brewkery.data.model.Category
import com.clickretina.brewkery.data.model.MenuItem
import com.clickretina.brewkery.data.model.MilkOption
import com.clickretina.brewkery.data.model.SizeOption
import com.clickretina.brewkery.data.model.StoreMeta
import com.clickretina.brewkery.data.remote.NetworkResult
import com.clickretina.brewkery.data.repository.BrewkeryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

data class BrewkeryUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val meta: StoreMeta = StoreMeta(),
    val categories: List<Category> = emptyList(),
    val items: List<MenuItem> = emptyList(),
    val selectedCategoryId: String = "ALL",
    val searchQuery: String = "",
    val favoriteItemIds: Set<Int> = emptySet(),
    val cartItems: List<CartItem> = emptyList(),
    val activeOrder: ActiveOrder? = null
) {
    val filteredItems: List<MenuItem>
        get() = items.filter { item ->
            val matchesCategory = (selectedCategoryId == "ALL" || item.categoryId == selectedCategoryId)
            val matchesQuery = searchQuery.isBlank() ||
                    item.name.contains(searchQuery, ignoreCase = true) ||
                    item.tagline.contains(searchQuery, ignoreCase = true) ||
                    item.description.contains(searchQuery, ignoreCase = true) ||
                    item.ingredients.any { it.contains(searchQuery, ignoreCase = true) }
            matchesCategory && matchesQuery
        }

    val cartItemCount: Int
        get() = cartItems.sumOf { it.quantity }

    val subtotal: Double
        get() = cartItems.sumOf { it.lineTotal }

    val deliveryFee: Double
        get() = if (cartItems.isNotEmpty()) meta.deliveryFee else 0.0

    val taxAmount: Double
        get() = if (cartItems.isNotEmpty()) (subtotal * (meta.taxRatePercent / 100.0)) else 0.0

    val totalAmount: Double
        get() = if (cartItems.isNotEmpty()) (subtotal + deliveryFee + taxAmount) else 0.0
}

class BrewkeryViewModel(
    private val repository: BrewkeryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BrewkeryUiState())
    val uiState: StateFlow<BrewkeryUiState> = _uiState.asStateFlow()

    init {
        loadMenu()
    }

    fun loadMenu() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = repository.getFullMenu()) {
                is NetworkResult.Success -> {
                    _uiState.update { current ->
                        current.copy(
                            isLoading = false,
                            meta = result.data.meta,
                            categories = result.data.categories,
                            items = result.data.items,
                            errorMessage = null
                        )
                    }
                }
                is NetworkResult.Error -> {
                    _uiState.update { current ->
                        current.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }
                is NetworkResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    fun selectCategory(categoryId: String) {
        _uiState.update { it.copy(selectedCategoryId = categoryId) }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun toggleFavorite(itemId: Int) {
        _uiState.update { state ->
            val updated = if (state.favoriteItemIds.contains(itemId)) {
                state.favoriteItemIds - itemId
            } else {
                state.favoriteItemIds + itemId
            }
            state.copy(favoriteItemIds = updated)
        }
    }

    fun addToCart(
        menuItem: MenuItem,
        selectedSize: SizeOption,
        selectedMilk: MilkOption?,
        selectedSugar: String,
        quantity: Int
    ) {
        val unitPrice = menuItem.basePrice + selectedSize.extraPrice + (selectedMilk?.extraPrice ?: 0.0)
        val newItem = CartItem(
            menuItemId = menuItem.id,
            name = menuItem.name,
            imageUrl = menuItem.imageUrl,
            selectedSize = selectedSize,
            selectedMilk = selectedMilk,
            selectedSugar = selectedSugar,
            basePrice = menuItem.basePrice,
            unitPrice = unitPrice,
            quantity = quantity
        )

        _uiState.update { state ->
            val existingIndex = state.cartItems.indexOfFirst {
                it.menuItemId == newItem.menuItemId &&
                        it.selectedSize.id == newItem.selectedSize.id &&
                        it.selectedMilk?.id == newItem.selectedMilk?.id &&
                        it.selectedSugar == newItem.selectedSugar
            }

            val updatedCart = if (existingIndex != -1) {
                state.cartItems.toMutableList().apply {
                    val existing = this[existingIndex]
                    this[existingIndex] = existing.copy(quantity = existing.quantity + quantity)
                }
            } else {
                state.cartItems + newItem
            }
            state.copy(cartItems = updatedCart)
        }
    }

    fun updateCartQuantity(cartItemId: String, delta: Int) {
        _uiState.update { state ->
            val updated = state.cartItems.mapNotNull { item ->
                if (item.cartItemId == cartItemId) {
                    val newQty = item.quantity + delta
                    if (newQty > 0) item.copy(quantity = newQty) else null
                } else {
                    item
                }
            }
            state.copy(cartItems = updated)
        }
    }

    fun clearCart() {
        _uiState.update { it.copy(cartItems = emptyList()) }
    }

    fun placeOrder(): ActiveOrder? {
        val currentState = _uiState.value
        if (currentState.cartItems.isEmpty()) return null

        val randomCode = "#BK-${Random.nextInt(10000, 99999)}"
        val activeOrder = ActiveOrder(
            ticketId = randomCode,
            itemCount = currentState.cartItemCount,
            totalAmount = currentState.totalAmount,
            waitTime = currentState.meta.estimatedDeliveryTime.ifBlank { "20 - 30 minutes" },
            status = "PREPARING",
            statusMessage = "Barista accepted your order!"
        )

        _uiState.update { state ->
            state.copy(
                cartItems = emptyList(),
                activeOrder = activeOrder
            )
        }

        return activeOrder
    }
}

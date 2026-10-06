package com.clickretina.brewkery.ui.screens.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clickretina.brewkery.ui.theme.Crema
import com.clickretina.brewkery.ui.theme.CremaDark
import com.clickretina.brewkery.ui.theme.RoseFavorite
import com.clickretina.brewkery.ui.theme.Terracotta
import com.clickretina.brewkery.ui.theme.TextDark
import com.clickretina.brewkery.ui.theme.TextMuted
import com.clickretina.brewkery.ui.theme.WarmBorder
import com.clickretina.brewkery.ui.viewmodel.BrewkeryViewModel
import java.util.Locale

@Composable
fun CartScreen(
    viewModel: BrewkeryViewModel,
    onBackClick: () -> Unit,
    onOrderPlaced: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Crema,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.dp, WarmBorder, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextDark,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Text(
                    text = "YOUR CART",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextDark,
                    letterSpacing = 0.5.sp
                )

                if (uiState.cartItems.isNotEmpty()) {
                    Text(
                        text = "Clear Cart",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoseFavorite,
                        modifier = Modifier
                            .clickable { viewModel.clearCart() }
                            .padding(4.dp)
                    )
                } else {
                    Spacer(modifier = Modifier.size(36.dp))
                }
            }
        },
        bottomBar = {
            if (uiState.cartItems.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Crema)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    // Bill Breakdown Card
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .border(1.dp, WarmBorder, RoundedCornerShape(16.dp))
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Subtotal", fontSize = 12.sp, color = TextMuted)
                            Text(
                                text = String.format(Locale.US, "$%.2f", uiState.subtotal),
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.SemiBold,
                                color = TextDark
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Delivery Fee", fontSize = 12.sp, color = TextMuted)
                            Text(
                                text = String.format(Locale.US, "$%.2f", uiState.deliveryFee),
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.SemiBold,
                                color = TextDark
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Est. Tax (${uiState.meta.taxRatePercent}%)",
                                fontSize = 12.sp,
                                color = TextMuted
                            )
                            Text(
                                text = String.format(Locale.US, "$%.2f", uiState.taxAmount),
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.SemiBold,
                                color = TextDark
                            )
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 4.dp),
                            thickness = 1.dp,
                            color = WarmBorder
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Total Payable",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = TextDark
                            )
                            Text(
                                text = String.format(Locale.US, "$%.2f", uiState.totalAmount),
                                fontSize = 16.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Black,
                                color = Terracotta
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Place Order Now Button
                    Button(
                        onClick = {
                            val order = viewModel.placeOrder()
                            if (order != null) {
                                onOrderPlaced()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Terracotta)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Checkout",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "Place Order Now",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Text(text = "•", fontSize = 12.sp, color = Color.White)
                            Text(
                                text = String.format(Locale.US, "$%.2f", uiState.totalAmount),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        if (uiState.cartItems.isEmpty()) {
            // Empty State
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(CremaDark)
                            .border(1.dp, WarmBorder, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ShoppingCart,
                            contentDescription = "Empty Cart",
                            tint = TextMuted,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Your Cart is Empty",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Explore our freshly brewed coffees & artisanal pastries to begin your order.",
                        fontSize = 12.sp,
                        color = TextMuted,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else {
            // Cart Items List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp)
            ) {
                items(uiState.cartItems, key = { it.cartItemId }) { cartItem ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White)
                            .border(1.dp, WarmBorder, RoundedCornerShape(14.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = cartItem.name,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                            val details = buildString {
                                append(cartItem.selectedSize.label)
                                if (cartItem.selectedMilk != null) {
                                    append(" • ")
                                    append(cartItem.selectedMilk.name)
                                }
                                if (cartItem.selectedSugar.isNotBlank()) {
                                    append(" • ")
                                    append(cartItem.selectedSugar)
                                }
                            }
                            Text(
                                text = details,
                                fontSize = 10.sp,
                                color = TextMuted,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                            Text(
                                text = String.format(Locale.US, "$%.2f", cartItem.lineTotal),
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = Terracotta,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        // Stepper [-] [qty] [+]
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(CremaDark)
                                .border(1.dp, WarmBorder, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "—",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = TextDark,
                                modifier = Modifier.clickable {
                                    viewModel.updateCartQuantity(cartItem.cartItemId, -1)
                                }
                            )
                            Text(
                                text = "${cartItem.quantity}",
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                            Text(
                                text = "+",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = TextDark,
                                modifier = Modifier.clickable {
                                    viewModel.updateCartQuantity(cartItem.cartItemId, 1)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

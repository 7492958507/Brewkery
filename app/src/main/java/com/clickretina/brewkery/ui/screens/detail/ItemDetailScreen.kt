package com.clickretina.brewkery.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.clickretina.brewkery.data.model.MenuItem
import com.clickretina.brewkery.data.model.MilkOption
import com.clickretina.brewkery.data.model.SizeOption
import com.clickretina.brewkery.ui.theme.Caramel
import com.clickretina.brewkery.ui.theme.Crema
import com.clickretina.brewkery.ui.theme.CremaDark
import com.clickretina.brewkery.ui.theme.Espresso
import com.clickretina.brewkery.ui.theme.RoseFavorite
import com.clickretina.brewkery.ui.theme.Terracotta
import com.clickretina.brewkery.ui.theme.TerracottaLight
import com.clickretina.brewkery.ui.theme.TextDark
import com.clickretina.brewkery.ui.theme.TextMuted
import com.clickretina.brewkery.ui.theme.WarmBorder
import com.clickretina.brewkery.ui.viewmodel.BrewkeryViewModel
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ItemDetailScreen(
    itemId: Int,
    viewModel: BrewkeryViewModel,
    onBackClick: () -> Unit,
    onAddedToCart: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val item: MenuItem? = uiState.items.find { it.id == itemId }

    if (item == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Crema),
            contentAlignment = Alignment.Center
        ) {
            Text("Item not found", color = TextDark)
        }
        return
    }

    val isFavorite = uiState.favoriteItemIds.contains(item.id)

    // Selection States
    val defaultSize = item.customizations.sizes.firstOrNull() ?: SizeOption(id = "default", label = "Regular", extraPrice = 0.0)
    val defaultMilk = item.customizations.milkOptions.firstOrNull()
    val defaultSugar = item.customizations.sugarLevels.firstOrNull() ?: "Standard"

    var selectedSize by remember(item) { mutableIntStateOf(0) }
    var selectedMilk by remember(item) { mutableIntStateOf(0) }
    var selectedSugar by remember(item) { mutableIntStateOf(0) }
    var quantity by remember { mutableIntStateOf(1) }

    val currentSizeOption = item.customizations.sizes.getOrElse(selectedSize) { defaultSize }
    val currentMilkOption = item.customizations.milkOptions.getOrNull(selectedMilk) ?: defaultMilk
    val currentSugarLevel = item.customizations.sugarLevels.getOrElse(selectedSugar) { defaultSugar }

    // Live Total Price Calculation: (base_price + sizeExtra + milkExtra) * quantity
    val unitPrice = item.basePrice + currentSizeOption.extraPrice + (currentMilkOption?.extraPrice ?: 0.0)
    val liveTotal = unitPrice * quantity

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
                    text = "ITEM CUSTOMIZER",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextDark,
                    letterSpacing = 0.5.sp
                )

                IconButton(
                    onClick = { viewModel.toggleFavorite(item.id) },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.dp, WarmBorder, CircleShape)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) RoseFavorite else TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        },
        bottomBar = {
            // Quantity Stepper and Add to Cart Action
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Crema)
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Stepper
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .border(1.dp, WarmBorder, RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "—",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = TextDark,
                        modifier = Modifier.clickable {
                            if (quantity > 1) quantity--
                        }
                    )
                    Text(
                        text = "$quantity",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Text(
                        text = "+",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = TextDark,
                        modifier = Modifier.clickable {
                            quantity++
                        }
                    )
                }

                // Add to Cart Button with dynamically computed total
                Button(
                    onClick = {
                        viewModel.addToCart(
                            menuItem = item,
                            selectedSize = currentSizeOption,
                            selectedMilk = currentMilkOption,
                            selectedSugar = currentSugarLevel,
                            quantity = quantity
                        )
                        onAddedToCart()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Terracotta)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Add to Cart",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(text = "•", fontSize = 12.sp, color = Color.White)
                        Text(
                            text = String.format(Locale.US, "$%.2f", liveTotal),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // Hero Image Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFF3ECE7))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(item.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = item.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                if (item.badge.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(10.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Espresso.copy(alpha = 0.85f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = item.badge,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = Caramel,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Title and Base Price
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = TextDark,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = String.format(Locale.US, "$%.2f", item.basePrice),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = Terracotta
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Description
            Text(
                text = item.description,
                fontSize = 12.sp,
                color = TextMuted,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Key Ingredients Chips
            if (item.ingredients.isNotEmpty()) {
                Text(
                    text = "KEY INGREDIENTS",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = TextMuted,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    item.ingredients.forEach { ingredient ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White)
                                .border(1.dp, WarmBorder, RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = ingredient,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextDark
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // 1. Size Options
            if (item.customizations.sizes.isNotEmpty()) {
                Text(
                    text = "CHOOSE SIZE",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = TextMuted,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item.customizations.sizes.forEachIndexed { index, sizeOpt ->
                        val isSelected = selectedSize == index
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) TerracottaLight else Color.White)
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) Terracotta else WarmBorder,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { selectedSize = index }
                                .padding(vertical = 10.dp, horizontal = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = sizeOpt.label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                    color = if (isSelected) Terracotta else TextDark
                                )
                                Text(
                                    text = if (sizeOpt.extraPrice > 0) "+$${String.format(Locale.US, "%.2f", sizeOpt.extraPrice)}" else "Included",
                                    fontSize = 10.sp,
                                    color = if (isSelected) Terracotta else TextMuted
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // 2. Milk Options / Spreads
            if (item.customizations.milkOptions.isNotEmpty()) {
                Text(
                    text = "MILK OPTIONS / SPREADS",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = TextMuted,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    item.customizations.milkOptions.forEachIndexed { index, milkOpt ->
                        val isSelected = selectedMilk == index
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) TerracottaLight else Color.White)
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) Terracotta else WarmBorder,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { selectedMilk = index }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = milkOpt.name,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                color = if (isSelected) Terracotta else TextDark
                            )
                            Text(
                                text = if (milkOpt.extraPrice > 0) "+$${String.format(Locale.US, "%.2f", milkOpt.extraPrice)}" else "Free",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Terracotta else TextMuted
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // 3. Sugar Levels / Serving
            if (item.customizations.sugarLevels.isNotEmpty()) {
                Text(
                    text = "SUGAR LEVEL / SERVING",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = TextMuted,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    item.customizations.sugarLevels.forEachIndexed { index, sugarVal ->
                        val isSelected = selectedSugar == index
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) Espresso else Color.White)
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) Espresso else WarmBorder,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable { selectedSugar = index }
                                .padding(horizontal = 12.dp, vertical = 7.dp)
                        ) {
                            Text(
                                text = sugarVal,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                color = if (isSelected) Color.White else TextDark
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

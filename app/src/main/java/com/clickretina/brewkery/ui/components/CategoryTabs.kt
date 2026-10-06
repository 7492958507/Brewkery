package com.clickretina.brewkery.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clickretina.brewkery.data.model.Category
import com.clickretina.brewkery.ui.theme.Espresso
import com.clickretina.brewkery.ui.theme.TextDark
import com.clickretina.brewkery.ui.theme.TextMuted
import com.clickretina.brewkery.ui.theme.WarmBorder

@Composable
fun CategoryTabs(
    categories: List<Category>,
    selectedCategoryId: String,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // "All Items" tab
        val isAllSelected = selectedCategoryId == "ALL"
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(999.dp))
                .background(if (isAllSelected) Espresso else Color.White)
                .border(
                    width = 1.dp,
                    color = if (isAllSelected) Espresso else WarmBorder,
                    shape = RoundedCornerShape(999.dp)
                )
                .clickable { onCategorySelected("ALL") }
                .padding(horizontal = 14.dp, vertical = 7.dp)
        ) {
            Text(
                text = "All Items",
                fontSize = 12.sp,
                fontWeight = if (isAllSelected) FontWeight.Bold else FontWeight.SemiBold,
                color = if (isAllSelected) Color.White else TextMuted
            )
        }

        // Dynamic categories from API
        categories.forEach { category ->
            val isSelected = selectedCategoryId == category.id
            val label = if (category.icon.isNotBlank()) "${category.icon} ${category.name}" else category.name

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(if (isSelected) Espresso else Color.White)
                    .border(
                        width = 1.dp,
                        color = if (isSelected) Espresso else WarmBorder,
                        shape = RoundedCornerShape(999.dp)
                    )
                    .clickable { onCategorySelected(category.id) }
                    .padding(horizontal = 14.dp, vertical = 7.dp)
            ) {
                Text(
                    text = label,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                    color = if (isSelected) Color.White else TextMuted
                )
            }
        }
    }
}

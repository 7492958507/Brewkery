package com.clickretina.brewkery.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.clickretina.brewkery.data.model.MenuItem
import com.clickretina.brewkery.ui.theme.Caramel
import com.clickretina.brewkery.ui.theme.Espresso
import com.clickretina.brewkery.ui.theme.Terracotta
import com.clickretina.brewkery.ui.theme.TextDark
import com.clickretina.brewkery.ui.theme.TextMuted
import com.clickretina.brewkery.ui.theme.WarmBorder
import java.util.Locale

@Composable
fun MenuItemCard(
    item: MenuItem,
    onItemClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, WarmBorder, RoundedCornerShape(16.dp))
            .clickable { onItemClicked() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Thumbnail with Badge
        Box(
            modifier = Modifier
                .size(88.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF3ECE7))
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            if (item.badge.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(4.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Espresso.copy(alpha = 0.85f))
                        .padding(horizontal = 5.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = item.badge,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black,
                        color = Caramel,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Content details
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = item.tagline,
                fontSize = 11.sp,
                color = TextMuted,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 15.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = Caramel,
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = String.format(Locale.US, "%.1f", item.rating),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Text(
                    text = "(${item.reviewCount})",
                    fontSize = 10.sp,
                    color = TextMuted
                )
                if (item.prepTime.isNotBlank()) {
                    Text(text = "•", fontSize = 10.sp, color = TextMuted)
                    Text(text = item.prepTime, fontSize = 10.sp, color = TextMuted)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = String.format(Locale.US, "$%.2f", item.basePrice),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = Terracotta
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Terracotta.copy(alpha = 0.12f))
                        .clickable { onItemClicked() }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Customize",
                            tint = Terracotta,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "Customize",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Terracotta
                        )
                    }
                }
            }
        }
    }
}

package com.clickretina.brewkery.ui.screens.order

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
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
import com.clickretina.brewkery.ui.theme.AmberBadgeBg
import com.clickretina.brewkery.ui.theme.AmberBadgeText
import com.clickretina.brewkery.ui.theme.Crema
import com.clickretina.brewkery.ui.theme.CremaDark
import com.clickretina.brewkery.ui.theme.Espresso
import com.clickretina.brewkery.ui.theme.SuccessGreenDark
import com.clickretina.brewkery.ui.theme.Terracotta
import com.clickretina.brewkery.ui.theme.TextDark
import com.clickretina.brewkery.ui.theme.TextMuted
import com.clickretina.brewkery.ui.theme.WarmBorder
import com.clickretina.brewkery.ui.viewmodel.BrewkeryViewModel

@Composable
fun OrderStatusScreen(
    viewModel: BrewkeryViewModel,
    onBackToMenu: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val activeOrder = uiState.activeOrder

    val ticketId = activeOrder?.ticketId ?: "#BK-74921"
    val waitTime = activeOrder?.waitTime ?: "20 - 30 minutes"
    val itemCount = activeOrder?.itemCount ?: 1
    val statusMsg = activeOrder?.statusMessage ?: "Barista accepted your order!"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Crema)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Center Content: Illustration + Ticket Card
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Animated Mug Badge
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(CremaDark)
                    .border(2.dp, Terracotta, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "☕", fontSize = 36.sp)
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "ORDER DISPATCHED",
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                color = Terracotta,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Brewing in Progress!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Your ticket was dispatched to our barista.",
                fontSize = 12.sp,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Ticket Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.White)
                    .border(1.dp, WarmBorder, RoundedCornerShape(18.dp))
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "ORDER TICKET",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = ticketId,
                            fontSize = 16.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Black,
                            color = TextDark
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(999.dp))
                            .background(AmberBadgeBg)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "PREPARING",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = AmberBadgeText
                        )
                    }
                }

                HorizontalDivider(color = WarmBorder.copy(alpha = 0.6f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Estimated Wait:", fontSize = 12.sp, color = TextMuted)
                    Text(
                        text = waitTime,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Terracotta
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Items Ordered:", fontSize = 12.sp, color = TextMuted)
                    Text(
                        text = "$itemCount Item${if (itemCount > 1) "s" else ""}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                }

                HorizontalDivider(color = WarmBorder.copy(alpha = 0.6f))

                Column {
                    Text(text = "Status:", fontSize = 10.sp, color = TextMuted)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = statusMsg,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SuccessGreenDark
                    )
                }
            }
        }

        // Return Back to Menu Button
        Button(
            onClick = onBackToMenu,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Espresso)
        ) {
            Text(
                text = "Back to Menu",
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
        }
    }
}

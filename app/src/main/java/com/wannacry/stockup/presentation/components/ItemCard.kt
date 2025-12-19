package com.wannacry.stockup.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wannacry.stockup.config.StockIndicator
import com.wannacry.stockup.domain.data.Item
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ItemCard(
    item: Item,
    onMarkAsFinished: (Item) -> Unit,
    stockIndicator: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
//    val isFinished = item.stockIndicator == StockIndicator.EMPTY

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                onClick = onClick
            ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = item.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onPrimaryContainer)
            Text(text = "${item.quantity} ${item.unit}", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            if (item.expiryDate != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarToday, contentDescription = "Expiry Date", modifier = Modifier.padding(end = 8.dp))
                    Text(
                        text = "Tarikh Luput: ${item.expiryDate?.format(DateTimeFormatter.ofPattern("d MMM yyyy")) ?: ""}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                when (stockIndicator) {
                    StockIndicator.EMPTY.name -> {
                        Text(
                            text = "Stok telah habis",
                            color = Color.White,
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.error,
                                    shape = MaterialTheme.shapes.small
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    StockIndicator.LOW.name -> {
                        Text(
                            text = "Stok hampir habis",
                            color = Color.White,
                            modifier = Modifier
                                .background(Color(0xFFFFA500), shape = MaterialTheme.shapes.small)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    StockIndicator.AVAILABLE.name -> {
                        Text(
                            text = "Available",
                            color = Color.White,
                            modifier = Modifier
                                .background(Color(0xFF4CAF50), shape = MaterialTheme.shapes.small)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    StockIndicator.EXPIRED.name -> {
                        Text(
                            text = "Expired",
                            color = Color.White,
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.error, shape = MaterialTheme.shapes.small)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
//                Button(
//                    onClick = { onMarkAsFinished(item) },
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = if (isFinished) Color.Red else Color.Transparent,
//                        contentColor = if (isFinished) Color.White else Color.Red
//                    ),
//                    shape = MaterialTheme.shapes.medium
//                ) {
//                    Text(if (isFinished) "Telah Habis" else "Tandakan Habis")
//                }
            }
        }
    }
}
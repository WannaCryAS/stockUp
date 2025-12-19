package com.wannacry.stockup.presentation.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.wannacry.stockup.R
import com.wannacry.stockup.config.StockIndicator
import com.wannacry.stockup.domain.data.Category
import com.wannacry.stockup.domain.data.Item
import com.wannacry.stockup.presentation.components.ItemCard
import com.wannacry.stockup.presentation.navigation.Screen
import com.wannacry.stockup.presentation.viewmodel.StockUpViewModel
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: StockUpViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var isSearchActive by remember { mutableStateOf(false) }

    Scaffold(modifier = modifier, topBar = {
        TopAppBar(title = {
            AnimatedContent(
                targetState = isSearchActive,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "SearchTitle"
            ) { searching ->
                if (searching) {
                    TextField(
                        value = uiState.searchQuery,
                        onValueChange = viewModel::onSearchQueryChange,
                        placeholder = { Text("Search items...") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true
                    )
                } else {
                    Text(
                        text = "StockUp",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
        }, actions = {
            AnimatedContent(
                targetState = isSearchActive,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "SearchAction"
            ) {
                if (it) {
                    IconButton(onClick = {
                        isSearchActive = false
                        viewModel.onSearchQueryChange("")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Search"
                        )
                    }
                } else {
                    IconButton(onClick = { isSearchActive = true }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
                }
            }
        })
    }, floatingActionButton = {
        FloatingActionButton(onClick = { navController.navigate(Screen.AddItem.route) }) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Item"
            )
        }
    }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            // Stock Status Filters
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = uiState.selectedStockFilter == StockIndicator.LOW.name,
                        onClick = {
                            val newFilter =
                                if (uiState.selectedStockFilter == StockIndicator.LOW.name) {
                                    null
                                }
                                else {
                                    StockIndicator.LOW.name
                                }
                            viewModel.selectStockFilter(newFilter)
                        },
                        label = { Text("Stock Low") }
                    )
                }
                item {
                    FilterChip(
                        selected = uiState.selectedStockFilter == StockIndicator.EMPTY.name,
                        onClick = {
                            val newFilter =
                                if (uiState.selectedStockFilter == StockIndicator.EMPTY.name) {
                                    null
                                }
                                else {
                                    StockIndicator.EMPTY.name
                                }
                            viewModel.selectStockFilter(newFilter)
                        },
                        label = { Text("Out of Stock") }
                    )
                }

                // Divider line between stock filters and categories (optional but good for UX)
//                item {
//                    VerticalDivider(
//                        modifier = Modifier
//                            .size(32.dp),
//                    )
//                }

                // Category Filters
                items(uiState.categories) { category ->
                    FilterChip(
                        selected = uiState.selectedCategory == category,
                        onClick = {
                            val newSelection =
                                if (uiState.selectedCategory == category) null else category
                            viewModel.selectCategory(newSelection)
                        },
                        label = { Text(category.name) }
                    )
                }
            }

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                ShowListItem(
                    items = uiState.items,
                    category = uiState.selectedCategory,
                    viewModel = viewModel,
                    navController = navController
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowListItem(
    items: List<Item>?,
    category: Category?,
    viewModel: StockUpViewModel = koinViewModel(),
    navController: NavController
) {
    val categoryString = if (category?.name.isNullOrEmpty()) "" else "for ${category.name}"
    if (items.isNullOrEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.no_data_foreground),
                    contentDescription = "No Data",
                    modifier = Modifier.size(350.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "No data found ${categoryString}. Please add data by click '+' button",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }
        }
    } else {
        LazyColumn(contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp)) {
            items(items) { item ->
                ItemCard(
                    item = item,
                    onMarkAsFinished = { updatedItem ->
                        val newStatus =
                            if (updatedItem.stockIndicator == StockIndicator.EMPTY.name) {
                                StockIndicator.AVAILABLE.name
                            }
                            else {
                                StockIndicator.EMPTY.name
                            }
                        viewModel.updateItem(updatedItem.copy(stockIndicator = newStatus))
                    },
                    stockIndicator = viewModel.stockIndicator(
                        quantity = item.quantity.toString(),
                        minLimit = item.minLimit.toString(),
                        expiryDate = item.expiryDate
                    ),
                    onClick = { navController.navigate(
                        route = Screen.ItemDetail.createRoute(
                            itemId = item.id.toString()
                        )
                    ) }
                )
            }
        }
    }
}
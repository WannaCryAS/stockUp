package com.wannacry.stockup.presentation.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.wannacry.stockup.domain.data.Category
import com.wannacry.stockup.presentation.components.Dropdown
import com.wannacry.stockup.presentation.viewmodel.BaseStockUpViewModel
import org.koin.androidx.compose.koinViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemScreen(
    navController: NavController,
    viewModel: BaseStockUpViewModel = koinViewModel(),
    itemId: String? = null
) {
    val uiState by viewModel.uiState.collectAsState()
    val isEditMode = itemId != null

    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("") }
    var limit by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var expiryDate by remember { mutableStateOf<LocalDate?>(null) }

    var categoryMenuExpanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(itemId) {
        if (isEditMode) {
            viewModel.getItemDetails(UUID.fromString(itemId))
        }
    }

    val detailUiState by viewModel.itemDetailUiState.collectAsState()
    LaunchedEffect(detailUiState) {
        detailUiState.item?.let {
            name = it.name
            quantity = it.quantity.toString()
            unit = it.unit
            limit = it.minLimit.toString()
            expiryDate = it.expiryDate
            selectedCategory = uiState.categories.find { c -> c.id == it.categoryId }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Edit Bahan" else "Tambah Bahan") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Nama Bahan", style = MaterialTheme.typography.bodyLarge)
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = { Text("Contoh: Tepung Gandum") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text("Kuantiti", style = MaterialTheme.typography.bodyLarge)
            OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it },
                placeholder = { Text("Contoh: 500") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text("Unit", style = MaterialTheme.typography.bodyLarge)
            OutlinedTextField(
                value = unit,
                onValueChange = { unit = it },
                placeholder = { Text("Contoh: kg, liter, pcs") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text("Had Minimum", style = MaterialTheme.typography.bodyLarge)
            OutlinedTextField(
                value = limit,
                onValueChange = { limit = it },
                placeholder = { Text("Contoh: 10") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text("Tarikh Luput (Pilihan)", style = MaterialTheme.typography.bodyLarge)
            Box(modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                onClick = { showDatePicker = true }
            )) {
                OutlinedTextField(
                    value = expiryDate?.format(DateTimeFormatter.ofPattern("d MMM yyyy")) ?: "Pilih tarikh",
                    onValueChange = {},
                    enabled = false,
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null) },
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text("Kategori (Pilihan)", style = MaterialTheme.typography.bodyLarge)
            Dropdown(
                text = selectedCategory?.name ?: "Pilih kategori",
                expanded = categoryMenuExpanded,
                onExpandChange = { categoryMenuExpanded = it }
            ) {
                uiState.categories.forEachIndexed { index, category ->
                    DropdownMenuItem(
                        text = { Text(category.name) },
                        onClick = {
                            selectedCategory = category
                            categoryMenuExpanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                    if (index < uiState.categories.lastIndex) {
                        HorizontalDivider(modifier = Modifier.padding(8.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.padding(all = 24.dp))

            Button(
                onClick = {
                    if (isEditMode) {
                        val updatedItem = detailUiState.item?.copy(
                            name = name,
                            quantity = quantity.toDoubleOrNull() ?: 0.0,
                            unit = unit,
                            minLimit = limit.toDoubleOrNull() ?: 0.0,
                            expiryDate = expiryDate,
                            categoryId = selectedCategory?.id,
                            stockIndicator = viewModel.stockIndicator(quantity, limit, expiryDate)
                        )
                        updatedItem?.let { viewModel.updateItem(it) }
                    } else {
                        viewModel.addItem(name, quantity, unit, limit, expiryDate, selectedCategory?.id, viewModel.stockIndicator(quantity, limit))
                    }
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF20C978)),
                enabled = name.isNotBlank() && quantity.isNotBlank() && unit.isNotBlank(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Simpan", color = Color.White)
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        expiryDate = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
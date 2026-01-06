package com.wannacry.stockup.presentation.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.wannacry.stockup.R
import com.wannacry.stockup.domain.data.Item
import com.wannacry.stockup.domain.data.Task
import com.wannacry.stockup.presentation.components.DialogBox
import com.wannacry.stockup.presentation.components.TaskCard
import com.wannacry.stockup.presentation.navigation.Screen
import com.wannacry.stockup.presentation.viewmodel.TaskViewModel
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    navController: NavController,
    viewModel: TaskViewModel = koinViewModel()
) {
    val tasks by viewModel.tasks.collectAsState()
    val allItemsState by viewModel.uiState.collectAsState()
    
    var showAddTaskDialog by remember { mutableStateOf(false) }
    var taskToDelete by remember { mutableStateOf<Task?>(null) }
    
    var taskTitle by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    val selectedMaterials = remember { mutableStateListOf<Pair<Item, Double>>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tugasan", fontWeight = FontWeight.Bold) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddTaskDialog = true },
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Tugasan")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (tasks.isEmpty()) {
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
                            text = "Anda belum mempunyai tugasan. Klik butang '+' untuk menambah tugasan",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 32.dp)
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(tasks, key = { it.id }) { task ->
                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = { value ->
                                if (value == SwipeToDismissBoxValue.EndToStart) {
                                    taskToDelete = task
                                }
                                false
                            }
                        )

                        SwipeToDismissBox(
                            state = dismissState,
                            enableDismissFromStartToEnd = false,
                            backgroundContent = {
                                val color = if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                                    Color.Red.copy(alpha = 0.8f)
                                } else Color.Transparent
                                
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(color, RoundedCornerShape(16.dp))
                                        .padding(horizontal = 24.dp),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Padam",
                                        tint = Color.White
                                    )
                                }
                            }
                        ) {
                            TaskCard(
                                task = task,
                                viewModel = viewModel,
                                onClick = { 
                                    navController.navigate(Screen.TaskDetail.createRoute(task.id.toString()))
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddTaskDialog) {
        Dialog(
            onDismissRequest = { showAddTaskDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth()
                ) {
                    Text("Tugasan Baharu", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = taskTitle,
                        onValueChange = { taskTitle = it },
                        label = { Text("Nama Tugasan") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = taskDescription,
                        onValueChange = { taskDescription = it },
                        label = { Text("Deskripsi (Pilihan)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        minLines = 2
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Text("Bahan yang Diperlukan", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    var tempSelectedItem by remember { mutableStateOf<Item?>(null) }
                    var tempQty by remember { mutableStateOf("") }
                    var isDropdownExpanded by remember { mutableStateOf(false) }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = tempSelectedItem?.name ?: "Pilih Bahan",
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier.fillMaxWidth().clickable { isDropdownExpanded = true },
                                trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) },
                                shape = RoundedCornerShape(12.dp),
                                enabled = false,
                                colors = OutlinedTextFieldDefaults.colors(
                                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                    disabledBorderColor = MaterialTheme.colorScheme.outline
                                )
                            )
                            DropdownMenu(
                                expanded = isDropdownExpanded,
                                onDismissRequest = { isDropdownExpanded = false }
                            ) {
                                allItemsState.items.forEach { item ->
                                    DropdownMenuItem(
                                        text = { Text(item.name) },
                                        onClick = {
                                            tempSelectedItem = item
                                            isDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = tempQty,
                            onValueChange = { tempQty = it },
                            label = { Text("Qty") },
                            modifier = Modifier.width(80.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = {
                                if (tempSelectedItem != null && tempQty.isNotBlank()) {
                                    selectedMaterials.add(tempSelectedItem!! to (tempQty.toDoubleOrNull() ?: 1.0))
                                    tempSelectedItem = null
                                    tempQty = ""
                                }
                            },
                            modifier = Modifier.background(Color(0xFF5CCB7B), shape = RoundedCornerShape(12.dp)).size(40.dp)
                        ) {
                            Icon(Icons.Default.Add, null, tint = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(modifier = Modifier.height(150.dp).fillMaxWidth().border(1.dp, Color.LightGray, RoundedCornerShape(12.dp)).padding(8.dp)) {
                        if (selectedMaterials.isEmpty()) {
                            Text("Tiada bahan dipilih", modifier = Modifier.align(Alignment.Center), color = Color.Gray)
                        } else {
                            LazyColumn {
                                items(selectedMaterials) { (item, qty) ->
                                    val formattedQty = String.format(Locale.US, "%.3f", qty)
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("${item.name} ($formattedQty ${item.unit})", modifier = Modifier.weight(1f))
                                        IconButton(onClick = { selectedMaterials.remove(item to qty) }) {
                                            Icon(Icons.Default.Delete, null, tint = Color.Red, modifier = Modifier.size(20.dp))
                                        }
                                    }
                                    HorizontalDivider()
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { 
                            showAddTaskDialog = false
                            selectedMaterials.clear()
                            taskTitle = ""
                            taskDescription = ""
                        }) {
                            Text("Batal")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (taskTitle.isNotBlank()) {
                                    viewModel.addTask(
                                        title = taskTitle,
                                        description = taskDescription,
                                        items = selectedMaterials.map { it.first.id to it.second }
                                    )
                                    showAddTaskDialog = false
                                    selectedMaterials.clear()
                                    taskTitle = ""
                                    taskDescription = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5CCB7B)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Simpan Tugasan")
                        }
                    }
                }
            }
        }
    }

    if (taskToDelete != null) {
        DialogBox(
            onDismiss = { taskToDelete = null },
            title = "Remove Task?",
            description = "Are you sure want to remove ${taskToDelete?.title} task?",
            positiveButtonText = "Remove",
            positiveButtonColor = Color.Red,
            positiveButtonAction = {
                taskToDelete?.let { viewModel.deleteTask(it.id) }
                taskToDelete = null
            },
            negativeButtonText = "Cancel",
            negativeButtonAction = { taskToDelete = null }
        )
    }
}

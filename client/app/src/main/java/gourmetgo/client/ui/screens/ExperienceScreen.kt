package gourmetgo.client.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import gourmetgo.client.viewmodel.ExperiencesViewModel
import gourmetgo.client.viewmodel.factories.ExperiencesViewModelFactory
import gourmetgo.client.ui.components.ExperienceCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExperiencesScreen(
    onNavigateToProfile: () -> Unit,
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current
    val experiencesViewModel: ExperiencesViewModel = viewModel(
        factory = ExperiencesViewModelFactory(context)
    )
    val uiState = experiencesViewModel.uiState
    val focusManager = LocalFocusManager.current

    var searchText by remember { mutableStateOf("") }

    // Mostrar errores como Toast
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            experiencesViewModel.clearError()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "GourmetGo",
                    fontWeight = FontWeight.Bold
                )
            },
            actions = {
                IconButton(onClick = onNavigateToProfile) {
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = "Perfil"
                    )
                }
                IconButton(onClick = onLogout) {
                    Icon(
                        Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Cerrar Sesión"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )

        // Contenido principal
        if (uiState.isLoading && uiState.experiences.isEmpty()) {
            // Estado de carga inicial
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Cargando experiencias...")
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Barra de búsqueda
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = {
                            searchText = it
                            if (it.isBlank()) {
                                experiencesViewModel.clearSearch()
                            }
                        },
                        label = { Text("Buscar experiencias...") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = "Buscar")
                        },
                        trailingIcon = {
                            if (searchText.isNotEmpty()) {
                                IconButton(onClick = {
                                    searchText = ""
                                    experiencesViewModel.clearSearch()
                                    focusManager.clearFocus()
                                }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                if (searchText.isNotBlank()) {
                                    experiencesViewModel.searchExperiences(searchText)
                                }
                                focusManager.clearFocus()
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        singleLine = true
                    )
                }

                // Filtros por categoría
                if (uiState.categories.isNotEmpty() && uiState.searchQuery.isBlank()) {
                    LazyRow(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            FilterChip(
                                onClick = { experiencesViewModel.clearCategoryFilter() },
                                label = { Text("Todas") },
                                selected = uiState.selectedCategory == null
                            )
                        }
                        items(uiState.categories) { category ->
                            FilterChip(
                                onClick = { experiencesViewModel.filterByCategory(category) },
                                label = { Text(category) },
                                selected = uiState.selectedCategory == category
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Lista de experiencias
                val experiencesToShow = experiencesViewModel.getCurrentExperiences()

                if (experiencesToShow.isEmpty() && !uiState.isLoading) {
                    // Estado vacío
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.SearchOff,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = when {
                                    uiState.searchQuery.isNotBlank() -> "No se encontraron experiencias"
                                    uiState.selectedCategory != null -> "No hay experiencias en esta categoría"
                                    else -> "No hay experiencias disponibles"
                                },
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = when {
                                    uiState.searchQuery.isNotBlank() -> "Intenta con otros términos de búsqueda"
                                    uiState.selectedCategory != null -> "Prueba con otra categoría"
                                    else -> "Vuelve pronto para descubrir nuevas experiencias"
                                },
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            if (uiState.searchQuery.isNotBlank() || uiState.selectedCategory != null) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = {
                                        searchText = ""
                                        experiencesViewModel.clearSearch()
                                        experiencesViewModel.clearCategoryFilter()
                                    }
                                ) {
                                    Text("Ver todas las experiencias")
                                }
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Título de sección
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = experiencesViewModel.getCurrentSectionTitle(),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                if (uiState.isSearching) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        strokeWidth = 2.dp
                                    )
                                }
                            }
                        }

                        // Lista de experiencias
                        items(experiencesToShow) { experience ->
                            ExperienceCard(
                                experience = experience,
                                onBookClick = {
                                    // TODO: Implementar navegación a detalle/reserva
                                    Toast.makeText(context, "Próximamente: Reservar ${experience.title}", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }

                        // Botón de refrescar al final
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                OutlinedButton(
                                    onClick = { experiencesViewModel.refreshExperiences() },
                                    enabled = !uiState.refreshing
                                ) {
                                    if (uiState.refreshing) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(16.dp),
                                            strokeWidth = 2.dp
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                    }
                                    Text("Actualizar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

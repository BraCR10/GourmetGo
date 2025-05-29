package gourmetgo.client.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gourmetgo.client.data.models.statesUi.ExperiencesUiState
import gourmetgo.client.data.models.Experience
import gourmetgo.client.data.repository.ExperiencesRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class ExperiencesViewModel(context: Context) : ViewModel() {
    private val repository = ExperiencesRepository(context)

    var uiState by mutableStateOf(ExperiencesUiState())

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                uiState = uiState.copy(isLoading = true, error = null)

                // Cargar experiencias y categorías en paralelo
                val experiencesResult = repository.getAllExperiences()
                val categoriesResult = repository.getAvailableCategories()
                val popularResult = repository.getPopularExperiences()
                val upcomingResult = repository.getUpcomingExperiences()

                experiencesResult
                    .onSuccess { experiences ->
                        uiState = uiState.copy(
                            experiences = experiences,
                            isLoading = false
                        )
                        Log.d("ExperiencesViewModel", "Loaded ${experiences.size} experiences")
                    }
                    .onFailure { error ->
                        uiState = uiState.copy(
                            isLoading = false,
                            error = error.message ?: "Error desconocido"
                        )
                        Log.e("ExperiencesViewModel", "Error loading experiences", error)
                    }

                categoriesResult
                    .onSuccess { categories ->
                        uiState = uiState.copy(categories = categories)
                        Log.d("ExperiencesViewModel", "Loaded ${categories.size} categories")
                    }
                    .onFailure { error ->
                        Log.e("ExperiencesViewModel", "Error loading categories", error)
                    }

                popularResult
                    .onSuccess { popular ->
                        uiState = uiState.copy(popularExperiences = popular)
                        Log.d("ExperiencesViewModel", "Loaded ${popular.size} popular experiences")
                    }
                    .onFailure { error ->
                        Log.e("ExperiencesViewModel", "Error loading popular experiences", error)
                    }

                upcomingResult
                    .onSuccess { upcoming ->
                        uiState = uiState.copy(upcomingExperiences = upcoming)
                        Log.d("ExperiencesViewModel", "Loaded ${upcoming.size} upcoming experiences")
                    }
                    .onFailure { error ->
                        Log.e("ExperiencesViewModel", "Error loading upcoming experiences", error)
                    }

            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    error = "Error inesperado: ${e.message}"
                )
                Log.e("ExperiencesViewModel", "Unexpected error in loadInitialData", e)
            }
        }
    }

    fun refreshExperiences() {
        viewModelScope.launch {
            try {
                uiState = uiState.copy(refreshing = true, error = null)

                repository.getAllExperiences()
                    .onSuccess { experiences ->
                        uiState = uiState.copy(
                            experiences = experiences,
                            refreshing = false
                        )
                        Log.d("ExperiencesViewModel", "Refreshed ${experiences.size} experiences")
                    }
                    .onFailure { error ->
                        uiState = uiState.copy(
                            refreshing = false,
                            error = error.message ?: "Error al actualizar"
                        )
                        Log.e("ExperiencesViewModel", "Error refreshing experiences", error)
                    }
            } catch (e: Exception) {
                uiState = uiState.copy(
                    refreshing = false,
                    error = "Error inesperado al actualizar: ${e.message}"
                )
                Log.e("ExperiencesViewModel", "Unexpected error in refreshExperiences", e)
            }
        }
    }

    fun searchExperiences(query: String) {
        if (query.isBlank()) {
            uiState = uiState.copy(
                searchQuery = "",
                searchResults = emptyList(),
                isSearching = false
            )
            return
        }

        viewModelScope.launch {
            try {
                uiState = uiState.copy(
                    searchQuery = query,
                    isSearching = true,
                    error = null
                )

                // Pequeño delay para evitar búsquedas muy frecuentes
                delay(300)

                repository.searchExperiences(query)
                    .onSuccess { results ->
                        uiState = uiState.copy(
                            searchResults = results,
                            isSearching = false
                        )
                        Log.d("ExperiencesViewModel", "Found ${results.size} results for: $query")
                    }
                    .onFailure { error ->
                        uiState = uiState.copy(
                            isSearching = false,
                            error = error.message ?: "Error en la búsqueda"
                        )
                        Log.e("ExperiencesViewModel", "Error searching experiences", error)
                    }
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isSearching = false,
                    error = "Error inesperado en búsqueda: ${e.message}"
                )
                Log.e("ExperiencesViewModel", "Unexpected error in searchExperiences", e)
            }
        }
    }

    fun filterByCategory(category: String?) {
        if (category == null) {
            uiState = uiState.copy(selectedCategory = null)
            return
        }

        viewModelScope.launch {
            try {
                uiState = uiState.copy(
                    selectedCategory = category,
                    isLoading = true,
                    error = null
                )

                repository.getExperiencesByCategory(category)
                    .onSuccess { experiences ->
                        uiState = uiState.copy(
                            experiences = experiences,
                            isLoading = false
                        )
                        Log.d("ExperiencesViewModel", "Filtered ${experiences.size} experiences by category: $category")
                    }
                    .onFailure { error ->
                        uiState = uiState.copy(
                            isLoading = false,
                            error = error.message ?: "Error al filtrar"
                        )
                        Log.e("ExperiencesViewModel", "Error filtering by category", error)
                    }
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    error = "Error inesperado al filtrar: ${e.message}"
                )
                Log.e("ExperiencesViewModel", "Unexpected error in filterByCategory", e)
            }
        }
    }

    fun clearCategoryFilter() {
        uiState = uiState.copy(selectedCategory = null)
        loadInitialData() // Recargar todas las experiencias
    }

    fun getExperienceById(id: String): Experience? {
        return uiState.experiences.find { it.id == id }
            ?: uiState.searchResults.find { it.id == id }
            ?: uiState.popularExperiences.find { it.id == id }
            ?: uiState.upcomingExperiences.find { it.id == id }
    }

    fun clearSearch() {
        uiState = uiState.copy(
            searchQuery = "",
            searchResults = emptyList(),
            isSearching = false
        )
    }

    fun clearError() {
        uiState = uiState.copy(error = null)
    }

    // Método para obtener las experiencias que se deben mostrar actualmente
    fun getCurrentExperiences(): List<Experience> {
        return when {
            uiState.searchQuery.isNotBlank() -> uiState.searchResults
            uiState.selectedCategory != null -> uiState.experiences
            else -> uiState.experiences
        }
    }

    // Método para obtener solo experiencias activas
    fun getActiveExperiences(): List<Experience> {
        return getCurrentExperiences().filter { it.status == "Activa" }
    }

    // Método para verificar si hay experiencias disponibles
    fun hasExperiences(): Boolean {
        return getCurrentExperiences().isNotEmpty()
    }

    // Método para obtener el título de la sección actual
    fun getCurrentSectionTitle(): String {
        return when {
            uiState.searchQuery.isNotBlank() -> "Resultados de búsqueda"
            uiState.selectedCategory != null -> "Categoría: ${uiState.selectedCategory}"
            else -> "Todas las experiencias"
        }
    }
}
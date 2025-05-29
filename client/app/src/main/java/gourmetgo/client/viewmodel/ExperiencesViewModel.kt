package gourmetgo.client.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gourmetgo.client.viewmodel.statesUi.ExperiencesUiState
import gourmetgo.client.data.models.Experience
import gourmetgo.client.data.repository.ExperiencesRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.Job

class ExperiencesViewModel(
    private val repository: ExperiencesRepository
) : ViewModel() {

    var uiState by mutableStateOf(ExperiencesUiState())
        private set

    private var searchJob: Job? = null

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                uiState = uiState.copy(isLoading = true, error = null)

                val experiencesResult = repository.getAllExperiences()
                val categoriesResult = repository.getAvailableCategories()

                experiencesResult
                    .onSuccess { experiences ->
                        uiState = uiState.copy(
                            experiences = experiences,
                            popularExperiences = getPopularExperiences(experiences),
                            upcomingExperiences = getUpcomingExperiences(experiences),
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
                            popularExperiences = getPopularExperiences(experiences),
                            upcomingExperiences = getUpcomingExperiences(experiences),
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
            clearSearch()
            return
        }
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            try {
                uiState = uiState.copy(
                    searchQuery = query,
                    isSearching = true,
                    error = null
                )

                delay(300)

                // Búsqueda local en las experiencias cargadas
                val filteredExperiences = uiState.experiences.filter { experience ->
                    experience.title.contains(query, ignoreCase = true) ||
                            experience.description.contains(query, ignoreCase = true) ||
                            experience.category.contains(query, ignoreCase = true) ||
                            experience.location.contains(query, ignoreCase = true)
                }

                uiState = uiState.copy(
                    searchResults = filteredExperiences,
                    isSearching = false
                )

                Log.d("ExperiencesViewModel", "Search '$query' returned ${filteredExperiences.size} results")

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
        viewModelScope.launch {
            try {
                uiState = uiState.copy(
                    selectedCategory = category,
                    error = null
                )

                Log.d("ExperiencesViewModel", "Filtered by category: $category")

            } catch (e: Exception) {
                uiState = uiState.copy(
                    error = "Error inesperado al filtrar: ${e.message}"
                )
                Log.e("ExperiencesViewModel", "Unexpected error in filterByCategory", e)
            }
        }
    }

    fun clearCategoryFilter() {
        uiState = uiState.copy(selectedCategory = null)
        Log.d("ExperiencesViewModel", "Category filter cleared")
    }

    fun clearSearch() {
        searchJob?.cancel()
        uiState = uiState.copy(
            searchQuery = "",
            searchResults = emptyList(),
            isSearching = false
        )
        Log.d("ExperiencesViewModel", "Search cleared")
    }

    fun clearError() {
        uiState = uiState.copy(error = null)
    }

    fun getCurrentExperiences(): List<Experience> {
        return when {
            uiState.searchQuery.isNotBlank() -> uiState.searchResults
            uiState.selectedCategory != null -> uiState.experiences.filter {
                it.category.equals(uiState.selectedCategory, ignoreCase = true)
            }
            else -> uiState.experiences
        }
    }

    fun getCurrentSectionTitle(): String {
        return when {
            uiState.searchQuery.isNotBlank() -> "Resultados de búsqueda (${uiState.searchResults.size})"
            uiState.selectedCategory != null -> "Categoría: ${uiState.selectedCategory}"
            else -> "Todas las experiencias (${uiState.experiences.size})"
        }
    }

    private fun getPopularExperiences(experiences: List<Experience>): List<Experience> {
        return experiences
            .filter { it.status == "Activa" }
            .sortedBy { it.remainingCapacity.toDouble() / it.capacity.toDouble() }
            .take(5)
    }

    private fun getUpcomingExperiences(experiences: List<Experience>): List<Experience> {
        return experiences
            .filter { it.status == "Activa" || it.status == "Próximamente" }
            .sortedBy { it.date }
            .take(5)
    }

}


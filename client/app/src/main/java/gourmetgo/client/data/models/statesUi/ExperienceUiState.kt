package gourmetgo.client.data.models.statesUi
import gourmetgo.client.data.models.Experience

data class ExperiencesUiState(
    val isLoading: Boolean = false,
    val experiences: List<Experience> = emptyList(),
    val categories: List<String> = emptyList(),
    val selectedCategory: String? = null,
    val searchQuery: String = "",
    val isSearching: Boolean = false,
    val searchResults: List<Experience> = emptyList(),
    val popularExperiences: List<Experience> = emptyList(),
    val upcomingExperiences: List<Experience> = emptyList(),
    val error: String? = null,
    val refreshing: Boolean = false
)
package gourmetgo.client.data.repository

import android.content.Context
import android.util.Log
import gourmetgo.client.data.remote.Connection
import gourmetgo.client.data.localStorage.SharedPrefsManager
import gourmetgo.client.data.mockups.ExperiencesMockup
import gourmetgo.client.data.models.Experience
import kotlinx.coroutines.delay

class ExperiencesRepository(context: Context) {
    private val connection = Connection()
    private val sharedPrefs = SharedPrefsManager(context)
    private val useMockup = true

    suspend fun getAllExperiences(): Result<List<Experience>> {
        return try {
            if (useMockup) {
                getAllExperiencesWithMockup()
            } else {
                getAllExperiencesWithApi()
            }
        } catch (e: Exception) {
            Log.e("ExperiencesRepository", "Error getting all experiences", e)
            Result.failure(Exception("Error connection: ${e.message}"))
        }
    }

    suspend fun getActiveExperiences(): Result<List<Experience>> {
        return try {
            if (useMockup) {
                getActiveExperiencesWithMockup()
            } else {
                getAllExperiencesWithApi()
            }
        } catch (e: Exception) {
            Log.e("ExperiencesRepository", "Error getting active experiences", e)
            Result.failure(Exception("Error de connection: ${e.message}"))
        }
    }

    suspend fun getExperienceById(id: String): Result<Experience?> {
        return try {
            if (useMockup) {
                getExperienceByIdWithMockup(id)
            } else {
                getExperienceByIdWithApi(id)
            }
        } catch (e: Exception) {
            Log.e("ExperiencesRepository", "Error getting experience by id", e)
            Result.failure(Exception("Error connection: ${e.message}"))
        }
    }

    suspend fun searchExperiences(query: String): Result<List<Experience>> {
        return try {
            if (useMockup) {
                searchExperiencesWithMockup(query)
            } else {
                searchExperiencesWithApi(query)
            }
        } catch (e: Exception) {
            Log.e("ExperiencesRepository", "Error searching experiences", e)
            Result.failure(Exception("Error connection: ${e.message}"))
        }
    }

    suspend fun getExperiencesByCategory(category: String): Result<List<Experience>> {
        return try {
            if (useMockup) {
                getExperiencesByCategoryWithMockup(category)
            } else {
                getExperiencesByCategoryWithApi(category)
            }
        } catch (e: Exception) {
            Log.e("ExperiencesRepository", "Error getting experiences by category", e)
            Result.failure(Exception("Error connection: ${e.message}"))
        }
    }

    suspend fun getPopularExperiences(): Result<List<Experience>> {
        return try {
            if (useMockup) {
                getPopularExperiencesWithMockup()
            } else {
                getAllExperiencesWithApi()
            }
        } catch (e: Exception) {
            Log.e("ExperiencesRepository", "Error getting popular experiences", e)
            Result.failure(Exception("Error connection: ${e.message}"))
        }
    }

    suspend fun getUpcomingExperiences(): Result<List<Experience>> {
        return try {
            if (useMockup) {
                getUpcomingExperiencesWithMockup()
            } else {
                getAllExperiencesWithApi()
            }
        } catch (e: Exception) {
            Log.e("ExperiencesRepository", "Error getting upcoming experiences", e)
            Result.failure(Exception("Error connection: ${e.message}"))
        }
    }

    suspend fun getAvailableCategories(): Result<List<String>> {
        return try {
            if (useMockup) {
                getAvailableCategoriesWithMockup()
            } else {
                Result.success(emptyList())
            }
        } catch (e: Exception) {
            Log.e("ExperiencesRepository", "Error getting categories", e)
            Result.failure(Exception("Error connection: ${e.message}"))
        }
    }

    // METHODS MOCKUP
    private suspend fun getAllExperiencesWithMockup(): Result<List<Experience>> {
        delay(800)
        return try {
            val experiences = ExperiencesMockup.getAllExperiences()
            Log.d("ExperiencesRepository", "Loaded ${experiences.size} experiences from mockup")
            Result.success(experiences)
        } catch (e: Exception) {
            Log.e("ExperiencesRepository", "Error in mockup getAllExperiences", e)
            Result.failure(e)
        }
    }

    private suspend fun getActiveExperiencesWithMockup(): Result<List<Experience>> {
        delay(600)
        return try {
            val experiences = ExperiencesMockup.getActiveExperiences()
            Log.d("ExperiencesRepository", "Loaded ${experiences.size} active experiences from mockup")
            Result.success(experiences)
        } catch (e: Exception) {
            Log.e("ExperiencesRepository", "Error in mockup getActiveExperiences", e)
            Result.failure(e)
        }
    }

    private suspend fun getExperienceByIdWithMockup(id: String): Result<Experience?> {
        delay(400)
        return try {
            val experience = ExperiencesMockup.getExperienceById(id)
            Log.d("ExperiencesRepository", "Loaded experience with id $id from mockup")
            Result.success(experience)
        } catch (e: Exception) {
            Log.e("ExperiencesRepository", "Error in mockup getExperienceById", e)
            Result.failure(e)
        }
    }

    private suspend fun searchExperiencesWithMockup(query: String): Result<List<Experience>> {
        delay(500)
        return try {
            val experiences = ExperiencesMockup.searchExperiences(query)
            Log.d("ExperiencesRepository", "Found ${experiences.size} experiences for query: $query")
            Result.success(experiences)
        } catch (e: Exception) {
            Log.e("ExperiencesRepository", "Error in mockup searchExperiences", e)
            Result.failure(e)
        }
    }

    private suspend fun getExperiencesByCategoryWithMockup(category: String): Result<List<Experience>> {
        delay(500)
        return try {
            val experiences = ExperiencesMockup.getExperiencesByCategory(category)
            Log.d("ExperiencesRepository", "Found ${experiences.size} experiences for category: $category")
            Result.success(experiences)
        } catch (e: Exception) {
            Log.e("ExperiencesRepository", "Error in mockup getExperiencesByCategory", e)
            Result.failure(e)
        }
    }

    private suspend fun getPopularExperiencesWithMockup(): Result<List<Experience>> {
        delay(400)
        return try {
            val experiences = ExperiencesMockup.getPopularExperiences()
            Log.d("ExperiencesRepository", "Loaded ${experiences.size} popular experiences from mockup")
            Result.success(experiences)
        } catch (e: Exception) {
            Log.e("ExperiencesRepository", "Error in mockup getPopularExperiences", e)
            Result.failure(e)
        }
    }

    private suspend fun getUpcomingExperiencesWithMockup(): Result<List<Experience>> {
        delay(400)
        return try {
            val experiences = ExperiencesMockup.getUpcomingExperiences()
            Log.d("ExperiencesRepository", "Loaded ${experiences.size} upcoming experiences from mockup")
            Result.success(experiences)
        } catch (e: Exception) {
            Log.e("ExperiencesRepository", "Error in mockup getUpcomingExperiences", e)
            Result.failure(e)
        }
    }

    private suspend fun getAvailableCategoriesWithMockup(): Result<List<String>> {
        delay(300)
        return try {
            val categories = ExperiencesMockup.getAvailableCategories()
            Log.d("ExperiencesRepository", "Loaded ${categories.size} categories from mockup")
            Result.success(categories)
        } catch (e: Exception) {
            Log.e("ExperiencesRepository", "Error in mockup getAvailableCategories", e)
            Result.failure(e)
        }
    }

    // METHODS API
    private suspend fun getAllExperiencesWithApi(): Result<List<Experience>> {
        return try {
            val experiences = connection.apiService.getExperiences()
            Log.d("ExperiencesRepository", "Loaded ${experiences.size} experiences from API")
            Result.success(experiences)
        } catch (e: Exception) {
            Log.e("ExperiencesRepository", "Error in API getAllExperiences", e)
            Result.failure(Exception("Error connection to server"))
        }
    }

    private suspend fun getExperienceByIdWithApi(id: String): Result<Experience?> {
        return try {
            // val experience = connection.apiService.getExperienceById(id)
            Log.d("ExperiencesRepository", "Getting experience by id from API: $id")
            Result.success(null) // Placeholder
        } catch (e: Exception) {
            Log.e("ExperiencesRepository", "Error in API getExperienceById", e)
            Result.failure(Exception("Error connection to server"))
        }
    }

    private suspend fun searchExperiencesWithApi(query: String): Result<List<Experience>> {
        return try {
            // val experiences = connection.apiService.searchExperiences(query)
            Log.d("ExperiencesRepository", "Searching experiences in API: $query")
            Result.success(emptyList()) // Placeholder
        } catch (e: Exception) {
            Log.e("ExperiencesRepository", "Error in API searchExperiences", e)
            Result.failure(Exception("Error connection to server"))
        }
    }

    private suspend fun getExperiencesByCategoryWithApi(category: String): Result<List<Experience>> {
        return try {
            // val experiences = connection.apiService.getExperiencesByCategory(category)
            Log.d("ExperiencesRepository", "Getting experiences by category from API: $category")
            Result.success(emptyList()) // Placeholder
        } catch (e: Exception) {
            Log.e("ExperiencesRepository", "Error in API getExperiencesByCategory", e)
            Result.failure(Exception("Error connection to server"))
        }
    }
}
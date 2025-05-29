package gourmetgo.client.data.repository

import android.content.Context
import android.util.Log
import gourmetgo.client.data.remote.Connection
import gourmetgo.client.data.mockups.ExperiencesMockup
import gourmetgo.client.data.models.Experience
import kotlinx.coroutines.delay
import gourmetgo.client.AppConfig

class ExperiencesRepository(context: Context) {
    private val connection = Connection()

    suspend fun getAllExperiences(): Result<List<Experience>> {
        return try {
            if (AppConfig.USE_MOCKUP) {
                getAllExperiencesWithMockup()
            } else {
                getAllExperiencesWithApi()
            }
        } catch (e: Exception) {
            Log.e("ExperiencesRepository", "Error getting all experiences", e)
            Result.failure(Exception("Error connection: ${e.message}"))
        }
    }
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
    private suspend fun getAllExperiencesWithApi(): Result<List<Experience>> {
        return try {
            val experiences = connection.apiService.getExperiences().experiences
            Log.d("ExperiencesRepository", "Loaded ${experiences.size} experiences from API")
            Result.success(experiences)
        } catch (e: Exception) {
            Log.e("ExperiencesRepository", "Error in API getAllExperiences", e)
            Result.failure(Exception("Error connection to server"))
        }
    }
    suspend fun getAvailableCategories(): Result<List<String>> {
        return try {
            if (AppConfig.USE_MOCKUP) {
                getAvailableCategoriesWithMockup()
            } else {
                //TODO:Logic with Api
                Result.success(emptyList())
            }
        } catch (e: Exception) {
            Log.e("ExperiencesRepository", "Error getting categories", e)
            Result.failure(Exception("Error connection: ${e.message}"))
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








}
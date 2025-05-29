package gourmetgo.client.viewmodel.factories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gourmetgo.client.viewmodel.ExperiencesViewModel
import gourmetgo.client.data.repository.ExperiencesRepository
import gourmetgo.client.data.remote.Connection

class ExperiencesViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExperiencesViewModel::class.java)) {

            val connection = Connection()
            val repository = ExperiencesRepository(
                apiService = connection.apiService
            )

            return ExperiencesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
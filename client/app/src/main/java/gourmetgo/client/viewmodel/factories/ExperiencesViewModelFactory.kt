package gourmetgo.client.viewmodel.factories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gourmetgo.client.viewmodel.ExperiencesViewModel

class ExperiencesViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExperiencesViewModel::class.java)) {
            return ExperiencesViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
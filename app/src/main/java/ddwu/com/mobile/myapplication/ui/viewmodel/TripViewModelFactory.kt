package ddwu.com.mobile.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ddwu.com.mobile.myapplication.data.repository.TripRepository

class TripViewModelFactory(private val repository: TripRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TripViewModel::class.java)) {
            return TripViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

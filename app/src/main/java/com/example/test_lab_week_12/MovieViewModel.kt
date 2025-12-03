package com.example.test_lab_week_13

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test_lab_week_13.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.Calendar

class MovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    init {
        fetchPopularMovies()
    }

    // StateFlow untuk list movie
    private val _popularMovies = MutableStateFlow(emptyList<Movie>())
    val popularMovies: StateFlow<List<Movie>> = _popularMovies

    // StateFlow untuk error
    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    private fun fetchPopularMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            movieRepository.fetchMovies()
                .catch { e ->
                    _error.value = "An exception occurred: ${e.message}"
                }
                .collect { movies ->
                    
                    // Dapatkan tahun saat ini
                    val currentYear = Calendar.getInstance().get(Calendar.YEAR).toString()
                    
                    // Filter & Sort data sebelum di-emit ke StateFlow
                    val filteredMovies = movies
                        .filter { movie ->
                            // Hanya ambil film yang tahun rilisnya dimulai dengan tahun ini
                            movie.releaseDate?.startsWith(currentYear) == true
                        }
                        .sortedByDescending { movie ->
                            // Urutkan dari popularitas tertinggi ke terendah
                            movie.popularity
                        }

                    // Masukkan data yang SUDAH difilter ke StateFlow
                    _popularMovies.value = filteredMovies

                }
        }
    }
}
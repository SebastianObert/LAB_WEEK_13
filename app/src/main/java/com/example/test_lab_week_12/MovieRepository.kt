package com.example.test_lab_week_13

import com.example.test_lab_week_13.api.MovieService
import com.example.test_lab_week_13.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MovieRepository(private val movieService: MovieService) {
    private val apiKey = "7c3f9513fa6db43815ebb38fb8b36e3e"

    // Fungsi fetchMovies sekarang mengembalikan Flow
    // Panggilan jaringan terjadi saat flow ini di-collect
    fun fetchMovies(): Flow<List<Movie>> = flow {
        val popularMovies = movieService.getPopularMovies(apiKey)
        emit(popularMovies.results)
    }
}
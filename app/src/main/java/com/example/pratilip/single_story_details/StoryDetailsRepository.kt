package com.example.pratilip.single_story_details

import androidx.lifecycle.LiveData

import com.example.pratilip.data.api.TheMovieDBInterface
import com.example.pratilip.data.repository.MovieDetailsNetworkDataSource
import com.example.pratilip.data.repository.NetworkState
import com.example.pratilip.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class StoryDetailsRepository (private val apiService : TheMovieDBInterface) {

    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource

    fun fetchSingleMovieDetails (compositeDisposable: CompositeDisposable, movieId: Int) : LiveData<MovieDetails> {

        movieDetailsNetworkDataSource = MovieDetailsNetworkDataSource(apiService,compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)

        return movieDetailsNetworkDataSource.downloadedMovieResponse

    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState> {
        return movieDetailsNetworkDataSource.networkState
    }



}
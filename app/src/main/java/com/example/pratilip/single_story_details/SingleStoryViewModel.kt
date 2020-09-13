package com.example.pratilip.single_story_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

import com.example.pratilip.data.repository.NetworkState
import com.example.pratilip.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class SingleStoryViewModel (private val movieRepository : StoryDetailsRepository, movieId: Int)  : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val  movieDetails : LiveData<MovieDetails> by lazy {
        movieRepository.fetchSingleMovieDetails(compositeDisposable,movieId)
    }

    val networkState : LiveData<NetworkState> by lazy {
        movieRepository.getMovieDetailsNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }



}
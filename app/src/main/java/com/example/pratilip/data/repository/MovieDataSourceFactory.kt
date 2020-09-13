package com.example.pratilip.data.repository


import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.MutableLiveData

import io.reactivex.disposables.CompositeDisposable
import androidx.paging.DataSource
import com.example.pratilip.data.api.TheMovieDBInterface
import com.example.pratilip.data.vo.Movie

class MovieDataSourceFactory (private val apiService : TheMovieDBInterface, private val compositeDisposable: CompositeDisposable)
    : DataSource.Factory<Int, Movie>() {

    val moviesLiveDataSource =  MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val movieDataSource = MovieDataSource(apiService,compositeDisposable)

        moviesLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }

}
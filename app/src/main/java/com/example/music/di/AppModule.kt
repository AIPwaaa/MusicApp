package com.example.music.di

import com.example.music.data.local.MediaStoreTracker
import com.example.music.data.remote.MusicPlayerImpl
import com.example.music.data.repository_impl.MusicRepositoryImpl
import com.example.music.domain.repositories.AudioPlayer
import com.example.music.domain.repositories.MusicRepository
import com.example.music.domain.usecases.GetCombinedTracksUseCase
import com.example.music.presentation.viewmodels.MusicViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { MediaStoreTracker(androidContext()) }
    single<MusicRepository> { MusicRepositoryImpl(get()) }
    factory { GetCombinedTracksUseCase(get()) }
    single<AudioPlayer> { MusicPlayerImpl(androidContext()) }
    viewModel { MusicViewModel(get(), get()) }
}
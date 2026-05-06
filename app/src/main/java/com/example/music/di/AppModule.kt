package com.example.music.di

import com.example.music.data.local.MediaStoreTracker
import com.example.music.data.repository_impl.MusicRepositoryImpl
import com.example.music.domain.repositories.MusicRepository
import com.example.music.domain.usecases.GetCombinedTracksUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { MediaStoreTracker(androidContext()) }
    single<MusicRepository> { MusicRepositoryImpl(get()) }
    factory { GetCombinedTracksUseCase(get()) }
}
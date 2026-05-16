package com.example.music.di

import com.example.music.data.local.MediaStoreTracker
import com.example.music.data.remote.MusicPlayerImpl
import com.example.music.data.repository_impl.MusicRepositoryImpl
import com.example.music.domain.repositories.AudioPlayer
import com.example.music.domain.repositories.MusicRepository
import com.example.music.domain.usecases.GetCombinedTracksUseCase
import com.example.music.presentation.viewmodels.MusicViewModel
import io.ktor.client.*
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.util.logging.Level


val appModule = module {
    single {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }
            install(Logging) {
                level = LogLevel.ALL
            }
        }
    }
    single { MediaStoreTracker(androidContext()) }
    single<MusicRepository> { MusicRepositoryImpl(get(), get()) }
    factory { GetCombinedTracksUseCase(get()) }
    single<AudioPlayer> { MusicPlayerImpl(androidContext()) }
    viewModel { MusicViewModel(get(), get()) }
}

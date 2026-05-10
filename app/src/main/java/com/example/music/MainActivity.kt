package com.example.music

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.music.presentation.screens.MusicScreen
import com.example.music.presentation.viewmodels.MusicViewModel
import com.example.music.ui.theme.MusicTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MusicViewModel = koinViewModel()
            val isDarkTheme by viewModel.isDarkTheme.collectAsState()
            
            MusicTheme(darkTheme = isDarkTheme) {
                MusicScreen(viewModel = viewModel)
            }
        }
    }
}

package com.jorgelobo.koobe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.jorgelobo.koobe.ui.app.AppViewModel
import com.jorgelobo.koobe.ui.navigation.NavGraph
import com.jorgelobo.koobe.ui.theme.KoobeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                android.graphics.Color.BLACK
            ),
            navigationBarStyle = SystemBarStyle.dark(
                android.graphics.Color.BLACK
            )
        )
        setContent {
            Koobe()
        }
    }
}

@Composable
fun Koobe(
    appViewModel: AppViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    val themeOption by appViewModel.themeOption.collectAsStateWithLifecycle()

    KoobeTheme(
        themeOption = themeOption
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    WindowInsets.systemBars
                        .asPaddingValues()
                )
        ) {
            NavGraph(
                navController = navController,
                appViewModel = appViewModel
            )
        }
    }
}
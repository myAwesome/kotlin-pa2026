package com.myawesome.kotlinpa2026

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import com.myawesome.kotlinpa2026.data.local.ThemeStore
import com.myawesome.kotlinpa2026.ui.navigation.AppNavGraph
import com.myawesome.kotlinpa2026.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var themeStore: ThemeStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDark by themeStore.isDark.collectAsState(initial = false)
            AppTheme(isDark = isDark) {
                AppNavGraph(
                    isDark = isDark,
                    onToggleTheme = { lifecycleScope.launch { themeStore.toggle() } }
                )
            }
        }
    }
}

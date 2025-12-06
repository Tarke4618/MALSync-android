package com.malsync.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.malsync.android.ui.theme.MALSyncTheme
import com.malsync.android.ui.navigation.MALSyncNavHost
import android.content.Intent
import androidx.activity.viewModels
import com.malsync.android.ui.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Splash screen
        installSplashScreen()
        
        super.onCreate(savedInstanceState)
        
        // Handle deep link if app is started via URL
        handleIntent(intent)
        
        setContent {
            MALSyncTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MALSyncNavHost()
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        intent?.data?.let { uri ->
            if (uri.scheme == "malsync" && uri.host == "oauth") {
                val code = uri.getQueryParameter("code")
                val state = uri.getQueryParameter("state")
                
                if (code != null) {
                    authViewModel.handleOAuthCallback(code, state)
                }
            }
        }
    }
}

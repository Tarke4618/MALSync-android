package com.malsync.android.ui.screens.browser

import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun BrowserScreen(
    initialUrl: String = "https://hianime.to",
    onNavigateBack: (() -> Unit)? = null,
    viewModel: BrowserViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var webView by remember { mutableStateOf<WebView?>(null) }
    var canGoBack by remember { mutableStateOf(false) }
    var canGoForward by remember { mutableStateOf(false) }
    var pageTitle by remember { mutableStateOf("Browser") }
    var loadingProgress by remember { mutableStateOf(0) }
    
    // Initial load tracking
    LaunchedEffect(initialUrl) {
        viewModel.onUrlChanged(initialUrl)
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { 
                        Column {
                            Text(
                                text = pageTitle,
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1
                            )
                            Text(
                                text = uiState.currentUrl,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1
                            )
                        }
                    },
                    navigationIcon = {
                        onNavigateBack?.let { callback ->
                            IconButton(onClick = callback) {
                                Icon(Icons.Filled.Close, contentDescription = "Close")
                            }
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { webView?.goBack() },
                            enabled = canGoBack
                        ) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                        
                        IconButton(
                            onClick = { webView?.goForward() },
                            enabled = canGoForward
                        ) {
                            Icon(Icons.Filled.ArrowForward, contentDescription = "Forward")
                        }
                        
                        IconButton(onClick = { webView?.reload() }) {
                            Icon(Icons.Filled.Refresh, contentDescription = "Refresh")
                        }
                    }
                )
                
                if (loadingProgress > 0 && loadingProgress < 100) {
                    LinearProgressIndicator(
                        progress = loadingProgress / 100f,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        bottomBar = {
            androidx.compose.animation.AnimatedVisibility(
                visible = uiState.detectedContent != null,
                enter = androidx.compose.animation.slideInVertically { it } + androidx.compose.animation.fadeIn(),
                exit = androidx.compose.animation.slideOutVertically { it } + androidx.compose.animation.fadeOut()
            ) {
                 uiState.detectedContent?.let { content ->
                     Surface(
                         color = MaterialTheme.colorScheme.primaryContainer,
                         modifier = Modifier.fillMaxWidth(),
                         tonalElevation = 3.dp,
                         shadowElevation = 4.dp
                     ) {
                         Row(
                             modifier = Modifier
                                 .padding(16.dp)
                                 .navigationBarsPadding(), // Handle edge-to-edge
                             verticalAlignment = Alignment.CenterVertically,
                             horizontalArrangement = Arrangement.SpaceBetween
                         ) {
                             Column(modifier = Modifier.weight(1f)) {
                                 Text(
                                     text = "Detected: ${content.title ?: "Unknown"}",
                                     style = MaterialTheme.typography.labelLarge,
                                     color = MaterialTheme.colorScheme.onPrimaryContainer
                                 )
                                 Text(
                                     text = "Episode ${content.episodeOrChapter ?: "?"} • ${content.site.name}",
                                     style = MaterialTheme.typography.bodyMedium,
                                     color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                 )
                             }
                             Button(
                                 onClick = { /* TODO: Sync Logic */ },
                                 colors = ButtonDefaults.buttonColors(
                                     containerColor = MaterialTheme.colorScheme.primary,
                                     contentColor = MaterialTheme.colorScheme.onPrimary
                                 )
                             ) {
                                 Icon(
                                     androidx.compose.material.icons.filled.Refresh, // Placeholder icon for sync
                                     contentDescription = null,
                                     modifier = Modifier.size(16.dp)
                                 )
                                 Spacer(modifier = Modifier.width(8.dp))
                                 Text("Sync")
                             }
                         }
                     }
                 }
            }
        }

    ) { paddingValues ->
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            canGoBack = view?.canGoBack() ?: false
                            canGoForward = view?.canGoForward() ?: false
                            url?.let { viewModel.onUrlChanged(it) }
                        }
                    }
                    
                    webChromeClient = object : WebChromeClient() {
                        override fun onProgressChanged(view: WebView?, newProgress: Int) {
                            super.onProgressChanged(view, newProgress)
                            loadingProgress = newProgress
                        }
                        
                        override fun onReceivedTitle(view: WebView?, title: String?) {
                            super.onReceivedTitle(view, title)
                            pageTitle = title ?: "Browser"
                        }
                    }
                    
                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        setSupportZoom(true)
                        builtInZoomControls = true
                        displayZoomControls = false
                        loadWithOverviewMode = true
                        useWideViewPort = true
                    }
                    
                    loadUrl(initialUrl)
                    webView = this
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }
}

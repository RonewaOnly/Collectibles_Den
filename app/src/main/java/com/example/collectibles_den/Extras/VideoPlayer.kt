package com.example.collectibles_den.Extras

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@Composable
fun VideoPlayer(link: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // Create and remember an ExoPlayer instance
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(Uri.parse(link))
            setMediaItem(mediaItem)
            prepare()
        }
    }

    // Dispose of the ExoPlayer instance when no longer needed
    DisposableEffect(
        AndroidView(factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
            }
        }, modifier = modifier)
    ) {
        onDispose {
            exoPlayer.release()
        }
    }
}

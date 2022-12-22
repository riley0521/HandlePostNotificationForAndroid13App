package com.rpfcoding.handlepostnotificationforandroid13app

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.rpfcoding.handlepostnotificationforandroid13app.ui.theme.HandlePostNotificationForAndroid13AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HandlePostNotificationForAndroid13AppTheme {

                val ctx = LocalContext.current
                var hasNotificationPermission by remember {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        mutableStateOf(
                            ContextCompat.checkSelfPermission(
                                ctx,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) == PackageManager.PERMISSION_GRANTED
                        )
                    } else mutableStateOf(true)
                }

                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { isGranted ->
                        hasNotificationPermission = isGranted
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            val shouldShowNotificationRationale =
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    !isGranted && shouldShowRequestPermissionRationale(
                                        Manifest.permission.POST_NOTIFICATIONS
                                    )
                                } else false

                            val isNotificationPermanentlyDenied =
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    !isGranted && !shouldShowRequestPermissionRationale(
                                        Manifest.permission.POST_NOTIFICATIONS
                                    )
                                } else false

                            if (shouldShowNotificationRationale) {
                                // TODO : Show rationale message why user needs to activate this permission
                            } else if(isNotificationPermanentlyDenied) {
                                // TODO : Go to user settings.
                            }
                        } else {
                            // TODO("VERSION.SDK_INT < M")
                        }
                    }
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                                !hasNotificationPermission
                            ) {
                                permissionLauncher.launch(
                                    Manifest.permission.POST_NOTIFICATIONS
                                )
                            } else {
                                Toast.makeText(
                                    ctx,
                                    "Your android device is below android 13. You do not need to notification permission.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    ) {
                        Text(text = "Request permission")
                    }

                    Button(
                        onClick = {
                            if (hasNotificationPermission) {
                                showNotification()
                            }
                        }
                    ) {
                        Text(text = "Show notification")
                    }
                }
            }
        }
    }

    private fun showNotification() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(this, "channel_id")
            .setContentTitle("Title 1")
            .setContentText("Content 1")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        notificationManager.notify(1, notification)
    }
}
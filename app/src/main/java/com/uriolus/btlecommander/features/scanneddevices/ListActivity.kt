package com.uriolus.btlecommander.features.scanneddevices

import DevicesListScreen
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import com.uriolus.btlecommander.features.detail.DetailActivity
import org.koin.androidx.compose.koinViewModel

class ListActivity : ComponentActivity() {
    private val permissionsNeeded = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        arrayOf(
            android.Manifest.permission.BLUETOOTH_CONNECT,
            android.Manifest.permission.BLUETOOTH_SCAN
        )
    } else {
        arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        )
    }
    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        )
        { permissions ->
            // Handle Permission granted/rejected
            permissions.entries.forEach {
                //val permissionName = it.key
                val isGranted = it.value
                if (isGranted) {
                    // Permission is granted
                } else {
                    // Permission is denied
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermissions()
        setContent {
            DevicesListScreen(koinViewModel()) { mac ->
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra(DetailActivity.MAC_PARAMETER, mac)
                startActivity(intent)
            }
        }
    }

    private fun checkPermissions() {
        activityResultLauncher.launch(permissionsNeeded)
    }
}

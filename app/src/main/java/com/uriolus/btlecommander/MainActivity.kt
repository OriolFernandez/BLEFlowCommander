package com.uriolus.btlecommander

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.uriolus.btlecommander.ui.theme.BTLECommanderTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val viewModel by viewModel<MainViewModel>()
    val permissionsNeeded = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
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
                val permissionName = it.key
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
            MainScreen(viewModel)
        }
    }

    private fun checkPermissions() {
        activityResultLauncher.launch(permissionsNeeded)
    }
}

@Composable
private fun MainScreen(viewModel: MainViewModel) {
    val scanStatus: ScanStatus by remember(viewModel) { viewModel.scanStatus }.collectAsState()
    BTLECommanderTheme {
        // A surface container using the 'background' color from the theme
        when (scanStatus) {
            ScanStatus.Idle -> UiForScannedStatus() { viewModel.scan() }
            is ScanStatus.Scanned -> UIForIddle()
            ScanStatus.Scanning -> UIForScanning()
        }

    }
}

@Composable
fun UIForScanning() {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.primary) {
        Text("Loading")
    }
}

@Composable
fun UIForIddle() {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.primary) {
        Text("Waiting")
    }
}

@Composable
fun UiForScannedStatus(onScan: () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        Greeting("Android") { onScan() }
    }
}

@Composable
fun Greeting(name: String, onScan: () -> Unit) {
    Column() {
        Text(text = "Hello $name!")
        Button(onClick = {
            println("Click")
            onScan()
        }) {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BTLECommanderTheme {
        Greeting("Android", { })
    }
}
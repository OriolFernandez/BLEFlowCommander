package com.uriolus.btlecommander

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uriolus.btlecommander.scanneddevices.BLEDevicePresentation
import com.uriolus.btlecommander.scanneddevices.DevicesList
import com.uriolus.btlecommander.scanneddevices.mapper.Mapper.toPresentation
import com.uriolus.btlecommander.ui.theme.BTLECommanderTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val viewModel by viewModel<MainViewModel>()
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
            MainScreen(viewModel)
        }
    }

    private fun checkPermissions() {
        activityResultLauncher.launch(permissionsNeeded)
    }
}

@Composable
private fun MainScreen(viewModel: MainViewModel) {
    val scanStatus: PresentationScanStatus by remember(viewModel) { viewModel.scanStatus }.collectAsState()
    BTLECommanderTheme {
        // A surface container using the 'background' color from the theme
        when (val status =
            scanStatus) {// this allows the smart cast to work with a delegate val as scanStatus
            PresentationScanStatus.Idle -> UiForScannedStatus() { viewModel.startScan() }
            is PresentationScanStatus.Scanned -> UIForScannedResults(status.devices.map { it.toPresentation() }) {
                viewModel.onDeviceClick(
                    it
                )
            }
            is PresentationScanStatus.ScanningDeviceFound -> UIForScanning(status.devices.map { it.toPresentation() },
                { viewModel.stopScan() }
            ) {
                viewModel.onDeviceClick(it)
            }
            is PresentationScanStatus.Error -> UIForError(status)
            PresentationScanStatus.Scanning -> UIForScanning(
                devices = emptyList(),
                onStop = { viewModel.stopScan() },
                onClick = {}
            )
        }
    }
}

@Composable
fun UIForScannedResults(
    devices: List<BLEDevicePresentation>,
    onClick: (BLEDevicePresentation) -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.secondary) {
        DevicesList(devices = devices, onClick)
    }
}


@Composable
fun UIForScanning(
    devices: List<BLEDevicePresentation>,
    onStop: () -> Unit,
    onClick: (BLEDevicePresentation) -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.primary) {
        Column(Modifier.padding(20.dp)) {
            Text(text = "Scanning")
            Button(onClick = {
                onStop()
            }) {
                Text(text = "Stop Scanner")
            }
            DevicesList(devices = devices, onClick)
        }
    }
}

@Composable
fun UIForIdle(onScan: () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.primary) {
        ReadyToScan { onScan() }
    }
}

@Composable
fun UIForError(scanStatus: PresentationScanStatus.Error) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.primary) {
        Text("Error: ${scanStatus.error.scanError}")
    }
}

@Composable
fun UiForScannedStatus(onScan: () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        ReadyToScan { onScan() }
    }
}

@Composable
fun ReadyToScan(onScan: () -> Unit) {
    Column(Modifier.padding(20.dp)) {
        Text(text = "Scan", color = Color.White)
        Button(onClick = {
            onScan()
        }) {
            Text(text = "Start scan", color = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BTLECommanderTheme {
        ReadyToScan { }
    }
}
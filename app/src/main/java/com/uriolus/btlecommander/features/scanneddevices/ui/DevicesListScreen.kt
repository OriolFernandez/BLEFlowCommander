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
import com.uriolus.btlecommander.features.scanneddevices.DevicesListViewModel
import com.uriolus.btlecommander.features.scanneddevices.NavigationState
import com.uriolus.btlecommander.features.scanneddevices.PresentationScanState
import com.uriolus.btlecommander.features.scanneddevices.mapper.Mapper.toPresentation
import com.uriolus.btlecommander.features.scanneddevices.models.BLEDevicePresentation
import com.uriolus.btlecommander.scanneddevices.DevicesList
import com.uriolus.btlecommander.ui.theme.BTLECommanderTheme

@Composable
fun DevicesListScreen(
    viewModel: DevicesListViewModel,
    onDeviceSelected: (String) -> Unit
) {

    val scanStatus: PresentationScanState by remember(viewModel) { viewModel.scanStatus }.collectAsState()
    val navigationStatus: NavigationState by remember(viewModel) { viewModel.navigationStatus }.collectAsState()

    BTLECommanderTheme {
        // A surface container using the 'background' color from the theme
        when (val status = scanStatus) {
            is PresentationScanState.Idle -> UiForScannedStatus() { viewModel.startScan() }
            is PresentationScanState.Scanned -> UIForScannedResults(status.devices.map { it.toPresentation() }) {
                viewModel.onDeviceClick(it)
            }
            is PresentationScanState.ScanningDeviceFound -> UIForScanning(status.devices.map { it.toPresentation() },
                { viewModel.stopScan() }
            ) {
                viewModel.onDeviceClick(it)
            }
            is PresentationScanState.Scanning -> UIForScanning(
                devices = emptyList(),
                onStop = { viewModel.stopScan() },
                onClick = {/*NOTHING since screen is empty*/ }
            )
            is PresentationScanState.Error -> UIForError(status)
        }
        when (val status = navigationStatus) {
            is NavigationState.Detail -> onDeviceSelected(status.device.mac)
            NavigationState.List -> {}//NOTHING
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
fun UIForError(scanStatus: PresentationScanState.Error) {
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
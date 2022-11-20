package com.uriolus.btlelib.data.datasource.impl

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.uriolus.btlelib.BLEDevice
import com.uriolus.btlelib.data.datasource.BLEDataSource
import com.uriolus.btlelib.data.datasource.mapping.toDonmain
import com.uriolus.btlelib.domain.ScanStatus
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

private const val SCAN_TIMEOUT = 30000L

@SuppressLint("MissingPermission")
class BLEDataSourceImpl(context: Application) : BLEDataSource {

    private val _stateFlow: MutableStateFlow<ScanStatus> = MutableStateFlow(ScanStatus.Stopped)
    val statusFlow: StateFlow<ScanStatus>
        get() = _stateFlow

    private var isScanning = false
    private val timeOutHandler = Handler(Looper.getMainLooper())
    private val bluetoothAdapter: BluetoothAdapter by lazy {
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            with(result.device) {
                _stateFlow.value = ScanStatus.DeviceFound(toDonmain())
                Log.d(
                    "ScanCallback",
                    "Found BLE device! Name: ${name ?: "Unnamed"}, address: $address"
                )
            }
        }
    }
    private val bleScanner by lazy {
        bluetoothAdapter.bluetoothLeScanner
    }

    private val scanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .build()

    override fun startScan(): StateFlow<ScanStatus>{
        bleScanner.startScan(null, scanSettings, scanCallback)
        setScanTimeOut()
        isScanning = true
       return _stateFlow
    }

    override fun stopScan() {
        bleScanner.stopScan(scanCallback)
    }

    private fun setScanTimeOut() {
        if (isScanning) {
            timeOutHandler.removeCallbacksAndMessages(null)
        }
        timeOutHandler.postDelayed({
            if (isScanning) {
                Log.d("ScanCallback", "Scanning Finished by timeout...")
                stopScan()
            }
        }, SCAN_TIMEOUT)
    }
}
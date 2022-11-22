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
import com.uriolus.btlelib.data.datasource.mapping.toBLEDevice
import com.uriolus.btlelib.domain.ScanError
import com.uriolus.btlelib.domain.ScanStatus
import kotlinx.coroutines.flow.*

private const val SCAN_TIMEOUT = 10000L

@SuppressLint("MissingPermission")
class BLEDataSourceImpl(context: Application) : BLEDataSource {

    private val _scanStatusFlow: MutableStateFlow<ScanStatus> = MutableStateFlow(ScanStatus.Stopped)
    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }
    private val bleScanner by lazy {
        bluetoothAdapter?.bluetoothLeScanner
    }
    private val devices: MutableSet<BLEDevice> = mutableSetOf()
    private var isScanning = false
    private val timeOutHandler = Handler(Looper.getMainLooper())

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            with(result) {
                devices.add(toBLEDevice())
                _scanStatusFlow.value = ScanStatus.Scanning(devices.toList())
                Log.d(
                    "ScanCallback",
                    "Found BLE device! Name: ${result.device.name ?: "Unnamed"}, address: ${result.device.address}"
                )
            }
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            Log.d("ScanCallback", " No idea what is this results: $results")
        }

        override fun onScanFailed(errorCode: Int) {
            Log.e("ScanCallback", "onScanFailed: code $errorCode")
        }
    }

    private val scanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .build()

    override fun connectToScanStatus(): StateFlow<ScanStatus> {
        return _scanStatusFlow
    }

    override fun startScan() {
        if (bluetoothAdapter.isAvailable()) {
            bleScanner?.startScan(null, scanSettings, scanCallback)
            setScanTimeOut()
            isScanning = true
        } else {
            _scanStatusFlow.value = ScanStatus.Error(ScanError.BluetoothNotAvailable)
        }
    }

    override fun stopScan() {
        timeOutHandler.removeCallbacksAndMessages(null)
        if (bluetoothAdapter.isAvailable()) {
            try {
                bleScanner?.stopScan(scanCallback)
                _scanStatusFlow.value = ScanStatus.Stopped // TODO not sure this should be here
            } catch (e: IllegalStateException) {
                _scanStatusFlow.value = ScanStatus.Error(ScanError.IllegalState)
            }
        } else {
            _scanStatusFlow.value = ScanStatus.Error(ScanError.BluetoothNotAvailable)
        }
    }

    private fun BluetoothAdapter?.isAvailable(): Boolean =
        this?.isEnabled ?: false

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
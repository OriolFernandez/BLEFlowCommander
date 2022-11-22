package com.uriolus.btlelib.data.datasource.mapping

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import com.uriolus.btlelib.BLEDevice

@SuppressLint("MissingPermission")
fun ScanResult.toBLEDevice(): BLEDevice {
    return BLEDevice(this.device.name ?: "NOT FOUND", this.device.address, this.rssi)
}
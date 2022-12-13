package com.uriolus.btlelib.scan.data.datasource.mapping

import android.annotation.SuppressLint
import android.bluetooth.le.ScanResult
import com.uriolus.btlelib.common.domain.model.BLEDevice

@SuppressLint("MissingPermission")
fun ScanResult.toBLEDevice(): BLEDevice {
    return BLEDevice(this.device.name ?: "NOT FOUND", this.device.address, this.rssi)
}
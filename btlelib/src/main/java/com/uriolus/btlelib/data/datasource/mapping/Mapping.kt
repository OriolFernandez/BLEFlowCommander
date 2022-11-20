package com.uriolus.btlelib.data.datasource.mapping

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import com.uriolus.btlelib.BLEDevice

@SuppressLint("MissingPermission")
fun BluetoothDevice.toDonmain(): BLEDevice {
    return BLEDevice(this.name ?: "NOT FOUND", this.address)
}
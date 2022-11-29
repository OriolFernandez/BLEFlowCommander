package com.uriolus.btlelib.common

import android.bluetooth.BluetoothDevice
import arrow.core.Either
import arrow.core.left
import arrow.core.right

class BLEDevicesCache {
    private val cache: MutableMap<String, BluetoothDevice> = mutableMapOf()
    fun storeBluetoothDevice(device: BluetoothDevice): Boolean {
        return if (device.address != null) {
            cache[device.address] = device
            true
        } else {
            false
        }
    }

    fun getBluetoothDevice(address: String): Either<Unit, BluetoothDevice> {
        val device: BluetoothDevice? = cache[address]
        return device?.right() ?: Unit.left()
    }
}
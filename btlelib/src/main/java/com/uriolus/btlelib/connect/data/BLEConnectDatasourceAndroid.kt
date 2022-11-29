package com.uriolus.btlelib.connect.data

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.util.Log
import arrow.core.Either
import arrow.core.right
import com.uriolus.btlelib.connect.domain.ConnectBLEDeviceError

class BLEConnectDatasourceAndroid(private val context: Context) : BLEConnectDataSource {
    private var bluetoothGatt: BluetoothGatt? = null
    private val gattCallback: BluetoothGattCallback = object : BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            val deviceAddress = gatt.device.address

            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    Log.w("BluetoothGattCallback", "Successfully connected to $deviceAddress")
                    // TODO: Store a reference to BluetoothGatt
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    Log.w("BluetoothGattCallback", "Successfully disconnected from $deviceAddress")
                    gatt.close()
                }
            } else {
                Log.w(
                    "BluetoothGattCallback",
                    "Error $status encountered for $deviceAddress! Disconnecting..."
                )
                gatt.close()
            }
        }
    }


    @SuppressLint("MissingPermission")
    override fun connect(device: BluetoothDevice): Either<ConnectBLEDeviceError, Unit> {
        bluetoothGatt = device.connectGatt(context, false, gattCallback)
        return Unit.right() //TODO
    }
}
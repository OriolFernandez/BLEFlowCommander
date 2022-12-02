package com.uriolus.btlelib.connect.data

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.util.Log
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.uriolus.btlelib.connect.domain.ConnectBLEDeviceError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine

class BLEConnectDatasourceAndroid(private val context: Context) : BLEConnectDataSource {
    private var bluetoothGatt: BluetoothGatt? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    @SuppressLint("MissingPermission")
    override suspend fun connect(device: BluetoothDevice): Either<ConnectBLEDeviceError, BluetoothGatt> {
        return suspendCancellableCoroutine { continuation ->
            val gattCallback: BluetoothGattCallback = object : BluetoothGattCallback() {
                @SuppressLint("MissingPermission")
                override fun onConnectionStateChange(
                    gatt: BluetoothGatt,
                    status: Int,
                    newState: Int
                ) {
                    val deviceAddress = gatt.device.address

                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        if (newState == BluetoothProfile.STATE_CONNECTED) {
                            Log.w(
                                "BluetoothGattCallback",
                                "Successfully connected to $deviceAddress"
                            )
                            continuation.resume(gatt.right()) { error ->
                                ConnectBLEDeviceError.ConnectionCanceled(error.message ?: "")
                            }
                        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                            Log.w(
                                "BluetoothGattCallback",
                                "Successfully disconnected from $deviceAddress"
                            )
                            gatt.close()
                            continuation.resume(ConnectBLEDeviceError.Disconnected.left()) { error ->
                                ConnectBLEDeviceError.ConnectionCanceled(error.message ?: "")
                            }
                        }
                    } else {
                        Log.w(
                            "BluetoothGattCallback",
                            "Error $status encountered for $deviceAddress! Disconnecting..."
                        )
                        continuation.resume(
                            ConnectBLEDeviceError.BluetoothError(status.toString()).left()
                        ) { error ->
                            ConnectBLEDeviceError.ConnectionCanceled(error.message ?: "")
                        }
                        gatt.close()
                    }
                }
            }
            bluetoothGatt = device.connectGatt(context, false, gattCallback)
            continuation.invokeOnCancellation {
                bluetoothGatt?.disconnect()
            }
        }
    }
}
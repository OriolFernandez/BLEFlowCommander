package com.uriolus.btlelib.connect.domain

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.content.Context
import com.uriolus.btlelib.common.domain.model.LogCustom
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filterIsInstance

@SuppressLint("MissingPermission")
class GattDevice(private val context: Context, private val bluetoothDevice: BluetoothDevice) {
    private var bluetoothGatt: BluetoothGatt? = null
    private val _gattFlow: MutableSharedFlow<GattEvent> = MutableSharedFlow()

    private val _loggerFlow = MutableSharedFlow<LogCustom>()
    val loggerFlow = _loggerFlow.asSharedFlow()

    private val callback =
        GatCallbackWithFlow(_gattFlow, _loggerFlow) {
            // TODO
        }


    fun connect(
        autoConnect: Boolean = true,
        transport: Int = BluetoothDevice.TRANSPORT_LE,
        phy: Int = BluetoothDevice.PHY_LE_1M,
    ): Flow<ConnectionChanged> {
        bluetoothGatt = bluetoothDevice.connectGatt(context, autoConnect, callback, transport, phy)
        return _gattFlow.filterIsInstance<ConnectionChanged>()
    }
}
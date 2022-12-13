package com.uriolus.btlelib.connect.data

import GattCharacteristic
import GattDescriptor
import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import android.util.Log
import arrow.core.left
import com.uriolus.btlelib.common.domain.model.LogCustom
import com.uriolus.btlelib.connect.domain.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect

class BLEConnectDatasourceFlowAndroid(private val gattDevice: GattDevice) : BLEConnectDataSourceFlow {
    private var bluetoothGatt: BluetoothGatt? = null

    private val _gattFlow: MutableSharedFlow<GattEvent> = MutableSharedFlow()

    private val _loggerFlow = MutableSharedFlow<LogCustom>()
    val loggerFlow = _loggerFlow.asSharedFlow()

    private val gattCallback = GatCallbackWithFlow(_gattFlow, _loggerFlow/* TODO on descriptor changed*/)

    @SuppressLint("MissingPermission")
    override suspend fun connect(): Flow<ConnectionChanged> {

      return  gattDevice.connect()

        /*
        continuation.invokeOnCancellation {
            bluetoothGatt?.disconnect()
        }
         */
    }


}

fun MutableSharedFlow<LogCustom>.log(level: Int, msg: String) {
    this.tryEmit(LogCustom(level, msg))
}
package com.uriolus.btlelib.connect.data

import android.annotation.SuppressLint
import android.bluetooth.*
import arrow.core.Either
import com.uriolus.btlelib.common.domain.model.LogCustom
import com.uriolus.btlelib.connect.domain.*
import com.wallbox.bluetooth.external.model.BTMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class BLEConnectDatasourceFlowAndroid(private val gattDevice: GattDevice) : BLEConnectDataSourceFlow {
    private var bluetoothGatt: BluetoothGatt? = null

    private val _gattFlow: MutableSharedFlow<GattEvent> = MutableSharedFlow()

    private val _loggerFlow = MutableSharedFlow<LogCustom>()
    val loggerFlow = _loggerFlow.asSharedFlow()

    private val gattCallback = GatCallbackWithFlow(_gattFlow, _loggerFlow/* TODO on descriptor changed*/)

    @SuppressLint("MissingPermission")
    override suspend fun connect(): Flow<ConnectionChanged> {
      return  gattDevice.connect()
    }

    override suspend fun setMode(btMode: BTMode): Either<SetModeError, Unit> {
        return gattDevice.setMode(btMode)
    }
}

fun MutableSharedFlow<LogCustom>.log(level: Int, msg: String) {
    this.tryEmit(LogCustom(level, msg))
}
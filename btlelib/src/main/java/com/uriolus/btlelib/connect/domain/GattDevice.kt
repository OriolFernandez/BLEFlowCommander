package com.uriolus.btlelib.connect.domain

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.os.Build
import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.uriolus.btlelib.common.domain.model.LogCustom
import com.uriolus.btlelib.connect.domain.modules.BluetoothModules
import com.wallbox.bluetooth.external.model.BTMode
import kotlinx.coroutines.flow.*
import java.util.*

@SuppressLint("MissingPermission")
class GattDevice(private val context: Context, private val bluetoothDevice: BluetoothDevice) {
    private var bluetoothGatt: BluetoothGatt? = null
    private val _gattFlow: MutableSharedFlow<GattEvent> = MutableSharedFlow()
    private val _loggerFlow = MutableSharedFlow<LogCustom>()
    val loggerFlow = _loggerFlow.asSharedFlow()

    private val module: BluetoothModules = BluetoothModules.AMSBleService

    private val callback = GatCallbackWithFlow(_gattFlow, _loggerFlow) {
        // TODO service changed
    }


    fun connect(
        autoConnect: Boolean = true,
        transport: Int = BluetoothDevice.TRANSPORT_LE,
        phy: Int = BluetoothDevice.PHY_LE_1M,
    ): Flow<ConnectionChanged> {
        bluetoothGatt = bluetoothDevice.connectGatt(context, autoConnect, callback, transport, phy)
        return _gattFlow.filterIsInstance<ConnectionChanged>()
    }

    suspend fun setMode(
        busMode: BTMode,
    ): Either<SetModeError, Unit> {
        return bluetoothGatt?.let { gatt ->
            val connectResult = connect(false).first()
            checkConnectionStatus(connectResult.status)
                .mapLeft { SetModeError.ConnectionError }
                .flatMap {
                    gatt.getService(UUID.fromString(module.serviceUuid))
                        .continueIfNotNull()
                }.mapLeft {
                    SetModeError.ServiceIsNull
                }
                .flatMap { bluetoothService ->
                    bluetoothService.getCharacteristic(module.modeCharacteristicUuid.toUUID())
                        .continueIfNotNull()
                }.mapLeft { SetModeError.CharacteristicIsNull }
                .flatMap { characteristic ->
                    writeCharacteristic(busMode, gatt, characteristic)
                    val characteristicWrittenResult =
                        _gattFlow.filterIsInstance<CharacteristicWritten>().first()
                    checkWrittenStatus(characteristicWrittenResult.status)
                        .mapLeft {
                            SetModeError.WriteCharacteristicDuringSetModeError(it)
                        }
                }
        } ?: SetModeError.GattIsNull.left()
    }

    private fun writeCharacteristic(
        busMode: BTMode,
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic
    ) {
        val writeType = BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
        val value = ByteArray(1).apply { this[0] = (busMode.value).toByte() }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            gatt.writeCharacteristic(characteristic, value, writeType)
        } else {
            characteristic.writeType = writeType
            characteristic.value = value
            gatt.writeCharacteristic(characteristic)
        }
    }

}


private fun <T> T?.continueIfNotNull(): Either<Unit, T> {
    return this?.right() ?: Unit.left()
}

private fun String.toUUID(): UUID {
    return UUID.fromString(this)
}

private fun checkConnectionStatus(status: Int): Either<ConnectionError, Unit> = when (status) {
    BluetoothGatt.GATT_SUCCESS -> {
        Unit.right()
    }
    BluetoothGatt.GATT_FAILURE -> {
        ConnectionError.GattFailure.left()
    }
    BluetoothGatt.GATT_INSUFFICIENT_AUTHENTICATION -> {
        // Call bond
        ConnectionError.GattInsufficientAuthentication.left()
    }
    BluetoothGatt.GATT_INSUFFICIENT_AUTHORIZATION -> {
        ConnectionError.GattInsufficientAuthorization.left()
    }
    BluetoothGatt.GATT_INSUFFICIENT_ENCRYPTION -> {
        // Call bond
        ConnectionError.GattInsufficientEncryption.left()
    }
    BluetoothGatt.GATT_CONNECTION_CONGESTED -> {
        // Call bond
        ConnectionError.GattInsufficientEncryption.left()
    }
    else -> ConnectionError.GattConnectionError.left()
}

private fun checkWrittenStatus(status: Int): Either<WriteCharacteristicError, Unit> {
    return when (status) {
        BluetoothGatt.GATT_SUCCESS -> {
            Unit.right()
        }
        BluetoothGatt.GATT_INVALID_ATTRIBUTE_LENGTH -> {
            WriteCharacteristicError.InvalidAttributeLength.left()
        }
        BluetoothGatt.GATT_WRITE_NOT_PERMITTED -> {
            WriteCharacteristicError.GattWriteNotPermitted.left()
        }
        else -> {
            WriteCharacteristicError.ErrorWritingCharacteristic.left()
        }
    }
}

sealed class SetModeError() {
    object ConnectionError : SetModeError()
    object ServiceIsNull : SetModeError()
    object CharacteristicIsNull : SetModeError()
    data class WriteCharacteristicDuringSetModeError(val error: WriteCharacteristicError) :
        SetModeError()

    object GattIsNull : SetModeError()
}

sealed class WriteCharacteristicError {
    object InvalidAttributeLength : WriteCharacteristicError()
    object GattWriteNotPermitted : WriteCharacteristicError()
    object ErrorWritingCharacteristic : WriteCharacteristicError()
}

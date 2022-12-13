package com.uriolus.btlelib.connect.domain

import GattCharacteristic
import GattDescriptor
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.util.Log
import com.uriolus.btlelib.common.domain.model.LogCustom
import com.uriolus.btlelib.connect.data.log
import kotlinx.coroutines.flow.MutableSharedFlow

internal class GatCallbackWithFlow(
    private val flow: MutableSharedFlow<GattEvent>,
    private val logger: MutableSharedFlow<LogCustom>?,
    private val servicesChanged: ((gatt: BluetoothGatt) -> Unit)? = null
) : BluetoothGattCallback() {

    override fun onPhyUpdate(gatt: BluetoothGatt?, txPhy: Int, rxPhy: Int, status: Int) {
        val phyUpdate = PhyUpdate(txPhy, rxPhy, status)
        logger?.log(Log.INFO, "onPhyUpdate: $phyUpdate")
        flow.tryEmit(phyUpdate)
    }

    override fun onPhyRead(gatt: BluetoothGatt, txPhy: Int, rxPhy: Int, status: Int) {
        val phyRead = PhyRead(txPhy, rxPhy, status)
        logger?.log(Log.INFO, "onPhyRead: $phyRead")
        flow.tryEmit(phyRead)
    }

    override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
        val connectionChanged = ConnectionChanged(status, ConnectionState.fromState(newState))
        logger?.log(Log.INFO, "onConnectionStateChange: $connectionChanged")
        flow.tryEmit(connectionChanged)
    }

    override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
        val servicesDiscovered = ServicesDiscovered(status)
        logger?.log(Log.INFO, "onServicesDiscovered: $servicesDiscovered")
        flow.tryEmit(servicesDiscovered)
        servicesChanged?.let { it(gatt) }
    }

    override fun onCharacteristicWrite(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
        status: Int
    ) {
        val characteristicWritten = CharacteristicWritten(
            GattCharacteristic(characteristic),
            status
        )
        logger?.log(Log.INFO, "onCharacteristicWrite: $characteristicWritten")
        flow.tryEmit(characteristicWritten)
    }

    override fun onDescriptorWrite(
        gatt: BluetoothGatt,
        descriptor: BluetoothGattDescriptor,
        status: Int
    ) {
        val descriptorWritten = DescriptorWritten(
            GattCharacteristic(descriptor.characteristic),
            descriptor.uuid,
            status
        )
        logger?.log(Log.INFO, "onDescriptorWrite: $descriptorWritten")
        flow.tryEmit(descriptorWritten)
    }

    override fun onCharacteristicRead(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
        status: Int
    ) {
        val characteristicRead = CharacteristicRead(
            GattCharacteristic(characteristic),
            characteristic.value,
            status
        )
        logger?.log(Log.INFO, "onCharacteristicRead: $characteristicRead")
        flow.tryEmit(characteristicRead)
    }

    override fun onCharacteristicChanged(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic
    ) {
        val characteristicChanged = CharacteristicChanged(
            GattCharacteristic(characteristic),
            characteristic.value
        )
        logger?.log(Log.INFO, "onCharacteristicChanged: $characteristicChanged")
        flow.tryEmit(characteristicChanged)
    }

    override fun onDescriptorRead(
        gatt: BluetoothGatt,
        descriptor: BluetoothGattDescriptor,
        status: Int
    ) {
        val descriptorRead = DescriptorRead(
            GattCharacteristic(descriptor.characteristic),
            GattDescriptor(descriptor),
            descriptor.value,
            status
        )
        logger?.log(Log.INFO, "onDescriptorRead: $descriptorRead")
        flow.tryEmit(descriptorRead)
    }

    override fun onReliableWriteCompleted(gatt: BluetoothGatt?, status: Int) {
        val reliableWriteCompleted = ReliableWriteCompleted(status)
        logger?.log(Log.INFO, "onReliableWriteCompleted: $reliableWriteCompleted")
        flow.tryEmit(reliableWriteCompleted)
    }

    override fun onReadRemoteRssi(gatt: BluetoothGatt?, rssi: Int, status: Int) {
        val readRemoteRssi = ReadRemoteRssi(rssi, status)
        logger?.log(Log.INFO, "onReadRemoteRssi: $readRemoteRssi")
        flow.tryEmit(readRemoteRssi)
    }

    override fun onMtuChanged(gatt: BluetoothGatt?, mtu: Int, status: Int) {
        val mtuChanged = MtuChanged(mtu, status)
        logger?.log(Log.INFO, "onMtuChanged: $mtuChanged")
        flow.tryEmit(mtuChanged)
    }

    override fun onServiceChanged(gatt: BluetoothGatt) {
        val serviceChanged = ServiceChanged
        logger?.log(Log.INFO, "onServiceChanged: $serviceChanged")
        flow.tryEmit(serviceChanged)
        servicesChanged?.let { it(gatt) }
    }
}
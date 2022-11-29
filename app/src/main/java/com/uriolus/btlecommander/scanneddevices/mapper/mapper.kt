package com.uriolus.btlecommander.scanneddevices.mapper

import com.uriolus.btlecommander.scanneddevices.BLEDevicePresentation
import com.uriolus.btlelib.common.domain.BLEDevice

object Mapper {
    fun BLEDevice.toPresentation(): BLEDevicePresentation {
        return BLEDevicePresentation(this.name, this.mac, this.rssi)
    }
}
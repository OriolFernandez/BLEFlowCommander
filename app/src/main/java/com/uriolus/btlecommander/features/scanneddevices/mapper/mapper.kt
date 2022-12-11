package com.uriolus.btlecommander.features.scanneddevices.mapper

import com.uriolus.btlecommander.features.scanneddevices.models.BLEDevicePresentation
import com.uriolus.btlelib.common.domain.BLEDevice

object Mapper {
    fun BLEDevice.toPresentation(): BLEDevicePresentation {
        return BLEDevicePresentation(this.name, this.mac, this.rssi)
    }
}
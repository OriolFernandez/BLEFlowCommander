package com.uriolus.app_normal.list.mapper

import com.uriolus.app_normal.list.models.BLEDevicePresentation
import com.uriolus.btlelib.common.domain.model.BLEDevice

object Mapper {
    fun BLEDevice.toPresentation(): BLEDevicePresentation {
        return BLEDevicePresentation(this.name, this.mac, this.rssi)
    }
}
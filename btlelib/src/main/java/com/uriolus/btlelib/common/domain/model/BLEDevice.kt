package com.uriolus.btlelib.common.domain.model

data class BLEDevice(val name: String, val mac: String, val rssi: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BLEDevice

        if (mac != other.mac) return false

        return true
    }

    override fun hashCode(): Int {
        return mac.hashCode()
    }

}
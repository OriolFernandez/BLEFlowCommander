package com.uriolus.btlelib.domain

sealed class ScanError {
    object BluetoothNotAvailable : ScanError()
    object Unknown : ScanError()
     object  IllegalState: ScanError()

}

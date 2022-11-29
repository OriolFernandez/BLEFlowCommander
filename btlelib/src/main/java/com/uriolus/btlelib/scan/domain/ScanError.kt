package com.uriolus.btlelib.scan.domain

sealed class ScanError {
    object BluetoothNotAvailable : ScanError()
    object Unknown : ScanError()
     object  IllegalState: ScanError()

}

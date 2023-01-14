package com.uriolus.btlelib.connect.domain

sealed class ConnectionError() {
    object GattFailure : ConnectionError()
    object GattInsufficientAuthentication : ConnectionError()
    object GattInsufficientAuthorization : ConnectionError()
    object GattInsufficientEncryption : ConnectionError()
    object GattConnectionError : ConnectionError()
}

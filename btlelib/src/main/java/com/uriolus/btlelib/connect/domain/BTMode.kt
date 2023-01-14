package com.wallbox.bluetooth.external.model

/**
 *  This class represents the bluetooth mode to work with.
 */
enum class BTMode(val value: Int) {

    /**
     * An invalid bus mode value that indicates the real value has not been determined yet.
     */
    UNKNOWN_MODE(0),

    /**
     * Send and receive serial data between the BT data lines and Bluetooth.
     */
    STREAM_MODE(1),

    /**
     * Data received over the serial lines is processed as a command and results returned over the serial lines.
     */
    LOCAL_COMMAND_MODE(2),

    /**
     * Data received over Bluetooth is processed as a command and results returned over Bluetooth.
     */
    COMMAND_MODE(3);

    companion object {
        fun get(value: Int): BTMode = values().find { it.value == value } ?: STREAM_MODE
        fun get(value: String): BTMode = values().find { it.name == value } ?: STREAM_MODE
    }
}
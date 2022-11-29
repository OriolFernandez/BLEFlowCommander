package com.uriolus.btlelib.statemonitor.service

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.uriolus.btlelib.statemonitor.domain.BluetoothState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.atomic.AtomicInteger

class BluetoothStateMonitorAndroid(private val context: Context) : BluetoothStateMonitor {
    private val _bluetoothStateFlow = MutableStateFlow<BluetoothState>(BluetoothState.Unknown)
    private var subscribersCount = AtomicInteger(0)

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val state = when (intent.action) {
                BluetoothAdapter.ACTION_STATE_CHANGED ->
                    intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, INVALID_STATE)
                BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED ->
                    intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, INVALID_STATE)
                else -> BluetoothAdapter.STATE_OFF
            }
            println("Bluetooth new state: $state")
            when (state) {
                INVALID_STATE -> _bluetoothStateFlow.update { BluetoothState.Unknown }
                BluetoothAdapter.STATE_OFF,
                BluetoothAdapter.STATE_DISCONNECTED ->
                    _bluetoothStateFlow.update { BluetoothState.Disconnected }
                BluetoothAdapter.STATE_ON,
                BluetoothAdapter.STATE_CONNECTED ->
                    _bluetoothStateFlow.update { BluetoothState.Connected }
            }
        }
    }

    override fun subscribe(): StateFlow<BluetoothState> {
        if (subscribersCount.incrementAndGet() == 1) {
            val currentState: BluetoothState = getCurrentState()
            _bluetoothStateFlow.value = currentState
            println("Bluetooth registered")
            context.registerReceiver(broadcastReceiver, INTENT_FILTER)
        }
        return _bluetoothStateFlow.asStateFlow()
    }

    private fun getCurrentState(): BluetoothState {
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val adapter = bluetoothManager.adapter
        return if (adapter != null && adapter.isEnabled) {
            BluetoothState.Connected
        } else {
            BluetoothState.Disconnected
        }
    }

    override fun unsubscribe() {
        if (subscribersCount.decrementAndGet() == 0) {
            println("Bluetooth unregistered")
            context.unregisterReceiver(broadcastReceiver)
        }
    }

    companion object {
        private const val INVALID_STATE = -1
        private val INTENT_FILTER = IntentFilter().apply {
            addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
            addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
        }
    }
}
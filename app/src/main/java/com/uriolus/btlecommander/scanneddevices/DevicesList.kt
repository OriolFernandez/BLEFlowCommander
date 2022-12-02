package com.uriolus.btlecommander.scanneddevices

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DevicesList(
    devices: List<BLEDevicePresentation>,
    onItemClick: (BLEDevicePresentation) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = devices) { device ->
            Row(
                modifier = Modifier.clickable {
                    onItemClick(device)
                }
            ) {
                Text("name:${device.name}")
                Text("mac:${device.mac}")
            }
        }
    }
}

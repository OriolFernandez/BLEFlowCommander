package com.uriolus.btlecommander.features.detail.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.uriolus.btlecommander.features.scanneddevices.models.BLEDevicePresentation

@Composable
fun DetailDevice(device: BLEDevicePresentation) {

    Surface {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = device.name,
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.h4,
                    color = MaterialTheme.colors.onSurface
                )
                Text(
                    text = device.mac,
                    modifier = Modifier.alpha(0.8f),
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onSurface
                )
                Text(
                    text = "$device.rssi",
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .alpha(0.7f),
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface,
                    textAlign = TextAlign.Justify
                )
            }
        }
    }
}
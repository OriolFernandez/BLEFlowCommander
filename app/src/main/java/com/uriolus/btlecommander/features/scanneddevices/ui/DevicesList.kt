package com.uriolus.btlecommander.features.scanneddevices.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.uriolus.btlecommander.features.scanneddevices.models.BLEDevicePresentation

@Composable
fun DevicesList(
    devices: List<BLEDevicePresentation>,
    onItemClick: (BLEDevicePresentation) -> Unit
) {
    val navController: NavHostController = rememberNavController()
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items = devices) { device ->
            Row(
                modifier = Modifier.clickable {
                    println("Item clicked")
                    onItemClick(device)
                }
            ) {
                Text("${device.name} ", fontSize = 18.sp)
                Text("${device.mac}", fontSize = 18.sp)
            }
        }
    }
}

@Preview
@Composable
fun ListPreview(){
    val dev1= BLEDevicePresentation("NO NAME","7D:33:97:7E:3A:9A",23)
    val dev2= BLEDevicePresentation("NO NAME","7D:33:97:7E:3A:9B",24)
    val dev3= BLEDevicePresentation("NO NAME","7D:33:97:7E:3A:9C",25)
    DevicesList(devices = listOf(dev1,dev2,dev3)){}
}
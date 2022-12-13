package com.uriolus.btlelib.connect.data

import com.uriolus.btlelib.connect.domain.ConnectionChanged
import kotlinx.coroutines.flow.Flow

interface BLEConnectDataSourceFlow {

   suspend fun connect(): Flow<ConnectionChanged>
}
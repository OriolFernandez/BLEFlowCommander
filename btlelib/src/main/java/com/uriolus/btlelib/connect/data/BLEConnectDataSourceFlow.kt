package com.uriolus.btlelib.connect.data

import arrow.core.Either
import com.uriolus.btlelib.connect.domain.ConnectionChanged
import com.uriolus.btlelib.connect.domain.SetModeError
import com.wallbox.bluetooth.external.model.BTMode
import kotlinx.coroutines.flow.Flow

interface BLEConnectDataSourceFlow {

   suspend fun connect(): Flow<ConnectionChanged>
   suspend fun setMode(btMode: BTMode): Either<SetModeError,Unit>
}
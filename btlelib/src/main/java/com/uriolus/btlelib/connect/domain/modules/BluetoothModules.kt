package com.uriolus.btlelib.connect.domain.modules

sealed class BluetoothModules(
    val serviceUuid: String,
    val rxCharacteristicUuid: String,
    val txCharacteristicUuid: String,
    val modeCharacteristicUuid: String
) {

    object AMSBleService : BluetoothModules(
        serviceUuid = "175f8f23-a570-49bd-9627-815a6a27de2a",
        rxCharacteristicUuid = "1cce1ea8-bd34-4813-a00a-c76e028fadcb",
        txCharacteristicUuid = "cacc07ff-ffff-4c48-8fae-a9ef71b75e26",
        modeCharacteristicUuid = "20b9794f-da1a-4d14-8014-a0fb9cefb2f7"
    )

    object BgExpressBleService : BluetoothModules(
        serviceUuid = "331a36f5-2459-45ea-9d95-6142f0c4b307",
        rxCharacteristicUuid = "a9da6040-0823-4995-94ec-9ce41ca28833",
        txCharacteristicUuid = "a73e9a10-628f-4494-a099-12efaf72258f",
        modeCharacteristicUuid = "75a9f022-af03-4e41-b4bc-9de90a47d50b"
    )

    object UbloxExpressBleService : BluetoothModules(
        serviceUuid = "2456e1b9-26e2-8f83-e744-f34f01e9d701",
        rxCharacteristicUuid = "2456e1b9-26e2-8f83-e744-f34f01e9d703",
        txCharacteristicUuid = "2456e1b9-26e2-8f83-e744-f34f01e9d703",
        modeCharacteristicUuid = "2456e1b9-26e2-8f83-e744-f34f01e9d703"
    )

    companion object {

        private val servicesList by lazy { listOf(AMSBleService, BgExpressBleService, UbloxExpressBleService) }

        fun getBluetoothServiceBy(serviceUuid: String): BluetoothModules? =
            servicesList.find { it.serviceUuid == serviceUuid }
    }
}
package io.mosip.tuvali.verifier

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService

class GattService {
  //TODO: Update UUIDs as per specification

  fun create(): BluetoothGattService {
    val service = BluetoothGattService(
      UUIDConstants.SERVICE_UUID,
      BluetoothGattService.SERVICE_TYPE_PRIMARY
    )

    val identifyRequestChar = BluetoothGattCharacteristic(
      UUIDConstants.IDENTIFY_REQUEST_CHAR_UUID,
      BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE or BluetoothGattCharacteristic.PROPERTY_WRITE,
      BluetoothGattCharacteristic.PERMISSION_WRITE
    )

    val requestSizeChar = BluetoothGattCharacteristic(
      UUIDConstants.REQUEST_SIZE_CHAR_UUID,
      BluetoothGattCharacteristic.PROPERTY_READ or BluetoothGattCharacteristic.PROPERTY_INDICATE,
      BluetoothGattCharacteristic.PERMISSION_READ
    )

    val requestChar = BluetoothGattCharacteristic(
      UUIDConstants.REQUEST_CHAR_UUID,
      BluetoothGattCharacteristic.PROPERTY_READ or BluetoothGattCharacteristic.PROPERTY_INDICATE,
      BluetoothGattCharacteristic.PERMISSION_READ
    )

    val responseSizeChar = BluetoothGattCharacteristic(
      UUIDConstants.RESPONSE_SIZE_CHAR_UUID,
      BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE or BluetoothGattCharacteristic.PROPERTY_WRITE,
      BluetoothGattCharacteristic.PERMISSION_WRITE
    )

    val submitResponseChar = BluetoothGattCharacteristic(
      UUIDConstants.SUBMIT_RESPONSE_CHAR_UUID,
      BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE or BluetoothGattCharacteristic.PROPERTY_WRITE,
      BluetoothGattCharacteristic.PERMISSION_WRITE
    )

    val transferReportRequestChar = BluetoothGattCharacteristic(
      UUIDConstants.TRANSFER_REPORT_REQUEST_CHAR_UUID,
      BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE,
      BluetoothGattCharacteristic.PERMISSION_WRITE
    )

    val transferReportResponseChar = BluetoothGattCharacteristic(
      UUIDConstants.TRANSFER_REPORT_RESPONSE_CHAR_UUID,
      BluetoothGattCharacteristic.PROPERTY_INDICATE,
      BluetoothGattCharacteristic.PERMISSION_READ
    )

    val verificationStatusChar = BluetoothGattCharacteristic(
      UUIDConstants.VERIFICATION_STATUS_CHAR_UUID,
      BluetoothGattCharacteristic.PROPERTY_INDICATE,
      BluetoothGattCharacteristic.PERMISSION_READ
    )

    val disconnectStatusChar = BluetoothGattCharacteristic(
      UUIDConstants.DISCONNECT_CHAR_UUID,
      BluetoothGattCharacteristic.PROPERTY_INDICATE,
      BluetoothGattCharacteristic.PERMISSION_READ
    )

    service.addCharacteristic(identifyRequestChar)
    service.addCharacteristic(requestSizeChar)
    service.addCharacteristic(requestChar)
    service.addCharacteristic(responseSizeChar)
    service.addCharacteristic(submitResponseChar)
    service.addCharacteristic(transferReportRequestChar)
    service.addCharacteristic(transferReportResponseChar)
    service.addCharacteristic(verificationStatusChar)
    service.addCharacteristic(disconnectStatusChar)

    return service
  }
}

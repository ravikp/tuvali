import Foundation
import CoreBluetooth

typealias CharacteristicTuple = (properties: CBCharacteristicProperties, permissions: CBAttributePermissions, value: Data?)

struct CBcharatcteristic {

    let identifyRequestChar = CBMutableCharacteristic(type: CBUUID(string: UUIDConstants.CharacteristicIds.IDENTIFY_REQUEST_CHAR_UUID.rawValue), properties: characteristicsMap[UUIDConstants.CharacteristicIds.IDENTIFY_REQUEST_CHAR_UUID.rawValue]!.properties, value: nil, permissions: characteristicsMap[UUIDConstants.CharacteristicIds.IDENTIFY_REQUEST_CHAR_UUID.rawValue]!.permissions)

    let requestSizeChar = CBMutableCharacteristic(type: CBUUID(string: UUIDConstants.CharacteristicIds.REQUEST_SIZE_CHAR_UUID.rawValue), properties: characteristicsMap[UUIDConstants.CharacteristicIds.REQUEST_SIZE_CHAR_UUID.rawValue]!.properties, value: nil, permissions: characteristicsMap[UUIDConstants.CharacteristicIds.REQUEST_SIZE_CHAR_UUID.rawValue]!.permissions)

    let requestChar = CBMutableCharacteristic(type: CBUUID(string: UUIDConstants.CharacteristicIds.REQUEST_CHAR_UUID.rawValue), properties: characteristicsMap[UUIDConstants.CharacteristicIds.REQUEST_CHAR_UUID.rawValue]!.properties, value: nil, permissions: characteristicsMap[UUIDConstants.CharacteristicIds.REQUEST_CHAR_UUID.rawValue]!.permissions)

    let responseSizeChar = CBMutableCharacteristic(type: CBUUID(string: UUIDConstants.CharacteristicIds.RESPONSE_SIZE_CHAR_UUID.rawValue), properties: characteristicsMap[UUIDConstants.CharacteristicIds.RESPONSE_SIZE_CHAR_UUID.rawValue]!.properties, value: nil, permissions: characteristicsMap[UUIDConstants.CharacteristicIds.RESPONSE_SIZE_CHAR_UUID.rawValue]!.permissions)

    let submitResponseChar = CBMutableCharacteristic(type: CBUUID(string: UUIDConstants.CharacteristicIds.SUBMIT_RESPONSE_CHAR_UUID.rawValue), properties: characteristicsMap[UUIDConstants.CharacteristicIds.SUBMIT_RESPONSE_CHAR_UUID.rawValue]!.properties, value: characteristicsMap[UUIDConstants.CharacteristicIds.SUBMIT_RESPONSE_CHAR_UUID.rawValue]!.value, permissions: characteristicsMap[UUIDConstants.CharacteristicIds.SUBMIT_RESPONSE_CHAR_UUID.rawValue]!.permissions)

    let transferReportRequestChar = CBMutableCharacteristic(type: CBUUID(string: UUIDConstants.CharacteristicIds.TRANSFER_REPORT_REQUEST_CHAR_UUID.rawValue), properties: characteristicsMap[UUIDConstants.CharacteristicIds.TRANSFER_REPORT_REQUEST_CHAR_UUID.rawValue]!.properties, value: characteristicsMap[UUIDConstants.CharacteristicIds.TRANSFER_REPORT_REQUEST_CHAR_UUID.rawValue]!.value, permissions: characteristicsMap[UUIDConstants.CharacteristicIds.TRANSFER_REPORT_REQUEST_CHAR_UUID.rawValue]!.permissions)

    let transferReportResponseChar = CBMutableCharacteristic(type: CBUUID(string: UUIDConstants.CharacteristicIds.TRANSFER_REPORT_RESPONSE_CHAR_UUID.rawValue), properties: characteristicsMap[UUIDConstants.CharacteristicIds.TRANSFER_REPORT_RESPONSE_CHAR_UUID.rawValue]!.properties, value: characteristicsMap[UUIDConstants.CharacteristicIds.TRANSFER_REPORT_RESPONSE_CHAR_UUID.rawValue]!.value, permissions: characteristicsMap[UUIDConstants.CharacteristicIds.TRANSFER_REPORT_RESPONSE_CHAR_UUID.rawValue]!.permissions)

    let verificationStatusChar = CBMutableCharacteristic(type: CBUUID(string: UUIDConstants.CharacteristicIds.VERIFICATION_STATUS_CHAR_UUID.rawValue), properties: characteristicsMap[UUIDConstants.CharacteristicIds.VERIFICATION_STATUS_CHAR_UUID.rawValue]!.properties, value: characteristicsMap[UUIDConstants.CharacteristicIds.VERIFICATION_STATUS_CHAR_UUID.rawValue]!.value, permissions: characteristicsMap[UUIDConstants.CharacteristicIds.VERIFICATION_STATUS_CHAR_UUID.rawValue]!.permissions)
}

// TODO: Add conn status change everywhere

let characteristicsMap: [String: CharacteristicTuple] = [
    UUIDConstants.CharacteristicIds.IDENTIFY_REQUEST_CHAR_UUID.rawValue : (properties: CBCharacteristicProperties([.writeWithoutResponse, .write]), permissions: CBAttributePermissions([.writeable]), value: nil),
    UUIDConstants.CharacteristicIds.REQUEST_SIZE_CHAR_UUID.rawValue : (properties: CBCharacteristicProperties([.read, .indicate]), permissions: CBAttributePermissions([.readable]), value: nil),
    UUIDConstants.CharacteristicIds.REQUEST_CHAR_UUID.rawValue : (properties: CBCharacteristicProperties([.read, .indicate]), permissions: CBAttributePermissions([.readable]), value: nil),
    UUIDConstants.CharacteristicIds.RESPONSE_SIZE_CHAR_UUID.rawValue : (properties: CBCharacteristicProperties([.writeWithoutResponse, .write]), permissions: CBAttributePermissions([.writeable]), value: nil),
    UUIDConstants.CharacteristicIds.SUBMIT_RESPONSE_CHAR_UUID.rawValue : (properties: CBCharacteristicProperties([.writeWithoutResponse, .write]), permissions: CBAttributePermissions([.writeable]), value: nil),
    UUIDConstants.CharacteristicIds.TRANSFER_REPORT_REQUEST_CHAR_UUID.rawValue : (properties: CBCharacteristicProperties([.writeWithoutResponse]), permissions: CBAttributePermissions([.writeable]), value: nil),
    UUIDConstants.CharacteristicIds.TRANSFER_REPORT_RESPONSE_CHAR_UUID.rawValue : (properties: CBCharacteristicProperties([.indicate]), permissions: CBAttributePermissions([.readable]), value: nil),
    UUIDConstants.CharacteristicIds.VERIFICATION_STATUS_CHAR_UUID.rawValue : (properties: CBCharacteristicProperties([.indicate]), permissions: CBAttributePermissions([.readable]), value: nil),
]

enum NotificationEvent: String {
    case EXCHANGE_RECEIVER_INFO = "EXCHANGE_RECEIVER_INFO"
    case CREATE_CONNECTION = "CREATE_CONNECTION"
    case RESPONSE_SIZE_WRITE_SUCCESS = "RESPONSE_SIZE_WRITE_SUCCESS"
    case HANDLE_TRANSFER_REPORT = "HANDLE_TRANSFER_REPORT"
    case INIT_RESPONSE_CHUNK_TRANSFER = "INIT_RESPONSE_CHUNK_TRANSFER"
    case VERIFICATION_STATUS_RESPONSE = "VERIFICATION_STATUS_RESPONSE"
    case DISCONNECT_STATUS_CHANGE = "DISCONNECT_STATUS_CHANGE"

}




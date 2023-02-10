import Foundation
import CoreBluetooth
import os
@available(iOS 13.0, *)
extension Central: CBPeripheralDelegate {
    func peripheral(_ peripheral: CBPeripheral, didDiscoverServices error: Error?) {
        if let error = error {
            retryServicesDiscovery(peripheral)
            return
        }

        guard let peripheralServices = peripheral.services else {
            retryServicesDiscovery(peripheral)
            return
        }

        let serviceUUIDS = peripheralServices.map({ service in
            return service.uuid
        })

        if !serviceUUIDS.contains(Peripheral.SERVICE_UUID) {
            retryServicesDiscovery(peripheral)
            return
        }

        for service in peripheralServices where Peripheral.SERVICE_UUID == service.uuid {
            peripheral.discoverCharacteristics(CharacteristicIds.allCases.map{CBUUID(string: $0.rawValue)}, for: service)
        }

        print("found \(String(describing: peripheral.services?.count)) services for peripheral \(String(describing: peripheral.name))")
    }

    func peripheral(_ peripheral: CBPeripheral, didDiscoverCharacteristicsFor service: CBService, error: Error?) {
        if let error = error {
            retryCharacteristicsDiscovery(peripheral,service)
            return
        }
        guard let serviceCharacteristics = service.characteristics else {
            retryCharacteristicsDiscovery(peripheral,service)
            return
        }
        for characteristic in serviceCharacteristics {
            // store a reference to the discovered characteristic in the Central for write.
            print("Characteristic UUID:: ", characteristic.uuid.uuidString)
            if characteristic.uuid == NetworkCharNums.responseCharacteristic {
                // BLEConstants.DEFAULT_CHUNK_SIZE = peripheral.maximumWriteValueLength(for: .withoutResponse)
            }
            self.cbCharacteristics[characteristic.uuid.uuidString] = characteristic
            // subscribe to the characteristics for (2035, 2036, 2037)
            if characteristic.uuid == NetworkCharNums.semaphoreCharacteristic ||
                characteristic.uuid == NetworkCharNums.verificationStatusCharacteristic
            {
                peripheral.setNotifyValue(true, for: characteristic)
            }
        }

        NotificationCenter.default.post(name: Notification.Name(rawValue: NotificationEvent.CREATE_CONNECTION.rawValue), object: nil)
    }


    func retryServicesDiscovery(_ peripheral : CBPeripheral){
        if retryStrategy.shouldRetry() {
            let waitTime = retryStrategy.getWaitTime()
            os_log("Error while discovering services retrying again after %d time", waitTime)
            DispatchQueue.main.asyncAfter(deadline: .now() + .milliseconds(Int(waitTime))) {
                peripheral.discoverServices([Peripheral.SERVICE_UUID])
            }
        }
        else {
            os_log("Error while discovering services after retrying multiple times")
            retryStrategy.reset()
            return
        }
    }

    func retryCharacteristicsDiscovery(_ peripheral : CBPeripheral, _ service : CBService){
        if retryStrategy.shouldRetry() {
            let waitTime = retryStrategy.getWaitTime()
            os_log("Error while discovering services retrying again after %d time", waitTime)
            DispatchQueue.main.asyncAfter(deadline: .now() + .milliseconds(Int(waitTime))) {
                peripheral.discoverCharacteristics(CharacteristicIds.allCases.map{CBUUID(string: $0.rawValue)}, for: service)
            }
        }
        else {
            os_log("Error while discovering services after retrying multiple times")
            retryStrategy.reset()
            return
        }
    }

    /// called when retrieving value succeeded or value got updated
    func peripheral(_ peripheral: CBPeripheral, didUpdateValueFor characteristic: CBCharacteristic, error: Error?) {
        print("Central was able to update value for the characteristic: ", characteristic.uuid.uuidString)
        if let error = error {
            os_log("Unable to recieve updates from device: %s", error.localizedDescription)
            return
        }
        switch characteristic.uuid {
        case NetworkCharNums.semaphoreCharacteristic:
            let report = characteristic.value as Data?
            print("CBPeripheral: received transfer summary report: ", report)
            NotificationCenter.default.post(name: Notification.Name(rawValue: NotificationEvent.HANDLE_TRANSMISSION_REPORT.rawValue), object: nil, userInfo: ["report": report])

        case NetworkCharNums.verificationStatusCharacteristic:
            let verificationStatus = characteristic.value as Data?
            print("CBPeripheral: received verification status: ", verificationStatus)
            NotificationCenter.default.post(name: Notification.Name(rawValue: NotificationEvent.VERIFICATION_STATUS_RESPONSE.rawValue), object: nil, userInfo: ["status": verificationStatus])

        case NetworkCharNums.connectionStatusChangeCharacteristic:
            let connectionStatus = characteristic.value as Data?
            print("CBPeripheral: received connection status change: ", connectionStatus)
            NotificationCenter.default.post(name: Notification.Name(rawValue: NotificationEvent.CONNECTION_STATUS_CHANGE.rawValue), object: nil, userInfo: ["connectionStatus": connectionStatus])
        default:
            print("CBPeripheral: received update on \(characteristic)")
        }
    }

    /// called when peripheral successfully set a value for the characteristic
    func peripheral(_ peripheral: CBPeripheral, didWriteValueFor characteristic: CBCharacteristic, error: Error?) {
        if let error = error {
            print("unable to write to characteristic \(characteristic): %@", error.localizedDescription)
        }
        switch characteristic.uuid {
        case NetworkCharNums.identifyRequestCharacteristic:
            print("CBPeripheral: received callback for identity char write")
            NotificationCenter.default.post(name: Notification.Name(rawValue: NotificationEvent.EXCHANGE_RECEIVER_INFO.rawValue), object: nil)
        case NetworkCharNums.responseSizeCharacteristic:
            print("CBPeripheral: received callback for response size char write")
            NotificationCenter.default.post(name: Notification.Name(rawValue: NotificationEvent.RESPONSE_SIZE_WRITE_SUCCESS.rawValue), object: nil)
        case NetworkCharNums.responseCharacteristic:
            print("CBPeripheral: received callback for response char write")
            NotificationCenter.default.post(name: Notification.Name(rawValue: NotificationEvent.INIT_RESPONSE_CHUNK_TRANSFER.rawValue), object: nil)
        case NetworkCharNums.semaphoreCharacteristic:
            let report = characteristic.value as Data?
            print("CBPeripheral: received transfer summary report: ", report)
            NotificationCenter.default.post(name: Notification.Name(rawValue: NotificationEvent.HANDLE_TRANSMISSION_REPORT.rawValue), object: nil, userInfo: ["report": report])
        default:
            print("CBPeripheral: received write on \(characteristic)")
        }
    }

    func peripheral(_ peripheral: CBPeripheral, didModifyServices invalidatedServices: [CBService]) {
        print("CBPeripheral: received callback for didModifyServices")
    }
}

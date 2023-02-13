import Foundation
import CoreBluetooth

@available(iOS 13.0, *)
class Peripheral: NSObject {
    private var peripheralManager: CBPeripheralManager!
    
    override init() {
        super.init()
        peripheralManager = CBPeripheralManager(delegate: self, queue: nil, options: [CBPeripheralManagerOptionShowPowerAlertKey: true])
    }

    func setupPeripheralsAndStartAdvertising() {
        let bleService = CBMutableService(type: UUIDConstants.SERVICE_UUID, primary: true)
        bleService.characteristics = Utils.createCBMutableCharacteristics()
        peripheralManager.add(bleService)
        peripheralManager.startAdvertising([CBAdvertisementDataServiceUUIDsKey: [UUIDConstants.SERVICE_UUID, UUIDConstants.SCAN_RESPONSE_SERVICE_UUID], CBAdvertisementDataLocalNameKey: "verifier"])
    }
}

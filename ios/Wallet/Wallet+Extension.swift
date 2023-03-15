import Foundation
import CoreBluetooth

protocol TransferHandlerDelegate: AnyObject {
    func write(serviceUuid: CBUUID, charUUID: CBUUID, data: Data, withResponse: Bool)
}

extension Wallet: WalletProtocol {
    func onDisconnectStatusChange(connectionStatusId: Int){
        if connectionStatusId == 1 {
            handleDestroyConnection(isSelfDisconnect: false)
        }
    }
    func onDisconnect() {
        self.onDeviceDisconnected()
    }
    
    func onIdentifyWriteSuccess() {
        EventEmitter.sharedInstance.emitNearbyMessage(event: "exchange-receiver-info", data: Self.EXCHANGE_RECEIVER_INFO_DATA)
    }
        
    func setVeriferKeyOnSameIdentifier(payload: Data, publicData: Data, completion: (() -> Void)) {
        if isSameAdvIdentifier(advertisementPayload: payload) {
            setVerifierPublicKey(publicKeyData: publicData)
            completion()
        }
    }
    
    func createConnectionHandler() {
        createConnection?()
    }
}

extension Wallet: TransferHandlerDelegate {
    func write(serviceUuid: CBUUID, charUUID: CBUUID, data: Data, withResponse: Bool) {
        if withResponse {
            central?.writeWithResponse(serviceUuid: serviceUuid, charUUID: charUUID, data: data)
        } else {
            central?.writeWithoutResp(serviceUuid: serviceUuid, charUUID: charUUID, data: data)
        }
    }
}



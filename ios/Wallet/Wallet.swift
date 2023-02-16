import Foundation
import CoreBluetooth
import Gzip

@objc(Wallet)
@available(iOS 13.0, *)
class Wallet: NSObject {
    var central: Central?
    var secretTranslator: SecretTranslator?
    var cryptoBox: WalletCryptoBox = WalletCryptoBoxBuilder().build()
    var advIdentifier: String?
    var verifierPublicKey: Data?
    static let EXCHANGE_RECEIVER_INFO_DATA = "{\"deviceName\":\"wallet\"}"
    var notificationHandler: NotificationHandler? = NotificationHandler()
    
    override init() {
        super.init()
        lookForDestroyConnection()
    }
    
    deinit {
        // De register all observers
        notificationHandler = nil
    }
    
    @objc(getModuleName:withRejecter:)
    func getModuleName(resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) -> Void {
        resolve(["iOS Wallet"])
    }
    
    func setAdvIdentifier(identifier: String) {
        registerCallbackForEvent(event: NotificationEvent.ON_DEVICE_DISCOVERED, callback: onDeviceDiscovered)
        self.advIdentifier = identifier
    }
    
    func onDeviceDiscovered(notification: Notification) {
        let peripheral = notificationHandler?.getObjectFromNotification(notification: notification, userInfoKey: "peripheral") as? CBPeripheral
        if peripheral == nil {
            return
        }
        let advertisementData = notificationHandler?.getObjectFromNotification(notification: notification, userInfoKey: "advData") as? Data
        if advertisementData == nil {
            return
        }
        let scanResponseData = notificationHandler?.getObjectFromNotification(notification: notification, userInfoKey: "scanRespData") as? Data
        if scanResponseData == nil {
            return
        }
        
        let publicKeyData =  advertisementData!.subdata(in: advertisementData!.count-5..<advertisementData!.count) + scanResponseData!
        print("veri pub key::", publicKeyData)
        if self.isSameAdvIdentifier(advertisementPayload: advertisementData!) {
            self.setVerifierPublicKey(publicKeyData: publicKeyData)
            self.central?.connectToPeripheral(peripheral: peripheral!)
        }
    }
    
    func registerCallbackForEvent(event: NotificationEvent, callback: @escaping (_ notification: Notification) -> Void) {
        notificationHandler?.registerCallbackForEvent(event: event, callback: callback)
    }
    
    func setVerifierPublicKey(publicKeyData: Data) {
        verifierPublicKey = publicKeyData
    }
    
    func lookForDestroyConnection(){
        registerCallbackForEvent(event: NotificationEvent.DISCONNECT_STATUS_CHANGE) { notification in
            print("Handling notification for \(notification.name.rawValue)")
            if let notifyObj = notification.userInfo?["disconnectStatus"] as? Data {
                let connStatusID = Int(notifyObj[0])
                if connStatusID == 1 {
                    print("con statusid:", connStatusID)
                    self.destroyConnection()
                }
            } else {
                print("weird reason!!")
            }
        }
        registerCallbackForEvent(event: NotificationEvent.ON_PERIPHERAL_DISCONNECTED) { notification in
            print("Handling notification for \(notification.name.rawValue)")
            self.destroyConnection()
        }
    }
    
    func destroyConnection(){
        NotificationCenter.default.removeObserver(self)
        onDeviceDisconnected(isManualDisconnect: false)
    }
    
    func isSameAdvIdentifier(advertisementPayload: Data) -> Bool {
        guard let advIdentifier = advIdentifier else {
            print("Found NO ADV Identifier")
            return false
        }
        let advIdentifierData = hexStringToData(string: advIdentifier)
        if advIdentifierData == advertisementPayload {
            return true
        }
        return false
    }
    
    func hexStringToData(string: String) -> Data {
        let stringArray = Array(string)
        var data: Data = Data()
        for i in stride(from: 0, to: string.count, by: 2) {
            let pair: String = String(stringArray[i]) + String(stringArray[i+1])
            if let byteNum = UInt8(pair, radix: 16) {
                let byte = Data([byteNum])
                data.append(byte)
            } else {
                fatalError()
            }
        }
        return data
    }
    
    func sendData(data: String) {
        var dataInBytes = Data(data.utf8)
        var compressedBytes = try! dataInBytes.gzipped()
        var encryptedData = secretTranslator?.encryptToSend(data: compressedBytes)
        if (encryptedData != nil) {
            DispatchQueue.main.async {
                let transferHandler = TransferHandler(wallet: self)
                // DOUBT: why is encrypted data written twice ?
                transferHandler.initialize(initdData: encryptedData!)
                var currentMTUSize =  self.central?.connectedPeripheral?.maximumWriteValueLength(for: .withoutResponse)
                if currentMTUSize == nil || currentMTUSize! < 0 {
                   currentMTUSize = BLEConstants.DEFAULT_CHUNK_SIZE
                }
                let imsgBuilder = imessage(msgType: .INIT_RESPONSE_TRANSFER, data: encryptedData!, mtuSize: currentMTUSize)
                transferHandler.sendMessage(message: imsgBuilder)
            }
        }
    }

    func writeToIdentifyRequest() {
        print("::: write identify called ::: ")
        let publicKey = self.cryptoBox.getPublicKey()
        print("verifier pub key:::", self.verifierPublicKey)
        guard let verifierPublicKey = self.verifierPublicKey else {
            print("Write Identify - Found NO KEY")
            return
        }
        secretTranslator = (cryptoBox.buildSecretsTranslator(verifierPublicKey: self.verifierPublicKey))
        var iv = (self.secretTranslator?.initializationVector())!
        let data = iv + publicKey
        var crc = CRC.evaluate(d: data)
        central?.write(serviceUuid: Peripheral.SERVICE_UUID, charUUID: NetworkCharNums.IDENTIFY_REQUEST_CHAR_UUID, data: data +  Utils.intToBytes(crc))
        registerCallbackForEvent(event: NotificationEvent.EXCHANGE_RECEIVER_INFO) { notification in
            EventEmitter.sharedInstance.emitNearbyMessage(event: "exchange-receiver-info", data: Self.EXCHANGE_RECEIVER_INFO_DATA)
        }
    }
    
    func onDeviceDisconnected(isManualDisconnect: Bool) {
        if(!isManualDisconnect) {
            central?.connectedPeripheral = nil
            EventEmitter.sharedInstance.emitNearbyEvent(event: "onDisconnected")
        }
    }
}

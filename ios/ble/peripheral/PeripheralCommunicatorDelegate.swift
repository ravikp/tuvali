
import Foundation

protocol PeripheralCommunicatorProtocol: AnyObject {
    func onTransmissionReportRequest(data: Data)
    func onResponseSizeWriteSuccess()
    func onVerificationStatusChange(status: Int)
    func onFailedToSendTransferReportRequest()
}

protocol WalletBleCommunicatorProtocol: AnyObject {
    func onIdentifyWriteSuccess()
    func onDisconnectStatusChange(connectionStatusId: Int)
    func createConnectionHandler()
    func setVeriferKeyOnSameIdentifier(payload: Data, publicData: Data, completion: (() -> Void))
    func onDisconnect()
}

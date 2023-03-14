import Foundation

enum OpenId4vpError: Error {
    case invalidMTUSizeError(mtu: Int)
    case crcCheckFailedError(characteristic: String)
}

extension OpenId4vpError: CustomStringConvertible {
    public var description: String {
        switch self {
        case .invalidMTUSizeError(let mtu):
            return "Negotiated MTU: \(mtu) is too low."
        case .crcCheckFailedError(let characteristic):
            return "CRC check failed for \(characteristic)."
        }
    }
}

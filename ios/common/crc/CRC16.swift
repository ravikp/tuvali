import Foundation
import CrcSwift

//CRC-16/Kermit: https://reveng.sourceforge.io/crc-catalogue/16.htm#crc.cat.crc-16-kermit
//width=16 poly=0x1021 init=0x0000 refin=true refout=true xorout=0x0000 check=0x2189 residue=0x0000 name="CRC-16/KERMIT"
//TODO: Need to identify what is check, and residue in the Kermit algorithm

class CRCValidator {
    static func calculate(d: Data) -> UInt16 {
        let crc = CrcSwift.computeCrc16(
            d,
            initialCrc: 0x0000,
            polynom: 0x1021,
            xor: 0x0000,
            refIn: true,
            refOut: true
        )
        return crc
    }

    static func verify(data: Data, characteristic: String) -> Bool {
        let crcValueReceived = Util.networkOrderedByteArrayToInt(num: Data(data.suffix(2)))
        let crcValueCalculated = calculate(d: data.dropLast(2))

        if crcValueCalculated != crcValueReceived {
            os_log(.error, "CRC check failed. Received CRC: %{public}d, Calculated CRC: %{public}d", crcValueReceived, crcValueCalculated)
            ErrorHandler.sharedInstance.handleException(type: .walletException, error: .crcCheckFailedError(characteristic: characteristic))
            return false
        }
        return true
    }
}


import Foundation
import CoreBluetooth


class DisconnectCharacteristic {
  var data: Data
  static let uuid = CharacteristicIds.DISCONNECT_CHAR_UUID
  var status:  Int

    init(data: Data){
        self.data =  data
        status = Int(data[0])
         CRCValidator.verify(data: data, characteristic: DisconnectCharacteristic.uuid.rawValue)
    }
}


import Foundation
import CoreBluetooth


class VerificationStatusCharacteristic {
  var data: Data
  static let uuid = CharacteristicIds.VERIFICATION_STATUS_CHAR_UUID
  var status : Int

  init(data: Data){
      self.data =  data
      status = Int(data[0])
      CRCValidator.verify(data: data, characteristic: "VerificationStatusCharacteristic")
  }
}

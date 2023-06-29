
import Foundation
import CoreBluetooth


class TransferReportResponseCharacteristic {
  var data: Data
  static let uuid = CharacteristicIds.VERIFICATION_STATUS_CHAR_UUID
  var status : Int

  init(data: Data){
      self.data =  data
      status = Int(data[0])
       CRCValidator.verify(data: data, characteristic: TransferReportResponseCharacteristic.uuid.rawValue)
  }
}

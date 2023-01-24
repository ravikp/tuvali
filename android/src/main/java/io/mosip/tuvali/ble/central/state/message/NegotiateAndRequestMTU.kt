package io.mosip.tuvali.ble.central.state.message



class NegotiateAndRequestMTU(val mtuSize: Int): IMessage(CentralStates.REQUEST_AND_NEGOTIATE_MTU) {

}

package io.mosip.tuvali.wallet

import io.mosip.tuvali.common.events.Event
import io.opentelemetry.api.OpenTelemetry

interface IWallet {
  fun startConnection(uri: String, otel: OpenTelemetry)
  fun sendData(payload: String)
  fun disconnect()
  fun subscribe(listener: (Event) -> Unit)
  fun unSubscribe()
}

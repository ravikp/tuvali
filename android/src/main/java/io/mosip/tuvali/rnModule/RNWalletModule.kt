package io.mosip.tuvali.rnModule

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import io.mosip.tuvali.opentelemetry.Configuration
import io.mosip.tuvali.wallet.IWallet
import io.opentelemetry.api.OpenTelemetry

class RNWalletModule(
  private val eventEmitter: RNEventEmitter,
  private val wallet: IWallet,

  reactContext: ReactApplicationContext
) : ReactContextBaseJavaModule(reactContext) {

  var otel: OpenTelemetry = Configuration.initOpenTelemetry()

  init {
    val tracer = otel.getTracer("Wallet")
    wallet.subscribe {
      eventEmitter.emitEvent(RNEventMapper.toMap(it))
    }
  }

  @ReactMethod(isBlockingSynchronousMethod = true)
  fun startConnection(uri: String) {

    wallet.startConnection(uri,otel)
  }

  @ReactMethod
  fun sendData(payload: String) {
    wallet.sendData(payload)
  }

  @ReactMethod(isBlockingSynchronousMethod = true)
  fun disconnect() {
    wallet.disconnect()
  }

  override fun getName(): String {
    return NAME
  }

  companion object {
    const val NAME = "WalletModule"
  }
}

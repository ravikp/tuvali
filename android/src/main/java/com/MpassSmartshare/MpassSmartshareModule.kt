package com.mpasssmartshare

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.RCTNativeAppEventEmitter
import com.facebook.react.uimanager.ViewManager

class MpassSmartshareModule(reactContext: ReactApplicationContext):
  ReactContextBaseJavaModule(reactContext) {

  override fun getName(): String {
    return NAME
  }
  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @ReactMethod
  fun getConnectionParameters(): String {
    return "BLE";
  }

  @ReactMethod
  fun setConnectionParameters(p: String) {
  }

  fun getConnectionParametersDebug(): String {
    return "BLE-debug"
  }

  fun createConnection(mode: ConnectionMode, callback: Callback) {
    System.out.print("create a connection");
    callback()
  }

  fun destroyConnection() {
    System.out.print("connection destroyed");
  }

  fun send(message: String, callback: Callback) {
    System.out.println("sent")
  }

  fun handleNearbyEvents(callback: Callback) : RCTNativeAppEventEmitter? {
    return null;
  }
  fun handleLogEvents(callback: Callback) : RCTNativeAppEventEmitter? {
    return null;
  }
  companion object {
    const val NAME = "MpassSmartshare"
  }
}

enum class ConnectionMode {
  Online,
  Offline
}

class MpassSmartsharePackage: ReactPackage {
  override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
    return listOf(MpassSmartshareModule(reactContext))
  }

  override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
    return emptyList()
  }
}

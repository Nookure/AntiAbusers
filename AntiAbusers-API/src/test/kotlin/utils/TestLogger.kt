package utils

import es.angelillo15.core.Logger

class TestLogger : Logger() {
  override fun debug(message: String?) {
    println("[DEBUG] $message")
  }

  override fun error(message: String?) {
    println("[ERROR] $message")
  }

  override fun info(message: String?) {
    println("[INFO] $message")
  }

  override fun warn(message: String?) {
    println("[WARN] $message")
  }
}
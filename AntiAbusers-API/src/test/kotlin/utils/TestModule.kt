package utils

import com.google.inject.AbstractModule
import es.angelillo15.core.Logger

class TestModule : AbstractModule() {
  override fun configure() {
    bind(Logger::class.java).to(TestLogger::class.java).asEagerSingleton()
  }
}
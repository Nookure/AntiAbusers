package es.angelillo15.antiabusers.inject

import com.google.inject.AbstractModule
import es.angelillo15.antiabusers.AntiAbusers
import es.angelillo15.antiabusers.utils.PluginLogger
import es.angelillo15.core.Logger

class PluginModule : AbstractModule() {
  override fun configure() {
    bind(AntiAbusers::class.java).toInstance(AntiAbusers.instance)
    bind(Logger::class.java).to(PluginLogger::class.java).asEagerSingleton()
  }
}
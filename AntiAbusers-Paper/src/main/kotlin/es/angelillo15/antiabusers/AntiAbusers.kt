package es.angelillo15.antiabusers

import com.google.inject.Guice
import com.google.inject.Injector
import es.angelillo15.antiabusers.inject.PluginModule
import es.angelillo15.antiabusers.utils.PluginLogger
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.InputStream

open class AntiAbusers : JavaPlugin(), AntiAbusersInstance {
  lateinit var pPluginLogger: PluginLogger
    private set
  lateinit var pluginInjector: Injector
    private set
  private var debug = false

  companion object {
    lateinit var instance: AntiAbusers
      private set
  }

  override fun onLoad() {
    instance = this
    pluginInjector = Guice.createInjector(PluginModule())
    pPluginLogger = pluginInjector.getInstance(PluginLogger::class.java)
  }

  override fun getDebug(): Boolean {
    return debug
  }

  override fun getInjector(): Injector {
    return pluginInjector
  }

  override fun getPluginDataFolder(): File {
    return dataFolder
  }

  override fun getPluginResource(s: String): InputStream {
    return getResource(s)!!
  }

  override fun setDebug(debug: Boolean) {
    this.debug = debug
  }
}
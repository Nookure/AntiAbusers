package es.angelillo15.antiabusers.config

import com.google.inject.Singleton
import es.angelillo15.antiabusers.AntiAbusersInstance
import es.angelillo15.core.config.Config

@Singleton
class ConfigManager : Config() {
  lateinit var config: es.angelillo15.core.libs.config.manager.ConfigManager
    private set
  lateinit var messages: es.angelillo15.core.libs.config.manager.ConfigManager
    private set
  private var instance = plugin as AntiAbusersInstance

  companion object {
    lateinit var configManager: ConfigManager
      private set
  }

  init {
    configManager = this
  }

  fun load() {
    loadConfig()
    loadMessages()
  }

  private fun loadConfig() {
    config = loadFile("config.yml", "config.yml")
    plugin.setDebug(config.config.getBoolean("Config.debug"))
    instance.getPluginLogger().debug("Config file loaded!")
  }

  private fun loadMessages() {
    instance.getPluginLogger().debug("Creating messages file...")
    loadFile("lang/en.yml", "lang/en.yml")
    loadFile("lang/es.yml", "lang/es.yml")

    val lang = config.config.getString("Config.language")
    instance.getPluginLogger().debug("Loading $lang.yml file...")

    messages = loadFile("lang/$lang.yml", "lang/$lang.yml")
    instance.getPluginLogger().debug("Messages file loaded!")
  }
}
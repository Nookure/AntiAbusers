package es.angelillo15.antiabusers.config

import com.google.inject.Inject
import com.google.inject.Singleton
import es.angelillo15.antiabusers.AntiAbusersInstance
import es.angelillo15.core.Logger
import es.angelillo15.core.config.Config

@Singleton
@Suppress("ConvertSecondaryConstructorToPrimary")
class ConfigManager : Config {
  var config: es.angelillo15.core.libs.config.manager.ConfigManager? = null
    private set
  var messages: es.angelillo15.core.libs.config.manager.ConfigManager? = null
    private set
  private val logger: Logger

  companion object {
    var configManager: ConfigManager? = null
      private set
  }

  @Inject
  constructor(logger: Logger, instance: AntiAbusersInstance) {
    configManager = this
    this.logger = logger
    this.plugin = instance
  }

  fun load() {
    loadConfig()
    loadMessages()
  }

  private fun loadConfig() {
    config = loadFile("config.yml", "config.yml")
    plugin.setDebug(config!!.config.getBoolean("Config.debug"))
    logger.debug("Config file loaded!")
  }

  private fun loadMessages() {
    logger.debug("Creating messages file...")
    loadFile("lang/en.yml", "lang/en.yml")
    loadFile("lang/es.yml", "lang/es.yml")

    val lang = config!!.config.getString("Config.language")
    logger.debug("Loading $lang.yml file...")

    messages = loadFile("lang/$lang.yml", "lang/$lang.yml")
    logger.debug("Messages file loaded!")
  }
}
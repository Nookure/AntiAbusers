package es.angelillo15.antiabusers

class AntiAbusersLoader : AntiAbusers() {
  override fun onEnable() {
    super.onEnable()
    pPluginLogger.info("Loading AntiAbusers...")
    loadConfig()
    loadHandlers()
    loadListeners()
    registerCommands()
  }

  override fun onDisable() {
    pPluginLogger.info("Disabling AntiAbusers...")
    unregisterListener()
    unregisterCommands()
  }
}
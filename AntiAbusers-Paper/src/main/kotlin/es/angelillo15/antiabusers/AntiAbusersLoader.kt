package es.angelillo15.antiabusers

class AntiAbusersLoader : AntiAbusers() {
  override fun onEnable() {
    super.onEnable()
    pPluginLogger.info("Loading AntiAbusers...")
    loadConfig()
    loadHandlers()
    loadListeners()
  }

  override fun onDisable() {
    pPluginLogger.info("Disabling AntiAbusers...")
    unregisterListener()
  }
}
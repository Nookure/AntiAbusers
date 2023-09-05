package es.angelillo15.antiabusers

import es.angelillo15.core.libs.obliviate.inventory.InventoryAPI

class AntiAbusersLoader : AntiAbusers() {
  override fun onEnable() {
    super.onEnable()
    pPluginLogger.info("Loading AntiAbusers...")
    loadConfig()
    loadHandlers()
    loadListeners()
    registerCommands()
    loadRegions()
    InventoryAPI(this).init()
  }

  override fun onDisable() {
    pPluginLogger.info("Disabling AntiAbusers...")
    unregisterListener()
    unregisterCommands()
  }
}
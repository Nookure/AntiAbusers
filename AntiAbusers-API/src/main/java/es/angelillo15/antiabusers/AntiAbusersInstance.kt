package es.angelillo15.antiabusers

import es.angelillo15.core.Logger
import es.angelillo15.core.NookInstance
import org.bukkit.entity.Player

interface AntiAbusersInstance : NookInstance {
  fun createPlayer(player: Player?): AntiAbuserPlayer?

  fun loadHandlers()

  fun loadListeners()

  fun reload()

  fun getPluginLogger() : Logger

  fun loadConfig()
}
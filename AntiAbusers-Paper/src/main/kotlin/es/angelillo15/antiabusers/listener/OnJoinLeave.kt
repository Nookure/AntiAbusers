package es.angelillo15.antiabusers.listener

import com.google.inject.Inject
import es.angelillo15.antiabusers.AntiAbusersInstance
import es.angelillo15.antiabusers.manager.AntiAbusersPlayers
import es.angelillo15.core.Logger
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class OnJoinLeave : Listener {
  @Inject
  private lateinit var manager: AntiAbusersPlayers
  @Inject
  private lateinit var plugin: AntiAbusersInstance
  @Inject
  private lateinit var logger: Logger

  /**
   * @param event the event that is called when a player joins the server
   * @see PlayerJoinEvent
   */
  @EventHandler
  fun onJoin(event: PlayerJoinEvent) {
    val player = plugin.createPlayer(event.player)
    if (player == null) {
      logger.error("Player cannot be null!")
      return
    }

    logger.debug("Created player ${player.getName()}")
    manager.addPlayer(player)
  }

  @EventHandler
  fun onLeave(event: PlayerQuitEvent) {
    manager.removePlayer(event.player.name)

    logger.debug("Removed player ${event.player.name}")
  }
}
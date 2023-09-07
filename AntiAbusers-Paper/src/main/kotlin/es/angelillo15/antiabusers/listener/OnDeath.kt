package es.angelillo15.antiabusers.listener

import com.google.inject.Inject
import es.angelillo15.antiabusers.manager.AntiAbusersPlayers
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class OnDeath : Listener {
  @Inject
  private lateinit var antiAbusersPlayers: AntiAbusersPlayers

  @EventHandler
  fun onPlayerDeath(event: PlayerDeathEvent) {
    val player = antiAbusersPlayers.getPlayer(event.entity.name) ?: return
    player.clearPVPs()
  }
}
package es.angelillo15.antiabusers.listener

import com.google.inject.Inject
import es.angelillo15.antiabusers.manager.AntiAbusersPlayers
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent

class OnItemPickup : Listener {
  @Inject
  private lateinit var antiAbusersPlayers: AntiAbusersPlayers

  @EventHandler
  fun onItemPickup(event: EntityPickupItemEvent) {
    if (event.entity !is Player) return

    val player = event.entity as Player
    val antiAbuserPlayer = antiAbusersPlayers.getPlayer(player.name) ?: return

    antiAbuserPlayer.check(true)
  }
}
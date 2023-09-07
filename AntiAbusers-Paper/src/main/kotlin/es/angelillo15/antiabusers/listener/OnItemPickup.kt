package es.angelillo15.antiabusers.listener

import com.google.inject.Inject
import es.angelillo15.antiabusers.AntiAbusers
import es.angelillo15.antiabusers.manager.AntiAbusersPlayers
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import java.util.function.Consumer

class OnItemPickup : Listener {
  @Inject
  private lateinit var antiAbusersPlayers: AntiAbusersPlayers
  @Inject
  private lateinit var plugin: AntiAbusers

  @EventHandler
  fun onItemPickup(event: EntityPickupItemEvent) {
    if (event.entity !is Player) return

    val player = event.entity as Player
    val antiAbuserPlayer = antiAbusersPlayers.getPlayer(player.name) ?: return

    Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, Consumer {
      antiAbuserPlayer.check(true)
    }, 1)  }
}
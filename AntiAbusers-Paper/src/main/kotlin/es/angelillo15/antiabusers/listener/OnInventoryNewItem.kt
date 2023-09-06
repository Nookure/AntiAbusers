package es.angelillo15.antiabusers.listener

import com.google.inject.Inject
import es.angelillo15.antiabusers.AntiAbusers
import es.angelillo15.antiabusers.manager.AntiAbusersPlayers
import es.angelillo15.antiabusers.manager.RegionManager
import es.angelillo15.antiabusers.thread.executeAsync
import es.angelillo15.core.Logger
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.scheduler.BukkitRunnable
import java.util.function.Consumer

class OnInventoryNewItem : Listener {
  @Inject
  private lateinit var logger: Logger
  @Inject
  private lateinit var antiAbusersPlayers: AntiAbusersPlayers
  @Inject
  private lateinit var plugin: AntiAbusers

  @EventHandler
  fun onInventoryNewItem(event: InventoryClickEvent) {
    if (event.clickedInventory == null || event.clickedInventory!!.type != InventoryType.PLAYER) {
      return
    }

    if (event.click == ClickType.CREATIVE) {
      return
    }

    if (event.whoClicked.gameMode == GameMode.CREATIVE) {
      return
    }

    if (event.action != InventoryAction.PLACE_ONE &&
      event.action != InventoryAction.PLACE_SOME &&
      event.action != InventoryAction.PLACE_ALL
    ) {
      return
    }
    val player = event.whoClicked as Player
    val abuserPlayer = antiAbusersPlayers.getPlayer(player.name) ?: return

    Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, Consumer {
      abuserPlayer.check(true)
    }, 1)
  }
}
package es.angelillo15.antiabusers.listener

import com.google.inject.Inject
import es.angelillo15.core.Logger
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType

class OnInventoryNewItem : Listener {
  @Inject
  private lateinit var logger: Logger

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
  }
}
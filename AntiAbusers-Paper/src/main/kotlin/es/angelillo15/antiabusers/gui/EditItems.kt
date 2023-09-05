package es.angelillo15.antiabusers.gui

import com.sk89q.worldguard.protection.regions.ProtectedRegion
import es.angelillo15.antiabusers.utils.raw
import es.angelillo15.core.libs.obliviate.inventory.pagination.PaginationManager
import es.angelillo15.core.utils.TextUtils
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryOpenEvent

class EditItems(val player: Player, val region: ProtectedRegion) : PaginatedGui(
  player,
  "add-items-gui",
  TextUtils.simpleColorize(raw("Gui.editItems.title")),
  3
) {
  val paginated = PaginationManager(this)
  override fun onOpen(event: InventoryOpenEvent?) {
    super.onOpen(event)
  }
}
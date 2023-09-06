package es.angelillo15.antiabusers.gui

import com.sk89q.worldguard.protection.regions.ProtectedRegion
import es.angelillo15.antiabusers.AntiAbusers
import es.angelillo15.antiabusers.region.Region
import es.angelillo15.antiabusers.utils.cmp
import es.angelillo15.antiabusers.utils.raw
import es.angelillo15.core.libs.obliviate.inventory.Icon
import es.angelillo15.core.libs.obliviate.inventory.pagination.PaginationManager
import es.angelillo15.core.utils.TextUtils
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.ItemStack

class EditItems(val player: Player, val region: ProtectedRegion) : PaginatedGui(
  player,
  "edit-items-gui",
  TextUtils.simpleColorize(raw("Gui.editItems.title").replace("{region}", region.id)),
  6
) {
  override fun onOpen(event: InventoryOpenEvent?) {
    load()

    val back = ItemStack(Material.valueOf(raw("Gui.editItems.back.material")))
    val backMeta = back.itemMeta
    backMeta.displayName(cmp("Gui.editItems.back.name"))
    back.itemMeta = backMeta

    addItem(Icon(back).onClick {
      RegionSettings(player, region).open()
    }, 45)

    loadItems()
  }

  private fun loadItems() {
    val regionData = Region.load(region.id, AntiAbusers.instance)

    regionData.regionData.blockedItems.forEach { item ->
      paginationManager.addItem(Icon(item).onClick {
        DeleteItem(player, regionData, region, item).open()
      })
    }

    paginationManager.update()
  }
}
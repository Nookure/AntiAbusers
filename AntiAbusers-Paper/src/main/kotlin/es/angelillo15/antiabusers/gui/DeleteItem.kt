package es.angelillo15.antiabusers.gui

import com.sk89q.worldguard.protection.regions.ProtectedRegion
import es.angelillo15.antiabusers.region.Region
import es.angelillo15.antiabusers.utils.cmp
import es.angelillo15.antiabusers.utils.raw
import es.angelillo15.core.libs.obliviate.inventory.Gui
import es.angelillo15.core.libs.obliviate.inventory.Icon
import es.angelillo15.core.utils.TextUtils
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.ItemStack

class DeleteItem(val player: Player, val region: Region, val protectedRegion: ProtectedRegion, val item: ItemStack) : Gui(
  player,
  "add-items-gui",
  TextUtils.simpleColorize(raw("Gui.deleteItem.title")),
  3
) {
  override fun onOpen(event: InventoryOpenEvent?) {
    fillGui(Material.GRAY_STAINED_GLASS_PANE)

    val cancel = ItemStack(Material.valueOf(raw("Gui.deleteItem.cancel.material")))
    val cancelMeta = cancel.itemMeta
    cancelMeta.displayName(cmp("Gui.deleteItem.cancel.name"))
    cancel.itemMeta = cancelMeta

    val confirm = ItemStack(Material.valueOf(raw("Gui.deleteItem.confirm.material")))
    val confirmMeta = confirm.itemMeta
    confirmMeta.displayName(cmp("Gui.deleteItem.confirm.name"))
    confirm.itemMeta = confirmMeta

    addItem(Icon(cancel).onClick {
      EditItems(player, protectedRegion).open()
    }, 21)

    addItem(Icon(confirm).onClick {
      region.regionData.blockedItems.remove(item)
      region.write()

      EditItems(player, protectedRegion).open()
    }, 23)

    addItem(Icon(item), 13)
  }
}
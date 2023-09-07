package es.angelillo15.antiabusers.gui

import com.sk89q.worldguard.protection.regions.ProtectedRegion
import es.angelillo15.antiabusers.AntiAbusers
import es.angelillo15.antiabusers.region.Region
import es.angelillo15.antiabusers.utils.cmp
import es.angelillo15.antiabusers.utils.raw
import es.angelillo15.core.libs.obliviate.inventory.Gui
import es.angelillo15.core.libs.obliviate.inventory.Icon
import es.angelillo15.core.libs.obliviate.inventory.advancedslot.AdvancedSlotManager
import es.angelillo15.core.utils.TextUtils
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.ItemStack

class AddItems(val player: Player, val region: ProtectedRegion) : Gui(
  player,
  "add-items-gui",
  TextUtils.simpleColorize(raw("Gui.addItemsToRegion.title")),
  4
) {
  private val slotManager = AdvancedSlotManager(this)
  private val itemsToAdd = ArrayList<ItemStack>()
  override fun onOpen(event: InventoryOpenEvent?) {
    fillGui(ItemStack(Material.GRAY_STAINED_GLASS_PANE))

    var slot = slotManager.addAdvancedIcon(13, Icon(Material.AIR))

    val confirm = ItemStack(Material.valueOf(raw("Gui.addItemsToRegion.confirm.material")))
    val confirmMeta = confirm.itemMeta
    confirmMeta.displayName(cmp("Gui.addItemsToRegion.confirm.name"))
    confirm.itemMeta = confirmMeta

    val cancel = ItemStack(Material.valueOf(raw("Gui.addItemsToRegion.cancel.material")))
    val cancelMeta = cancel.itemMeta
    cancelMeta.displayName(cmp("Gui.addItemsToRegion.cancel.name"))
    cancel.itemMeta = cancelMeta

    val add = ItemStack(Material.valueOf(raw("Gui.addItemsToRegion.add.material")))
    val addMeta = add.itemMeta
    addMeta.displayName(cmp("Gui.addItemsToRegion.add.name"))
    add.itemMeta = addMeta

    addItem(Icon(cancel).onClick {
      RegionSettings(player, region).open()
    }, 30)

    addItem(Icon(confirm).onClick {
      val region = Region.load(region.id, player.world.name, AntiAbusers.instance)
      region.regionData.blockedItems.addAll(itemsToAdd)

      region.write()

      player.closeInventory()
    }, 32)

    addItem(Icon(add).onClick {
      if (slot.puttedItem == null) return@onClick
      if (slot.puttedItem.type == Material.AIR) return@onClick

      itemsToAdd.add(slot.puttedItem)
      player.inventory.addItem(slot.puttedItem)

      slot = slotManager.addAdvancedIcon(13, Icon(Material.AIR))
    }, 31)

    slotManager
  }
}
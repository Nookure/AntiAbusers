package es.angelillo15.antiabusers.gui

import com.google.inject.Guice
import com.sk89q.worldguard.protection.regions.ProtectedRegion
import es.angelillo15.antiabusers.inject.PlayerModule
import es.angelillo15.antiabusers.utils.cmp
import es.angelillo15.antiabusers.utils.raw
import es.angelillo15.core.libs.obliviate.inventory.Gui
import es.angelillo15.core.libs.obliviate.inventory.Icon
import es.angelillo15.core.utils.TextUtils
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.ItemStack

class RegionSettings(val player: Player, val region: ProtectedRegion) : Gui(
  player,
  "add-items-gui",
  TextUtils.simpleColorize(raw("Gui.settingsRegion.title").replace("{region}", region.id)),
  3
) {
  override fun onOpen(event: InventoryOpenEvent?) {
    fillGui(Icon(Material.GRAY_STAINED_GLASS_PANE))

    val edit = ItemStack(Material.valueOf(raw("Gui.settingsRegion.edit.material")))
    val editMeta = edit.itemMeta
    editMeta.displayName(cmp("Gui.settingsRegion.edit.name"))
    edit.itemMeta = editMeta

    val addItems = ItemStack(Material.valueOf(raw("Gui.settingsRegion.addItems.material")))
    val addItemsMeta = addItems.itemMeta
    addItemsMeta.displayName(cmp("Gui.settingsRegion.addItems.name"))
    addItems.itemMeta = addItemsMeta

    val goBack = ItemStack(Material.valueOf(raw("Gui.settingsRegion.back.material")))
    val goBackMeta = goBack.itemMeta
    goBackMeta.displayName(cmp("Gui.settingsRegion.back.name"))
    goBack.itemMeta = goBackMeta

    addItem(Icon(edit).onClick {
    }, 12)

    addItem(Icon(addItems).onClick {
      AddItems(player, region).open()
    }, 14)

    addItem(Icon(goBack).onClick {
      Guice.createInjector(PlayerModule(player)).getInstance(SelectRegion::class.java).open()
    }, 18)
  }
}
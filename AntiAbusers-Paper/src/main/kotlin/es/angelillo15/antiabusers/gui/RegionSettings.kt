package es.angelillo15.antiabusers.gui

import com.sk89q.worldguard.protection.regions.ProtectedRegion
import es.angelillo15.antiabusers.AntiAbusers
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

class RegionSettings(val player: Player, val region: ProtectedRegion) : Gui(
  player,
  "add-items-gui",
  TextUtils.simpleColorize(raw("Gui.settingsRegion.title").replace("{region}", region.id)),
  3
) {
  override fun onOpen(event: InventoryOpenEvent?) {
    fillGui(Icon(Material.GRAY_STAINED_GLASS_PANE))

    val edit = ItemStack(Material.valueOf(raw("Gui.settingsRegion.listItems.material")))
    val editMeta = edit.itemMeta
    editMeta.displayName(cmp("Gui.settingsRegion.listItems.name"))
    edit.itemMeta = editMeta

    val addItems = ItemStack(Material.valueOf(raw("Gui.settingsRegion.addItems.material")))
    val addItemsMeta = addItems.itemMeta
    addItemsMeta.displayName(cmp("Gui.settingsRegion.addItems.name"))
    addItems.itemMeta = addItemsMeta

    val goBack = ItemStack(Material.valueOf(raw("Gui.settingsRegion.back.material")))
    val goBackMeta = goBack.itemMeta
    goBackMeta.displayName(cmp("Gui.settingsRegion.back.name"))
    goBack.itemMeta = goBackMeta

    val importFromRegions = ItemStack(Material.valueOf(raw("Gui.settingsRegion.importFromRegions.material")))
    val importFromRegionsMeta = importFromRegions.itemMeta
    importFromRegionsMeta.displayName(cmp("Gui.settingsRegion.importFromRegions.name"))
    importFromRegions.itemMeta = importFromRegionsMeta

    addItem(Icon(edit).onClick {
      EditItems(player, region).open()
    }, 14)

    addItem(Icon(addItems).onClick {
      AddItems(player, region).open()
    }, 12)

    addItem(Icon(goBack).onClick {
      SelectRegion(player, AntiAbusers.instance.pPluginLogger).open()
    }, 18)

    addItem(Icon(importFromRegions).onClick {
      SelectRegion(player, AntiAbusers.instance.pPluginLogger).onClick {
        val from = Region.load(it.id, player.world.name, AntiAbusers.instance)
        val to = Region.load(region.id, player.world.name, AntiAbusers.instance)

        from.regionData.blockedItems.forEach { item ->
          to.regionData.blockedItems.add(item)
        }

        to.write()
        RegionSettings(player, region).open()
      }.onOpen {
        it.addItem(Icon(goBack).onClick {
          RegionSettings(player, region).open()
        }, 45)
      }.open()
    }, 13)
  }
}
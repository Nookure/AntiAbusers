package es.angelillo15.antiabusers.gui

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldguard.WorldGuard
import es.angelillo15.antiabusers.utils.raw
import es.angelillo15.antiabusers.utils.tl
import es.angelillo15.core.Logger
import es.angelillo15.core.libs.obliviate.inventory.Icon
import es.angelillo15.core.utils.TextUtils
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.ItemStack

class SelectRegion (val player: Player, val logger: Logger) : PaginatedGui(
  player,
  "select-region-gui",
  TextUtils.simpleColorize(raw("Gui.selectRegionTitle")),
  6
) {
  override fun onOpen(event: InventoryOpenEvent) {
    addRegions(player)
    load()
  }

  fun addRegions(player: Player) {
    fillGui(ItemStack(Material.GRAY_STAINED_GLASS_PANE))

    val regionManager = WorldGuard.getInstance().platform.regionContainer.get(BukkitAdapter.adapt(player.world))

    if (regionManager == null || regionManager.regions.isEmpty()) {
      player.sendMessage(tl("General.noRegionsFound"))
      return
    }

    val items = Material.entries.toTypedArray()
    var i = 10

    regionManager.regions.forEach { region ->
      val itemStack = ItemStack(items[i++])
      val itemMeta = itemStack.itemMeta
      itemMeta.displayName(Component.text(region.value.id).asComponent())
      itemStack.itemMeta = itemMeta

      paginationManager.addItem(Icon(itemStack).onClick {
        RegionSettings(player, region.value).open()
      })

      logger.debug("Added region ${region.value.id} to the gui with material ${itemStack.type.name}")
    }
  }
}
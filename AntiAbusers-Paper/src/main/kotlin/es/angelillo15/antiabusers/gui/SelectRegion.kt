package es.angelillo15.antiabusers.gui

import com.google.inject.Inject
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldguard.WorldGuard
import es.angelillo15.antiabusers.config.tl
import es.angelillo15.core.Logger
import es.angelillo15.core.libs.obliviate.inventory.Gui
import es.angelillo15.core.libs.obliviate.inventory.Icon
import es.angelillo15.core.libs.obliviate.inventory.pagination.PaginationManager
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.ItemStack

class SelectRegion @Inject constructor(val player: Player) : Gui(player,"select-region-gui","pitos", 6) {
  @Inject
  private lateinit var logger: Logger

  private val paginationManager = PaginationManager(this)
  override fun onOpen(event: InventoryOpenEvent) {
    paginationManager.registerPageSlotsBetween(10, 16);
    paginationManager.registerPageSlotsBetween(19, 25);
    paginationManager.registerPageSlotsBetween(28, 34);
    paginationManager.registerPageSlotsBetween(37, 43);

    addRegions(player)

    paginationManager.update()
  }

  fun addRegions(player: Player) {
    val regionManager = WorldGuard.getInstance().platform.regionContainer.get(BukkitAdapter.adapt(player.world))

    if (regionManager == null || regionManager.regions.isEmpty()) {
      player.sendMessage(tl("General.noRegionsFound"))
      return
    }

    val items = Material.entries.toTypedArray()
    var i = 1

    regionManager.regions.forEach { region ->
      val itemStack = ItemStack(items[i++])
      val itemMeta = itemStack.itemMeta
      itemMeta.displayName(Component.text(region.value.id).asComponent())
      itemStack.itemMeta = itemMeta

      paginationManager.addItem(Icon(itemStack))

      logger.debug("Added region ${region.value.id} to the gui with material ${itemStack.type.name}")
    }

    items.forEach {
      val itemStack = ItemStack(it)

      paginationManager.addItem(Icon(itemStack))
    }
  }

  fun addNavigationButtons() {

  }
}
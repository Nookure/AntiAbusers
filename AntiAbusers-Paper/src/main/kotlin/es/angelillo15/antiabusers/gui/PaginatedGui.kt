package es.angelillo15.antiabusers.gui

import es.angelillo15.antiabusers.utils.cmp
import es.angelillo15.antiabusers.utils.raw
import es.angelillo15.core.libs.obliviate.inventory.Gui
import es.angelillo15.core.libs.obliviate.inventory.Icon
import es.angelillo15.core.libs.obliviate.inventory.pagination.PaginationManager
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.ItemStack

abstract class PaginatedGui(player: Player, id: String, title: String, rows: Int) : Gui(player, id, title, rows) {
  val paginationManager = PaginationManager(this)
  fun load() {
    fillGui(ItemStack(Material.GRAY_STAINED_GLASS_PANE))
    val backButton = ItemStack(Material.valueOf(raw("Gui.backItem.material")))
    val backButtonMeta = backButton.itemMeta
    backButtonMeta.displayName(cmp("Gui.backItem.name"))
    backButton.itemMeta = backButtonMeta

    val forwardButton = ItemStack(Material.valueOf(raw("Gui.forwardItem.material")))
    val forwardButtonMeta = forwardButton.itemMeta
    forwardButtonMeta.displayName(cmp("Gui.forwardItem.name"))
    forwardButton.itemMeta = forwardButtonMeta

    addItem(Icon(backButton).onClick {
      paginationManager.goPreviousPage()
      paginationManager.update()
    }, 48)

    addItem(Icon(forwardButton).onClick {
      paginationManager.goNextPage()
      paginationManager.update()
    }, 50)

    paginationManager.registerPageSlotsBetween(10, 16);
    paginationManager.registerPageSlotsBetween(19, 25);
    paginationManager.registerPageSlotsBetween(28, 34);
    paginationManager.registerPageSlotsBetween(37, 43);

    paginationManager.update()
  }
}
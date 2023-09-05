package es.angelillo15.antiabusers

import com.google.inject.Inject
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldguard.protection.regions.ProtectedRegion
import com.sk89q.worldguard.protection.regions.RegionContainer
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class PaperAntiAbuserPlayer @Inject constructor(private val player: Player) : AntiAbuserPlayer {
  @Inject
  private lateinit var regionContainer: RegionContainer

  private var abuser = false
  private var checking = false
  private var attack = false
  private var illegalItems = ArrayList<ItemStack>()
  private var currentRegions = ArrayList<String>()

  init {
    reloadRegionList()
  }

  override fun getName(): String {
    return player.name
  }

  override fun isAbuser(): Boolean {
    return abuser
  }

  override fun setAbuser(abuser: Boolean) {
    this.abuser = abuser
  }

  override fun getCurrentRegions(): ArrayList<String> {
    return currentRegions
  }

  override fun isChecking(): Boolean {
    return checking
  }

  override fun check() {

  }

  override fun canAttack(player: AntiAbuserPlayer): Boolean {
    return attack
  }

  override fun getIllegalItems(): ArrayList<ItemStack> {
    return illegalItems
  }

  override fun sendMessage(component: Component) {
    player.sendMessage(component)
  }

  fun reloadRegionList() {
    currentRegions.clear()

    regionContainer.createQuery().getApplicableRegions(BukkitAdapter.adapt(player.location)).forEach { region ->
      currentRegions.add(region.id)
    }
  }

  override fun reloadRegionList(exited: Set<ProtectedRegion>, entered: Set<ProtectedRegion>) {
    exited.forEach { region ->
      currentRegions.remove(region.id)
    }

    entered.forEach { region ->
      currentRegions.add(region.id)
    }
  }
}
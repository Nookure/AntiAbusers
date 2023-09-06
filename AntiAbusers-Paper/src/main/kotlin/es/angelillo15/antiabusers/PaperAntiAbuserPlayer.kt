package es.angelillo15.antiabusers

import com.google.inject.Inject
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldguard.protection.regions.ProtectedRegion
import com.sk89q.worldguard.protection.regions.RegionContainer
import es.angelillo15.antiabusers.enum.AttackResult
import es.angelillo15.antiabusers.factory.PlayerFactory
import es.angelillo15.antiabusers.manager.RegionManager
import es.angelillo15.antiabusers.utils.ItemUtils
import es.angelillo15.core.Logger
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.concurrent.ConcurrentHashMap

class PaperAntiAbuserPlayer @Inject constructor(
  private val regionContainer: RegionContainer,
  private val regionManager: RegionManager,
  private val logger: Logger
) : AntiAbuserPlayer, PlayerFactory<PaperAntiAbuserPlayer> {
  private var player: Player? = null
  private var abuser = false
  private var checking = false
  private var attack = false
  private var illegalItems = ConcurrentHashMap<String, ArrayList<ItemStack>>()
  private var currentRegions = ArrayList<String>()
  private var abuserRegions = ConcurrentHashMap<String, Boolean>()

  override fun getName(): String {
    return player!!.name
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

  override fun check(force: Boolean) {
    val start = System.currentTimeMillis()

    checking = force
    illegalItems.clear()
    abuserRegions.clear()

    regionManager.getRegions().values.forEach {
      logger.debug("Checking region ${it.regionData.id}")
      it.regionData.blockedItems.forEach { item ->
        player!!.inventory.contents.forEach { invItem ->
          if (invItem == null) return@forEach

          if (item.type != invItem.type) return@forEach

          if (ItemUtils.isSimilar(item, invItem)) {
            illegalItems.computeIfAbsent(it.regionData.id) { ArrayList() }.add(item)
            abuserRegions[it.regionData.id] = true
            logger.debug("Player ${player!!.name} has illegal item ${item.type} in region ${it.regionData.id}")
          }
        }
      }
    }

    checking = false

    val end = System.currentTimeMillis()
    logger.debug("Player ${player!!.name} checked in ${end - start}ms")
  }

  override fun canBeAttacked(player: AntiAbuserPlayer): AttackResult {
    if (player.getCurrentRegions() != currentRegions) {
      return AttackResult.DIFFERENT_REGION
    }

    if (player.isAbuser(currentRegions.first())) {
      return AttackResult.ABUSER
    } else {
      currentRegions.forEach {
        if (player.isAbuser(it)) {
          return AttackResult.ABUSER
        }
      }
    }

    return AttackResult.ALLOWED
  }

  override fun getIllegalItems(): Map<String, ArrayList<ItemStack>> {
    return illegalItems
  }

  override fun sendMessage(component: Component) {
    player!!.sendMessage(component)
  }

  fun reloadRegionList() {
    currentRegions.clear()
    currentRegions.add("__global__")

    regionContainer.createQuery().getApplicableRegions(BukkitAdapter.adapt(player!!.location)).forEach { region ->
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

  override fun isAbuser(regionID: String): Boolean {
    return abuserRegions[regionID] ?: false
  }

  override fun create(player: Player): PaperAntiAbuserPlayer {
    this.player = player
    reloadRegionList()
    return this
  }
}
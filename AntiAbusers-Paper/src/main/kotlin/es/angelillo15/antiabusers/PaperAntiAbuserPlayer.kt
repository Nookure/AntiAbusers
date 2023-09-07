package es.angelillo15.antiabusers

import com.google.inject.Inject
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldguard.protection.regions.ProtectedRegion
import com.sk89q.worldguard.protection.regions.RegionContainer
import es.angelillo15.antiabusers.enums.AttackResult
import es.angelillo15.antiabusers.factory.PlayerFactory
import es.angelillo15.antiabusers.manager.RegionManager
import es.angelillo15.antiabusers.utils.ItemUtils
import es.angelillo15.antiabusers.utils.bool
import es.angelillo15.antiabusers.utils.long
import es.angelillo15.antiabusers.utils.tl
import es.angelillo15.core.Logger
import es.angelillo15.core.libs.caffeine.cache.Caffeine
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap

class PaperAntiAbuserPlayer @Inject constructor(
  private val regionContainer: RegionContainer,
  private val regionManager: RegionManager,
  private val logger: Logger
) : AntiAbuserPlayer, PlayerFactory<PaperAntiAbuserPlayer> {
  private var player: Player? = null
  private var abuser = false
  private var checking = false
  private var illegalItems = ConcurrentHashMap<String, ArrayList<ItemStack>>()
  private var currentRegions = ArrayList<String>()
  private var abuserRegions = ConcurrentHashMap<String, Boolean>()
  private var pvpPlayers = Caffeine
    .newBuilder()
    .expireAfterWrite(Duration.ofSeconds(long("Config.pvpTimer")))
    .removalListener { k1: String, v1: AntiAbuserPlayer, _ ->
      sendMessage(tl("General.pvpFinished").replace("{player}", v1.getName()))
    }
    .build<String, AntiAbuserPlayer>()
  private var abusersAttacked = Caffeine
    .newBuilder()
    .expireAfterWrite(Duration.ofSeconds(long("Config.pvpTimer")))
    .build<String, AntiAbuserPlayer>()

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

    if (force && pvpPlayers.asMap().size > 0) {
      pvpPlayers.asMap().forEach { (name, player) ->
        player.stopPVPwith(this)
        stopPVPwith(player)

        sendMessage(tl("General.pvpCancelled").replace("{player}", name))
      }
    }

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

  override fun canBeAttacked(player: AntiAbuserPlayer, checkHit: Boolean): AttackResult {
    if (checking) AttackResult.CHECKING

    if (isPVPing(player)) {
      return AttackResult.ALLOWED
    }

    if (!bool("Config.allowDifferentRegionsAttack")) {
      if (player.getCurrentRegions().sorted() != currentRegions.sorted()) {
        return AttackResult.DIFFERENT_REGION
      }
    }
    if (checkHit) {
      if (bool("Config.cancelAbuserAttack")) {
        if (player.canBeAttacked(this) == AttackResult.ABUSER) {
          return AttackResult.ATTACKING_AN_ABUSER
        }
      }

      if (bool("Config.warnAndCancelOnAttack")) {
        if (player.canBeAttacked(this) == AttackResult.ABUSER) {
          return if (!abusersAttacked.asMap().containsKey(player.getName())) {
            abusersAttacked.put(player.getName(), player)
            AttackResult.ADVERT
          } else {
            AttackResult.ALLOWED
          }
        }
      }
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

  override fun sendActionBar(component: Component) {
    player!!.sendActionBar(component)
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

  override fun startPVPwith(player: AntiAbuserPlayer) {
    if (!isPVPing(player)) {
      sendMessage(tl("General.pvpStarted").replace("{player}", player.getName()))
    }

    pvpPlayers.put(player.getName(), player)
    abusersAttacked.put(player.getName(), player)
  }

  override fun isPVPing(player: AntiAbuserPlayer): Boolean {
    return pvpPlayers.getIfPresent(player.getName()) != null
  }

  override fun stopPVPwith(player: AntiAbuserPlayer) {
    pvpPlayers.invalidate(player.getName())
  }

  override fun clearPVPs() {
    pvpPlayers.invalidateAll()
  }

  override fun checkPVPs() {
    pvpPlayers.estimatedSize()
  }
}

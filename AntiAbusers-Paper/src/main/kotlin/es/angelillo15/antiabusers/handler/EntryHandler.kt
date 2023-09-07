package es.angelillo15.antiabusers.handler

import com.sk89q.worldedit.util.Location
import com.sk89q.worldguard.LocalPlayer
import com.sk89q.worldguard.protection.ApplicableRegionSet
import com.sk89q.worldguard.protection.regions.ProtectedRegion
import com.sk89q.worldguard.session.MoveType
import com.sk89q.worldguard.session.Session
import com.sk89q.worldguard.session.handler.Handler
import es.angelillo15.antiabusers.AntiAbuserPlayer
import es.angelillo15.antiabusers.AntiAbusers
import es.angelillo15.antiabusers.manager.AntiAbusersPlayers
import es.angelillo15.antiabusers.utils.bool
import es.angelillo15.antiabusers.utils.tl

open class EntryHandler protected constructor(session: Session?) : Handler(session) {
  class Factory : Handler.Factory<EntryHandler>() {
    override fun create(session: Session): EntryHandler {
      return EntryHandler(session)
    }
  }

  override fun onCrossBoundary(
    player: LocalPlayer,
    from: Location,
    to: Location,
    toSet: ApplicableRegionSet,
    entered: Set<ProtectedRegion>,
    exited: Set<ProtectedRegion>,
    moveType: MoveType
  ): Boolean {
    if (entered.isEmpty() && exited.isEmpty()) {
      return true
    }

    val manager = AntiAbusers.instance.pluginInjector.getInstance(AntiAbusersPlayers::class.java)
    val abuserPlayer: AntiAbuserPlayer = manager.getPlayer(player.name)!!

    abuserPlayer.reloadRegionList(exited, entered)
    abuserPlayer.check(false)

    AntiAbusers.instance.pPluginLogger
      .debug("Player ${player.name} has exited ${exited.size} regions and entered ${entered.size} regions")

    if (bool("Config.warnOnEnter"))
      for (region in entered) if (abuserPlayer.isAbuser(region.id)) {
        abuserPlayer.sendActionBar(tl("General.abuserRegion"))
        break
      }
    return true
  }

  companion object {
    val FACTORY = Factory()
  }
}

package es.angelillo15.antiabusers.listener

import com.google.inject.Inject
import es.angelillo15.antiabusers.enums.AttackResult
import es.angelillo15.antiabusers.event.PlayerDamageByPlayerEvent
import es.angelillo15.antiabusers.manager.AntiAbusersPlayers
import es.angelillo15.antiabusers.utils.tl
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

class OnEntityAttack : Listener {
  @Inject
  private lateinit var antiAbusersPlayers: AntiAbusersPlayers

  @EventHandler
  fun onEntityAttack(event: EntityDamageByEntityEvent) {
    if (event.damager !is Player) return
    if (event.entity !is Player) return
    val player = event.damager as Player
    val attacked = event.entity as Player

    val antiAbuserPlayer = antiAbusersPlayers.getPlayer(player.name) ?: return
    val antiAbuserAttacked = antiAbusersPlayers.getPlayer(attacked.name) ?: return

    val attackResult = antiAbuserAttacked.canBeAttacked(antiAbuserPlayer, true)
    val playerDamageByPlayerEvent = PlayerDamageByPlayerEvent(antiAbuserPlayer, antiAbuserAttacked, attackResult)
    Bukkit.getPluginManager().callEvent(playerDamageByPlayerEvent)
    if (playerDamageByPlayerEvent.isCancelled) return

    when (attackResult) {
      AttackResult.DIFFERENT_REGION -> {
        event.isCancelled = true
        antiAbuserPlayer.sendMessage(tl("General.playersAreNotInTheSameRegion").replace("{player}", attacked.name))
      }

      AttackResult.ABUSER -> {
        event.isCancelled = true
        antiAbuserPlayer.sendMessage(tl("General.abuser"))
      }

      AttackResult.ALLOWED -> {
        antiAbuserPlayer.startPVPwith(antiAbuserAttacked)
        antiAbuserAttacked.startPVPwith(antiAbuserPlayer)
      }

      AttackResult.ATTACKING_AN_ABUSER -> {
        event.isCancelled = true
        antiAbuserPlayer.sendMessage(tl("General.cancelAbuserAttack"))
      }

      AttackResult.ADVERT -> {
        event.isCancelled = true
        antiAbuserPlayer.sendMessage(tl("General.warnAndCancelOnAttack"))
      }

      AttackResult.CHECKING -> {
        event.isCancelled = true
        antiAbuserPlayer.sendMessage(tl("General.checking"))
      }

      else -> {}
    }

  }
}
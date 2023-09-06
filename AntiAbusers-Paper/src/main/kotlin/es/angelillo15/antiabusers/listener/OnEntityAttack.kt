package es.angelillo15.antiabusers.listener

import com.google.inject.Inject
import es.angelillo15.antiabusers.enum.AttackResult
import es.angelillo15.antiabusers.manager.AntiAbusersPlayers
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

    val attackResult = antiAbuserAttacked.canBeAttacked(antiAbuserPlayer)

    when(attackResult) {
      AttackResult.DIFFERENT_REGION -> {
        event.isCancelled = true
        player.sendMessage("§cNo puedes atacar a este jugador porque no estás en la misma región")
      }

      AttackResult.ABUSER -> {
        event.isCancelled = true
        player.sendMessage("§cNo puedes atacar a este jugador porque es un abuser")
      }
      else -> {}
    }

  }
}
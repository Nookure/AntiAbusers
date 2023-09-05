package es.angelillo15.antiabusers.inject

import es.angelillo15.antiabusers.PaperAntiAbuserPlayer
import org.bukkit.entity.Player

class PlayerModule(val player: Player) : PluginModule() {
  override fun configure() {
    super.configure()
    bind(Player::class.java).toInstance(player)
    bind(PaperAntiAbuserPlayer::class.java)
  }
}
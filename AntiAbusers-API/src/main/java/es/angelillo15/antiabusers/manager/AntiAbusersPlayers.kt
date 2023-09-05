package es.angelillo15.antiabusers.manager

import com.google.inject.Inject
import com.google.inject.Singleton
import es.angelillo15.antiabusers.AntiAbuserPlayer
import es.angelillo15.antiabusers.AntiAbusersInstance
import org.bukkit.Bukkit
import org.bukkit.entity.Player

@Singleton
class AntiAbusersPlayers {
  @Inject
  private lateinit var instance: AntiAbusersInstance

  private val players = HashMap<String, AntiAbuserPlayer>()

  /**
   * Adds a player to the list
   * @param player the player
   */
  fun addPlayer(player: AntiAbuserPlayer) {
    if (players.containsKey(player.getName())) {
      players.remove(player.getName())
    }

    players[player.getName()] = player
  }

  /**
   * Returns the AntiAbuserPlayer of the player
   * @param player the player
   * @return the AntiAbuserPlayer of the player
   */
  fun getPlayer(name: String): AntiAbuserPlayer? {
    if (players.containsKey(name)) {
      return players[name]
    }

    val player = instance.createPlayer(Bukkit.getPlayer(name))
    if (player != null) {
      players[name] = player
    }
    return player
  }

  /**
   * Removes a player from the list
   * @param player the player
   */
  fun removePlayer(player: AntiAbuserPlayer) {
    players.remove(player.getName())
  }

  /**
   * Removes a player from the list
   * @param name the player name
   */
  fun removePlayer(name: String) {
    players.remove(name)
  }

  /**
   * Returns all the players
   * @return all the players
   */
  fun getPlayers(): Collection<AntiAbuserPlayer> {
    return players.values
  }

  /**
   * Returns if the player is an AntiAbuserPlayer
   * @param name the player name
   * @return true if the player is an AntiAbuserPlayer
   */
  fun isAntiAbuserPlayer(name: String): Boolean {
    return players.containsKey(name)
  }

  /**
   * Returns if the player is an AntiAbuserPlayer
   * @param player the player
   * @return true if the player is an AntiAbuserPlayer
   */
  fun isAntiAbuserPlayer(player: Player): Boolean {
    return players.contains(player.name)
  }
}
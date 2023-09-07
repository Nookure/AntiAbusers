package es.angelillo15.antiabusers

import com.sk89q.worldguard.protection.regions.ProtectedRegion
import es.angelillo15.antiabusers.enum.AttackResult
import es.angelillo15.core.utils.TextUtils
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.inventory.ItemStack

interface AntiAbuserPlayer {
  /**
   * Player name
   * @return the player name
   */
  fun getName(): String

  /**
   * Returns if the player is an abuser
   * @return true if the player is an abuser
   */
  fun isAbuser(regionID: String): Boolean

  /**
   * Sets the abuser state of the player
   * @param abuser true if the player is an abuser
   */
  fun setAbuser(abuser: Boolean)

  /**
   * Returns the current region of worldguard
   * @return the current region of worldguard
   */
  fun getCurrentRegions(): ArrayList<String>

  /**
   * Returns if the player is checking
   * @return true if the player is checking
   */
  fun isChecking(): Boolean

  /**
   * Run the abuser check
   */
  fun check(force: Boolean = false)

  /**
   * Returns if the player can attack
   * @param player the player to try to attack
   * @return true if the player can attack
   */
  fun canBeAttacked(player: AntiAbuserPlayer): AttackResult

  /**
   * Returns all the illegal items
   */
  fun getIllegalItems(): Map<String, ArrayList<ItemStack>>

  /**
   * Sends a message to the player
   * @param message the message to send
   */
  fun sendMessage(message: String) {
    sendMessage(MiniMessage.miniMessage().deserialize(TextUtils.toMM(message)))
  }

  /**
   * Sends an action bar to the player
   * @param message the message to send
   */
  fun sendActionBar(message: String) {
    sendActionBar(MiniMessage.miniMessage().deserialize(TextUtils.toMM(message)))
  }

  /**
   * Sends a message to the player
   * @param component the message to send
   */
  fun sendMessage(component: Component)

  /**
   * Sends an action bar to the player
   * @param component the message to send
   */
  fun sendActionBar(component: Component)

  /**
   * Reloads the region list
   * @param exited the exited regions
   * @param entered the entered regions
   */
  fun reloadRegionList(exited: Set<ProtectedRegion>, entered: Set<ProtectedRegion>)

  /**
   * Starts the PVP with the player
   * @param player the player to start the PVP
   * @return true if the PVP started
   */
  fun startPVPwith(player: AntiAbuserPlayer)

  /**
   * Returns if the player is PVPing with another player
   * @param player the player to check
   * @return true if the player is PVPing with another player
   */
  fun isPVPing(player: AntiAbuserPlayer): Boolean

  /**
   * Stops the PVP with the player
   *
   * @param player the player to stop the PVP
   * @return true if the PVP stopped
   */
  fun stopPVPwith(player: AntiAbuserPlayer)

  /**
   * fun check pvp expired
   */
  fun checkPVPs()

  /**
   * Clears the PVPs
   */
  fun clearPVPs()
}
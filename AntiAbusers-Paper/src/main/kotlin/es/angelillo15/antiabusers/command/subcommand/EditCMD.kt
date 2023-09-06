package es.angelillo15.antiabusers.command.subcommand

import com.google.inject.Inject
import es.angelillo15.antiabusers.AntiAbusers
import es.angelillo15.antiabusers.gui.SelectRegion
import es.angelillo15.core.cmd.SubCommand
import es.angelillo15.core.cmd.sender.CommandSender
import es.angelillo15.core.cmd.sender.PlayerCommandSender
import org.bukkit.Bukkit

class EditCMD : SubCommand() {
  @Inject
  private lateinit var antiAbusers: AntiAbusers
  override val description: String
    get() = "Edit the regions of the server"
  override val name: String
    get() = "edit"
  override val permission: String
    get() = "antiabusers.edit"
  override val syntax: String
    get() = "/antiabusers edit"

  override fun onCommand(sender: CommandSender, label: String, args: Array<String>) {
    if (sender !is PlayerCommandSender) return
    val player = Bukkit.getPlayer(sender.name)

    val gui = SelectRegion(player!!, antiAbusers.getInjector().getInstance(es.angelillo15.core.Logger::class.java))
    gui.open()
  }
}
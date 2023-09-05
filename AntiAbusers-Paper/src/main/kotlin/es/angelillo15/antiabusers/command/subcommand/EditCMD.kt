package es.angelillo15.antiabusers.command.subcommand

import com.google.inject.Guice
import es.angelillo15.antiabusers.gui.SelectRegion
import es.angelillo15.antiabusers.inject.PlayerModule
import es.angelillo15.core.cmd.SubCommand
import es.angelillo15.core.cmd.sender.CommandSender
import es.angelillo15.core.cmd.sender.PlayerCommandSender
import org.bukkit.Bukkit

class EditCMD : SubCommand() {
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

    val injector = Guice.createInjector(PlayerModule(player!!))

    val gui = injector.getInstance(SelectRegion::class.java)
    gui.open()
  }
}
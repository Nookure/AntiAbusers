package es.angelillo15.antiabusers.command.subcommand

import com.google.inject.Inject
import es.angelillo15.antiabusers.AntiAbusersInstance
import es.angelillo15.antiabusers.config.Messages
import es.angelillo15.core.cmd.SubCommand
import es.angelillo15.core.cmd.sender.CommandSender

class ReloadCMD : SubCommand() {
  @Inject
  private lateinit var messages: Messages
  @Inject
  private lateinit var antiAbusersInstance: AntiAbusersInstance
  override val description: String
    get() = "Reload the plugin"
  override val name: String
    get() = "reload"
  override val permission: String
    get() = "antiabusers.admin"
  override val syntax: String
    get() = "/antiabusers reload"

  override fun onCommand(sender: CommandSender, label: String, args: Array<String>) {
    antiAbusersInstance.reload()
    sender.sendMessage(messages.reloaded())
  }
}
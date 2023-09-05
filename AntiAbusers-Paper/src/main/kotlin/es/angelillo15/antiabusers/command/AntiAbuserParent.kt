package es.angelillo15.antiabusers.command

import com.google.inject.Inject
import es.angelillo15.antiabusers.AntiAbusers
import es.angelillo15.antiabusers.command.subcommand.ReloadCMD
import es.angelillo15.core.cmd.CommandData
import es.angelillo15.core.cmd.CommandParent
import es.angelillo15.core.cmd.sender.CommandSender
@CommandData(
  name = "antiabusers",
  permission = "antiabusers.admin",
  aliases = ["aa", "anab"]
)
@Suppress("UnstableApiUsage")
class AntiAbuserParent : CommandParent() {
  @Inject
  private lateinit var antiAbusers: AntiAbusers
  override fun registerSubCommands() {
    registerHelpSubCommand("/antiabusers")
    registerSubCommand(antiAbusers.pluginInjector.getInstance(ReloadCMD::class.java))
  }

  override fun sendHelp(sender: CommandSender) {
    sender.sendMessage("<light_purple>AntiAbusers</light_purple> &7- &r&fv${antiAbusers.pluginMeta.version}")

    getCommands().forEach {
      sender.sendMessage("<light_purple><b>></b></light_purple> &r" + it.syntax + " &7- &7" + it.description)
    }
  }
}
package es.angelillo15.antiabusers.command

import es.angelillo15.antiabusers.AntiAbusers
import es.angelillo15.core.cmd.Command
import es.angelillo15.core.cmd.sender.BukkitConsoleCommandSender
import es.angelillo15.core.cmd.sender.PlayerCommandSender
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandTemplate : org.bukkit.command.Command {
  private val command: Command

  constructor(name: String, command: Command) : super(name) {
    this.command = command
  }

  constructor(name: String, command: Command, permission: String) : super(name) {
    this.command = command
    setPermission(permission)
  }

  constructor(name: String, command: Command, permission: String, vararg aliases: String) : super(name) {
    this.command = command
    setPermission(permission)
    setAliases(aliases.toList())
  }

  override fun execute(sender: CommandSender, commandLabel: String, args: Array<String>): Boolean {
    val commandSender = if (sender is Player) {
      PlayerCommandSender(sender)
    } else {
      AntiAbusers.instance.getInjector().getInstance(BukkitConsoleCommandSender::class.java)
    }

    command.onCommand(commandSender, commandLabel, args);
    return true
  }
}
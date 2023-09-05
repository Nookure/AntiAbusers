package es.angelillo15.antiabusers.command

import es.angelillo15.core.cmd.Command
import es.angelillo15.core.cmd.CommandData
import org.bukkit.Bukkit

object CommandManager {
  fun registerCommand(command: Command) {
    val commandData: CommandData?

    try {
      commandData = command.javaClass.getDeclaredAnnotation(CommandData::class.java)
    } catch (e: Exception) {
      e.printStackTrace()
      return
    }

    if (commandData == null) {
      return
    }

    if (commandData.aliases.isEmpty() && commandData.permission.isEmpty()) {
      registerIntoCommandMap(commandData, CommandTemplate(commandData.name, command))
    }

    if (commandData.aliases.isEmpty() && commandData.permission.isNotEmpty()) {
      registerIntoCommandMap(commandData, CommandTemplate(commandData.name, command, commandData.permission))
    }

    registerIntoCommandMap(
      commandData,
      CommandTemplate(commandData.name, command, commandData.permission, *commandData.aliases)
    )
  }

  private fun registerIntoCommandMap(commandData: CommandData, command: org.bukkit.command.Command) {
    Bukkit.getServer().commandMap.register(commandData.name, command)
  }
}
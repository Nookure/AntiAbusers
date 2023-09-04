package es.angelillo15.antiabusers.utils

import com.google.inject.Inject
import com.google.inject.Singleton
import es.angelillo15.antiabusers.AntiAbusers
import es.angelillo15.core.Logger
import es.angelillo15.core.utils.TextUtils
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit

@Singleton
class PluginLogger : Logger() {
  @Inject
  private lateinit var instance: AntiAbusers;
  private val prefix = "<light_purple>AntiAbusers</light_purple> <gray>|</gray>"
  override fun debug(message: String?) {
    if (!instance.getDebug()) return
    Bukkit.getConsoleSender().sendMessage(getFormattedPrefix("$prefix Debug <gray>></gray> $message"))
  }

  override fun error(message: String?) {
    Bukkit.getConsoleSender().sendMessage(getFormattedPrefix("$prefix <red>ERROR </red><gray>></gray> $message"))
  }

  override fun info(message: String?) {
    Bukkit.getConsoleSender().sendMessage(getFormattedPrefix("$prefix INFO <gray>></gray> $message"))
  }

  override fun warn(message: String?) {
    Bukkit.getConsoleSender().sendMessage(getFormattedPrefix("$prefix <yellow>WARN </yellow><gray>></gray> $message"))
  }

  private fun getFormattedPrefix(str: String): Component {
    return MiniMessage.miniMessage().deserialize(TextUtils.toMM(str))
  }
}
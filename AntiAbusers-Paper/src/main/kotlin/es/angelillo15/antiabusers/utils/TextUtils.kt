package es.angelillo15.antiabusers.utils

import es.angelillo15.antiabusers.AntiAbusers
import es.angelillo15.antiabusers.config.ConfigManager
import es.angelillo15.antiabusers.config.Messages
import es.angelillo15.core.libs.simpleyaml.configuration.file.YamlFile
import es.angelillo15.core.utils.TextUtils
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

fun raw(path: String): String {
  return getMessages().getString(path) ?: path
}

fun tl(path: String): String {
  return TextUtils.toMM(
    raw(path).replace(
      "{prefix}",
      AntiAbusers.instance.pluginInjector.getInstance(Messages::class.java).prefix()
    )
  )
}

fun cmp(path: String): Component {
  return MiniMessage.miniMessage().deserialize(tl(path))
}

fun string(path: String): String {
  return getConfig().getString(path) ?: path
}

fun integer(path: String): Int {
  return getMessages().getInt(path)
}

fun long(path: String): Long {
  return getConfig().getLong(path)
}

fun bool(path: String): Boolean {
  return getConfig().getBoolean(path)
}

private fun getMessages(): YamlFile {
  return AntiAbusers.instance.pluginInjector.getInstance(ConfigManager::class.java).messages!!.config
}

private fun getConfig(): YamlFile {
  return AntiAbusers.instance.pluginInjector.getInstance(ConfigManager::class.java).config!!.config
}
package es.angelillo15.antiabusers.utils

import es.angelillo15.antiabusers.AntiAbusers
import es.angelillo15.antiabusers.config.ConfigManager
import es.angelillo15.core.libs.simpleyaml.configuration.file.YamlFile
import es.angelillo15.core.utils.TextUtils
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

fun raw(path: String): String {
  return getMessages().getString(path) ?: path
}

fun tl(path: String): String {
  return TextUtils.toMM(raw(path))
}

fun cmp(path: String): Component {
  return MiniMessage.miniMessage().deserialize(tl(path))
}

private fun getMessages() : YamlFile {
  return AntiAbusers.instance.pluginInjector.getInstance(ConfigManager::class.java).messages!!.config
}
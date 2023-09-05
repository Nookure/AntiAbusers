package es.angelillo15.antiabusers.config

import com.google.inject.Inject
import com.google.inject.Singleton
import es.angelillo15.core.messages.CoreMessages
import es.angelillo15.core.utils.TextUtils
import net.kyori.adventure.text.Component

@Singleton
class Messages @Inject constructor(var configManager: ConfigManager) : CoreMessages() {
  fun prefix(): String {
    return getMessage("General.prefix")
  }

  fun reloaded(): String {
    return formatMessage("General.reloaded")
  }

  override fun noPermission(): String {
    return formatMessage("General.noPermission")
  }

  private fun formatMessage(path: String): String {
    return getMessage(path).replace("{prefix}", prefix())
  }

  private fun formatMessages(path: String): List<String> {
    return configManager.messages!!.config.getStringList(path).stream().map { s: String ->
      TextUtils.simpleColorize(s)
    }.toList()
  }

  private fun getMessage(path: String): String {
    return TextUtils.toMM(configManager.messages!!.config.getString(path))
  }

  private fun formatComponent(path: String): Component {
    return TextUtils.toComponent(configManager.messages!!.config.getString(path))
  }

  fun formatComponents(path: String): List<Component> {
    return configManager.messages!!.config.getStringList(path).stream().map { s: String ->
      TextUtils.toComponent(s)
    }.toList()
  }
}

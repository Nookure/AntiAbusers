package es.angelillo15.antiabusers.config

import com.google.inject.Inject
import com.google.inject.Singleton
import es.angelillo15.core.messages.CoreMessages
import es.angelillo15.core.utils.TextUtils
import net.kyori.adventure.text.Component

@Singleton
class Messages @Inject constructor(var configManager: ConfigManager) : CoreMessages() {
  companion object {
    var instance: Messages? = null
      private set
  }

  init {
    instance = this
  }

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

  fun getMessage(path: String): String {
    return TextUtils.toMM(configManager.messages.config.getString(path))
  }
}

fun tl(path: String): String {
  return Messages.instance!!.getMessage(path)
}
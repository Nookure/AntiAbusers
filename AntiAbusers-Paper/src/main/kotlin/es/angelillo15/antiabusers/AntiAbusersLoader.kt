package es.angelillo15.antiabusers

import com.google.inject.Injector
import es.angelillo15.core.utils.TextUtils
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.InputStream

class AntiAbusersLoader : JavaPlugin(), AntiAbusersInstance {
    override fun onEnable() {
        server.consoleSender.sendMessage(TextUtils.toComponent("&aAntiAbusers has been enabled!"))
    }

    override fun onDisable() {
        logger.info("AntiAbusers has been disabled!")
    }

    override fun getDebug(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getInjector(): Injector {
        TODO("Not yet implemented")
    }

    override fun getPluginDataFolder(): File {
        TODO("Not yet implemented")
    }

    override fun getPluginResource(s: String): InputStream {
        TODO("Not yet implemented")
    }

    override fun setDebug(debug: Boolean) {
        TODO("Not yet implemented")
    }
}
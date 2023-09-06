package es.angelillo15.antiabusers.inject

import com.google.inject.AbstractModule
import com.sk89q.worldguard.WorldGuard
import com.sk89q.worldguard.protection.regions.RegionContainer
import es.angelillo15.antiabusers.AntiAbusers
import es.angelillo15.antiabusers.AntiAbusersInstance
import es.angelillo15.antiabusers.config.ConfigManager
import es.angelillo15.antiabusers.config.Messages
import es.angelillo15.antiabusers.manager.RegionManager
import es.angelillo15.antiabusers.thread.AsyncExecutor
import es.angelillo15.antiabusers.utils.PluginLogger
import es.angelillo15.core.Logger
import es.angelillo15.core.NookInstance
import es.angelillo15.core.messages.CoreMessages
import net.kyori.adventure.platform.AudienceProvider
import net.kyori.adventure.platform.bukkit.BukkitAudiences

open class PluginModule : AbstractModule() {
  override fun configure() {
    bind(AntiAbusers::class.java).toInstance(AntiAbusers.instance)
    bind(Logger::class.java).to(PluginLogger::class.java).asEagerSingleton()
    bind(RegionContainer::class.java).toInstance(WorldGuard.getInstance().platform.regionContainer)
    bind(AntiAbusersInstance::class.java).toInstance(AntiAbusers.instance)
    bind(AntiAbusers::class.java).toInstance(AntiAbusers.instance)
    bind(ConfigManager::class.java).asEagerSingleton()
    bind(NookInstance::class.java).toInstance(AntiAbusers.instance)
    bind(CoreMessages::class.java).to(Messages::class.java).asEagerSingleton()
    bind(AudienceProvider::class.java).toInstance(BukkitAudiences.create(AntiAbusers.instance))
    bind(RegionManager::class.java).asEagerSingleton()
    bind(AsyncExecutor::class.java).asEagerSingleton()
  }
}
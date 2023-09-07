package es.angelillo15.antiabusers

import com.google.inject.Guice
import com.google.inject.Injector
import com.sk89q.worldguard.WorldGuard
import es.angelillo15.antiabusers.command.AntiAbuserParent
import es.angelillo15.antiabusers.command.CommandManager
import es.angelillo15.antiabusers.config.ConfigManager
import es.angelillo15.antiabusers.handler.EntryHandler
import es.angelillo15.antiabusers.inject.PluginModule
import es.angelillo15.antiabusers.listener.*
import es.angelillo15.antiabusers.region.Region
import es.angelillo15.antiabusers.task.PlayerCheckTask
import es.angelillo15.antiabusers.task.PlayerPVPCheckTask
import es.angelillo15.antiabusers.utils.PluginLogger
import es.angelillo15.antiabusers.utils.StaticMembersInjector
import es.angelillo15.core.Logger
import es.angelillo15.core.utils.TextUtils
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import java.io.File
import java.io.InputStream

open class AntiAbusers : JavaPlugin(), AntiAbusersInstance {
  private val listeners = ArrayList<Listener>()
  lateinit var pPluginLogger: PluginLogger
    private set
  lateinit var pluginInjector: Injector
    private set
  private var debug = false
  private var playerInvCheckTask: BukkitTask? = null
  private var playerPVPCheckTask: BukkitTask? = null

  companion object {
    lateinit var instance: AntiAbusers
      private set
  }

  override fun onEnable() {
    instance = this
    pluginInjector = Guice.createInjector(PluginModule())
    pPluginLogger = pluginInjector.getInstance(PluginLogger::class.java)
    StaticMembersInjector.injectStatics(pluginInjector, TextUtils::class.java)
  }

  override fun getDebug(): Boolean {
    return debug
  }

  override fun getInjector(): Injector {
    return pluginInjector
  }

  override fun getPluginDataFolder(): File {
    return dataFolder
  }

  override fun getPluginResource(s: String): InputStream {
    return getResource(s)!!
  }

  override fun setDebug(debug: Boolean) {
    this.debug = debug
  }

  override fun createPlayer(player: Player?): AntiAbuserPlayer? {
    if (player == null) {
      pPluginLogger.error("Player cannot be null!")
      return null
    }
    pPluginLogger.debug("Creating player ${player.name}")
    return getInjector().getInstance(PaperAntiAbuserPlayer::class.java).create(player)
  }

  override fun loadHandlers() {
    val sessionManager = WorldGuard.getInstance().platform.sessionManager
    sessionManager.registerHandler(EntryHandler.FACTORY, null)
  }

  override fun loadListeners() {
    registerListener(pluginInjector.getInstance(OnJoinLeave::class.java))
    registerListener(pluginInjector.getInstance(OnInventoryNewItem::class.java))
    registerListener(pluginInjector.getInstance(OnEntityAttack::class.java))
    registerListener(pluginInjector.getInstance(OnItemPickup::class.java))
    registerListener(pluginInjector.getInstance(OnDeath::class.java))
  }

  private fun registerListener(listener: Listener) {
    listeners.add(listener)
    server.pluginManager.registerEvents(listener, this)
  }

  fun unregisterListener() {
    HandlerList.getHandlerLists().forEach {
      listeners.forEach(it::unregister)
    }

    listeners.clear()
  }

  fun loadTasks() {
    playerInvCheckTask = Bukkit.getScheduler()
      .runTaskTimerAsynchronously(
        this,
        getInjector().getInstance(PlayerCheckTask::class.java),
        20,
        3 * 20
      )

    playerPVPCheckTask = Bukkit.getScheduler()
      .runTaskTimerAsynchronously(
        this,
        getInjector().getInstance(PlayerPVPCheckTask::class.java),
        20,
        1 * 20
      )
  }

  fun unloadTasks() {
    playerInvCheckTask?.cancel()
    playerPVPCheckTask?.cancel()
  }

  override fun reload() {
    val start = System.currentTimeMillis()
    pPluginLogger.debug("Reloading plugin...")
    pPluginLogger.debug("Unregistering listeners...")
    unregisterListener()
    pPluginLogger.debug("Unregistering commands...")
    unregisterCommands()
    pPluginLogger.debug("Stoping tasks...")
    unloadTasks()
    pPluginLogger.debug("Loading config...")
    loadConfig()
    pPluginLogger.debug("Registering commands...")
    registerCommands()
    pPluginLogger.debug("Loading listeners...")
    loadListeners()
    pPluginLogger.debug("Loading tasks...")
    loadTasks()
    val end = System.currentTimeMillis()
    pPluginLogger.debug("Plugin reloaded in ${end - start}ms")
  }

  override fun getPluginLogger(): Logger {
    return pPluginLogger
  }

  override fun loadConfig() {
    pluginInjector.getInstance(ConfigManager::class.java).load()
  }

  override fun registerCommands() {
    CommandManager.registerCommand(pluginInjector.getInstance(AntiAbuserParent::class.java))
  }

  override fun unregisterCommands() {
    Bukkit.getServer().commandMap.getCommand("antiabusers")?.unregister(Bukkit.getServer().commandMap)
  }

  fun loadRegions() {
    val folder = File(dataFolder, "/regions/")
    if (!folder.exists()) {
      folder.mkdirs()
    }

    folder.listFiles()?.forEach {
      Region.loadFile(it.nameWithoutExtension, this)
    }
  }
}
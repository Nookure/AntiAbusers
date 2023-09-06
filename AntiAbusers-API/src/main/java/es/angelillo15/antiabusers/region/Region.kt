package es.angelillo15.antiabusers.region

import com.google.inject.Inject
import es.angelillo15.antiabusers.AntiAbusersInstance
import es.angelillo15.antiabusers.manager.RegionManager
import es.angelillo15.antiabusers.thread.executeAsync
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream

class Region @Inject constructor(val regionData: RegionData, private val antiAbusersInstance: AntiAbusersInstance) {
  private val folder = antiAbusersInstance.getPluginDataFolder().resolve("regions")
  private val finalFile = FileOutputStream(folder.resolve("${regionData.id}.anab"))
  private val writer = BukkitObjectOutputStream(finalFile)

  init {
    if (!folder.exists()) {
      folder.mkdirs()
    }
  }

  /**
   * Writes the region synchronously
   */
  fun write() {
    antiAbusersInstance.getPluginLogger().debug("Writing binary for region ${regionData.id}")
    writer.writeObject(regionData)
  }

  /**
   * Writes the region data asynchronously
   * @see write
   */
  fun writeAsync() {
    executeAsync {
      write()
    }
  }

  companion object {
    fun load(regionID: String, instance: AntiAbusersInstance): Region {
      val regionManager: RegionManager = instance.getInjector().getInstance(RegionManager::class.java)

      if (regionManager.getRegion(regionID) != null) {
        return regionManager.getRegion(regionID)!!
      }

      val fileInputStream: InputStream
      try {
        fileInputStream = FileInputStream(
          instance.getPluginDataFolder().resolve("regions").resolve("${regionID}.anab")
        )
      } catch (e: Exception) {
        instance.getPluginLogger().debug("Region $regionID has no binary file")
        val region = Region(RegionData(regionID, ArrayList()), instance)
        region.write()
        return region
      }

      val reader = BukkitObjectInputStream(fileInputStream)
      val regionData = reader.readObject() as RegionData
      val region = Region(regionData, instance)

      regionManager.addRegion(region)
      return region
    }
  }
}

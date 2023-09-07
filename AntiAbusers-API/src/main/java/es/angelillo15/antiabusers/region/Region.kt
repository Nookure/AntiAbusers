package es.angelillo15.antiabusers.region

import com.google.inject.Inject
import es.angelillo15.antiabusers.AntiAbusersInstance
import es.angelillo15.antiabusers.manager.RegionManager
import es.angelillo15.antiabusers.thread.executeAsync
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files

class Region @Inject constructor(val regionData: RegionData, private val antiAbusersInstance: AntiAbusersInstance) {
  private val folder = antiAbusersInstance.getPluginDataFolder().resolve("regions")
  private val finalFile = folder.resolve("${regionData.world}.${regionData.id}.anab")
  private val tempFile = folder.resolve("${regionData.world}.${regionData.id}.anab.tmp")

  init {
    if (!folder.exists()) {
      folder.mkdirs()
    }
  }

  /**
   * Writes the region synchronously
   */
  fun write() {
    antiAbusersInstance.getPluginLogger().debug("Writing region ${regionData.id}")
    val start = System.currentTimeMillis()
    try {
      FileOutputStream(tempFile).use {
        BukkitObjectOutputStream(it).use { tempFileWriter ->
          tempFileWriter.writeObject(regionData)
          tempFileWriter.flush()
          tempFileWriter.close()
        }
      }

      Files.move(tempFile.toPath(), finalFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING)
    } catch (e: IOException) {
      antiAbusersInstance.getPluginLogger().error("Error while writing region ${regionData.id}")
      e.printStackTrace()
    }

    antiAbusersInstance.getPluginLogger()
      .debug("Region ${regionData.id} written in ${System.currentTimeMillis() - start}ms")
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
    fun loadFile(fileName: String, instance: AntiAbusersInstance) {
      fileName.split(".").let {
        load(it[1], it[0], instance)
      }
    }

    fun load(regionID: String, world: String, instance: AntiAbusersInstance): Region {
      val regionManager: RegionManager = instance.getInjector().getInstance(RegionManager::class.java)

      if (regionManager.getRegion(regionID) != null) {
        instance.getPluginLogger().debug("Region $regionID already loaded")
        return regionManager.getRegion(regionID)!!
      }

      val fileInputStream: InputStream
      try {
        fileInputStream = FileInputStream(
          instance.getPluginDataFolder().resolve("regions").resolve("${world}.${regionID}.anab")
        )
      } catch (e: Exception) {
        instance.getPluginLogger().debug("Region $regionID has no binary file")
        val region = Region(RegionData(regionID, world, ArrayList()), instance)
        region.write()
        return region
      }

      val reader = BukkitObjectInputStream(fileInputStream)
      return try {
        instance.getPluginLogger().debug("Loading region $regionID")
        val regionData = reader.readObject() as RegionData
        val region = Region(regionData, instance)

        regionManager.addRegion(region)
        region
      } catch (e: Exception) {
        instance.getPluginLogger().error("Error while loading region $regionID")
        val region = Region(RegionData(regionID, world, ArrayList()), instance)
        region.write()
        region
      }
    }
  }
}

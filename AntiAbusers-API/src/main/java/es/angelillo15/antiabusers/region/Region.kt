package es.angelillo15.antiabusers.region

import com.google.inject.Inject
import es.angelillo15.antiabusers.AntiAbusersInstance
import es.angelillo15.antiabusers.thread.executeAsync
import org.bukkit.inventory.ItemStack
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class Region @Inject constructor(val regionData: RegionData, private val antiAbusersInstance: AntiAbusersInstance) {
  private val folder = antiAbusersInstance.getPluginDataFolder().resolve("regions")
  private val finalFile = FileOutputStream(folder.resolve("${regionData.id}.anab"))
  private val writer = BukkitObjectOutputStream(finalFile)

  init {
    if (!folder.exists()) {
      folder.mkdirs()
    }
  }

  fun write() {
    antiAbusersInstance.getPluginLogger().debug("Writing binary for region ${regionData.id}")
    writer.writeObject(regionData)
  }

  companion object {
    fun load(regionID: String, instance: AntiAbusersInstance): Region {
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

      return Region(regionData, instance)
    }
  }
}

package es.angelillo15.antiabusers.manager

import com.google.inject.Inject
import com.google.inject.Singleton
import es.angelillo15.antiabusers.AntiAbusersInstance
import es.angelillo15.antiabusers.region.Region
import es.angelillo15.antiabusers.thread.executeAsync
import es.angelillo15.core.Logger

@Singleton
class RegionManager {
  @Inject
  private lateinit var instance: AntiAbusersInstance
  @Inject
  private lateinit var logger: Logger

  private val regions = HashMap<String, Region>()

  /**
   * Adds a region to the list
   * @param region the region
   */
  fun addRegion(region: Region) {
    if (regions.containsKey(region.regionData.id)) {
      regions.remove(region.regionData.id)
    }

    regions[region.regionData.id] = region
  }

  /**
   * Returns the region
   * @param regionID the region ID
   * @return the region
   */
  fun getRegion(regionID: String): Region? {
    return regions[regionID]
  }

  /**
   * Removes a region from the list
   * @param region the region
   */
  fun removeRegion(region: Region) {
    regions.remove(region.regionData.id)
  }

  /**
   * Removes a region from the list
   * @param regionID the region ID
   */
  fun removeRegion(regionID: String) {
    regions.remove(regionID)
  }

  /**
   * Returns the regions
   * @return the regions
   */
  fun getRegions(): HashMap<String, Region> {
    return regions
  }

  /**
   * Returns if the region exists
   * @param regionID the region ID
   * @return if the region exists
   */
  fun exists(regionID: String): Boolean {
    return regions.containsKey(regionID)
  }

  /**
   * Returns if the region exists
   * @param region the region
   * @return if the region exists
   */
  fun exists(region: Region): Boolean {
    return regions.containsKey(region.regionData.id)
  }

  fun saveAll() {
    logger.debug("Saving all regions")
    regions.values.forEach { region ->
      executeAsync {
        region.write()
      }
    }
  }
}
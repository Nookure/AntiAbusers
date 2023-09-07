package es.angelillo15.antiabusers.region

import org.bukkit.inventory.ItemStack
import java.io.Serializable

class RegionData(val id: String, val world: String,val blockedItems: ArrayList<ItemStack>) : Serializable

package es.angelillo15.antiabusers.utils

import org.bukkit.Utility
import org.bukkit.inventory.ItemStack
import java.util.Objects

class ItemUtils {
  companion object {
    /**
     * Checks if two ItemStacks are similar.
     * Similar means that they are the same type,
     * same display name, same lore, same enchants,
     * same flags, etc.
     *
     * But it doesn't check the number of items in the stack
     * or the durability of the item.
     *
     * @param original The original ItemStack
     * @param itemToCompare The ItemStack to compare
     * @return true if the two ItemStacks are similar, false otherwise
     * @see ItemStack
     */
    @Utility
    fun isSimilar(original: ItemStack, itemToCompare: ItemStack): Boolean {
      if (itemToCompare === original) return true
      if (original.type != itemToCompare.type) return false
      if (original.itemMeta == null && itemToCompare.itemMeta == null) return true
      if (!Objects.equals(original.itemMeta.enchants, itemToCompare.itemMeta.enchants)) return false
      if (!original.itemMeta.hasDisplayName() && !itemToCompare.itemMeta.hasDisplayName()) return true
      if (original.displayName() != itemToCompare.displayName()) return false
      if (!Objects.equals(original.itemMeta.lore(), itemToCompare.itemMeta.lore())) return false
      return true
    }
  }
}
package es.angelillo15.antiabusers.factory

import org.bukkit.entity.Player

interface PlayerFactory<T> {
  fun create(player: Player) : T
}
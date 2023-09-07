package es.angelillo15.antiabusers.task

import com.google.inject.Inject
import es.angelillo15.antiabusers.manager.AntiAbusersPlayers

class PlayerPVPCheckTask : Runnable {
  @Inject
  private lateinit var antiAbusersPlayers: AntiAbusersPlayers

  override fun run() {
    antiAbusersPlayers.getPlayers().forEach {
      it.checkPVPs()
    }
  }
}
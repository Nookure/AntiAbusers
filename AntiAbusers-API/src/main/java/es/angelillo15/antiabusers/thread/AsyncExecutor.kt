package es.angelillo15.antiabusers.thread

import com.google.inject.Inject
import com.google.inject.Singleton
import es.angelillo15.core.Logger
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor

private const val TPS = 5
private const val MILES = 1000 / TPS

@Singleton
@Suppress("UNCHECKED_CAST")
class AsyncExecutor @Inject constructor(private val logger: Logger) {
  companion object {
    @JvmStatic
    var instance: AsyncExecutor? = null
      private set
  }

  private var threadPoolExecutor = Executors.newFixedThreadPool(5)
  private var actions = ArrayList<Action>()
  private var shuttingDown = false

  fun start() {
    Thread({
      while (!shuttingDown) {
        try {
          Thread.sleep(5)
        } catch (e: InterruptedException) {
          logger.error("Error while sleeping thread: ${e.message}")
        }

        if (shuttingDown) {
          break
        }

        val actionsClone: ArrayList<Action> = actions.clone() as ArrayList<Action>

        for (action in actionsClone) {
          if (action.delayTask > 0) {
            action.delayTask -= 5
            continue
          } else {
            action.delayTask = action.delay
          }

          threadPoolExecutor.execute {
            try {
              action.runnable.invoke()
              logger.debug("Executed action $action")
            } catch (e: Exception) {
              logger.error("Error while executing action ${action}: ${e.message}")
            }
          }

          if (!action.repeat) removeAction(action)
        }
      }

      shuttingDown = false
      logger.debug("Parallel thread stopped!")
      threadPoolExecutor.shutdownNow()
    }, "AntiAbusers-ParallelThread").start()

  }

  fun stop() {
    logger.debug("Stopping parallel thread...")
    shuttingDown = true
    clearActions()
  }

  /**
   * Adds an action to the thread
   * @param action The action to add
   */
  fun addAction(action: Action): Int {
    actions.add(action)
    return actions.indexOf(action)
  }

  /**
   * Removes an action from the thread
   * @param action The action to remove
   */
  fun removeAction(action: Action) {
    actions.remove(action)
  }

  /**
   * Removes an action from the thread
   * @param index The index of the action to remove
   */
  fun removeAction(index: Int) {
    actions.removeAt(index)
  }

  /**
   * Removes all actions from the thread
   */
  fun clearActions() {
    actions.clear()
  }

  fun execute(runnable: Runnable) {
    Thread { threadPoolExecutor.execute(runnable) }.start()
  }

  /**
   * Adjusts the threads
   * @param threads The number of threads
   */
  fun adjustThreads(threads: Int) {
    getThreadPoolExecutor().corePoolSize = threads
  }

  /**
   * Get the current thread pool executor
   * @return The thread pool executor
   */
  fun getThreadPoolExecutor(): ThreadPoolExecutor {
    return threadPoolExecutor as ThreadPoolExecutor
  }

  /**
   * Increment the threads by 1
   */
  fun incrementThreads() {
    incrementThreads(1)
  }

  /**
   * Decrement the threads by 1
   */
  fun decrementThreads() {
    decrementThreads(1)
  }

  /**
   * Increment the threads by an amount
   * @param amount The amount to increment
   */
  fun incrementThreads(amount: Int) {
    adjustThreads(getThreadPoolExecutor().corePoolSize + amount)
  }

  /**
   * Decrement the threads by an amount
   * @param amount The amount to decrement
   */
  fun decrementThreads(amount: Int) {
    adjustThreads(getThreadPoolExecutor().corePoolSize - amount)
  }

  /**
   * Executes a runnable
   * @param runnable The runnable to execute
   * @param delay The delay in milliseconds
   * @param repeat If the action should repeat
   */
  fun execute(runnable: () -> Unit, delay: Int?, repeat: Boolean?): Int {
    return addAction(Action(runnable, delay ?: 0, repeat ?: false))
  }

  /**
   * Executes a runnable
   * @param runnable The runnable to execute
   */
  fun execute(runnable: () -> Unit): Int {
    return execute(runnable, 0, false)
  }

  init {
    instance = this
  }
}

/**
 * Executes a runnable
 * @param runnable The runnable to execute
 */
fun executeAsync(runnable: Runnable) {
  AsyncExecutor.instance!!.execute(runnable)
}

/**
 * Executes a runnable
 * @param runnable The runnable to execute
 */
fun executeAsync(runnable: () -> Unit) {
  AsyncExecutor.instance!!.execute(runnable)
}

/**
 * Executes a runnable
 * @param runnable The runnable to execute
 * @param delay The delay in milliseconds
 */
fun executeAsync(runnable: () -> Unit, delay: Int) {
  AsyncExecutor.instance!!.execute(runnable, delay, false)
}

/**
 * Executes a runnable
 * @param runnable The runnable to execute
 * @param delay The delay in milliseconds
 * @param repeat If the action should repeat
 */
fun executeAsync(runnable: () -> Unit, delay: Int, repeat: Boolean) {
  AsyncExecutor.instance!!.execute(runnable, delay, repeat)
}

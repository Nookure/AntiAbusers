import com.google.inject.Guice
import com.google.inject.Injector
import es.angelillo15.antiabusers.thread.AsyncExecutor
import es.angelillo15.antiabusers.thread.executeAsync
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import utils.TestModule

class AsyncExecutorTest {
  companion object {
    private lateinit var injector : Injector
    @JvmStatic
    @BeforeAll
    fun setUp() {
      injector = Guice.createInjector(TestModule())

      injector.getInstance(AsyncExecutor::class.java).start()
    }
  }

  @Test
  fun test() {
    val asyncExecutor = injector.getInstance(AsyncExecutor::class.java)

    executeAsync {
      println("Hello world!")
      Thread.sleep(100)
      println("Sleeped 100 ms for the thread ${Thread.currentThread().name}")
    }

    executeAsync {
      println("Hello world 2!")
      Thread.sleep(50)
      println("Sleeped 50ms for the thread ${Thread.currentThread().name}")
    }

    Thread.sleep(20)
    println("Sleeped 20ms for the thread ${Thread.currentThread().name}")

    Thread.sleep(100)
  }
}
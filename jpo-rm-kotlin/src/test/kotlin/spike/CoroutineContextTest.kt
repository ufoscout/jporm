package spike

import com.jporm.rm.quasar.RmKotlinTestBase
import kotlinx.coroutines.experimental.*
import org.junit.Test

/**
 * Created by ufo on 03/07/17.
 */

class CoroutineContextTest : RmKotlinTestBase() {

    @Test
    fun testContext() = runBlocking<Unit> {
        val jobs = arrayListOf<Job>()
        jobs += launch(Unconfined) { // not confined -- will work with main thread
            println(" 'Unconfined': I'm working in thread ${Thread.currentThread().name}")
        }
        jobs += launch(context) { // context of the parent, runBlocking coroutine
            println("    'context': I'm working in thread ${Thread.currentThread().name}")
        }
        jobs += launch(CommonPool) { // will get dispatched to ForkJoinPool.commonPool (or equivalent)
            println(" 'CommonPool': I'm working in thread ${Thread.currentThread().name}")
        }
        jobs += launch(newSingleThreadContext("MyOwnThread")) { // will get its own new thread
            println("     'newSTC': I'm working in thread ${Thread.currentThread().name}")
        }
        jobs.forEach { it.join() }
    }

}
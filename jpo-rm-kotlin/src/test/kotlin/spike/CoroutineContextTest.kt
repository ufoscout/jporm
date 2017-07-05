package spike

import com.jporm.rm.kotlin.RmKotlinTestBase
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

    @Test
    fun testContext2() = runBlocking<Unit> {
        val jobs = arrayListOf<Job>()
        jobs += launch(Unconfined) { // not confined -- will work with main thread
            println(" 'Unconfined': I'm working in thread ${Thread.currentThread().name}")
            printContext()
            delay(500)
            println(" 'Unconfined': After delay in thread ${Thread.currentThread().name}")
            printContext()
        }
        jobs += launch(context) { // context of the parent, runBlocking coroutine
            Thread.sleep(1000)
            println("    'context': I'm working in thread ${Thread.currentThread().name}")
            printContext()
            delay(1000)
            println("    'context': After delay in thread ${Thread.currentThread().name}")
            printContext()
        }
        jobs.forEach { it.join() }
    }

    suspend fun printContext() {
        println(" 'printContext': I'm working in thread ${Thread.currentThread().name}")
    }
}
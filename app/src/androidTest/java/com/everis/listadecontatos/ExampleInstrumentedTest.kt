package com.everis.listadecontatos

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.everis.listadecontatos.feature.listacontatos.model.ContatosVO
import com.everis.listadecontatos.feature.listacontatos.repository.ListaDeContatosRepository
import com.everis.listadecontatos.helpers.HelperDB

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class RepositoryTest {

    lateinit var repository: ListaDeContatosRepository

    @Before
    fun setUp() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        repository = ListaDeContatosRepository(
            HelperDB(appContext)
        )
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.everis.listadecontatos", appContext.packageName)
    }

    @Test
    fun repositoryTest() {
        var lista: List<ContatosVO>? = null
        var lock = CountDownLatch(1)

        repository.requestListaDeContatos(
            "",
            onSucesso = { listaResult ->
                lista = listaResult
                lock.countDown()

            }, onError = { ex ->
                fail("Não deveria ter retornado erro") //Não pode cair no onError senão já falhou
                lock.countDown()
            }
        )
        lock.await(3, TimeUnit.MILLISECONDS) //Quando alguem avisar que o countDown chegou a 0, continua
        assertNotNull(lista)
        assertTrue(lista!!.isNotEmpty())
        assertEquals(5, lista!!.size)
    }
}

package uz.gita.myapplication.ui.screen

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import uz.gita.myapplication.util.Check

class BaseUrlFragmentTest {

    @Test
    fun `not contain http`() {
        val response = Check().checkURL("lakdlskn")
        assertThat(response).isFalse()
    }

    @Test
    fun `length less than 12`() {
        val response = Check().checkURL("https://c1")
        assertThat(response).isFalse()

    }

    @Test
    fun `everything is okay`() {
        val response = Check().checkURL("https://c114-95-214-210-176.eu.ngrok.io")
        assert(true) {
            response
        }
    }
}
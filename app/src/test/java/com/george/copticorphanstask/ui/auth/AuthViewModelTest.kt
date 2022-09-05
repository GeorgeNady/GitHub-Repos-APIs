package com.george.copticorphanstask.ui.auth

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AuthViewModelTest {

    private lateinit var email: String
    private lateinit var password: String
    private lateinit var repassword: String

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
        email = ""
        password = ""
        repassword = ""
    }

    @Test
    fun `email not empty`() {
        this.email = "george@gmail.com"
        this.password = "00000000"
        val imageNotEmpty = email.isNotEmpty()
        val validPassword = password.length >= 8
        assertEquals(true, imageNotEmpty && validPassword)
    }

    @Test
    fun `email is empty`() {
        this.email = ""
        this.password = "00000000"
        val imageNotEmpty = email.isNotEmpty()
        val validPassword = password.length >= 8
        assertEquals(false, imageNotEmpty && validPassword)
    }

    @Test
    fun `password is valid`() {
        this.email = "george@gmail.com"
        this.password = "00000000"
        val imageNotEmpty = email.isNotEmpty()
        val validPassword = password.length >= 8
        assertEquals(true, imageNotEmpty && validPassword)
    }

    @Test
    fun `password is invalid`() {
        this.email = "george@gmail.com"
        this.password = "000000"
        val imageNotEmpty = email.isNotEmpty()
        val validPassword = password.length >= 8
        assertEquals(false, imageNotEmpty && validPassword)
    }

}
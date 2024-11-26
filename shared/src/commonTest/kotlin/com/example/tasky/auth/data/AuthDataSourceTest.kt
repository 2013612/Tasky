package com.example.tasky.auth.data

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.example.tasky.auth.data.model.LoginBody
import com.example.tasky.auth.data.model.LoginResponse
import com.example.tasky.auth.data.model.RegisterBody
import com.example.tasky.commom.mock.HttpManagerMock
import com.example.tasky.common.domain.model.ResultWrapper
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class AuthDataSourceTest {
    private lateinit var authDataSource: AuthDataSource

    @BeforeTest
    fun setUp() {
        val mockEngine =
            MockEngine { request ->
                if (request.url.encodedPath.contains("login")) {
                    respond(
                        content =
                            ByteReadChannel(
                                """{"accessToken":"accessToken","refreshToken":"refreshToken",
                                    |"fullName":"fullName","userId":"userId",
                                    |"accessTokenExpirationTimestamp":0}
                                """.trimMargin(),
                            ),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json"),
                    )
                } else {
                    respond(
                        content = ByteArray(0),
                        status = HttpStatusCode.OK,
                    )
                }
            }
        val mockHttp = HttpManagerMock(mockEngine)
        authDataSource = AuthDataSource(mockHttp.httpClient)
    }

    @Test
    fun `test login`() =
        runTest {
            val loginBody = LoginBody(email = "email", password = "password")

            val actual = authDataSource.login(loginBody)

            val expect =
                ResultWrapper.Success(
                    LoginResponse(
                        accessToken = "accessToken",
                        refreshToken = "refreshToken",
                        fullName = "fullName",
                        userId = "userId",
                        accessTokenExpirationTimestamp = 0,
                    ),
                )

            assertThat(actual).isEqualTo(expect)
        }

    @Test
    fun `test register`() =
        runTest {
            val registerBody = RegisterBody(fullName = "fullName", email = "email", password = "password")

            val actual = authDataSource.register(registerBody)

            val expect = ResultWrapper.Success(Unit)

            assertThat(actual).isEqualTo(expect)
        }

    @Test
    fun `test logout`() =
        runTest {
            val actual = authDataSource.logout()

            val expect = ResultWrapper.Success(Unit)

            assertThat(actual).isEqualTo(expect)
        }
}

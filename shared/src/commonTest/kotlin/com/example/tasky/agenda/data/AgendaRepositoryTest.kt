package com.example.tasky.agenda.data

import assertk.assertThat
import assertk.assertions.containsExactlyInAnyOrder
import assertk.assertions.extracting
import assertk.assertions.isEmpty
import com.example.tasky.agenda.data.mapper.toRemoteReminder
import com.example.tasky.agenda.data.mapper.toRemoteTask
import com.example.tasky.agenda.data.model.GetAgendaResponse
import com.example.tasky.agenda.domain.model.Event
import com.example.tasky.agenda.domain.model.Reminder
import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.alarm.data.AlarmLocalDataSource
import com.example.tasky.alarm.data.AlarmRepository
import com.example.tasky.alarm.domain.mapper.toNotificationData
import com.example.tasky.alarm.mock.AlarmSchedulerMock
import com.example.tasky.auth.mock.SessionManagerMock
import com.example.tasky.commom.mock.HttpManagerMock
import com.example.tasky.commom.mock.NetworkManagerMock
import com.example.tasky.database.mapper.toEventEntity
import com.example.tasky.database.mapper.toReminderEntity
import com.example.tasky.database.mapper.toTaskEntity
import com.example.tasky.database.mock.AppDatabaseMock
import com.example.tasky.database.mock.dao.AgendaAlarmDaoMock
import com.example.tasky.database.mock.dao.EventDaoMock
import com.example.tasky.database.mock.dao.OfflineHistoryDaoMock
import com.example.tasky.database.mock.dao.ReminderDaoMock
import com.example.tasky.database.mock.dao.TaskDaoMock
import com.example.tasky.database.model.AgendaAlarmEntity
import com.example.tasky.database.model.ApiType
import com.example.tasky.database.model.OfflineHistoryEntity
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.BeforeTest
import kotlin.test.Test

class AgendaRepositoryTest {
    private lateinit var agendaRepository: AgendaRepository
    private lateinit var appDatabase: AppDatabaseMock
    private lateinit var alarmScheduler: AlarmSchedulerMock

    @BeforeTest
    fun setUp() {
        appDatabase =
            AppDatabaseMock(
                reminderDao = ReminderDaoMock(listOf(Reminder.EMPTY.toReminderEntity()).toMutableList()),
                taskDao = TaskDaoMock(listOf(Task.EMPTY.toTaskEntity()).toMutableList()),
                eventDao = EventDaoMock(listOf(Event.EMPTY.toEventEntity()).toMutableList()),
                offlineHistoryDao =
                    OfflineHistoryDaoMock(
                        listOf(OfflineHistoryEntity(apiType = ApiType.DELETE_TASK, params = "", body = "", userId = "")).toMutableList(),
                    ),
                agendaAlarmDao =
                    AgendaAlarmDaoMock(
                        listOf(
                            AgendaAlarmEntity(
                                agendaId = "",
                                requestCode = 0,
                            ),
                        ).toMutableList(),
                    ),
            )
        alarmScheduler = AlarmSchedulerMock()
        val alarmRepository = AlarmRepository(AlarmLocalDataSource(appDatabase))
        val sessionManager = SessionManagerMock

        val getAgendaResponseJson =
            Json.encodeToString(
                GetAgendaResponse(
                    events = emptyList(),
                    tasks = listOf(Task.DUMMY, Task.EMPTY).map { it.toRemoteTask() },
                    reminders = listOf(Reminder.DUMMY).map { it.toRemoteReminder() },
                ),
            )

        val mockEngine =
            MockEngine { request ->
                if (request.url.encodedPath.contains("fullAgenda")) {
                    respond(
                        content =
                            ByteReadChannel(getAgendaResponseJson),
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
        val agendaDataSource = AgendaDataSource(HttpManagerMock(mockEngine).httpClient)
        val agendaLocalDataSource =
            AgendaLocalDataSource(
                appDatabase,
            )
        val networkManager = NetworkManagerMock()
        agendaRepository =
            AgendaRepository(
                alarmScheduler = alarmScheduler,
                alarmRepository = alarmRepository,
                sessionManager = sessionManager,
                agendaDataSource = agendaDataSource,
                agendaLocalDataSource = agendaLocalDataSource,
                networkManager = networkManager,
            )
    }

    @Test
    fun `test syncAgenda`() =
        runTest {
            agendaRepository.syncAgenda()

            assertThat(
                alarmScheduler.alarms,
            ).containsExactlyInAnyOrder(
                Task.DUMMY.toNotificationData(),
                Task.EMPTY.toNotificationData(),
                Reminder.DUMMY.toNotificationData(),
            )
            assertThat(appDatabase.agendaAlarmDao.entities).extracting(AgendaAlarmEntity::agendaId).containsExactlyInAnyOrder(
                Task.DUMMY.id,
                Task.EMPTY.id,
                Reminder.DUMMY.id,
            )
            assertThat(appDatabase.taskDao.entities).containsExactlyInAnyOrder(
                Task.DUMMY.toTaskEntity(),
                Task.EMPTY.toTaskEntity(),
            )
            assertThat(appDatabase.reminderDao.entities).containsExactlyInAnyOrder(
                Reminder.DUMMY.toReminderEntity(),
            )
            assertThat(appDatabase.eventDao.entities).isEmpty()
            assertThat(appDatabase.offlineHistoryDao.entities).isEmpty()
        }
}

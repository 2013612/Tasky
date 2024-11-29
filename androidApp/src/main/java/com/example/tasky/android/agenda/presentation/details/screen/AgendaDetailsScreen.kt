package com.example.tasky.android.agenda.presentation.details.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.example.tasky.agenda.domain.model.AgendaType
import com.example.tasky.agenda.domain.model.Event
import com.example.tasky.agenda.domain.model.Photo
import com.example.tasky.agenda.domain.model.Reminder
import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.android.AGENDA_DETAILS_DEEPLINK
import com.example.tasky.android.R
import com.example.tasky.android.agenda.presentation.details.component.DetailsAttendeeSection
import com.example.tasky.android.agenda.presentation.details.component.DetailsDateTimeSection
import com.example.tasky.android.agenda.presentation.details.component.DetailsDeleteSection
import com.example.tasky.android.agenda.presentation.details.component.DetailsDescSection
import com.example.tasky.android.agenda.presentation.details.component.DetailsEditText
import com.example.tasky.android.agenda.presentation.details.component.DetailsEditTextType
import com.example.tasky.android.agenda.presentation.details.component.DetailsHeaderSection
import com.example.tasky.android.agenda.presentation.details.component.DetailsLargePhoto
import com.example.tasky.android.agenda.presentation.details.component.DetailsPhoto
import com.example.tasky.android.agenda.presentation.details.component.DetailsPhotoSection
import com.example.tasky.android.agenda.presentation.details.component.DetailsRemindAtSection
import com.example.tasky.android.agenda.presentation.details.component.DetailsTitleSection
import com.example.tasky.android.agenda.presentation.details.component.DetailsTopBar
import com.example.tasky.android.agenda.presentation.details.model.AgendaDetailsScreenEvent
import com.example.tasky.android.agenda.presentation.details.model.AgendaDetailsScreenState
import com.example.tasky.android.common.presentation.utils.ObserveAsEvents
import com.example.tasky.android.common.presentation.utils.showToast
import com.example.tasky.android.theme.Black
import com.example.tasky.android.theme.Light
import com.example.tasky.android.theme.MyApplicationTheme
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import java.time.format.DateTimeFormatter

fun NavGraphBuilder.agendaDetailsScreen(navigateUp: () -> Unit) {
    composable<AgendaDetails>(
        deepLinks = listOf(navDeepLink<AgendaDetails>(basePath = AGENDA_DETAILS_DEEPLINK)),
    ) {
        val viewModel: AgendaDetailsViewModel = koinViewModel()

        val screenState by viewModel.screenStateFlow.collectAsStateWithLifecycle()

        val context = LocalContext.current

        ObserveAsEvents(viewModel.eventsFlow) { event ->
            when (event) {
                AgendaDetailsOneTimeEvent.OnDeleteSuccess -> navigateUp()
                is AgendaDetailsOneTimeEvent.OnPhotoSkipped -> {
                    val text = context.getString(R.string.skip_photo_toast, event.skippedPhotoCount)
                    context.showToast(text)
                }

                is AgendaDetailsOneTimeEvent.OnAddAttendee -> {
                    val text = context.getString(R.string.add_attendee_toast, event.name)
                    context.showToast(text)
                }

                is AgendaDetailsOneTimeEvent.OnAddAttendeeFail -> {
                    val text =
                        context.getString(
                            R.string.add_attendee_fail_toast,
                            event.email,
                            event.error,
                        )
                    context.showToast(text)
                }

                is AgendaDetailsOneTimeEvent.OnAddDuplicateAttendee -> {
                    val text = context.getString(R.string.add_duplicate_attendee_toast, event.name)
                    context.showToast(text)
                }
            }
        }

        AgendaDetailsScreen(
            state = screenState,
            onEvent = { event ->
                when (event) {
                    AgendaDetailsScreenEvent.OnCloseClick -> navigateUp()
                    else -> viewModel.onEvent(event)
                }
            },
        )
    }
}

fun NavController.navigateToAgendaDetails(
    agendaId: String,
    type: AgendaType,
    isEdit: Boolean = false,
) {
    navigate(AgendaDetails(agendaId = agendaId, type = type, isEdit = isEdit))
}

@Serializable
data class AgendaDetails(
    val agendaId: String,
    val type: AgendaType,
    val isEdit: Boolean,
)

@Composable
private fun AgendaDetailsScreen(
    state: AgendaDetailsScreenState,
    onEvent: (AgendaDetailsScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(Black),
    ) {
        Column {
            Spacer(modifier = Modifier.height(8.dp))
            DetailsTopBar(
                title =
                    if (state.isEdit) {
                        stringResource(
                            when (state.agendaItem) {
                                is Task -> R.string.edit_task
                                is Event -> R.string.edit_event
                                is Reminder -> R.string.edit_reminder
                            },
                        )
                    } else {
                        getTitleTimeDisplay(state.agendaItem.getStartTime())
                    },
                isEdit = state.isEdit,
                onCloseClick = {
                    onEvent(AgendaDetailsScreenEvent.OnCloseClick)
                },
                onEditClick = {
                    onEvent(AgendaDetailsScreenEvent.OnEditClick)
                },
                onSaveClick = {
                    onEvent(AgendaDetailsScreenEvent.OnSaveClick)
                },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(
                            Color.White,
                            RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                        ).padding(top = 30.dp)
                        .verticalScroll(rememberScrollState()),
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    DetailsHeaderSection(item = state.agendaItem)
                    Spacer(Modifier.height(30.dp))

                    DetailsTitleSection(
                        title = state.agendaItem.title,
                        isEdit = state.isEdit && state.isCreator,
                        modifier =
                            Modifier.clickable(enabled = state.isEdit && state.isCreator) {
                                onEvent(AgendaDetailsScreenEvent.OnTitleClick)
                            },
                    )

                    Spacer(Modifier.height(16.dp))
                    HorizontalDivider(color = Light)
                    Spacer(Modifier.height(16.dp))

                    DetailsDescSection(
                        desc = state.agendaItem.description,
                        isEdit = state.isEdit && state.isCreator,
                        modifier =
                            Modifier.clickable(enabled = state.isEdit && state.isCreator) {
                                onEvent(AgendaDetailsScreenEvent.OnDescClick)
                            },
                    )
                }

                if (state.agendaItem is Event && (state.isEdit || state.photos.isNotEmpty())) {
                    Spacer(Modifier.height(16.dp))
                    DetailsPhotoSection(
                        photos = state.photos.toImmutableList(),
                        showAddButton = state.isEdit && state.isCreator && state.photos.size < 10 && state.hasNetwork,
                        onAddPhoto = {
                            onEvent(AgendaDetailsScreenEvent.OnAddPhoto(it))
                        },
                        onPhotoClick = {
                            onEvent(AgendaDetailsScreenEvent.OnPhotoClick(it))
                        },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 110.dp),
                    )
                }

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Spacer(Modifier.height(16.dp))
                    HorizontalDivider(color = Light)
                    Spacer(Modifier.height(16.dp))

                    DetailsDateTimeSection(
                        item = state.agendaItem,
                        isEdit = state.isEdit && state.isCreator,
                        onDateSelect = {
                            onEvent(AgendaDetailsScreenEvent.OnStartDateChange(it))
                        },
                        onTimeSelect = { hour, minute ->
                            onEvent(
                                AgendaDetailsScreenEvent.OnStartTimeChange(
                                    newHour = hour,
                                    newMinute = minute,
                                ),
                            )
                        },
                    )

                    if (state.agendaItem is Event) {
                        DetailsDateTimeSection(
                            item = state.agendaItem,
                            isEdit = state.isEdit && state.isCreator,
                            onDateSelect = {
                                onEvent(AgendaDetailsScreenEvent.OnEndDateChange(it))
                            },
                            onTimeSelect = { hour, minute ->
                                onEvent(
                                    AgendaDetailsScreenEvent.OnEndTimeChange(
                                        newHour = hour,
                                        newMinute = minute,
                                    ),
                                )
                            },
                            isEventEndSection = true,
                        )
                    }

                    Spacer(Modifier.height(16.dp))
                    HorizontalDivider(color = Light)
                    Spacer(Modifier.height(16.dp))

                    DetailsRemindAtSection(
                        item = state.agendaItem,
                        isEdit = state.isEdit,
                        onRemindAtSelect = {
                            onEvent(
                                AgendaDetailsScreenEvent.OnRemindAtChange(
                                    it,
                                ),
                            )
                        },
                    )

                    Spacer(Modifier.height(16.dp))
                    HorizontalDivider(color = Light)

                    if (state.agendaItem is Event) {
                        Spacer(Modifier.height(24.dp))
                        DetailsAttendeeSection(
                            attendees = state.agendaItem.attendees.toImmutableList(),
                            curTab = state.curTab,
                            isEdit = state.isEdit && state.isCreator && state.hasNetwork,
                            onTabSelect = {
                                onEvent(AgendaDetailsScreenEvent.OnAttendeeTabChange(it))
                            },
                            onAddClick = {
                                onEvent(AgendaDetailsScreenEvent.OnAttendeeAdd(it))
                            },
                            onDeleteIconClick = {
                                onEvent(AgendaDetailsScreenEvent.OnAttendeeDelete(it))
                            },
                            creatorId = state.agendaItem.host,
                        )
                    }

                    Spacer(Modifier.weight(1f))

                    HorizontalDivider(color = Light)
                    Spacer(Modifier.height(16.dp))
                    DetailsDeleteSection(
                        item = state.agendaItem,
                        onClick = {
                            onEvent(AgendaDetailsScreenEvent.OnBottomTextClick)
                        },
                        eventIsGoing = state.eventIsGoing,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                    )

                    Spacer(Modifier.height(32.dp))
                }
            }
        }

        if (state.detailsEditTextType != null) {
            DetailsEditText(
                initialValue =
                    when (state.detailsEditTextType) {
                        DetailsEditTextType.TITLE -> state.agendaItem.title
                        DetailsEditTextType.DESCRIPTION -> state.agendaItem.description
                    },
                type = state.detailsEditTextType,
                onBackClick = {
                    onEvent(AgendaDetailsScreenEvent.CloseEditText)
                },
                onSaveClick = {
                    when (state.detailsEditTextType) {
                        DetailsEditTextType.TITLE ->
                            onEvent(
                                AgendaDetailsScreenEvent.OnTitleChange(
                                    it,
                                ),
                            )

                        DetailsEditTextType.DESCRIPTION ->
                            onEvent(
                                AgendaDetailsScreenEvent.OnDescChange(
                                    it,
                                ),
                            )
                    }
                    onEvent(AgendaDetailsScreenEvent.CloseEditText)
                },
                modifier = Modifier.fillMaxSize(),
            )
        }

        if (state.enlargedPhoto != null) {
            DetailsLargePhoto(
                photo = state.enlargedPhoto,
                isEdit = state.isEdit && state.isCreator && state.hasNetwork,
                onCloseClick = {
                    onEvent(AgendaDetailsScreenEvent.CloseLargePhoto)
                },
                onDeleteClick = {
                    onEvent(
                        AgendaDetailsScreenEvent.OnPhotoDelete(
                            when (state.enlargedPhoto) {
                                is DetailsPhoto.RemotePhoto -> state.enlargedPhoto.photo.key
                                is DetailsPhoto.LocalPhoto -> state.enlargedPhoto.key
                            },
                        ),
                    )
                },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

private fun getTitleTimeDisplay(time: Long): String {
    val dateTimeFormat = DateTimeFormatter.ofPattern("dd MMM yyyy")

    return Instant
        .fromEpochMilliseconds(time)
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .toJavaLocalDateTime()
        .format(dateTimeFormat)
}

@Preview
@Composable
private fun AgendaDetailsScreenPreview() {
    MyApplicationTheme {
        AgendaDetailsScreen(state = AgendaDetailsScreenState(Task.DUMMY, false), onEvent = {})
    }
}

@Preview
@Composable
private fun AgendaDetailsScreenEditPreview() {
    MyApplicationTheme {
        AgendaDetailsScreen(state = AgendaDetailsScreenState(Task.DUMMY, true), onEvent = {})
    }
}

@Preview
@Composable
private fun AgendaDetailsScreenEditTextPreview() {
    MyApplicationTheme {
        AgendaDetailsScreen(
            state =
                AgendaDetailsScreenState(
                    Task.DUMMY,
                    true,
                    DetailsEditTextType.TITLE,
                ),
            onEvent = {},
        )
    }
}

@Preview
@Composable
private fun AgendaDetailsScreenEventEditPreview() {
    MyApplicationTheme {
        AgendaDetailsScreen(state = AgendaDetailsScreenState(Event.DUMMY, true), onEvent = {})
    }
}

@Preview
@Composable
private fun AgendaDetailsScreenLargePhotoPreview() {
    MyApplicationTheme {
        AgendaDetailsScreen(
            state =
                AgendaDetailsScreenState(
                    Event.DUMMY,
                    true,
                    enlargedPhoto = DetailsPhoto.RemotePhoto(Photo.DUMMY_LIST.first()),
                ),
            onEvent = {},
        )
    }
}

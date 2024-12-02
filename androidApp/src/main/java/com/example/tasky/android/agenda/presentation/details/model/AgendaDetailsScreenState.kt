package com.example.tasky.android.agenda.presentation.details.model

import com.example.tasky.agenda.domain.model.AgendaItem
import com.example.tasky.android.agenda.presentation.details.component.DetailsAttendeeSectionTabOption
import com.example.tasky.android.agenda.presentation.details.component.DetailsEditTextType
import com.example.tasky.android.agenda.presentation.details.component.DetailsPhoto

data class AgendaDetailsScreenState(
    val agendaItem: AgendaItem,
    val isEdit: Boolean = false,
    val detailsEditTextType: DetailsEditTextType? = null,
    val eventIsGoing: Boolean = true,
    val curTab: DetailsAttendeeSectionTabOption = DetailsAttendeeSectionTabOption.ALL,
    val isCreator: Boolean = true,
    val enlargedPhoto: DetailsPhoto? = null,
    val photos: List<DetailsPhoto> = emptyList(),
    val hasNetwork: Boolean = false,
    val showUnsavedDialog: Boolean = false,
)

package com.nilay.evenger.utils

const val MAX_STACK_SIZE = 30
const val REQUEST_ADD_SUBJECT_FROM_SYLLABUS = 345 * 453
const val ERROR_IN_UPDATE = 452 * 53
const val REQUEST_EDIT_SUBJECT_FROM_LIST_ALL = 674 * 453
const val REQUEST_MENU_FROM_ARCHIVE = 345 * 453 + 1
const val REQUEST_EDIT_SUBJECT_FROM_SYLLABUS = 347 * 453

const val UPDATE_REQUEST = "Update"

enum class SharePrefKeys {
    SharedPreferenceName, ChooseSemLastSelectedSem, KeyToggleSyllabusSource, SyllabusVisibility,
    UserHasDataInCloud, RestoreDone, PermanentSkipLogin, SetUpDone, AppTheme
}

enum class AppTheme { Light, Dark, Sys }

const val BASE_URL = "https://logicalnil.github.io/Evenger_Android/"

const val CHANNEL_ID = "Evenger"
const val CHANNEL_NAME = "Evenger Notification"
const val CHANNEL_DESCRIPTION = "Evenger Notification Channel"
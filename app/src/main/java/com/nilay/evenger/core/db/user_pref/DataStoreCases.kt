package com.nilay.evenger.core.db.user_pref

import javax.inject.Inject

data class DataStoreCases @Inject constructor(
    val getAll: GetAllPref,
    val updatePercentage: UpdatePercentage,
)


class GetAllPref @Inject constructor(
    private val preferencesManager: PreferencesManager
) {
    operator fun invoke() = preferencesManager.preferenceFlow
}

class UpdatePercentage @Inject constructor(
    private val preferencesManager: PreferencesManager
) {
    suspend operator fun invoke(defPercentage: Int) =
        preferencesManager.updatePercentage(defPercentage)
}
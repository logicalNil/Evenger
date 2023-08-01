package com.nilay.evenger.core.firebase.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Suppress("")
@Parcelize
data class UserModel(
    var uid: String? = null,
    var name: String? = null,
    var email: String? = null,
    var profilePic: String? = null,
    var syncTime: Long? = System.currentTimeMillis()
) : Parcelable
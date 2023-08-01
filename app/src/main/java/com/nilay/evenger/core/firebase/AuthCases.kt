package com.nilay.evenger.core.firebase

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.nilay.evenger.core.db.attendance.AttendanceDao
import com.nilay.evenger.core.firebase.model.UserModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

data class AuthCases @Inject constructor(
    val login: Login,
    val hasLogIn: HasLogIn,
    val getUid: GetUid,
)


class HasLogIn @Inject constructor(
    private val auth: FirebaseAuth
) {
    operator fun invoke(): Boolean = auth.currentUser != null
}

class GetUid @Inject constructor(
    private val auth: FirebaseAuth
) {
    operator fun invoke(): String? = auth.currentUser?.uid
}

class Login @Inject constructor(
    private val auth: FirebaseAuth,
    @ApplicationContext private val context: Context,
    private val firebaseCases: FireStoreCases,
    private val attendanceDao: AttendanceDao,
) {
    operator fun invoke(token: String, callback: (Pair<Boolean?, Exception?>) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(token, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                user?.let {
                    val userId = user.uid
                    val userName = user.displayName
                    val userEmail = user.email
                    val userPhoto = user.photoUrl
                    val userModel = UserModel(
                        userId, userName, userEmail, userPhoto.toString()
                    )
                    addUserToDatabase(userModel, callback)
                }
            } else callback(null to task.exception)
        }
    }

    private fun addUserToDatabase(
        userModel: UserModel, callback: (Pair<Boolean?, Exception?>) -> Unit
    ) {
        firebaseCases.addUser.invoke(userModel) { (uid, error) ->
            if (error != null) callback(null to error)
            else firebaseCases.checkUserData.invoke(uid) { (hasData, exception) ->
                if (exception != null) callback(null to exception)
                else callback(hasData to null)
            }
        }
    }
}


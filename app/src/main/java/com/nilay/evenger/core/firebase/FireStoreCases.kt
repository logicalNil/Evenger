package com.nilay.evenger.core.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.nilay.evenger.core.firebase.model.UserModel
import javax.inject.Inject


enum class Db(val value: String) {
 User("Evenger_Users"), Data("data")
}

data class FireStoreCases @Inject constructor(
    val addUser: AddUser,
    val checkUserData: CheckUserData,
)

class AddUser @Inject constructor(
    private val db: FirebaseFirestore
) {
    operator fun invoke(
        user: UserModel, callback: (Pair<String, Exception?>) -> Unit
    ) {
        val ref = db.collection(Db.User.value)
        ref.document(user.uid!!).set(user).addOnSuccessListener {
            callback(Pair(user.uid!!, null))
        }.addOnFailureListener { exception ->
            callback(Pair("", exception))
        }
    }
}

class CheckUserData @Inject constructor(
    private val db: FirebaseFirestore
) {
    operator fun invoke(
        uid: String, callback: (Pair<Boolean?, Exception?>) -> Unit
    ) {
        db.collection(Db.User.value).document(uid).collection(Db.Data.value).document(uid).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val s = document.getString("courseSem")
                    if (s != null) callback(true to null)
                    else callback(false to null)
                } else {
                    callback(false to null)
                }
            }.addOnFailureListener { exception ->
                callback(null to exception)
            }
    }
}
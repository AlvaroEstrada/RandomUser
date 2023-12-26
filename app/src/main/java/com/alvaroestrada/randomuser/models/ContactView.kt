package com.alvaroestrada.randomuser.models

import android.os.Parcelable
import com.alvaroestrada.domain.interfaces.view.IContactView
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContactView(
    override val uuid: String,
    override val username: String,
    override val firstName: String,
    override val lastName: String,
    override val email: String,
    override val gender: String,
    override val registrationDate: String,
    override val registrationAge: String,
    override val phoneNumber: String,
    override val largePicUrl: String,
    override val mediumPicUrl: String,
    override val thumbPicUrl: String,
    override val latitude: String,
    override val longitude: String
) : IContactView, Parcelable {
    val fullName: String
        get() = "$firstName $lastName"
}

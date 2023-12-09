package com.alvaroestrada.randomuser.mappers

import com.alvaroestrada.domain.interfaces.view.IContactView
import com.alvaroestrada.randomuser.models.ContactView

fun IContactView.toContactView(): ContactView{
    return ContactView(
        uuid = this.uuid,
        username = this.username,
        firstName = this.firstName,
        lastName = this.lastName,
        email = this.email,
        gender = this.gender,
        registrationDate = this.registrationDate,
        registrationAge = this.registrationAge,
        phoneNumber = this.phoneNumber,
        largePicUrl = this.largePicUrl,
        mediumPicUrl = this.mediumPicUrl,
        thumbPicUrl = this.thumbPicUrl,
        latitude = this.latitude,
        longitude = this.longitude
    )
}
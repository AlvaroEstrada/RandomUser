package com.alvaroestrada.service.implementations

import com.alvaroestrada.domain.interfaces.remote.IContactRemote
import com.alvaroestrada.domain.interfaces.view.IContactView
import com.alvaroestrada.domain.mappers.ContactMapper

class ContactMapperImpl  : ContactMapper {
    override fun fromRemoteToView(contactRemote: IContactRemote): IContactView {
        return object : IContactView {
            override val uuid = contactRemote.login.uuid
            override val username = contactRemote.login.username
            override val firstName = contactRemote.name.firstName
            override val lastName = contactRemote.name.lastName
            override val email = contactRemote.email
            override val gender = contactRemote.gender
            override val registrationDate = contactRemote.registered.date
            override val registrationAge = contactRemote.registered.age
            override val phoneNumber = contactRemote.phone
            override val largePicUrl = contactRemote.picture.largePic
            override val mediumPicUrl = contactRemote.picture.mediumPic
            override val thumbPicUrl = contactRemote.picture.thumbPic
            override val latitude = contactRemote.location.coordinates.latitude
            override val longitude = contactRemote.location.coordinates.longitude
        }
    }
}
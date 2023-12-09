package com.alvaroestrada.domain.interfaces.view

interface IContactView {
    val uuid: String
    val username: String
    val firstName: String
    val lastName: String
    val email: String
    val gender: String
    val registrationDate: String
    val registrationAge: String
    val phoneNumber: String
    val largePicUrl: String
    val mediumPicUrl: String
    val thumbPicUrl: String
    val latitude: String
    val longitude: String
}
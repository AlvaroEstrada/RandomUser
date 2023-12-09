package com.alvaroestrada.domain.interfaces.remote

interface IContactRemote {
    val login: ILogin
    val name: IName
    val email: String
    val gender: String
    val registered: IRegistered
    val phone: String
    val picture: IPicture
    val location: ILocation
}

interface ILogin {
    val uuid: String
    val username: String
    val md5: String
}

interface IName {
    val firstName: String
    val lastName: String
}

interface IRegistered {
    val date: String
    val age: String
}

interface IPicture {
    val largePic: String
    val mediumPic: String
    val thumbPic: String
}

interface ILocation {
    val coordinates: ICoordinates
}

interface ICoordinates {
    val latitude: String
    val longitude: String
}
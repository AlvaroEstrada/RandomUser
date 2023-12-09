package com.alvaroestrada.data.models

import com.alvaroestrada.domain.interfaces.remote.IContactRemote
import com.alvaroestrada.domain.interfaces.remote.ICoordinates
import com.alvaroestrada.domain.interfaces.remote.ILocation
import com.alvaroestrada.domain.interfaces.remote.ILogin
import com.alvaroestrada.domain.interfaces.remote.IName
import com.alvaroestrada.domain.interfaces.remote.IPicture
import com.alvaroestrada.domain.interfaces.remote.IRegistered
import com.squareup.moshi.Json

data class ContactRemote (
    @Json(name = "login") override val login: Login,
    @Json(name = "name") override val name: Name,
    @Json(name = "email") override val email: String,
    @Json(name = "gender") override val gender: String,
    @Json(name = "registered") override val registered: Registered,
    @Json(name = "phone") override val phone: String,
    @Json(name = "picture") override val picture: Picture,
    @Json(name = "location") override val location: Location,
): IContactRemote

data class Login(
    @Json(name = "uuid") override val uuid: String,
    @Json(name = "username") override val username: String,
    @Json(name = "md5") override val md5: String,
): ILogin

data class Name(
    @Json(name = "first") override val firstName: String,
    @Json(name = "last") override val lastName: String
): IName

data class Registered(
    @Json(name = "date") override val date: String,
    @Json(name = "age") override val age: String
): IRegistered

data class Picture(
    @Json(name = "large") override val largePic: String,
    @Json(name = "medium") override val mediumPic: String,
    @Json(name = "thumbnail") override val thumbPic: String,
): IPicture

data class Location(
    @Json(name = "coordinates") override val coordinates: Coordinates,
): ILocation

data class Coordinates(
    @Json(name = "latitude") override val latitude: String,
    @Json(name = "longitude") override val longitude: String
): ICoordinates
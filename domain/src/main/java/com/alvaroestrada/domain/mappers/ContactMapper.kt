package com.alvaroestrada.domain.mappers

import com.alvaroestrada.domain.interfaces.remote.IContactRemote
import com.alvaroestrada.domain.interfaces.view.IContactView

interface ContactMapper {
    fun fromRemoteToView(contactRemote: IContactRemote): IContactView
}
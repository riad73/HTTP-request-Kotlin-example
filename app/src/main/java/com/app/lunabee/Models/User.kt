package com.app.lunabee.Models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(val id : Int = 0,
                val first_name: String ?= null,
                val last_name: String ?= null,
                val email: String? = null,
                val gender: String? = null,
                val avatar: String? = null): Parcelable
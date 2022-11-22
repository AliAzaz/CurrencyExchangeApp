package com.currencyexchange.app.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.currencyexchange.app.utils.CONSTANTS
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = CONSTANTS.CURRENCIES_TABLE)
data class CurrencyModel(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val code: String,
    val country: String
) : Parcelable
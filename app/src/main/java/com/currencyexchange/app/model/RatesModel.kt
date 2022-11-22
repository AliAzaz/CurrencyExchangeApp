package com.currencyexchange.app.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.currencyexchange.app.utils.CONSTANTS
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = CONSTANTS.EXCHANGE_RATES_TABLE)
data class RatesModel(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @Expose val code: String,
    @Expose val amount: Double
) : Parcelable
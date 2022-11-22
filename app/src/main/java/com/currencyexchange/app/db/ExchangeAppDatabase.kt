package com.currencyexchange.app.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.currencyexchange.app.model.CurrencyModel
import com.currencyexchange.app.model.RatesModel
import com.currencyexchange.app.utils.CONSTANTS.DATABASE_NAME
import com.currencyexchange.app.utils.CONSTANTS.DATABASE_VERSION

@Database(
    entities = [RatesModel::class, CurrencyModel::class],
    version = DATABASE_VERSION,
    exportSchema = false
)
abstract class ExchangeAppDatabase : RoomDatabase() {

    abstract fun exchangeDao(): ExchangeDAO

    companion object {
        @Volatile
        private var INSTANCE: ExchangeAppDatabase? = null

        private val LOCK = Any()

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ExchangeAppDatabase::class.java,
                DATABASE_NAME
            ).fallbackToDestructiveMigration().build()

        operator fun invoke(context: Context) = INSTANCE ?: synchronized(LOCK) {
            INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
        }

    }
}
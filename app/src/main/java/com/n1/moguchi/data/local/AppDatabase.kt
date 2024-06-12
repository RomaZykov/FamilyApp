package com.n1.moguchi.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

interface AppDataBase {
    fun
}
@Database(entities = [ShopItemDbModel::class], version = 1, exportSchema = false)
class AppDatabase : RoomDatabase() {
    abstract fun shopListDao(): ShopListDao

    companion object {
        private var INSTANCE: ShopItemRoomDatabase? = null
        private val LOCK = Any()
        private const val DB_NAME = "shop_item.db"

        fun getDatabase(application: Application): ShopItemRoomDatabase {
            INSTANCE?.let {
                return it
            }

            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
            }
            val instance = Room.databaseBuilder(
                application.applicationContext,
                ShopItemRoomDatabase::class.java,
                DB_NAME
            )
                .build()
            INSTANCE = instance
            return instance
        }
    }
}
}
package fr.isen.vincenti.isensmartcompanion.db

import android.content.Context
import androidx.room.Room

object DBInstance {
    @Volatile
    private var INSTANCE: ChatDatabase? = null
    private var chatDao: ChatDao? = null

    fun getDatabase(context: Context): ChatDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                ChatDatabase::class.java,
                "IsenSC-db"
            ).build()
            INSTANCE = instance
            chatDao = instance.ChatDao()
            instance
        }
    }

    fun getChatDao(context: Context): ChatDao {
        return chatDao ?: getDatabase(context).ChatDao()
    }
}

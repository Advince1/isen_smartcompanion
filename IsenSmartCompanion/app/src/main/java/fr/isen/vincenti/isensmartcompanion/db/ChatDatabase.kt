package fr.isen.vincenti.isensmartcompanion.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ChatMessage::class], version = 1)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun ChatDao(): ChatDao
}
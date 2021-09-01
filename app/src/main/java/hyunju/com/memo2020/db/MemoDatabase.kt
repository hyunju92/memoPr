package hyunju.com.memo2020.db

import android.content.Context
import androidx.room.*
import java.util.*

@Database(entities = [Memo::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MemoDatabase : RoomDatabase() {
    abstract fun memoDao() : MemoDao

    companion object {
        private val DB_NAME = "memoDb"
        private var INSTANCE : MemoDatabase? = null

        @Synchronized
        fun get(context: Context): MemoDatabase {
            if (INSTANCE == null) {
                INSTANCE = buildDatabase(context)
            }
            return INSTANCE!!
        }

        private fun buildDatabase(context: Context): MemoDatabase {
            return Room.databaseBuilder(context.applicationContext,
                MemoDatabase::class.java, DB_NAME).build()
        }


    }
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun fromString(stringListString: String): List<String> {
        return stringListString.split(",").map { it }
    }

    @TypeConverter
    fun toString(stringList: List<String>): String {
        return stringList.joinToString(separator = ",")
    }
}



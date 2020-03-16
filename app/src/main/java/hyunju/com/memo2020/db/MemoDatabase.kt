package hyunju.com.memo2020.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import hyunju.com.memo2020.R
import hyunju.com.memo2020.model.Converters
import hyunju.com.memo2020.model.Memo


@Database(entities = [Memo::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MemoDatabase : RoomDatabase() {
    abstract fun memoDao() : MemoDao

    companion object {
        private var INSTANCE : MemoDatabase? = null

        @Synchronized
        fun get(context: Context): MemoDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                        MemoDatabase::class.java, context.getString(R.string.db_file_name)).build()
            }
            return INSTANCE!!
        }


    }
}
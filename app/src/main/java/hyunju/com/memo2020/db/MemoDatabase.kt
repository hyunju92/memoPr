package hyunju.com.memo2020.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
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
                        MemoDatabase::class.java, "memoDb").build()
            }
            return INSTANCE!!
        }


//        fun getInstance(context: Context) :MemoDatabase {
//            return instance ?: synchronized(MemoDatabase::class) {
//                instance ?: Room.databaseBuilder(context.applicationContext,
//                        MemoDatabase::class.java, "memo.db").build().also { instance = it }
//            }
//        }
//
//        fun destroyInstance() {
//            instance = null
//        }

//        fun getInstance(context: Context): MemoDatabase =
//                INSTANCE ?: synchronized(this) {
//                    INSTANCE
//                            ?: buildDatabase(context).also { INSTANCE = it }
//                }
//
//        private fun buildDatabase(context: Context) =
//                Room.databaseBuilder(context.applicationContext,
//                        MemoDatabase::class.java, "Github.db")
//                        .build()



    }
}
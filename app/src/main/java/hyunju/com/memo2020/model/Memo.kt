package hyunju.com.memo2020.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.*


@Entity
data class Memo(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        var title: String,
        var contents: String,
        var images: String,
        var date: Date
        )

class Converters {
        @TypeConverter
        fun fromTimestamp(value : Long) : Date {
                return Date(value)
        }

        @TypeConverter
        fun dateToTimestamp(date: Date) : Long{
                return date.time
        }
}




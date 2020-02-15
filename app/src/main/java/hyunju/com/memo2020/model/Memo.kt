package hyunju.com.memo2020.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.*


@Entity
data class Memo(
        @PrimaryKey(autoGenerate = true) val id: Int,
        val title: String,
        val contents: String,
        val images: String,
        val date: Date
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




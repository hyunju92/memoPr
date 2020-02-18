package hyunju.com.memo2020.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.util.*


@Entity
data class Memo(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        var title: String,
        var contents: String,
        var images: List<ImageData>,       // image uri
        var date: Date
)

@Entity
data class ImageData(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        var uriPath: String
)

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
    fun fromJsonStrToList(jsonStr: String): List<ImageData> {
        return GsonBuilder().create()
                .fromJson(jsonStr, Array<ImageData>::class.java).toList()

    }

    @TypeConverter
    fun fromListToJsonStr(list: List<ImageData>): String {
        val gson = GsonBuilder().create()


        val str = gson.fromJson(list, Array<String>::class.java).asList
    }
}




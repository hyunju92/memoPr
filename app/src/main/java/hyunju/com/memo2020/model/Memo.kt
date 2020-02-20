package hyunju.com.memo2020.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
@Entity
data class Memo(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        var title: String,
        var contents: String,
        var images: String,       // image uri
        var date: Date
) : Parcelable {
    fun getImageList(): ArrayList<String> {
        val arrayList = ArrayList<String>()
        arrayList.addAll(images.split(" "))

        return arrayList
    }
}

//
//@Entity(foreignKeys = arrayOf(ForeignKey(entity = Memo::class,
//        parentColumns = arrayOf("id"),
//        childColumns = arrayOf("id"),
//        onDelete = ForeignKey.CASCADE)))
//data class ImageData(
//        @PrimaryKey(autoGenerate = true) val id: Long = 0,
//        var uriPath: String
//)

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date): Long {
        return date.time
    }
}






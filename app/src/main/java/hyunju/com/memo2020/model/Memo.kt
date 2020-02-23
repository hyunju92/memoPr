package hyunju.com.memo2020.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
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
        val tempList = images.split(" ").let {
            if (it.size == 1 && it[0].isEmpty()) ArrayList() else it    // delte default value ""
        }
        val arrayList = ArrayList<String>()
        arrayList.addAll(tempList)

        return arrayList
    }

    fun setImgStr(imgList: ArrayList<String>) {
        var imgStr = ""

        for (i in 1..imgList.size) {
            val item = imgList[i - 1]

            if (item.isNotEmpty()) {
                imgStr += item
                if (i != imgList.size)
                    imgStr += " "
            }
        }

        this.images = imgStr
    }

    fun getDateText(): String {
//        return SimpleDateFormat("yyyy-MM-dd").format(date)
        return SimpleDateFormat("yyyy년 MM월 dd일 HH:mm").format(date)
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
}






package hyunju.com.memo2020.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity
data class Memo(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    var title: String,
    var contents: String,
    var imageUriList: List<String>,       // image uri
    var date: Date
) : Parcelable


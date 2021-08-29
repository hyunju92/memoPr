package hyunju.com.memo2020.util

import hyunju.com.memo2020.model.Memo
import java.text.SimpleDateFormat

fun Memo?.getDateText(): String {
    return this?.let { SimpleDateFormat("yyyy년 MM월 dd일 HH:mm").format(date) } ?: "수정중"
}




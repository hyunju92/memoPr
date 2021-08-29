package hyunju.com.memo2020.util

import hyunju.com.memo2020.model.Memo
import java.text.SimpleDateFormat

fun Memo.getDateText(): String {
//    return SimpleDateFormat(context.getString(R.string.date_format)).format(date)
    return SimpleDateFormat("yyyy년 MM월 dd일 HH:mm").format(date)
}




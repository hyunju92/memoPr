package hyunju.com.memo2020.db

import androidx.paging.DataSource
import androidx.room.*
import io.reactivex.rxjava3.core.Completable

@Dao
interface MemoDao {
    @Query("SELECT * FROM Memo ORDER BY id DESC")
    fun getAllMemoById(): DataSource.Factory<Int, Memo>

    @Query("SELECT * FROM Memo ORDER BY date DESC")
    fun getAllMemoByDate(): DataSource.Factory<Int, Memo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(memo: Memo) : Completable

    @Update
    fun update(memo: Memo) : Completable

    @Delete
    fun delete(memo: Memo) : Completable

}

package hyunju.com.memo2020.db

import androidx.paging.DataSource
import androidx.room.*
import hyunju.com.memo2020.model.Memo

@Dao
interface MemoDao {
    @Query("SELECT * FROM Memo ORDER BY id DESC")
    fun getAllMemoById(): DataSource.Factory<Int, Memo>

    @Query("SELECT * FROM Memo ORDER BY date DESC")
    fun getAllMemoByDate(): DataSource.Factory<Int, Memo>

    @Query("SELECT * FROM Memo WHERE id = :id")
    fun getMemoById(id: Long): Memo

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(memoList: List<Memo?>?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(memo: Memo)

    @Update
    fun update(memo: Memo?)

    @Delete
    fun delete(memo: Memo?)

    @Query("DELETE from Memo")
    fun deleteAll()

    @Query("DELETE FROM Memo WHERE id = :id")
    fun deleteById(id: Long)


}

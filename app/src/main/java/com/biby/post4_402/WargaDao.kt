package com.biby.post4_402

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WargaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(warga: DataWarga)

    @Update
    fun update(warga: DataWarga)

    @Delete
    fun delete(warga: DataWarga)

    @Query("SELECT * FROM datawarga")
    fun getAll(): List<DataWarga>

    @Query("DELETE FROM datawarga")
    fun deleteAll()
}

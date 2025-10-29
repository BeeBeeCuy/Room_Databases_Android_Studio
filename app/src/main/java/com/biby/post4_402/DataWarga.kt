package com.biby.post4_402

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "DataWarga")
data class DataWarga(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "nama")
    val nama: String,

    @ColumnInfo(name = "nik")
    val nik: Int,

    @ColumnInfo(name = "kabupaten")
    val kabupaten: String,

    @ColumnInfo(name = "kecamatan")
    val kecamatan: String,

    @ColumnInfo(name = "desa")
    val desa: String,

    @ColumnInfo(name = "rt")
    val rt: Int,

    @ColumnInfo(name = "rw")
    val rw: Int,

    @ColumnInfo(name = "jk")
    val jk: String,

    @ColumnInfo(name = "stnikah")
    val stnikah: String

)

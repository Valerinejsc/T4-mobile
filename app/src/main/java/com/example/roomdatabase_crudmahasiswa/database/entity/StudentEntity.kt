package com.example.roomdatabase_crudmahasiswa.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "nim")
    val nim: String,

    @ColumnInfo(name = "prodi")
    val prodi: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "semester")
    val semester: Int,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
): Parcelable

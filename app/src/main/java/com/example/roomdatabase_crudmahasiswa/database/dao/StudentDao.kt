package com.example.roomdatabase_crudmahasiswa.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.roomdatabase_crudmahasiswa.database.entity.StudentEntity

@Dao
interface StudentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(student: StudentEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(students: List<StudentEntity>)

    @Query("SELECT * FROM students ORDER BY name ASC")
    suspend fun getAllStudents(): List<StudentEntity>

    @Query("SELECT * FROM students WHERE id = :id")
    suspend fun getStudentById(id: Int): StudentEntity?

    @Query("SELECT * FROM students WHERE name LIKE :query OR nim LIKE :query")
    suspend fun searchStudents(query: String): List<StudentEntity>

    @Update
    suspend fun update(student: StudentEntity): Int

    @Query("DELETE FROM students WHERE id = :id")
    suspend fun deleteById(id: Int): Int

    @Query("SELECT COUNT(*) FROM students")
    suspend fun getStudentCount(): Int

    @Delete
    suspend fun delete(student: StudentEntity)
}
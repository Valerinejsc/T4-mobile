package com.example.roomdatabase_crudmahasiswa.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdatabase_crudmahasiswa.R
import com.example.roomdatabase_crudmahasiswa.database.AppDatabase
import com.example.roomdatabase_crudmahasiswa.database.dao.StudentDao
import com.example.roomdatabase_crudmahasiswa.database.entity.StudentEntity
import kotlinx.coroutines.launch
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var studentDao: StudentDao
    private lateinit var adapter: StudentAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = AppDatabase.getInstance(requireContext())
        studentDao = db.studentDao()

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvStudents)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        view.findViewById<FloatingActionButton>(R.id.fabAdd).setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_formFragment)
        }

        adapter = StudentAdapter(
            list = listOf(),
            onEditClick = { student ->
                val bundle = Bundle()
                bundle.putInt("student_id", student.id)

                findNavController().navigate(R.id.action_homeFragment_to_formFragment, bundle)
            },
            onDeleteClick = { student ->
                showDeleteDialog(student)
            },
            onItemClick = { student ->
                val bundle = Bundle()
                bundle.putInt("student_id", student.id)
                findNavController().navigate(R.id.action_homeFragment_to_detailFragment, bundle)
            }
        )

        recyclerView.adapter = adapter

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val student = adapter.getStudentAt(position)
                showDeleteDialog(student)
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)

        initialDataCheck()
    }

    private fun initialDataCheck() {
        lifecycleScope.launch {
            val listMahasiswa = studentDao.getAllStudents()
            if (listMahasiswa.isEmpty()) {
                val sampleData = listOf(
                    StudentEntity(name = "Ahmad Fauzi", nim = "2024001", prodi = "IT", email = "a@m.com", semester = 4),
                    StudentEntity(name = "Budi Santoso", nim = "2024002", prodi = "Sistem Informasi", email = "b@m.com", semester = 2),
                    StudentEntity(name = "Clara Wijaya", nim = "2024003", prodi = "Teknik Komputer", email = "c@m.com", semester = 6)
                )
                studentDao.insertAll(sampleData)
                adapter.updateData(studentDao.getAllStudents())
            } else {
                adapter.updateData(listMahasiswa)
            }
        }
    }

    private fun loadStudentData() {
        lifecycleScope.launch {
            val data = studentDao.getAllStudents()
            adapter.updateData(data)
        }
    }

    private fun showDeleteDialog(student: StudentEntity) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Hapus Data?")
        builder.setMessage("Hapus \"${student.name}\"? Tindakan ini tidak dapat dibatalkan.")

        builder.setPositiveButton("Hapus") { _, _ ->
            lifecycleScope.launch {
                studentDao.delete(student)
                loadStudentData()
                Toast.makeText(requireContext(), "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Batal") { dialog, _ ->
            loadStudentData()
            dialog.dismiss()
        }

        builder.setOnCancelListener { loadStudentData() }

        val dialog = builder.create()
        dialog.show()

        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
            .setTextColor(android.graphics.Color.RED)
    }
}
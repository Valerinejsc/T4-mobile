package com.example.roomdatabase_crudmahasiswa.ui

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdatabase_crudmahasiswa.R
import com.example.roomdatabase_crudmahasiswa.database.AppDatabase
import com.example.roomdatabase_crudmahasiswa.database.dao.StudentDao
import kotlinx.coroutines.launch
import androidx.navigation.fragment.findNavController
import com.example.roomdatabase_crudmahasiswa.database.entity.StudentEntity

class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var studentDao: StudentDao
    private lateinit var adapter: StudentAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = AppDatabase.getInstance(requireContext())
        studentDao = db.studentDao()

        val rvSearch = view.findViewById<RecyclerView>(R.id.rvSearchStudents)

        adapter = StudentAdapter(
            list = emptyList(),
            onEditClick = { student ->
                val bundle = Bundle().apply {
                    putInt("STUDENT_ID", student.id)
                }
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

        rvSearch.layoutManager = LinearLayoutManager(requireContext())
        rvSearch.adapter = adapter

        performSearch("")

        val etSearch = view.findViewById<EditText>(R.id.etSearch)
        etSearch.addTextChangedListener { editable ->
            performSearch(editable.toString())
        }
    }

    private fun performSearch(keyword: String) {
        lifecycleScope.launch {
            val results = if (keyword.isEmpty()) {
                studentDao.getAllStudents()
            } else {
                studentDao.searchStudents("%$keyword%")
            }
            adapter.setData(results)
        }
    }

    private fun showDeleteDialog(student: StudentEntity) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Hapus Data?")
        builder.setMessage("Hapus \"${student.name}\"? Tindakan ini tidak dapat dibatalkan.")

        builder.setPositiveButton("Hapus") { _, _ ->
            lifecycleScope.launch {
                studentDao.delete(student)

                performSearch("")

                Toast.makeText(requireContext(), "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Batal") { dialog, _ ->
            performSearch("")
            dialog.dismiss()
        }
        builder.setOnCancelListener { performSearch("") }

        val dialog = builder.create()
        dialog.show()

        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
            .setTextColor(android.graphics.Color.RED)
    }
}
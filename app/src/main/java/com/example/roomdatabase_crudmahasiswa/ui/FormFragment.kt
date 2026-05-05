package com.example.roomdatabase_crudmahasiswa.ui

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.roomdatabase_crudmahasiswa.R
import com.example.roomdatabase_crudmahasiswa.database.AppDatabase
import com.example.roomdatabase_crudmahasiswa.database.entity.StudentEntity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class FormFragment : Fragment(R.layout.fragment_form) {

    private var studentId: Int = 0
    private lateinit var db: AppDatabase

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = AppDatabase.getInstance(requireContext())

        val navBar = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar?.visibility = View.GONE

        val btnBack = view.findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        val etName = view.findViewById<EditText>(R.id.etName)
        val etNim = view.findViewById<EditText>(R.id.etNim)
        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val etSemester = view.findViewById<EditText>(R.id.etSemester)
        val spProdi = view.findViewById<Spinner>(R.id.spProdi)
        val btnSave = view.findViewById<Button>(R.id.btnSave)

        val listProdi = arrayOf("Pilih Prodi", "Teknik Informatika", "Sistem Informasi", "Teknik Elektro", "Teknik Mesin")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listProdi)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spProdi.adapter = adapter

        arguments?.let {
            studentId = it.getInt("student_id", 0)

            if (studentId != 0) {
                val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
                tvTitle.text = "Edit Mahasiswa"
                lifecycleScope.launch {
                    val s = db.studentDao().getStudentById(studentId)
                    s?.let { student ->
                        etName.setText(student.name)
                        etNim.setText(student.nim)
                        etEmail.setText(student.email)
                        etSemester.setText(student.semester.toString())

                        val spinnerPosition = adapter.getPosition(student.prodi)
                        if (spinnerPosition != -1) {
                            spProdi.setSelection(spinnerPosition)
                        }
                    }
                }
            }
        }

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val nim = etNim.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val semesterStr = etSemester.text.toString().trim()
            val prodi = spProdi.selectedItem.toString()

            if (name.isEmpty() || nim.isEmpty() || email.isEmpty() || semesterStr.isEmpty() || prodi == "Pilih Prodi") {
                Toast.makeText(requireContext(), "Harap isi semua data dan pilih Prodi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val student = StudentEntity(
                    id = studentId,
                    name = name,
                    nim = nim,
                    prodi = prodi,
                    email = email,
                    semester = semesterStr.toInt()
                )

                if (studentId == 0) {
                    db.studentDao().insert(student)
                    Toast.makeText(requireContext(), "Berhasil menambah data", Toast.LENGTH_SHORT).show()
                } else {
                    db.studentDao().update(student)
                    Toast.makeText(requireContext(), "Berhasil memperbarui data", Toast.LENGTH_SHORT).show()
                }

                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val navBar = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar?.visibility = View.VISIBLE
    }
}
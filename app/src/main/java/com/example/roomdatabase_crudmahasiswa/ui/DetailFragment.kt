package com.example.roomdatabase_crudmahasiswa.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.roomdatabase_crudmahasiswa.R
import com.example.roomdatabase_crudmahasiswa.database.AppDatabase
import kotlinx.coroutines.launch
import java.io.File

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private var studentId: Int = 0
    private lateinit var fileName: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = AppDatabase.getInstance(requireContext())
        studentId = arguments?.getInt("student_id") ?: 0
        fileName = "note_$studentId.txt"

        val tvName = view.findViewById<TextView>(R.id.tvDetailName)
        val tvNimProdi = view.findViewById<TextView>(R.id.tvDetailNimProdi)
        val tvInitial = view.findViewById<TextView>(R.id.tvDetailInitial)
        val etNote = view.findViewById<EditText>(R.id.etNote)
        val btnSave = view.findViewById<Button>(R.id.btnSaveNote)
        val btnLoad = view.findViewById<Button>(R.id.btnLoadNote)
        val tvStatus = view.findViewById<TextView>(R.id.tvStatus)
        val btnBack = view.findViewById<ImageView>(R.id.btnBack)

        lifecycleScope.launch {
            val student = db.studentDao().getStudentById(studentId)
            student?.let {
                tvName.text = it.name
                tvNimProdi.text = "${it.nim} · ${it.prodi}"

                tvInitial.text = it.name.take(1).uppercase()
            }
        }

        btnSave.setOnClickListener {
            val noteContent = etNote.text.toString()
            if (noteContent.isEmpty()) {
                Toast.makeText(context, "Catatan masih kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                requireContext().openFileOutput(fileName, Context.MODE_PRIVATE).use { output ->
                    output.write(noteContent.toByteArray())
                }

                val file = File(requireContext().filesDir, fileName)
                val fileSize = file.length()

                tvStatus.text = "✓ Tersimpan ($fileSize bytes)"
                tvStatus.visibility = View.VISIBLE
                Toast.makeText(context, "Berhasil disimpan", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Gagal menyimpan file", Toast.LENGTH_SHORT).show()
            }
        }

        btnLoad.setOnClickListener {
            try {
                val file = File(requireContext().filesDir, fileName)
                if (file.exists()) {
                    val content = requireContext().openFileInput(fileName).bufferedReader().use { it.readText() }
                    etNote.setText(content)
                    Toast.makeText(context, "Catatan berhasil dimuat", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "File catatan belum ada", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Gagal membaca catatan", Toast.LENGTH_SHORT).show()
            }
        }

        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}
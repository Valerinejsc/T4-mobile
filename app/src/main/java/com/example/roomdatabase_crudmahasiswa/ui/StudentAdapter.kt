package com.example.roomdatabase_crudmahasiswa.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdatabase_crudmahasiswa.R
import com.example.roomdatabase_crudmahasiswa.database.entity.StudentEntity

class StudentAdapter(
    private var list: List<StudentEntity>,
    private val onEditClick: (StudentEntity) -> Unit,
    private val onDeleteClick: (StudentEntity) -> Unit,
    private val onItemClick: (StudentEntity) -> Unit
) : RecyclerView.Adapter<StudentAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvInitial: TextView = view.findViewById(R.id.tvInitial)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvNim: TextView = view.findViewById(R.id.tvNim)
        val btnEdit: TextView = view.findViewById(R.id.btnEdit)
        val btnDelete: TextView = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student = list[position]
        holder.tvName.text = student.name
        holder.tvNim.text = student.nim

        val initials = student.name.split(" ")
            .filter { it.isNotEmpty() }
            .map { it[0] }
            .take(2)
            .joinToString("")
        holder.tvInitial.text = initials.uppercase()

        holder.btnEdit.setOnClickListener { onEditClick(student) }

        holder.btnDelete.setOnClickListener { onDeleteClick(student) }

        holder.itemView.setOnClickListener { onItemClick(student) }
    }

    override fun getItemCount(): Int = list.size
    fun setData(newList: List<StudentEntity>) {
        this.list = newList
        notifyDataSetChanged()
    }

    fun getStudentAt(position: Int): StudentEntity {
        return list[position]
    }

    fun updateData(newList: List<StudentEntity>) {
        this.list = newList
        notifyDataSetChanged()
    }
}
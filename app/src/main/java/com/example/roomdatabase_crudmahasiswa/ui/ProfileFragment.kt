package com.example.roomdatabase_crudmahasiswa.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.roomdatabase_crudmahasiswa.R
import com.example.roomdatabase_crudmahasiswa.utils.PrefManager

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var prefManager: PrefManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefManager = PrefManager(requireContext())

        val tvName = view.findViewById<TextView>(R.id.tvProfileName)
        val tvInitial = view.findViewById<TextView>(R.id.tvProfileInitial)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)

        val username = prefManager.getUsername()
        if (!username.isNullOrEmpty()) {
            tvName.text = username.capitalize()
            tvInitial.text = username.take(2).uppercase()
        }

        btnLogout.setOnClickListener {
            prefManager.logout()

            val intent = Intent(requireContext(), LoginActivity::class.java)

            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)

            activity?.finish()
        }
    }
}
package com.example.batani.ui.settingss

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.batani.auth.LoginActivity

import com.example.batani.databinding.FragmentSettingsBinding // Pastikan Anda mengimpor binding yang benar
import com.example.batani.ui.feedback.FeedbackActivity
import com.example.batani.ui.rekomendasi.ViewModelFactory
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {
    private val viewModel by viewModels<SettingsViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!! // Properti binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)


        binding.buttonFeedback.setOnClickListener {
   
            val intent = Intent(requireContext(), FeedbackActivity::class.java)
            startActivity(intent)
        }

        binding.logout.setOnClickListener{
            logout()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            showExitConfirmationDialog()
        }
        return binding.root
    }

    private fun logout() {
        lifecycleScope.launch {

            val result = viewModel.logout() // Pastikan viewModel.logout mengembalikan status atau hasil

            if (result) {
   
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish() // Menutup activity ini agar tidak bisa kembali ke halaman sebelumnya
            } else {

                Toast.makeText(requireContext(), "Logout failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun showExitConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Apakah Anda yakin ingin keluar?")
            .setCancelable(false)
            .setPositiveButton("Ya") { _, _ ->
                requireActivity().finish()
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }


        val alert = builder.create()
        alert.show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        
        _binding = null
    }
}

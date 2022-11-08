package com.example.gd10_d_11007.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gd10_d_11007.MahasiswaAdapter
import com.example.gd10_d_11007.MahasiswaData
import com.example.gd10_d_11007.RClient
import com.example.gd10_d_11007.ResponseDataMahasiswa
import com.example.gd10_d_11007.databinding.FragmentDataMahasiswaBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("UNREACHABLE_CODE")
class DataMahasiswaFragment : Fragment() {
    private var _binding: FragmentDataMahasiswaBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!
    private val listMahasiswa = ArrayList<MahasiswaData>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDataMahasiswaBinding.inflate(inflater,
            container, false)
        return binding.root
        getDataMahasiswa()
    }
    override fun onStart() {
        super.onStart()
        getDataMahasiswa()
    }
    private fun getDataMahasiswa() {
        binding.rvData.setHasFixedSize(true)
        binding.rvData.layoutManager= LinearLayoutManager(context)
        val bundle = arguments
        val cari = bundle?.getString(/* key = */ "cari")
        binding.progressBar.visibility
        RClient.instances.getData(cari).enqueue(object :
            Callback<ResponseDataMahasiswa> {
            override fun onResponse(
                call: Call<ResponseDataMahasiswa>,
                response: Response<ResponseDataMahasiswa>
            ){
                if (response.isSuccessful){
                    listMahasiswa.clear()
                    response.body()?.let {
                        listMahasiswa.addAll(it.data) }
                    val adapter =
                        MahasiswaAdapter(listMahasiswa, requireContext())
                    binding.rvData.adapter = adapter
                    adapter.notifyDataSetChanged()
                    binding.progressBar.isVisible = false
                }
            }
            override fun onFailure(call: Call<ResponseDataMahasiswa>, t: Throwable) {
                AlertDialog.Builder(context!!).apply {
                    setTitle("Informasi")
                    setMessage("Error: ${t.message}")
                    setPositiveButton("ok"){dialog, which ->
                        dialog.dismiss()
                    }
                }.show()
            }
        }
        )
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

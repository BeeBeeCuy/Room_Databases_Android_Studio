package com.biby.post4_402

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.biby.post4_402.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dbWarga: DatabaseWarga
    private lateinit var wargaDao: WargaDao
    private lateinit var appExecutors: AppExecutor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        appExecutors = AppExecutor()
        dbWarga = DatabaseWarga.getDatabase(applicationContext)
        wargaDao = dbWarga.WargaDao()


        val statusList = arrayOf("Belum Menikah", "Menikah", "Cerai Hidup", "Cerai Mati")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statusList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spStatus.adapter = adapter


        tampilkanSemuaData()


        binding.btnSimpan.setOnClickListener {
            val nama = binding.etNama.text.toString().trim()
            val nikText = binding.etNIK.text.toString().trim()
            val kab = binding.etKabupaten.text.toString().trim()
            val kec = binding.etKecamatan.text.toString().trim()
            val desa = binding.etDesa.text.toString().trim()
            val rtText = binding.etRT.text.toString().trim()
            val rwText = binding.etRW.text.toString().trim()


            if (nama.isEmpty() || nikText.isEmpty() || kab.isEmpty() || kec.isEmpty() ||
                desa.isEmpty() || rtText.isEmpty() || rwText.isEmpty()
            ) {
                Toast.makeText(this, "Semua data harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val selectedId = binding.rgJenisKelamin.checkedRadioButtonId
            if (selectedId == -1) {
                Toast.makeText(this, "Pilih jenis kelamin!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val rb = findViewById<RadioButton>(selectedId)
            val jk = rb.text.toString()
            val status = binding.spStatus.selectedItem.toString()


            val nik = nikText.toLongOrNull() ?: 0
            val rt = rtText.toIntOrNull() ?: 0
            val rw = rwText.toIntOrNull() ?: 0


            val warga = DataWarga(
                nama = nama,
                nik = nik.toInt(),
                kabupaten = kab,
                kecamatan = kec,
                desa = desa,
                rt = rt,
                rw = rw,
                jk = jk,
                stnikah = status
            )


            appExecutors.diskIO.execute {
                wargaDao.insert(warga)
                appExecutors.mainThread.execute {
                    Toast.makeText(this, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show()
                    resetForm()
                    tampilkanSemuaData()
                }
            }
        }


        binding.btnReset.setOnClickListener {
            appExecutors.diskIO.execute {
                wargaDao.deleteAll()
                appExecutors.mainThread.execute {
                    resetForm()
                    binding.tvDaftar.text = "Daftar Warga Negara:\nBelum ada data warga yang tersimpan."
                    Toast.makeText(this, "Semua data berhasil dihapus!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun tampilkanSemuaData() {
        appExecutors.diskIO.execute {
            val daftarWarga = wargaDao.getAll()
            appExecutors.mainThread.execute {
                if (daftarWarga.isEmpty()) {
                    binding.tvDaftar.text = "Daftar Warga Negara:\nBelum ada data warga yang tersimpan."
                } else {
                    val builder = StringBuilder("Daftar Warga Negara:\n")
                    daftarWarga.forEachIndexed { index, warga ->
                        builder.append("${index + 1}. ${warga.nama} (${warga.jk}) - ${warga.stnikah}\n")
                        builder.append("   NIK: ${warga.nik}\n")
                        builder.append("   Alamat: RT ${warga.rt}/RW ${warga.rw}, ${warga.desa}, ${warga.kecamatan}, ${warga.kabupaten}\n\n")
                    }
                    binding.tvDaftar.text = builder.toString().trim()
                }
            }
        }
    }


    private fun resetForm() {
        binding.apply {
            etNama.text.clear()
            etNIK.text.clear()
            etKabupaten.text.clear()
            etKecamatan.text.clear()
            etDesa.text.clear()
            etRT.text.clear()
            etRW.text.clear()
            rgJenisKelamin.clearCheck()
            spStatus.setSelection(0)
        }
    }
}

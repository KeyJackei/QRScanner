package com.example.qrscanner

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.example.qrscanner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val scannedDataList = mutableListOf<String>()
    private lateinit var adapter: ScannedDataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Настроить RecyclerView
        adapter = ScannedDataAdapter(scannedDataList) { position ->
            // Удаление элемента из списка
            scannedDataList.removeAt(position)
            adapter.notifyItemRemoved(position)
            saveData()  // Сохраняем обновленные данные
        }
        binding.scannedListView.layoutManager = LinearLayoutManager(this)
        binding.scannedListView.adapter = adapter

        // Обработчик для кнопки сканирования
        binding.scanButton.setOnClickListener {
            startQrScanner()
        }

        // Загружаем сохраненные данные при запуске приложения
        loadData()
    }

    private val qrScanLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            scannedDataList.add(result.contents)
            adapter.notifyItemInserted(scannedDataList.size - 1)
            saveData()  // Сохраняем обновленные данные
        }
    }

    private fun startQrScanner() {
        val options = ScanOptions()
        options.setPrompt("Наведите камеру на QR-код")
        options.setBeepEnabled(true)
        options.setOrientationLocked(false)
        qrScanLauncher.launch(options)
    }

    // Сохранение данных в SharedPreferences
    private fun saveData() {
        val sharedPreferences = getSharedPreferences("ScannedData", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putStringSet("scannedData", scannedDataList.toSet())
        editor.apply()
    }

    // Загрузка данных из SharedPreferences
    private fun loadData() {
        val sharedPreferences = getSharedPreferences("ScannedData", MODE_PRIVATE)
        val savedData = sharedPreferences.getStringSet("scannedData", emptySet())
        if (savedData != null) {
            scannedDataList.addAll(savedData)
            adapter.notifyDataSetChanged()
        }
    }
}


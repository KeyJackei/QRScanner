package com.example.qrscanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ScannedDataAdapter(
    private val dataList: MutableList<String>,
    private val onItemLongClick: (Int) -> Unit
) : RecyclerView.Adapter<ScannedDataAdapter.ScannedDataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScannedDataViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_scanned_data, parent, false)
        return ScannedDataViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScannedDataViewHolder, position: Int) {
        holder.textView.text = dataList[position]

        // Обработчик долгого нажатия на элемент для его удаления
        holder.itemView.setOnLongClickListener {
            onItemLongClick(position)  // Передаем индекс элемента
            true  // Возвращаем true, чтобы событие не пошло дальше
        }
    }

    override fun getItemCount(): Int = dataList.size

    class ScannedDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
    }
}

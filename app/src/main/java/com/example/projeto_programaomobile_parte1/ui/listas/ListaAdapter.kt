package com.example.projeto_programaomobile_parte1.ui.listas

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.projeto_programaomobile_parte1.data.model.ListaCompra
import com.example.projeto_programaomobile_parte1.databinding.ItemListaBinding

class ListaAdapter(
    private val aoClicar: (ListaCompra, View) -> Unit,
    private val aoLongo: (ListaCompra, View) -> Unit
) : ListAdapter<ListaCompra, ListaAdapter.VH>(DIFF) {

    object DIFF : DiffUtil.ItemCallback<ListaCompra>() {
        override fun areItemsTheSame(oldItem: ListaCompra, newItem: ListaCompra): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: ListaCompra, newItem: ListaCompra): Boolean = oldItem == newItem
    }

    inner class VH(val b: ItemListaBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemListaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.b.txtTitulo.text = item.titulo
        val uri: Uri? = item.imagemUri
        if (uri != null) {
            holder.b.imgThumb.setImageURI(uri)
        } else {
            holder.b.imgThumb.setImageResource(com.example.projeto_programaomobile_parte1.R.drawable.ic_placeholder)
        }
        holder.itemView.setOnClickListener { aoClicar(item, it) }
        holder.itemView.setOnLongClickListener { aoLongo(item, it); true }
    }
}

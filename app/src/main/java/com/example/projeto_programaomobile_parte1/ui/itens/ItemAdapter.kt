package com.example.projeto_programaomobile_parte1.ui.itens

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.projeto_programaomobile_parte1.data.model.Categoria
import com.example.projeto_programaomobile_parte1.data.model.Item
import com.example.projeto_programaomobile_parte1.databinding.ItemHeaderBinding
import com.example.projeto_programaomobile_parte1.databinding.ItemItemBinding

sealed class LinhaItem {
    data class Header(val titulo: String) : LinhaItem()
    data class ItemLinha(val item: Item) : LinhaItem()
}

class ItemAdapter(
    private val onEditar: (Item) -> Unit,
    private val onAlternarComprado: (Item) -> Unit
) : ListAdapter<LinhaItem, RecyclerView.ViewHolder>(DIFF) {

    companion object {
        private const val TIPO_HEADER = 0
        private const val TIPO_ITEM = 1
    }

    object DIFF : DiffUtil.ItemCallback<LinhaItem>() {
        override fun areItemsTheSame(oldItem: LinhaItem, newItem: LinhaItem): Boolean = when {
            oldItem is LinhaItem.Header && newItem is LinhaItem.Header -> oldItem.titulo == newItem.titulo
            oldItem is LinhaItem.ItemLinha && newItem is LinhaItem.ItemLinha -> oldItem.item.id == newItem.item.id
            else -> false
        }
        override fun areContentsTheSame(oldItem: LinhaItem, newItem: LinhaItem): Boolean = oldItem == newItem
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is LinhaItem.Header -> TIPO_HEADER
        is LinhaItem.ItemLinha -> TIPO_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        TIPO_HEADER -> HeaderVH(ItemHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        else -> ItemVH(ItemItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val linha = getItem(position)) {
            is LinhaItem.Header -> (holder as HeaderVH).bind(linha.titulo)
            is LinhaItem.ItemLinha -> (holder as ItemVH).bind(linha.item, onEditar, onAlternarComprado)
        }
    }

    class HeaderVH(private val b: ItemHeaderBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(titulo: String) { b.txtHeader.text = titulo }
    }

    class ItemVH(private val b: ItemItemBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: Item, onEditar: (Item) -> Unit, onAlternar: (Item) -> Unit) {
            b.txtNome.text = item.nome
            val ctx = b.root.context
            b.txtDetalhe.text = ctx.getString(com.example.projeto_programaomobile_parte1.R.string.detalhe_item, item.quantidade, item.unidade.toString())
            b.chkComprado.setOnCheckedChangeListener(null)
            b.chkComprado.isChecked = item.comprado
            b.chkComprado.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
                if (item.comprado != isChecked) onAlternar(item)
            }
            val iconRes = when (item.categoria) {
                Categoria.ALIMENTOS -> com.example.projeto_programaomobile_parte1.R.drawable.ic_categoria_alimentos
                Categoria.BEBIDAS -> com.example.projeto_programaomobile_parte1.R.drawable.ic_categoria_bebidas
                Categoria.HIGIENE -> com.example.projeto_programaomobile_parte1.R.drawable.ic_categoria_higiene
                Categoria.LIMPEZA -> com.example.projeto_programaomobile_parte1.R.drawable.ic_categoria_limpeza
                Categoria.OUTROS -> com.example.projeto_programaomobile_parte1.R.drawable.ic_categoria_outros
            }
            b.imgCategoria.setImageResource(iconRes)
            b.root.setOnClickListener { onEditar(item) }
        }
    }
}

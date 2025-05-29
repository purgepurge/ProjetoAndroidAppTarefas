package com.exercicio.projetoandroidapptarefas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListaTarefasAdapter(
    private val tarefas: List<Tarefa>,
    private val onItemClick: (Tarefa) -> Unit,
    private val onItemLongClick: (Tarefa) -> Unit
    ): RecyclerView.Adapter<ListaTarefasAdapter.TarefaViewHolder>() {

    class TarefaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeTarefa: TextView = itemView.findViewById(R.id.textViewItemNomeTarefa)
        val descricaoTarefa: TextView = itemView.findViewById(R.id.textViewItemDescricaoTarefa)
        val prioridadeTarefa: TextView = itemView.findViewById(R.id.textViewItemPrioridadeTarefa)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TarefaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item_tarefa, parent, false) // Certifica que está inflando o xml certo
        return TarefaViewHolder(view)
    }
    //Preenche as views do recycle view
    override fun onBindViewHolder(holder: TarefaViewHolder, position: Int) {
        val tarefa = tarefas[position]

        holder.nomeTarefa.text = tarefa.nome
        holder.descricaoTarefa.text = tarefa.descricao
        holder.prioridadeTarefa.text = when (tarefa.prioridade) {
            3 -> "Prioridade: Alta"
            2 -> "Prioridade: Média"
            1 -> "Prioridade: Baixa"
            else -> "Prioridade: Não Definida"
        }

        val corPrioridade = when (tarefa.prioridade) {
            3 -> holder.itemView.context.getColor(android.R.color.holo_red_dark)
            2 -> holder.itemView.context.getColor(android.R.color.holo_orange_dark)
            1 -> holder.itemView.context.getColor(android.R.color.holo_green_dark)
            else -> holder.itemView.context.getColor(android.R.color.black)
        }
        holder.prioridadeTarefa.setTextColor(corPrioridade)

        // ✅ Clique no item
        holder.itemView.setOnClickListener {
            onItemClick(tarefa)
        }
        if (tarefa.concluida) {
            holder.itemView.alpha = 0.5f
        } else {
            holder.itemView.alpha = 1.0f
        }
        holder.itemView.setOnLongClickListener {
            onItemLongClick(tarefa)
            true
        }

    }


    override fun getItemCount(): Int {
        return tarefas.size
    }
}

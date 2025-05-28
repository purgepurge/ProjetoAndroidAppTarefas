package com.exercicio.projetoandroidapptarefas

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Date

class ListaTarefas : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adaptadorTarefas: ListaTarefasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_tarefas)

        recyclerView = findViewById(R.id.recyclerViewTarefas)
        recyclerView.layoutManager = LinearLayoutManager(this)

        FirestoreService.listarTarefas({ tarefas ->
            adaptadorTarefas = ListaTarefasAdapter(tarefas,
                onItemClick = { tarefa ->
                    exibirDialogoConclusao(tarefa)
                },
                onItemLongClick = { tarefa ->
                    exibirDialogoDeletar(tarefa)
                }
            )
            recyclerView.adapter = adaptadorTarefas
        }, { e ->
            Toast.makeText(this, "Erro ao carregar tarefas: ${e.message}", Toast.LENGTH_SHORT).show()
        })

    }
    private fun exibirDialogoConclusao(tarefa: Tarefa) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Tarefa concluída?")
        builder.setMessage("Você concluiu a tarefa \"${tarefa.nome}\"?")

        builder.setPositiveButton("Sim") { _, _ ->
            val dataConclusao = Date()
            tarefa.concluida = true
            tarefa.dataConclusao = dataConclusao

            // Calcula o tempo gasto
            val tempoGastoMillis = dataConclusao.time - (tarefa.dataCriacao?.time ?: dataConclusao.time)
            val tempoGastoMinutos = tempoGastoMillis / 60000

            FirestoreService.atualizarTarefa(tarefa, {
                Toast.makeText(
                    this,
                    "Tarefa concluída! Tempo gasto: $tempoGastoMinutos minutos.",
                    Toast.LENGTH_LONG
                ).show()

                // Recarrega a lista (opcional, pode fazer um refresh aqui)
                recreate()

            }, { e ->
                Toast.makeText(this, "Erro ao atualizar: ${e.message}", Toast.LENGTH_SHORT).show()
            })
        }

        builder.setNegativeButton("Não") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }

    private fun exibirDialogoDeletar(tarefa: Tarefa) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Arquivar tarefa")
        builder.setMessage("Deseja realmente Arquivar a tarefa \"${tarefa.nome}\"?")

        builder.setPositiveButton("Sim") { _, _ ->
            tarefa.apagada = true
            FirestoreService.atualizarTarefa(tarefa, {
                Toast.makeText(this, "Tarefa arquivada com sucesso.", Toast.LENGTH_SHORT).show()
                recreate()
            }, { e ->
                Toast.makeText(this, "Erro ao arquivar: ${e.message}", Toast.LENGTH_SHORT).show()
            })
        }

        builder.setNegativeButton("Não") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }


}
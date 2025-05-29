package com.exercicio.projetoandroidapptarefas

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*

object FirestoreService { // classe que serve para fazer todo o CRUD do banco de dados

    private val userId = FirebaseAuth.getInstance().currentUser?.uid  //Inicia uma instancia do banco de dados, pegando a UID (Unique Identification) do usuario
    private val tarefasCollection = FirebaseFirestore.getInstance() //Vincula a tabela Tarefas.
        .collection("usuarios")
        .document(userId!!)
        .collection("tarefas")

    // Adiciona uma tarefa
    fun adicionarTarefa(tarefa: Tarefa, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        tarefa.dataCriacao = Date()
        tarefasCollection.add(tarefa)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    // Lista todas as tarefas ordenadas por data de criação
    fun listarTarefas(
        onSuccess: (List<Tarefa>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        tarefasCollection
            .whereEqualTo("apagada", false)
            .orderBy("dataCriacao", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val tarefas = result.map { document ->
                    document.toObject(Tarefa::class.java).apply { id = document.id }
                }
                onSuccess(tarefas)
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    // Remove uma tarefa
    fun deletarTarefa(id: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        tarefasCollection.document(id)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    // Atualiza uma tarefa
    fun atualizarTarefa(tarefa: Tarefa, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        tarefa.id?.let { id ->
            tarefasCollection.document(id)
                .set(tarefa)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { e -> onFailure(e) }
        }
    }
}

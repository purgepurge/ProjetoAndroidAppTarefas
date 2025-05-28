package com.exercicio.projetoandroidapptarefas
import com.google.firebase.firestore.DocumentId
import java.util.Date

import java.io.Serializable // Implementa Serializable para que objetos Tarefa possam ser passados entre Activities via Intent.

// Classe para representar uma tarefa.
data class Tarefa(
    @DocumentId //Preenche automaticamente este campo com o ID do documento ao ler
    var id: String? = null,
    var nome: String? = null,       // Nome da tarefa
    var descricao: String? = null,  // Descrição da tarefa (pode ser nula)
    var prioridade: Int? = null,   // Prioridade da tarefa (Alta, Média, Baixa)
    var concluida: Boolean = false, // Se a tarefa já foi concluida.
    var dataCriacao: Date? = null, //A data que a tarefa foi criada.
    var dataConclusao: Date? = null, //A data que a tarefa foi concluida
    var apagada: Boolean = false //ela inicialmente não está apagada
)
{
    constructor() : this(null,null,null,null,false,null,null,false)
}



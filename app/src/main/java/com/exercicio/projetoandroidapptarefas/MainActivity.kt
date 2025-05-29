package com.exercicio.projetoandroidapptarefas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import java.util.Date


class MainActivity : AppCompatActivity() {

    // Declaração das variáveis para os elementos da UI (User Interface)
    private lateinit var editTextNomeTarefa: EditText
    private lateinit var editTextDescricao: EditText
    private lateinit var radioGroupPrioridade: RadioGroup
    private lateinit var buttonSalvarTarefa: Button
    private lateinit var varbuttonCancelar: Button
    private lateinit var buttonVerTarefas: Button // Novo botão para ver as tarefas
    private lateinit var buttonSair: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Habilita o modo "edge-to-edge" para que o conteúdo se estenda por toda a tela,
        // incluindo as áreas das barras de sistema (status bar e navigation bar).
        enableEdgeToEdge()

        // Define o layout XML para esta Activity.
        setContentView(R.layout.activity_main)

        // Configura um listener para aplicar insets (espaçamentos) para as barras do sistema.
        // Isso garante que o conteúdo não seja sobreposto pelas barras de status e navegação.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Conectar elementos visuais definidos no XML usando seus IDs.
        editTextNomeTarefa = findViewById(R.id.editTextNomeTarefa)
        editTextDescricao = findViewById(R.id.editTextDescricao)
        radioGroupPrioridade = findViewById(R.id.radioGroupPrioridade)
        buttonSalvarTarefa = findViewById(R.id.buttonSalvarTarefa)
        varbuttonCancelar = findViewById(R.id.buttonCancelar)
        buttonVerTarefas = findViewById(R.id.buttonVerTarefas) // Inicializa o novo botão
        buttonSair = findViewById(R.id.buttonSair)

        // Listener para o botão "Salvar Tarefa"
        buttonSalvarTarefa.setOnClickListener {
            val nomeTarefa = editTextNomeTarefa.text.toString().trim()
            val descricao = editTextDescricao.text.toString().trim()
            val idPrioridadeSelecionada = radioGroupPrioridade.checkedRadioButtonId
            val prioridade = when (idPrioridadeSelecionada) {
                R.id.radioButtonPrioridadeAlta -> 3
                R.id.radioButtonPrioridadeMedia -> 2
                R.id.radioButtonPrioridadeBaixa -> 1
                else -> 2
            }

            if (nomeTarefa.isNotEmpty()) { //cria o objeto Tarefa,o nome da tarefa não pode ser nulo
                val novaTarefa = Tarefa(
                    nome = nomeTarefa,
                    descricao = descricao,
                    prioridade = prioridade,
                    concluida = false, //inicialmente a tarefa não está concluida
                    dataCriacao = Date()
                )

                FirestoreService.adicionarTarefa(novaTarefa, { //comando que adiciona Tarefa no banco de dados (FirestoreService)
                    Toast.makeText(this, "Tarefa salva com sucesso!", Toast.LENGTH_SHORT).show()
                    editTextNomeTarefa.text.clear()
                    editTextDescricao.text.clear()
                    radioGroupPrioridade.check(R.id.radioButtonPrioridadeMedia)
                }, { e ->
                    Toast.makeText(this, "Erro ao salvar: ${e.message}", Toast.LENGTH_SHORT).show()
                })
            } else {
                Toast.makeText(this, "Digite o nome da tarefa", Toast.LENGTH_SHORT).show()
            }
        }
        //Botão para deslogar o usuario
        buttonSair.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Listener para o botão "Cancelar"
        varbuttonCancelar.setOnClickListener {
            // Finaliza a Activity atual.
            // Isso geralmente leva o usuário de volta à Activity anterior na pilha,
            // ou fecha o aplicativo se esta for a última Activity.
            finish()
        }

        // Listener para o novo botão "Ver Tarefas"
        buttonVerTarefas.setOnClickListener {
            val intent = Intent(this, ListaTarefas::class.java)
            startActivity(intent)
        }
    }
}
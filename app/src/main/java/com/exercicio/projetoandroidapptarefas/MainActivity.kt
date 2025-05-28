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
import java.util.Date


class MainActivity : AppCompatActivity() {

    // Declaração das variáveis para os elementos da UI (User Interface)
    private lateinit var editTextNomeTarefa: EditText
    private lateinit var editTextDescricao: EditText
    private lateinit var radioGroupPrioridade: RadioGroup
    private lateinit var buttonSalvarTarefa: Button
    private lateinit var varbuttonCancelar: Button
    private lateinit var buttonVerTarefas: Button // Novo botão para ver as tarefas
    //private lateinit var binding:


    // Lista mutável para armazenar as tarefas.
    // NOTA: Esta lista é temporária e não persiste após o aplicativo ser fechado.
    // Para persistência real, você precisaria de um banco de dados (SQLite, Room, Firebase, etc.).
    private val listaDeTarefas = mutableListOf<Tarefa>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Habilita o modo "edge-to-edge" para que o conteúdo se estenda por toda a tela,
        // incluindo as áreas das barras de sistema (status bar e navigation bar).
        enableEdgeToEdge()

        // Define o layout XML para esta Activity.
        // R.layout.activity_main refere-se ao arquivo activity_main.xml na pasta res/layout.
        setContentView(R.layout.activity_main)

        // Configura um listener para aplicar insets (espaçamentos) para as barras do sistema.
        // Isso garante que o conteúdo não seja sobreposto pelas barras de status e navegação.
        // É importante que o ConstraintLayout raiz no seu XML tenha o ID "main" para isso funcionar.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // --- Inicialização dos elementos da UI ---
        // Conecta as variáveis Kotlin aos elementos visuais definidos no XML usando seus IDs.
        editTextNomeTarefa = findViewById(R.id.editTextNomeTarefa)
        editTextDescricao = findViewById(R.id.editTextDescricao)
        radioGroupPrioridade = findViewById(R.id.radioGroupPrioridade)
        buttonSalvarTarefa = findViewById(R.id.buttonSalvarTarefa)
        varbuttonCancelar = findViewById(R.id.buttonCancelar)
        buttonVerTarefas = findViewById(R.id.buttonVerTarefas) // Inicializa o novo botão

        // --- Configuração dos Listeners de Clique para os Botões ---

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
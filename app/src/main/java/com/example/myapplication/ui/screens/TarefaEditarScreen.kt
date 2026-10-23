package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.viewmodel.TarefaViewModel

@Composable
fun TarefaCadastrarScreen(
    viewModel: TarefaViewModel,
    voltar: () -> Unit
) {
    TarefaFormScreen(
        tarefaId = null,
        titulo = "Nova tarefa",
        viewModel = viewModel,
        voltar = voltar
    )
}

@Composable
fun TarefaEditarScreen(
    tarefaId: Long,
    viewModel: TarefaViewModel,
    voltar: () -> Unit
) {
    TarefaFormScreen(
        tarefaId = tarefaId,
        titulo = "Alterar tarefa",
        viewModel = viewModel,
        voltar = voltar
    )
}

@Composable
private fun TarefaFormScreen(
    tarefaId: Long?,
    titulo: String,
    viewModel: TarefaViewModel,
    voltar: () -> Unit
) {
    LaunchedEffect(tarefaId) {
        if (tarefaId != null) viewModel.buscarPorId(tarefaId)
    }
    LaunchedEffect(viewModel.salvo) {
        if (viewModel.salvo) {
            viewModel.consumirSalvo()
            voltar()
        }
    }

    if (viewModel.usuarioAtivoId == null) {
        Text("Faça Login para continuar.")
        return
    }

    val editando = tarefaId != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(titulo, style = MaterialTheme.typography.headlineMedium)
        viewModel.mensagem?.let { Text(it) }
        if (viewModel.carregando) CircularProgressIndicator()

        val pronto = !editando || viewModel.tarefa != null
        if (pronto) {
            OutlinedTextField(
                value = viewModel.nome,
                onValueChange = viewModel::mudarNome,
                label = { Text("Nome") },
                singleLine = true,
                enabled = !viewModel.carregando,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.descricao,
                onValueChange = viewModel::mudarDescricao,
                label = { Text("Descrição") },
                enabled = !viewModel.carregando,
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    if (editando) viewModel.salvar() else viewModel.cadastrar()
                },
                enabled = !viewModel.carregando && !viewModel.salvo
            ) { Text(if (editando) "Salvar alterações" else "Cadastrar") }
        } else if (!viewModel.carregando) {
            val id = tarefaId ?: return@Column
            TextButton(onClick = { viewModel.buscarPorId(id) }) {
                Text("Tentar carregar novamente")
            }
        }
        TextButton(onClick = voltar, enabled = !viewModel.carregando) {
            Text("Voltar para tarefas")
        }
    }
}

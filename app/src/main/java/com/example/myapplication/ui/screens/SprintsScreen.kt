package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.viewmodel.SprintViewModel

@Composable
fun SprintsScreen(
    viewModel: SprintViewModel,
    irParaCadastro: () -> Unit,
    irParaEdicao: (Long) -> Unit,
    voltar: () -> Unit
) {
    val atual = viewModel.usuarioAtivoId
    var excluirId by rememberSaveable { mutableStateOf<Long?>(null) }
    if (atual == null) {
        Text("Faça Login pra continuar")
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Sprints", style = MaterialTheme.typography.headlineMedium)
                Button(onClick = irParaCadastro, enabled = !viewModel.carregando) {
                    Text("Nova sprint")
                }
                TextButton(onClick = viewModel::listar, enabled = !viewModel.carregando) {
                    Text("Atualizar lista")
                }
                viewModel.mensagem?.let { Text(it) }
                if (viewModel.carregando) CircularProgressIndicator()
                if (!viewModel.carregando && viewModel.sprints.isEmpty())
                    Text("Nenhuma sprint encontrada")
            }
        }
        items(viewModel.sprints, key = { it.id }) { sprint ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(sprint.nome, style = MaterialTheme.typography.titleMedium)
                    Text(sprint.descricao)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TextButton(
                            onClick = { irParaEdicao(sprint.id) },
                            enabled = !viewModel.carregando
                        ) { Text("Alterar") }
                        TextButton(
                            onClick = { excluirId = sprint.id },
                            enabled = !viewModel.carregando
                        ) { Text("Excluir") }
                    }
                }
            }
        }
        item { TextButton(onClick = voltar) { Text("Voltar") } }
    }

    val selecionada = viewModel.sprints.firstOrNull { it.id == excluirId }
    if (selecionada != null) {
        AlertDialog(
            onDismissRequest = { excluirId = null },
            title = { Text("Excluir sprint?") },
            text = { Text("Excluir \"${selecionada.nome}\"? Esta ação não pode ser desfeita.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        excluirId = null
                        viewModel.excluir(selecionada.id)
                    },
                    enabled = !viewModel.carregando
                ) { Text("Excluir") }
            },
            dismissButton = {
                TextButton(onClick = { excluirId = null }) { Text("Cancelar") }
            }
        )
    }
}

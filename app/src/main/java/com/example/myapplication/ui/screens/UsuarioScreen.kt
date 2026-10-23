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
import com.example.myapplication.ui.viewmodel.UsuarioViewModel

@Composable
fun UsuariosScreen(
    viewModel: UsuarioViewModel,
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
                Text("Usuários", style = MaterialTheme.typography.headlineMedium)
                Button(onClick = irParaCadastro, enabled = !viewModel.carregando) {
                    Text("Novo usuário")
                }
                TextButton(onClick = viewModel::listar, enabled = !viewModel.carregando) {
                    Text("Atualizar lista")
                }
                viewModel.mensagem?.let { Text(it) }
                if (viewModel.carregando) CircularProgressIndicator()
                if (!viewModel.carregando && viewModel.usuarios.isEmpty())
                    Text("Nenhum usuário encontrado")
            }
        }
        items(viewModel.usuarios, key = { it.id }) { usuario ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(usuario.nome, style = MaterialTheme.typography.titleMedium)
                    Text(usuario.email)
                    if (usuario.id == atual) Text("Você — usuário conectado")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TextButton(
                            onClick = { irParaEdicao(usuario.id) },
                            enabled = !viewModel.carregando
                        ) { Text("Alterar") }
                        TextButton(
                            onClick = { excluirId = usuario.id },
                            enabled = !viewModel.carregando && usuario.id != atual
                        ) { Text("Excluir") }
                    }
                }
            }
        }
        item { TextButton(onClick = voltar) { Text("Voltar") } }
    }

    val selecionado = viewModel.usuarios.firstOrNull { it.id == excluirId }
    if (selecionado != null && selecionado.id != atual) {
        AlertDialog(
            onDismissRequest = { excluirId = null },
            title = { Text("Excluir usuário?") },
            text = { Text("Excluir \"${selecionado.nome}\"? Esta ação não pode ser desfeita.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        excluirId = null
                        viewModel.excluir(selecionado.id)
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

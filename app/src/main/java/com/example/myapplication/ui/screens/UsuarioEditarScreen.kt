package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.viewmodel.UsuarioViewModel

@Composable
fun UsuarioEditarScreen(
    usuarioId: Long,
    viewModel: UsuarioViewModel,
    voltar: () -> Unit
) {
    LaunchedEffect(usuarioId) { viewModel.buscarPorId(usuarioId) }
    LaunchedEffect(viewModel.salvo) {
        if (viewModel.salvo) {
            viewModel.consumirSalvo()
            voltar()
        }
    }

    var novaSenha by remember { mutableStateOf("") }
    if (viewModel.usuarioAtivoId == null) {
        Text("Faça Login para continuar.")
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Alterar usuário", style = MaterialTheme.typography.headlineMedium)
        viewModel.mensagem?.let { Text(it) }
        if (viewModel.carregando) CircularProgressIndicator()
        val usuario = viewModel.usuario
        if (usuario != null) {
            OutlinedTextField(
                value = viewModel.nome,
                onValueChange = viewModel::mudarNome,
                label = { Text("Nome") },
                singleLine = true,
                enabled = !viewModel.carregando,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = usuario.email,
                onValueChange = {},
                readOnly = true,
                label = { Text("E-mail (não pode ser alterado)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = novaSenha,
                onValueChange = { if (it.length <= 128) novaSenha = it },
                label = { Text("Nova senha") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    autoCorrectEnabled = false
                ),
                singleLine = true,
                enabled = !viewModel.carregando,
                modifier = Modifier.fillMaxWidth()
            )
            Text("Deixe a senha em branco para manter a atual. Para trocar, use de 6 a 128 caracteres.")
            Button(
                onClick = {
                    viewModel.salvar(novaSenha)
                    novaSenha = ""
                },
                enabled = !viewModel.carregando && !viewModel.salvo
            ) { Text("Salvar alterações") }
        } else if (!viewModel.carregando) {
            TextButton(onClick = { viewModel.buscarPorId(usuarioId) }) {
                Text("Tentar carregar novamente")
            }
        }
        TextButton(onClick = voltar, enabled = !viewModel.carregando) {
            Text("Voltar para usuários")
        }
    }
}

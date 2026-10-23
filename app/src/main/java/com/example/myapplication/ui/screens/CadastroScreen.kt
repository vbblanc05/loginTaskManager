package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.viewmodel.AuthViewModel

@Composable
fun CadastroScreen(
    irLogin: () -> Unit,
    authViewModel: AuthViewModel
) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var senhaVisivel by remember { mutableStateOf(false) }

    LaunchedEffect(authViewModel.concluido) {
        if (authViewModel.concluido) {
            authViewModel.consumirResultado()
            irLogin()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Cadastro:", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nome,
            onValueChange = { if (it.length <= 100) nome = it },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { if (it.length <= 254) email = it },
            label = { Text("E-mail") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = senha,
            onValueChange = { if (it.length <= 128) senha = it },
            label = { Text("Senha") },
            visualTransformation = if (senhaVisivel) VisualTransformation.None
            else PasswordVisualTransformation(),
            trailingIcon = {
                val imagem = if (senhaVisivel) Icons.Default.Visibility
                else Icons.Default.VisibilityOff
                val descricao = if (senhaVisivel) "Esconder Senha"
                else "Mostrar Senha"

                IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                    Icon(imageVector = imagem, contentDescription = descricao)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        Text("Use uma senha entre 6 e 128 caracteres")
        authViewModel.mensagem?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }
        if (authViewModel.carregando) Text("Aguarde...")

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                authViewModel.cadastrar(nome, email, senha)
                senha = ""
            },
            enabled = !authViewModel.carregando,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cadastrar")
        }

        TextButton(onClick = irLogin, enabled = !authViewModel.carregando) {
            Text("Já tenho cadastro.")
        }
    }
}

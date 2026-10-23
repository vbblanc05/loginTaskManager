package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen(
    irParaTarefas: () -> Unit,
    irParaSprints: () -> Unit,
    irParaUsuarios: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Home",
            style = MaterialTheme.typography.headlineLarge
        )
        Button(onClick = irParaTarefas) {
            Text("Ir para tarefas")
        }
        Button(onClick = irParaSprints) {
            Text("Ir para sprints")
        }
        Button(onClick = irParaUsuarios) {
            Text("Gerenciar Usuarios")
        }
    }
}

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
fun ProdutosDetalhesScreen(
    produtoId: Int,
    irHome: () -> Unit,
    voltar: () -> Unit
){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Detalhes do produto",
            style = MaterialTheme.typography.headlineMedium)
        Text("ID Recebido:  $produtoId")

        Button(onClick = irHome) {
            Text("Ir para Home")
        }

        Button(onClick = voltar) {
            Text("Voltar")
        }
    }
}
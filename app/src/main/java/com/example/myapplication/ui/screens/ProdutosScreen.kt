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
fun ProdutosScreen(
    irParaDetalhesProduto: (Int) -> Unit,
    voltar: () -> Unit
){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Produtos",
            style = MaterialTheme.typography.headlineMedium)

        Button(onClick = {irParaDetalhesProduto(10)}) {
            Text("Ver detalhes")
        }
        Button(onClick = voltar) {
            Text("Voltar")
        }
    }
}
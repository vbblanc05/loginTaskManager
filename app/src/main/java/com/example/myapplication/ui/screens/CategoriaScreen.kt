package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CategoriaScreen(){
    Column(
        modifier = Modifier.statusBarsPadding()
    ) {
        Text("Tela Categoria")
    }
}

package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable


@Serializable
data object Home

@Serializable
data object Produtos

@Serializable
data object Categoria

@Serializable
data class ProdutoDetalhes(val id: Int)

@Serializable
data object Login

@Serializable
data object Cadastro
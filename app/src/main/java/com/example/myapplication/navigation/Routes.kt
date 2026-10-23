package com.example.myapplication.navigation

import kotlinx.serialization.Serializable

@Serializable
data object Home

@Serializable
data object Tarefas

@Serializable
data object TarefaCadastrar

@Serializable
data class TarefaEditar(val id: Long)

@Serializable
data object Sprints

@Serializable
data object SprintCadastrar

@Serializable
data class SprintEditar(val id: Long)

@Serializable
data object Login

@Serializable
data object Cadastro

@Serializable
data object CadastrarUsuario

@Serializable
data class UsuarioEditar(val id: Long)

@Serializable
data object Usuarios

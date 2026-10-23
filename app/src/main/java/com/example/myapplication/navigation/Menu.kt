package com.example.myapplication.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.myapplication.auth.SessaoUsuario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Menu(
    navController: NavHostController
){
    TopAppBar(
        title = {
            Text("Minha Aplicação")
        },
        actions = {
            TextButton(onClick = {
                navController.navigate(Home)
            }) {
                Text("Home")
            }
            TextButton(onClick = {
                navController.navigate(Tarefas)
            }) {
                Text("Tarefas")
            }
            TextButton(onClick = {
                navController.navigate(Sprints)
            }) {
                Text("Sprints")
            }

            IconButton(
                onClick = {
                    SessaoUsuario.encerrar()
                    navController.navigate(Login){
                        popUpTo(0) { inclusive = true }
                    }
                }
            ) {
                Icon(
                   imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Logout")
            }
        }

    )
}
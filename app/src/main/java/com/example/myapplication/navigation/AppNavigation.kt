package com.example.myapplication.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.myapplication.ui.screens.CadastroScreen
import com.example.myapplication.ui.screens.CategoriaScreen
import com.example.myapplication.ui.screens.HomeScreen
import com.example.myapplication.ui.screens.LoginScreen
import com.example.myapplication.ui.screens.ProdutosDetalhesScreen
import com.example.myapplication.ui.screens.ProdutosScreen

@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier){
    NavHost(
        navController = navController,
        startDestination = Login
    ){
        composable<Home>{
            HomeScreen(
                irParaProdutos = {
                    navController.navigate(Produtos)
                }
            )
        }

        composable<Produtos> {
            ProdutosScreen(irParaDetalhesProduto = {
                    id -> navController
                .navigate(ProdutoDetalhes(id))
            },
                voltar = {
                    navController.popBackStack()
                })
        }

        composable<Categoria> {
            CategoriaScreen()
        }

        composable<ProdutoDetalhes>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(600)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(900)
                )
            }
        ){ back ->
            val rota = back.toRoute<ProdutoDetalhes>()
            ProdutosDetalhesScreen(
                produtoId = rota.id,
                irHome = {
                    navController.popBackStack(
                        route = Home,
                        inclusive = false
                    )
                },
                voltar = {
                    navController.popBackStack()
                }
            )

        }

    composable<Login>{
        LoginScreen(
            loginSucesso = {
                navController.navigate(Home){
                    popUpTo(Login){ inclusive = true}
                }
            },
            irCadastro = { navController.navigate(Cadastro)}
        )
    }
        composable<Cadastro> {
            CadastroScreen(
                irLogin = {
                    navController.popBackStack()
                }
            )
        }
    }
}
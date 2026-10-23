package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import com.example.myapplication.auth.SessaoUsuario
import com.example.myapplication.ui.screens.CadastroScreen
import com.example.myapplication.ui.screens.HomeScreen
import com.example.myapplication.ui.screens.LoginScreen
import com.example.myapplication.ui.screens.SprintCadastrarScreen
import com.example.myapplication.ui.screens.SprintEditarScreen
import com.example.myapplication.ui.screens.SprintsScreen
import com.example.myapplication.ui.screens.TarefaCadastrarScreen
import com.example.myapplication.ui.screens.TarefaEditarScreen
import com.example.myapplication.ui.screens.TarefasScreen
import com.example.myapplication.ui.screens.UsuarioEditarScreen
import com.example.myapplication.ui.screens.UsuariosScreen
import com.example.myapplication.ui.viewmodel.SprintViewModel
import com.example.myapplication.ui.viewmodel.TarefaViewModel
import com.example.myapplication.ui.viewmodel.UsuarioViewModel

@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier) {
    val rotaAtual by navController.currentBackStackEntryAsState()
    val destino = rotaAtual?.destination
    val usuarioAtual = SessaoUsuario.usuarioId

    LaunchedEffect(destino, usuarioAtual) {
        val publica = destino?.hasRoute<Login>() == true ||
                destino?.hasRoute<Cadastro>() == true

        if (destino != null && !publica && usuarioAtual == null) {
            navController.navigate(Login) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Login,
        modifier = modifier
    ) {
        composable<Home> {
            HomeScreen(
                irParaTarefas = {
                    navController.navigate(Tarefas)
                },
                irParaSprints = {
                    navController.navigate(Sprints)
                },
                irParaUsuarios = {
                    navController.navigate(Usuarios)
                }
            )
        }

        composable<Usuarios> { entrada ->
            val usuarioViewModel: UsuarioViewModel = viewModel()
            DisposableEffect(entrada) {
                val observer = LifecycleEventObserver { _, evento ->
                    if (evento == Lifecycle.Event.ON_RESUME) usuarioViewModel.listar()
                }
                entrada.lifecycle.addObserver(observer)
                onDispose { entrada.lifecycle.removeObserver(observer) }
            }
            UsuariosScreen(
                viewModel = usuarioViewModel,
                irParaCadastro = { navController.navigate(CadastrarUsuario) },
                irParaEdicao = { navController.navigate(UsuarioEditar(it)) },
                voltar = { navController.popBackStack() }
            )
        }

        composable<Tarefas> { entrada ->
            val tarefaViewModel: TarefaViewModel = viewModel()
            DisposableEffect(entrada) {
                val observer = LifecycleEventObserver { _, evento ->
                    if (evento == Lifecycle.Event.ON_RESUME) tarefaViewModel.listar()
                }
                entrada.lifecycle.addObserver(observer)
                onDispose { entrada.lifecycle.removeObserver(observer) }
            }
            TarefasScreen(
                viewModel = tarefaViewModel,
                irParaCadastro = { navController.navigate(TarefaCadastrar) },
                irParaEdicao = { navController.navigate(TarefaEditar(it)) },
                voltar = { navController.popBackStack() }
            )
        }

        composable<TarefaCadastrar> {
            TarefaCadastrarScreen(
                viewModel = viewModel(),
                voltar = { navController.popBackStack() }
            )
        }

        composable<TarefaEditar> { entrada ->
            val rota = entrada.toRoute<TarefaEditar>()
            TarefaEditarScreen(
                tarefaId = rota.id,
                viewModel = viewModel(),
                voltar = { navController.popBackStack() }
            )
        }

        composable<Sprints> { entrada ->
            val sprintViewModel: SprintViewModel = viewModel()
            DisposableEffect(entrada) {
                val observer = LifecycleEventObserver { _, evento ->
                    if (evento == Lifecycle.Event.ON_RESUME) sprintViewModel.listar()
                }
                entrada.lifecycle.addObserver(observer)
                onDispose { entrada.lifecycle.removeObserver(observer) }
            }
            SprintsScreen(
                viewModel = sprintViewModel,
                irParaCadastro = { navController.navigate(SprintCadastrar) },
                irParaEdicao = { navController.navigate(SprintEditar(it)) },
                voltar = { navController.popBackStack() }
            )
        }

        composable<SprintCadastrar> {
            SprintCadastrarScreen(
                viewModel = viewModel(),
                voltar = { navController.popBackStack() }
            )
        }

        composable<SprintEditar> { entrada ->
            val rota = entrada.toRoute<SprintEditar>()
            SprintEditarScreen(
                sprintId = rota.id,
                viewModel = viewModel(),
                voltar = { navController.popBackStack() }
            )
        }

        composable<Login> {
            LoginScreen(
                loginSucesso = {
                    navController.navigate(Home) {
                        popUpTo(Login) { inclusive = true }
                    }
                },
                irCadastro = { navController.navigate(Cadastro) },
                authViewModel = viewModel()
            )
        }

        composable<Cadastro> {
            CadastroScreen(
                irLogin = {
                    navController.popBackStack()
                },
                authViewModel = viewModel()
            )
        }

        composable<CadastrarUsuario> {
            CadastroScreen(
                authViewModel = viewModel(),
                irLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable<UsuarioEditar> { entrada ->
            val rota = entrada.toRoute<UsuarioEditar>()
            UsuarioEditarScreen(
                usuarioId = rota.id,
                viewModel = viewModel(),
                voltar = { navController.popBackStack() }
            )
        }
    }
}

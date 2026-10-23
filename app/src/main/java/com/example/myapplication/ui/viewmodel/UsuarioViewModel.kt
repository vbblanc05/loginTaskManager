package com.example.myapplication.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.auth.AuthManager
import com.example.myapplication.auth.SessaoUsuario
import com.example.myapplication.data.dal.UsuarioDao
import com.example.myapplication.data.model.Usuario
import com.example.myapplication.validation.CadastroValidator
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UsuarioViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = UsuarioDao(application)
    val usuarioAtivoId: Long? get() = SessaoUsuario.usuarioId
    var usuarios by mutableStateOf<List<Usuario>>(emptyList())
        private set
    var usuario by mutableStateOf<Usuario?>(null)
        private set
    var nome by mutableStateOf("")
        private set
    var carregando by mutableStateOf(false)
        private set
    var salvo by mutableStateOf(false)
        private set
    var mensagem by mutableStateOf<String?>(null)
        private set

    private fun executar(op: suspend () -> Unit) {
        if (carregando || salvo) return
        carregando = true
        mensagem = null
        viewModelScope.launch {
            try {
                SessaoUsuario.exigirUsuario()
                op()
            } catch (e: CancellationException) {
                throw e
            } catch (e: IllegalArgumentException) {
                mensagem = e.message
            } catch (e: Exception) {
                mensagem = "Um erro inesperado aconteceu."
            } finally {
                carregando = false
            }
        }
    }

    fun listar() = executar {
        val lista = withContext(Dispatchers.IO) { dao.listar() }
        usuarios = lista
    }

    fun buscarPorId(id: Long) {
        if (usuario?.id == id) return
        executar {
            val encontrado = withContext(Dispatchers.IO) { dao.buscarPorId(id) }
            usuario = requireNotNull(encontrado) { "Usuário não encontrado" }
            nome = encontrado.nome
        }
    }

    fun mudarNome(valor: String) {
        if (!carregando && valor.length <= 100) nome = valor
    }

    fun salvar(novaSenha: String) = executar {
        val usuarioSelecionado = requireNotNull(usuario) { "Selecione um usuário" }

        CadastroValidator.validar(nome, novaSenha)
        withContext(Dispatchers.IO) {
            if (novaSenha.isEmpty()) {
                dao.alterar(usuarioSelecionado.id, nome)
            } else {
                val salt = AuthManager.criarSalt()
                val hash = AuthManager.gerarHash(novaSenha.trim(), salt)

                try {
                    dao.alterar(usuarioSelecionado.id, nome, hash, salt)
                } finally {
                    hash.fill(0)
                }
            }
            salvo = true
        }
    }

    fun excluir(id: Long) = executar {
        val atual = SessaoUsuario.exigirUsuario()
        require(id != atual) { "Você não pode apagar o seu usuário" }
        withContext(Dispatchers.IO) { dao.excluir(id, atual) }
        usuarios = usuarios.filterNot { it.id == id }
        mensagem = "Usuario Excluído"
    }

    fun consumirSalvo() {
        salvo = false
    }
}

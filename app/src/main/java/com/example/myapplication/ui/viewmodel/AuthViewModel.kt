package com.example.myapplication.ui.viewmodel

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.auth.AuthManager
import com.example.myapplication.auth.SessaoUsuario
import com.example.myapplication.data.dal.UsuarioDao
import com.example.myapplication.validation.CadastroValidator
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = UsuarioDao(application)
    var carregando by mutableStateOf(false)
        private set
    var concluido by mutableStateOf(false)
        private set
    var mensagem by mutableStateOf<String?>(null)

    private fun executar(op: suspend () -> Boolean) {
        if (carregando || concluido) return
        carregando = true
        mensagem = null

        viewModelScope.launch {
            try {
                concluido = withContext(Dispatchers.IO) { op() }
                if (!concluido) mensagem = "E-mail ou senha Inválido."
            } catch (e: CancellationException) {
                throw e
            } catch (e: SQLiteConstraintException) {
                mensagem = "Não foi possível cadastrar."
            } catch (e: IllegalArgumentException) {
                mensagem = e.message
            } catch (e: Exception) {
                mensagem = "Ocorreu um erro inesperado."
            } finally {
                carregando = false
            }
        }
    }

    fun cadastrar(nome: String, email: String, senha: String) = executar {
        val nomeLimpo = nome.trim()
        val emailLimpo = email.trim().lowercase()
        val senhaLimpa = senha.trim()

        CadastroValidator.validar(nomeLimpo, emailLimpo, senhaLimpa)

        val salt = AuthManager.criarSalt()
        val senhaHash = AuthManager.gerarHash(senhaLimpa, salt)
        try {
            dao.cadastrar(nomeLimpo, emailLimpo, senhaHash, salt)
            true
        } finally {
            senhaHash.fill(0)
        }
    }

    fun login(email: String, senha: String) = executar {
        val emailLimpo = email.trim().lowercase()
        val senhaLimpa = senha.trim()
        require(emailLimpo.length <= 254 && senhaLimpa.length in 6..128) {
            "E-mail ou senha inválido"
        }

        val idAutenticado = withContext(Dispatchers.IO) {
            val dados = dao.buscarSenha(emailLimpo)
            val senhaHash = dados?.hash ?: ByteArray(32)
            val salt = dados?.salt ?: ByteArray(16)

            try {
                val autenticado = AuthManager.validarSenha(senhaLimpa, salt, senhaHash)
                if (autenticado) dados?.usuarioID else null
            } finally {
                senhaHash.fill(0)
            }
        }
        if (idAutenticado != null) {
            SessaoUsuario.iniciar(idAutenticado)
            true
        } else false
    }

    fun consumirResultado() {
        concluido = false
    }
}

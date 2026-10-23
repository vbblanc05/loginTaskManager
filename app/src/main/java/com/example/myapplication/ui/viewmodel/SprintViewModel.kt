package com.example.myapplication.ui.viewmodel

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.auth.SessaoUsuario
import com.example.myapplication.data.dal.SprintDao
import com.example.myapplication.data.model.Sprint
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SprintViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = SprintDao(application)
    val usuarioAtivoId: Long? get() = SessaoUsuario.usuarioId
    var sprints by mutableStateOf<List<Sprint>>(emptyList())
        private set
    var sprint by mutableStateOf<Sprint?>(null)
        private set
    var nome by mutableStateOf("")
        private set
    var descricao by mutableStateOf("")
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
            } catch (e: SQLiteConstraintException) {
                mensagem = "Não foi possível salvar."
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
        sprints = lista
    }

    fun buscarPorId(id: Long) {
        if (sprint?.id == id) return
        executar {
            val encontrada = withContext(Dispatchers.IO) { dao.buscarPorId(id) }
            sprint = requireNotNull(encontrada) { "Sprint não encontrada" }
            nome = encontrada.nome
            descricao = encontrada.descricao
        }
    }

    fun mudarNome(valor: String) {
        if (!carregando && valor.length <= 100) nome = valor
    }

    fun mudarDescricao(valor: String) {
        if (!carregando && valor.length <= 300) descricao = valor
    }

    fun cadastrar() = executar {
        val nomeLimpo = nome.trim()
        val descricaoLimpa = descricao.trim()
        validar(nomeLimpo, descricaoLimpa)
        withContext(Dispatchers.IO) { dao.cadastrar(nomeLimpo, descricaoLimpa) }
        salvo = true
    }

    fun salvar() = executar {
        val selecionada = requireNotNull(sprint) { "Selecione uma sprint" }
        val nomeLimpo = nome.trim()
        val descricaoLimpa = descricao.trim()
        validar(nomeLimpo, descricaoLimpa)
        withContext(Dispatchers.IO) {
            dao.alterar(selecionada.id, nomeLimpo, descricaoLimpa)
        }
        salvo = true
    }

    fun excluir(id: Long) = executar {
        withContext(Dispatchers.IO) { dao.excluir(id) }
        sprints = sprints.filterNot { it.id == id }
        mensagem = "Sprint excluída"
    }

    fun consumirSalvo() {
        salvo = false
    }

    private fun validar(nome: String, descricao: String) {
        require(nome.length in 3..100) {
            "Informe um nome entre 3 e 100 caracteres."
        }
        require(descricao.length in 3..300) {
            "Informe uma descrição entre 3 e 300 caracteres."
        }
    }
}

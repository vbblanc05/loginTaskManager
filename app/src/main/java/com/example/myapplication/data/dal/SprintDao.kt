package com.example.myapplication.data.dal

import android.content.ContentValues
import android.content.Context
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.model.Sprint

internal class SprintDao(context: Context) {
    private val appContext = context.applicationContext

    fun cadastrar(nome: String, descricao: String) {
        AppDatabase(appContext).use { db ->
            val dados = ContentValues().apply {
                put("nome", nome)
                put("descricao", descricao)
            }
            db.writableDatabase.insertOrThrow("sprints", null, dados)
        }
    }

    fun listar(): List<Sprint> = AppDatabase(appContext).use { db ->
        db.readableDatabase.query(
            "sprints",
            arrayOf("id", "nome", "descricao"),
            null, null, null, null,
            "nome COLLATE NOCASE, id"
        ).use { cursor ->
            buildList {
                while (cursor.moveToNext()) {
                    add(
                        Sprint(
                            cursor.getLong(0),
                            cursor.getString(1),
                            cursor.getString(2)
                        )
                    )
                }
            }
        }
    }

    fun buscarPorId(id: Long): Sprint? = AppDatabase(appContext).use { db ->
        db.readableDatabase.query(
            "sprints",
            arrayOf("id", "nome", "descricao"),
            "id = ?",
            arrayOf(id.toString()),
            null, null, null
        ).use { cursor ->
            if (cursor.moveToFirst()) Sprint(
                cursor.getLong(0),
                cursor.getString(1),
                cursor.getString(2)
            ) else null
        }
    }

    fun alterar(id: Long, nome: String, descricao: String) {
        AppDatabase(appContext).use { db ->
            val dados = ContentValues().apply {
                put("nome", nome)
                put("descricao", descricao)
            }
            val alterados = db.writableDatabase.update(
                "sprints",
                dados,
                "id = ?",
                arrayOf(id.toString())
            )
            require(alterados == 1) { "Erro ao alterar a sprint" }
        }
    }

    fun excluir(id: Long) {
        AppDatabase(appContext).use { db ->
            val excluidos = db.writableDatabase.delete(
                "sprints",
                "id = ?",
                arrayOf(id.toString())
            )
            require(excluidos == 1) { "Sprint não pode ser excluída" }
        }
    }
}

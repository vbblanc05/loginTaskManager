package com.example.myapplication.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.File

internal class AppDatabase(context: Context) : SQLiteOpenHelper(
    context.applicationContext,
    File(context.noBackupFilesDir, "aula_sqlite.db").absolutePath,
    null,
    2
) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE usuarios(
            id INTEGER PRIMARY KEY,
            nome TEXT NOT NULL CHECK(length(nome) BETWEEN 3 AND 100),
            email TEXT NOT NULL UNIQUE CHECK(length(email) BETWEEN 3 AND 254),
            senha BLOB NOT NULL,
            salt BLOB NOT NULL
            )
            """.trimIndent()
        )
        criarTarefas(db)
        criarSprints(db)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        if (db == null) return
        db.execSQL("DROP TABLE IF EXISTS produtos")
        db.execSQL("DROP TABLE IF EXISTS categorias")
        db.execSQL("DROP TABLE IF EXISTS tarefas")
        db.execSQL("DROP TABLE IF EXISTS sprints")
        criarTarefas(db)
        criarSprints(db)
    }

    private fun criarTarefas(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE tarefas(
            id INTEGER PRIMARY KEY,
            nome TEXT NOT NULL CHECK(length(nome) BETWEEN 3 AND 100),
            descricao TEXT NOT NULL CHECK(length(descricao) BETWEEN 3 AND 300)
            )
            """.trimIndent()
        )
    }

    private fun criarSprints(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE sprints(
            id INTEGER PRIMARY KEY,
            nome TEXT NOT NULL CHECK(length(nome) BETWEEN 3 AND 100),
            descricao TEXT NOT NULL CHECK(length(descricao) BETWEEN 3 AND 300)
            )
            """.trimIndent()
        )
    }
}

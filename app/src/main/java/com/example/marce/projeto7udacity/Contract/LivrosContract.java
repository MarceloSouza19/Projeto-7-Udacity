package com.example.marce.projeto7udacity.Contract;

import android.provider.BaseColumns;

public class LivrosContract implements BaseColumns {

    public static final String TABLE_NAME = "livros";

    public static final String COLUNA_ID = BaseColumns._ID;
    public static final String COLUNA_NOME_LIVRO = "nome_livro";
    public static final String COLUNA_MODELO = "modelo";
    public static final String COLUNA_PREÇO = "preço";
    public static final String COLUNA_QUANTIDADE = "quantidade";


    public static final int MODELO_DESCONHECIDO = 0;
    public static final int MODELO_LITERATURA = 1;
    public static final int MODELO_INFANTIL = 2;


    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " +TABLE_NAME +
            " (" + COLUNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
            COLUNA_NOME_LIVRO + " TEXT_TYPE ," +
            COLUNA_MODELO + " TEXT_TYPE ," +
            COLUNA_PREÇO + " REAL ," +
            COLUNA_QUANTIDADE + " INTEGER);";

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "+TABLE_NAME;

}

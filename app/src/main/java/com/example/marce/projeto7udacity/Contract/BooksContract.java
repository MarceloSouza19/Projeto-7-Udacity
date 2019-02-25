package com.example.marce.projeto7udacity.Contract;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.BaseColumns;

public class BooksContract implements BaseColumns {

    public static final String TABLE_NAME = "livros";

    public static final String CONTENT_AUTHORITY = "com.example.marce.projeto7udacity";
    public static final String PATH_BOOKS = "livros";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_BOOKS;
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_BOOKS;

    public static final String COLUNA_ID = BaseColumns._ID;
    public static final String COLUNA_NOME_LIVRO = "nome_livro";
    public static final String COLUNA_DESCRICAO = "modelo";
    public static final String COLUNA_PREÇO = "preço";
    public static final String COLUNA_QUANTIDADE = "quantidade";
    public static final String COLUNA_FORNECEDOR="fornecedor";
    public static final String COLUNA_TELEFONE="telefone";
    public static final String COLUNA_IMAGEM="imagem";


    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS);

    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " +TABLE_NAME +
            " (" + COLUNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
            COLUNA_NOME_LIVRO + " TEXT ," +
            COLUNA_DESCRICAO + " TEXT ," +
            COLUNA_PREÇO + " REAL ," +
            COLUNA_QUANTIDADE + " INTEGER ,"+
            COLUNA_FORNECEDOR+" TEXT ,"+
            COLUNA_TELEFONE+" TEXT ,"+
            COLUNA_IMAGEM+" BLOB );";

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "+TABLE_NAME;

}

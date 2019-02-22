package com.example.marce.projeto7udacity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.marce.projeto7udacity.Contract.LivrosContract;

import com.example.marce.projeto7udacity.Conexão.ConexãoDbHelper;

import java.util.ArrayList;
import java.util.List;

public class TelaPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

        ConexãoDbHelper dbHelper = new ConexãoDbHelper(this);

        SQLiteDatabase dbWrite = dbHelper.getWritableDatabase();

        for(ContentValues conteudo : this.retornarDados()) {
            dbWrite.insert(LivrosContract.TABLE_NAME, null, conteudo);
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + LivrosContract.TABLE_NAME, null);


        int idColunaIndex           = cursor.getColumnIndex(LivrosContract.COLUNA_ID);
        int indexNomeLivro          = cursor.getColumnIndex(LivrosContract.COLUNA_NOME_LIVRO);
        int indexModeloLivro        = cursor.getColumnIndex(LivrosContract.COLUNA_MODELO);
        int indexPrecoLivro         = cursor.getColumnIndex(LivrosContract.COLUNA_PREÇO);
        int indexQTDLivro           = cursor.getColumnIndex(LivrosContract.COLUNA_QUANTIDADE);
        int indexFornecedorLivro    = cursor.getColumnIndex(LivrosContract.COLUNA_FORNECEDOR);
        int indexTelefoneForn       = cursor.getColumnIndex(LivrosContract.COLUNA_TELEFONE);


        StringBuilder texto = new StringBuilder();

        while (cursor.moveToNext()) {

            int idAtual            = cursor.getInt(idColunaIndex);
            String nomeLivro       = cursor.getString(indexNomeLivro);
            String modeloLivro     = cursor.getString(indexModeloLivro);
            String precoLivro      = cursor.getString(indexPrecoLivro);
            String qtdLivro        = cursor.getString(indexQTDLivro);
            String fornecedorLivro = cursor.getString(indexFornecedorLivro);
            String telefoneForn    = cursor.getString(indexTelefoneForn);

            switch(Integer.parseInt(modeloLivro)){
                case 0:
                    modeloLivro = "Desconhecido";
                    break;
                case 1:
                    modeloLivro="Literatura";
                    break;
            }

            texto.append("\n Id Cadastro - " + idAtual + " | "
                    + "\n Nome Livro - " + nomeLivro + " | "
                    + "\n Modelo Livro - " + modeloLivro + " | "
                    + "\n Preço Livro - " + precoLivro + " | "
                    + "\n QTD Disponivel - " + qtdLivro+ " | "
                    + "\n Fornecedor - "+fornecedorLivro+" | "
                    + "\n Telefone - "+telefoneForn + "\n"
                    + "________________________");
        }

        TextView teste = findViewById(R.id.Teste);

        teste.setText(texto.toString());

        cursor.close();
    }

public List<ContentValues> retornarDados(){

        List<ContentValues> conteudo = new ArrayList<>();

    ContentValues contentValues = new ContentValues();

    contentValues.put(LivrosContract.COLUNA_NOME_LIVRO, "Meu Primeiro Dado");
    contentValues.put(LivrosContract.COLUNA_MODELO, LivrosContract.MODELO_DESCONHECIDO);
    contentValues.put(LivrosContract.COLUNA_PREÇO, "150.00");
    contentValues.put(LivrosContract.COLUNA_QUANTIDADE, "50");
    contentValues.put(LivrosContract.COLUNA_FORNECEDOR,"Livraria Amor");
    contentValues.put(LivrosContract.COLUNA_TELEFONE,"11 1234-5678");

    conteudo.add(contentValues);

    contentValues = new ContentValues();

    contentValues.put(LivrosContract.COLUNA_NOME_LIVRO, "É o Amor 1");
    contentValues.put(LivrosContract.COLUNA_MODELO, LivrosContract.MODELO_LITERATURA);
    contentValues.put(LivrosContract.COLUNA_PREÇO, "125.40");
    contentValues.put(LivrosContract.COLUNA_QUANTIDADE, "12");
    contentValues.put(LivrosContract.COLUNA_FORNECEDOR,"Livraria Top");
    contentValues.put(LivrosContract.COLUNA_TELEFONE,"14 5678-1234");

    conteudo.add(contentValues);

    return conteudo;
}
}

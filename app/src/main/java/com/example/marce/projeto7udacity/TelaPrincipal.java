package com.example.marce.projeto7udacity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.example.marce.projeto7udacity.Contract.LivrosContract;

import com.example.marce.projeto7udacity.Conexão.ConexãoDbHelper;

public class TelaPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

        ConexãoDbHelper dbHelper = new ConexãoDbHelper(this);

        SQLiteDatabase dbWrite = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(LivrosContract.COLUNA_NOME_LIVRO,"Teste 1");
        contentValues.put(LivrosContract.COLUNA_MODELO,"AAAA");
        contentValues.put(LivrosContract.COLUNA_PREÇO,"150.00");
        contentValues.put(LivrosContract.COLUNA_QUANTIDADE,"50");

        dbWrite.insert(LivrosContract.TABLE_NAME,null,contentValues);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+LivrosContract.TABLE_NAME,null);

        TextView teste = findViewById(R.id.Teste);

        teste.setText(cursor.getCount()+"");
    }


}

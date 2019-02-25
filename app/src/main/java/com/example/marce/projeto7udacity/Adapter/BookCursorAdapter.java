package com.example.marce.projeto7udacity.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.marce.projeto7udacity.Contract.BooksContract;
import com.example.marce.projeto7udacity.R;

import java.util.zip.Inflater;

public class BookCursorAdapter extends CursorAdapter {

    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

       View view = LayoutInflater.from(context).inflate(R.layout.item_list_book, parent,false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView bookName = view.findViewById(R.id.bookName);
        TextView bookModel = view.findViewById(R.id.bookModel);


        int columnIndexName = cursor.getColumnIndex(BooksContract.COLUNA_NOME_LIVRO);
        int columnIndexModel = cursor.getColumnIndex(BooksContract.COLUNA_DESCRICAO);

        String stringName = cursor.getString(columnIndexName);
        String stringModel = cursor.getString(columnIndexModel);

        if(stringModel.length()==0){
            stringModel="Não identificado";
        }
        bookName.setText(stringName);
        bookModel.setText(stringModel);

    }
}

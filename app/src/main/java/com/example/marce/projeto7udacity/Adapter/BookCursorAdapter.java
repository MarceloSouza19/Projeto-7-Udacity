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
        TextView bookPrice = view.findViewById(R.id.bookPrice);
        TextView bookQuantity = view.findViewById(R.id.bookQuantity);

        int columnIndexName = cursor.getColumnIndex(BooksContract.COLUNA_NOME_LIVRO);
        int columnIndexModel = cursor.getColumnIndex(BooksContract.COLUNA_DESCRICAO);
        int columnIndexPrice = cursor.getColumnIndex(BooksContract.COLUNA_PREÇO);
        int columnIndexQTD = cursor.getColumnIndex(BooksContract.COLUNA_QUANTIDADE);

        String stringName = cursor.getString(columnIndexName);
        String stringModel = cursor.getString(columnIndexModel);
        String stringPrice = cursor.getString(columnIndexPrice);
        String stringQuantity = cursor.getString(columnIndexQTD);

        if(stringModel.length()==0){
            stringModel=context.getResources().getString(R.string.nao_identificado);
        }
        bookName.setText(stringName);
        bookModel.setText(stringModel);
        bookPrice.setText(context.getResources().getString(R.string.valor_atual)+stringPrice);
        bookQuantity.setText(Integer.valueOf(stringQuantity)>0 ?
                stringQuantity+" "+context.getResources().getString(R.string.em_estoque) :
                    context.getResources().getString(R.string.sem_estoque));
    }
}

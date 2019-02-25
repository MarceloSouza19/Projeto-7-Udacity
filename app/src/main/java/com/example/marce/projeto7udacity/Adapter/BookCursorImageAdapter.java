package com.example.marce.projeto7udacity.Adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marce.projeto7udacity.Contract.BooksContract;
import com.example.marce.projeto7udacity.R;
import com.example.marce.projeto7udacity.Utils.Util;

public class BookCursorImageAdapter extends CursorAdapter {

    public BookCursorImageAdapter(Context context, Cursor cursor) {

        super(context, cursor,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_list_book_image, parent,false);
        return view;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView bookName = view.findViewById(R.id.bookName);
        TextView bookModel = view.findViewById(R.id.bookModel);
        ImageView imageView = view.findViewById(R.id.imageListItem);

        int columnIndexName = cursor.getColumnIndex(BooksContract.COLUNA_NOME_LIVRO);
        int columnIndexModel = cursor.getColumnIndex(BooksContract.COLUNA_DESCRICAO);
        int columnIndexImage = cursor.getColumnIndex(BooksContract.COLUNA_IMAGEM);

        String stringName = cursor.getString(columnIndexName);
        String stringModel = cursor.getString(columnIndexModel);
        byte[] imageItem = cursor.getBlob(columnIndexImage);

        if(imageItem!=null) {
            Bitmap bitmap = Util.getImage(imageItem);
            imageView.setImageBitmap(bitmap);
        } else {
            Drawable drawable = context.getDrawable(R.drawable.sem_imagem);
            drawable.setTint(context.getColor(R.color.colorWhiteE));
            imageView.setImageDrawable(drawable);
        }

        if(stringModel.length()==0){
            stringModel=context.getResources().getString(R.string.nao_identificado);
        }
        bookName.setText(stringName);
        bookModel.setText(stringModel);
    }
}

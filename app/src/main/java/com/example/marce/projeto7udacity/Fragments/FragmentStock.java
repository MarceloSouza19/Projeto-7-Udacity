package com.example.marce.projeto7udacity.Fragments;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;

import com.example.marce.projeto7udacity.Activities.EditorOrAddActivity;
import com.example.marce.projeto7udacity.Adapter.BookCursorAdapter;
import com.example.marce.projeto7udacity.Contract.BooksContract;
import com.example.marce.projeto7udacity.R;

public class FragmentStock extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    BookCursorAdapter mCursorAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View view =  LayoutInflater.from(getContext()).inflate(R.layout.fragment_stock,container,false);

        FloatingActionButton btnFlutuante = (FloatingActionButton) view.findViewById(R.id.testeB);
        btnFlutuante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FragmentStock.super.getContext(), EditorOrAddActivity.class);
                intent.putExtra("telaEdicao",false);
                getContext().startActivity(intent);
            }
        });

        ListView bookItemList = view.findViewById(R.id.recycler_item_livros);

        mCursorAdapter = new BookCursorAdapter(getContext(),null);

        bookItemList.setAdapter(mCursorAdapter);

        bookItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getContext(),EditorOrAddActivity.class);

                Uri currentBookUri = ContentUris.withAppendedId(BooksContract.CONTENT_URI, l);

                intent.setData(currentBookUri);

                getContext().startActivity(intent);
            }
        });

        getLoaderManager().initLoader(1,null, this).forceLoad();

        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                BooksContract._ID,
                BooksContract.COLUNA_NOME_LIVRO,
                BooksContract.COLUNA_DESCRICAO};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(getContext(),   // Parent activity context
                BooksContract.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}

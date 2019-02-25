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
import android.widget.Toast;

import com.example.marce.projeto7udacity.Activities.DetailsItemActivity;
import com.example.marce.projeto7udacity.Activities.EditorOrAddActivity;
import com.example.marce.projeto7udacity.Adapter.BookCursorAdapter;
import com.example.marce.projeto7udacity.Adapter.BookCursorImageAdapter;
import com.example.marce.projeto7udacity.Contract.BooksContract;
import com.example.marce.projeto7udacity.R;

public class FragmentSale extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    BookCursorImageAdapter mCursorAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  LayoutInflater.from(getContext()).inflate(R.layout.fragment_sale,container,false);


        ListView bookItemList = view.findViewById(R.id.recycler_item_livros);

        mCursorAdapter = new BookCursorImageAdapter(getContext(),null);

        bookItemList.setAdapter(mCursorAdapter);

        bookItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getContext(),DetailsItemActivity.class);

                Uri currentBookUri = ContentUris.withAppendedId(BooksContract.CONTENT_URI, l);

                intent.setData(currentBookUri);

                Toast.makeText(getContext(),getContext().getResources().getString(R.string.processando),Toast.LENGTH_SHORT).show();

                getContext().startActivity(intent);
            }
        });

        getLoaderManager().initLoader(2,null, this).forceLoad();

        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                BooksContract._ID,
                BooksContract.COLUNA_NOME_LIVRO,
                BooksContract.COLUNA_DESCRICAO,
                BooksContract.COLUNA_IMAGEM};

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

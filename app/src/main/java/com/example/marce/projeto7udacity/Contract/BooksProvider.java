package com.example.marce.projeto7udacity.Contract;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.marce.projeto7udacity.ConnectionDB.ConnectionDbHelper;
import com.example.marce.projeto7udacity.R;

public class BooksProvider extends ContentProvider {

    private static final String LOG_TAG = BooksProvider.class.getSimpleName();
    private static final int BOOKS = 100;
    private static final int BOOKS_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(BooksContract.CONTENT_AUTHORITY, BooksContract.PATH_BOOKS, BOOKS);

        sUriMatcher.addURI(BooksContract.CONTENT_AUTHORITY, BooksContract.PATH_BOOKS + "/#", BOOKS_ID);
    }

    private ConnectionDbHelper mConnectionDBHelper;

    @Override
    public boolean onCreate() {
        mConnectionDBHelper = new ConnectionDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sorterOrder) {

        SQLiteDatabase dataBase = mConnectionDBHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);

        switch (match) {
            case BOOKS:

                cursor = dataBase.query(BooksContract.TABLE_NAME, projection, selection, selectionArgs, null, null, sorterOrder);
                break;
            case BOOKS_ID:

                selection = BooksContract.COLUNA_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = dataBase.query(BooksContract.TABLE_NAME, projection, selection, selectionArgs, null, null, sorterOrder);
                break;
            default:
                throw new IllegalArgumentException(getContext().getResources().getString(R.string.sem_metodo_busca));

        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {

        int match = sUriMatcher.match(uri);

        switch (match){
            case BOOKS:
                return BooksContract.CONTENT_LIST_TYPE;
            case BOOKS_ID:
                return BooksContract.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException(getContext().getResources().getString(R.string.sem_metodo_busca)+uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case BOOKS:
                return insertBooks(uri, contentValues);
            default:
                throw new IllegalArgumentException(getContext().getResources().getString(R.string.sem_metodo_busca)+ uri);
        }

    }

    public Uri insertBooks(Uri uri, ContentValues contentValues) {

        if (contentValues.containsKey(BooksContract.COLUNA_NOME_LIVRO)) {
            String bookName = contentValues.getAsString(BooksContract.COLUNA_NOME_LIVRO);
            if (!(bookName.length() > 0)) {
                Toast.makeText(getContext(),getContext().getResources().getString(R.string.sem_nome),Toast.LENGTH_SHORT).show();
                return null;
            }
        }
        if (contentValues.containsKey(BooksContract.COLUNA_PREÇO)) {
            double bookPrice = contentValues.getAsDouble(BooksContract.COLUNA_PREÇO);
            if (!(bookPrice > 0 )) {
                Toast.makeText(getContext(),getContext().getResources().getString(R.string.preco_zerado),Toast.LENGTH_SHORT).show();
                return null;
            }
            if(bookPrice >= 10000){
                Toast.makeText(getContext(),getContext().getResources().getString(R.string.preco_alto),Toast.LENGTH_SHORT).show();
                return null;
            }
        }
        if (contentValues.containsKey(BooksContract.COLUNA_QUANTIDADE)) {
            int bookQuantity = contentValues.getAsInteger(BooksContract.COLUNA_QUANTIDADE);
            if (!(bookQuantity > 0)) {
                Toast.makeText(getContext(),getContext().getResources().getString(R.string.quantidade_negativa),Toast.LENGTH_SHORT).show();
                return null;
            }
            if(bookQuantity >= 10000){
                Toast.makeText(getContext(),getContext().getResources().getString(R.string.estoque_alto),Toast.LENGTH_SHORT).show();
                return null;
            }
        }
        if (contentValues.containsKey(BooksContract.COLUNA_FORNECEDOR)) {
            String bookProvider = contentValues.getAsString(BooksContract.COLUNA_FORNECEDOR);
            if (!(bookProvider.length() > 0)) {
                Toast.makeText(getContext(),getContext().getResources().getString(R.string.fornecedor_livro),Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        SQLiteDatabase database = mConnectionDBHelper.getWritableDatabase();

        long id = database.insert(BooksContract.TABLE_NAME, null, contentValues);

        if (id == -1) {
            Log.e(LOG_TAG, getContext().getResources().getString(R.string.sem_metodo_busca)+ uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);
        int idTransaction;
        SQLiteDatabase database = mConnectionDBHelper.getWritableDatabase();

        switch (match) {
            case BOOKS:

                idTransaction = database.delete(BooksContract.TABLE_NAME, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);

                break;
            case BOOKS_ID:
                selection = BooksContract.COLUNA_ID + "=?";

                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                idTransaction = database.delete(BooksContract.TABLE_NAME, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);

                break;
            default:
                throw new IllegalArgumentException(getContext().getResources().getString(R.string.sem_metodo_busca)+ uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return idTransaction;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case BOOKS:
                int rows = updateBook(uri, contentValues, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return rows;
            case BOOKS_ID:

                selection = BooksContract.COLUNA_ID + "=?";

                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                int rowsID =  updateBook(uri, contentValues, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return rowsID;
            default:
                throw new IllegalArgumentException(getContext().getResources().getString(R.string.sem_metodo_busca) + uri);
        }
    }

    public int updateBook(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        if (contentValues.containsKey(BooksContract.COLUNA_NOME_LIVRO)) {
            String bookName = contentValues.getAsString(BooksContract.COLUNA_NOME_LIVRO);
            if (!(bookName.length() > 0)) {
                Toast.makeText(getContext(),getContext().getResources().getString(R.string.sem_nome),Toast.LENGTH_SHORT).show();
                return 0;
            }
        }
        if (contentValues.containsKey(BooksContract.COLUNA_PREÇO)) {
            double bookPrice = contentValues.getAsDouble(BooksContract.COLUNA_PREÇO);
            if (!(bookPrice > 0 )) {
                Toast.makeText(getContext(),getContext().getResources().getString(R.string.preco_zerado),Toast.LENGTH_SHORT).show();
                return 0;
            }
            if(bookPrice >= 10000){
                Toast.makeText(getContext(),getContext().getResources().getString(R.string.preco_alto),Toast.LENGTH_SHORT).show();
                return 0;
            }
        }
        if (contentValues.containsKey(BooksContract.COLUNA_QUANTIDADE)) {
            int bookQuantity = contentValues.getAsInteger(BooksContract.COLUNA_QUANTIDADE);
            if (!(bookQuantity >= 0)) {
                Toast.makeText(getContext(),getContext().getResources().getString(R.string.quantidade_negativa),Toast.LENGTH_SHORT).show();
                return 0;
            }
            if(bookQuantity >= 10000){
                Toast.makeText(getContext(),getContext().getResources().getString(R.string.estoque_alto),Toast.LENGTH_SHORT).show();
                return 0;
            }
        }
        if (contentValues.containsKey(BooksContract.COLUNA_FORNECEDOR)) {
            String bookProvider = contentValues.getAsString(BooksContract.COLUNA_FORNECEDOR);
            if (!(bookProvider.length() > 0)) {
                Toast.makeText(getContext(),getContext().getResources().getString(R.string.fornecedor_livro),Toast.LENGTH_SHORT).show();
                return 0;
            }
        }

        SQLiteDatabase dataBase = mConnectionDBHelper.getWritableDatabase();

        int rowsUpdated = dataBase.update(BooksContract.TABLE_NAME, contentValues, selection, selectionArgs);

        return rowsUpdated;
    }
}

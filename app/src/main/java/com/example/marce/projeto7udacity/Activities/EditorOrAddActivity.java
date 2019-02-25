package com.example.marce.projeto7udacity.Activities;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marce.projeto7udacity.Adapter.BookCursorAdapter;
import com.example.marce.projeto7udacity.Contract.BooksContract;
import com.example.marce.projeto7udacity.Fragments.FragmentStock;
import com.example.marce.projeto7udacity.R;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class EditorOrAddActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private Uri mBookUri;
    BookCursorAdapter mCursorAdapter;

    TextView mbookName;
    TextView mbookPrice;
    TextView mbookModel;
    TextView mbookProvider;
    TextView mbookTelProvider;
    TextView mbookQuantity;

    String mName;
    double mPrice;
    String mModel;
    String mProvider;
    String mTelProvider;
    int mQuantity;
    byte[] imagem;

    public static int QUALITY_OK = 50;
    public static int QUALITY_MEDIUM = 30;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);

        getSupportActionBar().setHomeButtonEnabled(true);

        mbookName = findViewById(R.id.bookName);
        mbookPrice = findViewById(R.id.bookPrice);
        mbookModel = findViewById(R.id.bookModel);
        mbookProvider = findViewById(R.id.bookProvider);
        mbookTelProvider = findViewById(R.id.bookTelProvider);
        mbookQuantity = findViewById(R.id.bookQuantity);

        Intent intent = getIntent();

        mBookUri = intent.getData();

        if (mBookUri != null) {
            setTitle("Editando um Livro");
           getSupportLoaderManager().initLoader(1,null,this).forceLoad();
        } else {
            setTitle("Cadastrando um Livro");
        }

        FloatingActionButton btnFlutuante = (FloatingActionButton) findViewById(R.id.addPhoto);
        btnFlutuante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == 1){
            Uri imageSelected = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(imageSelected);
                Bitmap image = BitmapFactory.decodeStream(inputStream);

                ByteArrayOutputStream by = new ByteArrayOutputStream();
                if(image.getDensity()>350)
                image.compress(Bitmap.CompressFormat.JPEG,QUALITY_MEDIUM,by);
                else
                    image.compress(Bitmap.CompressFormat.JPEG,50,by);
                this.imagem = by.toByteArray();
                Toast.makeText(getApplicationContext(),"Imagem Selecionada :)",Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }



        }
    }

    public void setarDados(){
        mName = mbookName.getText().toString().trim();
        mPrice = 0;
        if(mbookPrice.getText().toString().length()>0){
            mPrice=Double.valueOf(mbookPrice.getText().toString());
        }
        mModel = mbookModel.getText().toString().trim();
        mProvider = mbookProvider.getText().toString().trim();
        mTelProvider = mbookTelProvider.getText().toString().trim();
        mQuantity = 0;
        if(mbookQuantity.getText().toString().length()>0){
            mQuantity=Integer.parseInt(mbookQuantity.getText().toString());
        }
    }

    public boolean saveBook() {

        this.setarDados();

        ContentValues contentValues = new ContentValues();

        contentValues.put(BooksContract.COLUNA_NOME_LIVRO, mName);
        contentValues.put(BooksContract.COLUNA_DESCRICAO, mModel);
        contentValues.put(BooksContract.COLUNA_PREÇO, mPrice);
        contentValues.put(BooksContract.COLUNA_QUANTIDADE, mQuantity);
        contentValues.put(BooksContract.COLUNA_FORNECEDOR, mProvider);
        contentValues.put(BooksContract.COLUNA_TELEFONE, mTelProvider);

        if(this.imagem!=null) {
                contentValues.put(BooksContract.COLUNA_IMAGEM, imagem);
        }


        if (mBookUri == null) {
            Uri newUri = getContentResolver().insert(BooksContract.CONTENT_URI, contentValues);

            if(newUri==null)
                return false;
        } else {
            int a = getContentResolver().update(mBookUri, contentValues, null, null);

            if(a>0){
                Toast.makeText(getApplicationContext(),"Atualizado com Sucesso!",Toast.LENGTH_SHORT).show();
            } else{
                return false;
            }
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        super.onBackPressed();
        return;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {

        if(mBookUri!=null) {
            String[] projection = {
                    BooksContract.COLUNA_ID,
                    BooksContract.COLUNA_NOME_LIVRO,
                    BooksContract.COLUNA_DESCRICAO,
                    BooksContract.COLUNA_PREÇO,
                    BooksContract.COLUNA_QUANTIDADE,
                    BooksContract.COLUNA_FORNECEDOR,
                    BooksContract.COLUNA_TELEFONE,
            };

           CursorLoader c = new CursorLoader(this, mBookUri, projection, null, null, null);

           return c;
        }
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor objeto) {

        if(objeto!=null && objeto.moveToFirst()) {
            int nameIndex = objeto.getColumnIndex(BooksContract.COLUNA_NOME_LIVRO);
            int modelIndex = objeto.getColumnIndex(BooksContract.COLUNA_DESCRICAO);
            int priceIndex = objeto.getColumnIndex(BooksContract.COLUNA_PREÇO);
            int qtdIndex = objeto.getColumnIndex(BooksContract.COLUNA_QUANTIDADE);
            int providerIndex = objeto.getColumnIndex(BooksContract.COLUNA_FORNECEDOR);
            int providerTelIndex = objeto.getColumnIndex(BooksContract.COLUNA_TELEFONE);

            String sName = objeto.getString(nameIndex);
            String sModel = objeto.getString(modelIndex).trim();
            String dPrice = objeto.getString(priceIndex);
            String iQuantity = objeto.getString(qtdIndex);
            String sProvider = objeto.getString(providerIndex);
            String sProviderTel = objeto.getString(providerTelIndex);

            mbookName.setText(sName);
            mbookPrice.setText(dPrice.toString());
            mbookModel.setText(sModel != null ? sModel : "Não Informado");
            mbookProvider.setText(sProvider);
            mbookTelProvider.setText(sProviderTel);
            mbookQuantity.setText(iQuantity != null ? iQuantity.toString() : "0");
        }
        else if(objeto.getCount()<1){
            return;
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
       loader.reset();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_cadastro, menu);

        if(mBookUri==null){
            menu.getItem(1).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.confirmar:
                if(this.saveBook())
                finish();
                break;
            case R.id.deletar_objeto:
                if(mBookUri!=null)
                this.showDeleteConfirmationDialog();
                break;

        }
        return true;
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Deseja realmente deletar?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                getContentResolver().delete(mBookUri, null, null);
                finish();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

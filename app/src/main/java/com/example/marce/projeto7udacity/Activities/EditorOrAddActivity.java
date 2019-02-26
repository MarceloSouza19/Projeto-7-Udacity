package com.example.marce.projeto7udacity.Activities;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marce.projeto7udacity.Adapter.BookCursorAdapter;
import com.example.marce.projeto7udacity.Contract.BooksContract;
import com.example.marce.projeto7udacity.Fragments.FragmentStock;
import com.example.marce.projeto7udacity.R;
import com.example.marce.projeto7udacity.Utils.Util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditorOrAddActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private Uri mBookUri;
    private Util util = new Util();

    private static Bundle savedInstance;

    private TextView mbookName;
    private TextView mbookPrice;
    private TextView mbookModel;
    private TextView mbookProvider;
    private TextView mbookTelProvider;
    private TextView mbookQuantity;
    private FloatingActionButton btnFlutuante;
    private CircleImageView mbookImage;

    private static int SPACE_GREAT = 30;

    private String mName;
    private double mPrice;
    private String mModel;
    private String mProvider;
    private String mTelProvider;
    private int mQuantity;
    private byte[] imagemUpdate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);
        getSupportActionBar().setHomeButtonEnabled(true);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_tela_cadastro);

        mbookName = findViewById(R.id.bookName);
        mbookPrice = findViewById(R.id.bookPrice);
        mbookModel = findViewById(R.id.bookModel);
        mbookProvider = findViewById(R.id.bookProvider);
        mbookTelProvider = findViewById(R.id.bookTelProvider);
        mbookQuantity = findViewById(R.id.bookQuantity);
        mbookImage = findViewById(R.id.imagemProduto);

        Intent intent = getIntent();

        mBookUri = intent.getData();

        if (mBookUri != null) {
            if(savedInstanceState==null)
                getSupportLoaderManager().initLoader(1, null, this).forceLoad();

            setTitle(getResources().getString(R.string.editando_livro));
        } else {
            setTitle(getResources().getString(R.string.cadastrando_livro));
        }

        btnFlutuante = findViewById(R.id.addPhoto);

        btnFlutuante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        mbookImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                mbookImage.setImageResource(R.drawable.sem_imagem);
                imagemUpdate=null;
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.imagem_removida),Toast.LENGTH_LONG).show();
                return true;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 1) {
            Uri imageSelected = data.getData();
            try {
                this.imagemUpdate = util.getImageByteCode(imageSelected, getApplicationContext());
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.imagem_selecionada), Toast.LENGTH_SHORT).show();
                this.mbookImage.setImageBitmap(Util.getImage(imagemUpdate));

            } catch (FileNotFoundException e) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.imagem_erro), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }


        }
    }

    public void setData() {
        mName = mbookName.getText().toString().trim();
        mPrice = 0;
        if (mbookPrice.getText().toString().length() > 0) {
            mPrice = Double.valueOf(mbookPrice.getText().toString());
        }
        mModel = mbookModel.getText().toString().trim();
        mProvider = mbookProvider.getText().toString().trim();
        mTelProvider = mbookTelProvider.getText().toString().trim();
        mQuantity = 0;
        if (mbookQuantity.getText().toString().length() > 0) {
            mQuantity = Integer.parseInt(mbookQuantity.getText().toString());
        }
    }

    public boolean saveBook() {

        this.setData();

        ContentValues contentValues = new ContentValues();

        contentValues.put(BooksContract.COLUNA_NOME_LIVRO, mName);
        contentValues.put(BooksContract.COLUNA_DESCRICAO, mModel);
        contentValues.put(BooksContract.COLUNA_PREÇO, mPrice);
        contentValues.put(BooksContract.COLUNA_QUANTIDADE, mQuantity);
        contentValues.put(BooksContract.COLUNA_FORNECEDOR, mProvider);
        contentValues.put(BooksContract.COLUNA_TELEFONE, mTelProvider);

        if (this.imagemUpdate != null) {
            contentValues.put(BooksContract.COLUNA_IMAGEM, imagemUpdate);
        }

        if (mBookUri == null) {
            Uri newUri = getContentResolver().insert(BooksContract.CONTENT_URI, contentValues);

            if (newUri == null)
                return false;
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.salvo_sucesso), Toast.LENGTH_SHORT).show();
        } else {
            int id = getContentResolver().update(mBookUri, contentValues, null, null);

            if (id > 0) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.atualizado_sucesso), Toast.LENGTH_SHORT).show();
            } else {
                return false;
            }
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        super.onBackPressed();
        this.onStop();
        return;
    }

    @Override
    protected void onStop() {
        savedInstance=null;
        super.onStop();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {


        if (mBookUri != null) {
            String[] projection = {
                    BooksContract.COLUNA_ID,
                    BooksContract.COLUNA_NOME_LIVRO,
                    BooksContract.COLUNA_DESCRICAO,
                    BooksContract.COLUNA_PREÇO,
                    BooksContract.COLUNA_QUANTIDADE,
                    BooksContract.COLUNA_FORNECEDOR,
                    BooksContract.COLUNA_TELEFONE,
                    BooksContract.COLUNA_IMAGEM
            };

            return new CursorLoader(this, mBookUri, projection, null, null, null);

        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor objeto) {

        if (objeto != null && objeto.moveToFirst()) {

            if(savedInstance!=null){
                if(savedInstance.getBoolean(getResources().getString(R.string.estadoAnterior)))
                    return;
            }

            int nameIndex = objeto.getColumnIndex(BooksContract.COLUNA_NOME_LIVRO);
            int modelIndex = objeto.getColumnIndex(BooksContract.COLUNA_DESCRICAO);
            int priceIndex = objeto.getColumnIndex(BooksContract.COLUNA_PREÇO);
            int qtdIndex = objeto.getColumnIndex(BooksContract.COLUNA_QUANTIDADE);
            int providerIndex = objeto.getColumnIndex(BooksContract.COLUNA_FORNECEDOR);
            int providerTelIndex = objeto.getColumnIndex(BooksContract.COLUNA_TELEFONE);
            int imageIndex = objeto.getColumnIndex(BooksContract.COLUNA_IMAGEM);

            mName = objeto.getString(nameIndex);
            mModel = objeto.getString(modelIndex).trim();
            mPrice = Double.valueOf(objeto.getString(priceIndex));
            mQuantity = Integer.valueOf(objeto.getString(qtdIndex));
            mProvider = objeto.getString(providerIndex);
            mTelProvider = objeto.getString(providerTelIndex);
            byte[] imageObject = objeto.getBlob(imageIndex);
            Bitmap imagem;

            if (imageObject != null) {

                if (imagemUpdate != null && imagemUpdate != imageObject) {
                    imagem = Util.getImage(this.imagemUpdate);
                    mbookImage.setImageBitmap(imagem);
                } else {
                    imagem = Util.getImage(imageObject);
                    mbookImage.setImageBitmap(imagem);
                }
            } else {
                Drawable drawable = getResources().getDrawable(R.drawable.sem_imagem);
                mbookImage.setImageDrawable(drawable);
                mbookImage.setPadding(SPACE_GREAT, SPACE_GREAT, SPACE_GREAT, SPACE_GREAT);
            }
            mbookName.setText(mName);
            mbookPrice.setText(String.valueOf(mPrice));
            mbookModel.setText(mModel != null ? mModel: getResources().getString(R.string.nao_informado));
            mbookProvider.setText(mProvider);
            mbookTelProvider.setText(mTelProvider);
            mbookQuantity.setText(String.valueOf(mQuantity) != null ? String.valueOf(mQuantity) : "0");

        } else if (objeto.getCount() < 1) {
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_cadastro, menu);

        if (mBookUri == null) {
            menu.getItem(1).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.confirmar:
                if (this.saveBook())
                    finish();
                break;
            case R.id.deletar_objeto:
                if (mBookUri != null)
                    this.showDeleteConfirmationDialog();
                break;

        }
        return true;
    }

    private void showDeleteConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.realmente_deletar));
        builder.setPositiveButton(getResources().getString(R.string.sim), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                getContentResolver().delete(mBookUri, null, null);
                finish();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancelar), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putBoolean("estadoAnterior", true);
        savedInstance = savedInstanceState;

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        loader.reset();
        savedInstance=null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

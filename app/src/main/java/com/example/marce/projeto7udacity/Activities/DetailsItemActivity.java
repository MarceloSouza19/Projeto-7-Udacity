package com.example.marce.projeto7udacity.Activities;

import android.app.LoaderManager;
import android.content.Loader;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import android.content.CursorLoader;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marce.projeto7udacity.Contract.BooksContract;
import com.example.marce.projeto7udacity.R;

import java.io.ByteArrayOutputStream;


public class DetailsItemActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    Uri mCurrentBookUri;

    TextView mbookName;
    TextView mbookPrice;
    TextView mbookModel;
    TextView mbookProvider;
    TextView mbookTelProvider;
    TextView mbookQuantity;
    ImageView mbookImage;

    public static int QUALITY_GREAT = 100;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mCurrentBookUri = getIntent().getData();

        mbookName = findViewById(R.id.nomeInicio);
        mbookPrice = findViewById(R.id.precoAtual);
        mbookModel = findViewById(R.id.detalhes);
        mbookProvider = findViewById(R.id.fornecedor);
        mbookTelProvider = findViewById(R.id.fornecedorTelefone);
        mbookQuantity = findViewById(R.id.quantidadeDisponivel);
        mbookImage = findViewById(R.id.imagemObjeto);

        getLoaderManager().initLoader(3, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
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
        return new CursorLoader(this, mCurrentBookUri, projection, null, null, null);

    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor objeto) {

        if (objeto != null && objeto.moveToFirst()) {

            int nameIndex = objeto.getColumnIndex(BooksContract.COLUNA_NOME_LIVRO);
            int modelIndex = objeto.getColumnIndex(BooksContract.COLUNA_DESCRICAO);
            int priceIndex = objeto.getColumnIndex(BooksContract.COLUNA_PREÇO);
            int qtdIndex = objeto.getColumnIndex(BooksContract.COLUNA_QUANTIDADE);
            int providerIndex = objeto.getColumnIndex(BooksContract.COLUNA_FORNECEDOR);
            int providerTelIndex = objeto.getColumnIndex(BooksContract.COLUNA_TELEFONE);
            int imageIndex = objeto.getColumnIndex(BooksContract.COLUNA_IMAGEM);

            String sName = objeto.getString(nameIndex);
            String sModel = objeto.getString(modelIndex).trim();
            String dPrice = objeto.getString(priceIndex);
            String iQuantity = objeto.getString(qtdIndex);
            String sProvider = objeto.getString(providerIndex);
            String sProviderTel = objeto.getString(providerTelIndex);
            byte[] imageObject = objeto.getBlob(imageIndex);


            if(imageObject!=null){
               Bitmap bit = BitmapFactory.decodeByteArray(imageObject, 0, imageObject.length);
                ByteArrayOutputStream by = new ByteArrayOutputStream();

                bit.compress(Bitmap.CompressFormat.PNG, QUALITY_GREAT, by);
                mbookImage.setImageBitmap(bit);
            }


            mbookName.setText(sName);
            mbookPrice.setText(dPrice);
            mbookModel.setText(sModel != null ? sModel : "Não Informado dados sobre o produto");
            mbookProvider.setText(sProvider);
            mbookTelProvider.setText(sProviderTel);
            mbookQuantity.setText(iQuantity != null ? "Quantidade disponível no estoque " + iQuantity : "Não existe mais produto deste no estoque!");

        } else if (objeto.getCount() < 1) {
            return;
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        loader.reset();
    }


}

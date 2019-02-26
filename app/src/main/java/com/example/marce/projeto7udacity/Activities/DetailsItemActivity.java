package com.example.marce.projeto7udacity.Activities;

import android.Manifest;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.WindowManager;

import android.content.CursorLoader;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marce.projeto7udacity.Contract.BooksContract;
import com.example.marce.projeto7udacity.R;
import com.example.marce.projeto7udacity.Utils.Util;

import java.io.ByteArrayOutputStream;


public class DetailsItemActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    Uri mCurrentBookUri;

    private TextView mbookName;
    private TextView mbookPrice;
    private TextView mbookModel;
    private TextView mbookProvider;
    private TextView mbookTelProvider;
    private TextView mbookQuantity;
    private TextView btnVenderTxt;
    private ImageView mbookImage;
    private CardView btnVender;
    private CardView btnSolicitarForn;
    private CardView btnBotaoAdd;
    private CardView btnBotaoDecrement;
    private LinearLayout solicitarFornArea;

    private String sName;
    private String sModel;
    private String dPrice;
    private String iQuantity;
    private String sProvider;
    private String sProviderTel;
    private byte[] imageObject;

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
        btnVender = findViewById(R.id.botaoVender);
        btnVenderTxt = findViewById(R.id.btnVenderTxt);
        btnSolicitarForn = findViewById(R.id.btnSolicitarForn);
        btnBotaoAdd = findViewById(R.id.btnBotaoAdd);
        btnBotaoDecrement = findViewById(R.id.btnDescrement);
        solicitarFornArea = findViewById(R.id.solicitarFornArea);

        getLoaderManager().initLoader(3, null, this);

        btnVender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(iQuantity) > 0) {
                    iQuantity = String.valueOf(Integer.valueOf(iQuantity) - 1);
                    if (updateItem())
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.item_vendido), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.sem_decremento), Toast.LENGTH_LONG).show();
                }
            }
        });

        btnBotaoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iQuantity = String.valueOf(Integer.valueOf(iQuantity) + 1);
                if (updateItem())
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.item_incrementado), Toast.LENGTH_LONG).show();
            }
        });
        btnBotaoDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(iQuantity) > 0) {
                    iQuantity = String.valueOf(Integer.valueOf(iQuantity) - 1);
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.item_decrementado), Toast.LENGTH_LONG).show();
                    if (updateItem())
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.item_decrementado), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.sem_decremento), Toast.LENGTH_LONG).show();
                }
            }
        });

        btnSolicitarForn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sProviderTel.isEmpty()) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.sem_telefone_fornecedor), Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + sProviderTel));
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(intent);
                    }
                }

            }
        });

    }

    public boolean updateItem() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(BooksContract.COLUNA_QUANTIDADE, iQuantity);

        int id = getContentResolver().update(mCurrentBookUri, contentValues, null, null);

        if (id > 0) {
            return true;
        }

        return false;
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

    @RequiresApi(api = Build.VERSION_CODES.M)
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

            sName = objeto.getString(nameIndex);
            sModel = objeto.getString(modelIndex);
            dPrice = objeto.getString(priceIndex);
            iQuantity = objeto.getString(qtdIndex);
            sProvider = objeto.getString(providerIndex);
            sProviderTel = objeto.getString(providerTelIndex);
            imageObject = objeto.getBlob(imageIndex);

            if (imageObject != null) {

                Bitmap picture = Util.getImage(imageObject);

                mbookImage.setImageBitmap(picture);
            } else {
                mbookImage.setImageResource(R.drawable.sem_imagem);
                mbookImage.setScaleType(ImageView.ScaleType.CENTER);
            }

            mbookName.setText(sName);
            mbookPrice.setText(getResources().getString(R.string.valor_atual) + dPrice);
            mbookModel.setText(!sModel.isEmpty() ? sModel : getResources().getString(R.string.sem_descricao));
            mbookProvider.setText(getResources().getString(R.string.fornecedor_produto) + " " + sProvider);
            mbookTelProvider.setText(!sProviderTel.isEmpty() ? getResources().getString(R.string.telefone_fornecedor) + " " + sProviderTel : getResources().getString(R.string.sem_telefone_fornecedor));
            mbookQuantity.setText(Integer.valueOf(iQuantity) > 0 ? getResources().getString(R.string.quantidade_estoque) + " " + iQuantity : getResources().getString(R.string.sem_produto));


        }
        if (iQuantity != null) {
            if (Integer.valueOf(iQuantity) < 5) {
                solicitarFornArea.setVisibility(View.VISIBLE);
                if (sProviderTel.isEmpty())
                    btnSolicitarForn.setCardBackgroundColor(getColor(R.color.colorGrey));
                if(Integer.valueOf(iQuantity)==0){
                    btnVender.setCardBackgroundColor(getColor(R.color.colorGrey));
                    btnVenderTxt.setTextColor(getColor(R.color.colorWhite));
                } else{
                    btnVender.setCardBackgroundColor(getColor(R.color.colorWhite));
                    btnVenderTxt.setTextColor(getColor(R.color.colorAccent));
                }
            } else {
                solicitarFornArea.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        loader.reset();
    }
}

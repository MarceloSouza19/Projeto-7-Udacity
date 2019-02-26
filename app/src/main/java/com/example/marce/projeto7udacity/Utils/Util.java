package com.example.marce.projeto7udacity.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Util {

    public static int QUALITY_MEDIUM = 15;
    public static int QUALITY_GREAT = 20;
    public static int QUALITY_BETTER = 40;

    public static Bitmap getImage(byte[] imageObject){

        Bitmap bitmap = BitmapFactory.decodeByteArray(imageObject, 0, imageObject.length);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY_BETTER, byteArrayOutputStream);

        return bitmap;
    }

    public static byte[] getImageByteCode(Uri imageSelected, Context context) throws FileNotFoundException {

        byte[] imagem;

        InputStream inputStream = context.getContentResolver().openInputStream(imageSelected);
        Bitmap imageBit = BitmapFactory.decodeStream(inputStream);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (imageBit.getDensity() > 350)
            imageBit.compress(Bitmap.CompressFormat.JPEG, QUALITY_MEDIUM, byteArrayOutputStream);
        else
            imageBit.compress(Bitmap.CompressFormat.JPEG, QUALITY_GREAT, byteArrayOutputStream);

        imagem = byteArrayOutputStream.toByteArray();

        return imagem;
    }
}

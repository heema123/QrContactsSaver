package com.maximum.qrme;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.OutputStream;

public class QRImageView extends AppCompatActivity {

    ImageView imageViewQR;
    Button btnSave;
    Button btnShare;
    String url;
    TextView textViewQrGenerated;
    AVLoadingIndicatorView avi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_qrimage_view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                //File write logic here

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

        Intent intent = getIntent();
        url = intent.getStringExtra("EXTRA_URL");
        Log.v("TAG", url);
        imageViewQR = findViewById(R.id.imageViewQR);
        btnSave = findViewById(R.id.save);
        btnShare = findViewById(R.id.share);
        textViewQrGenerated = findViewById(R.id.textGenerated);
        avi = findViewById(R.id.avi);


        btnSave.setVisibility(View.GONE);
        btnShare.setVisibility(View.GONE);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadImageUri();
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareImage();
            }
        });

        avi.setVisibility(View.VISIBLE);
        avi.setIndicatorColor(R.color.black);
        startAnim();

        glideUrlFetcher();

    }

    public void glideUrlFetcher() {
        Glide.with(this)
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        avi.setVisibility(View.GONE);
                        stopAnim();
                        Toast.makeText(getApplicationContext(), R.string.tryAgaingText, Toast.LENGTH_LONG).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        avi.setVisibility(View.GONE);
                        stopAnim();
                        //progressBar.setVisibility(View.GONE);
                        textViewQrGenerated.setText(R.string.qrSuccessText);
                        btnSave.setVisibility(View.VISIBLE);
                        btnShare.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(imageViewQR);
    }


    void startAnim() {
        avi.smoothToShow();
    }

    void stopAnim() {
        avi.hide();
    }

    public void shareImage() {

        imageViewQR.setDrawingCacheEnabled(true);
        imageViewQR.buildDrawingCache();
        Bitmap icon = imageViewQR.getDrawingCache();
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "QR Code Image");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values);


        OutputStream outstream;
        try {
            assert uri != null;
            outstream = getContentResolver().openOutputStream(uri);
            icon.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
            assert outstream != null;
            outstream.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }

        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, "Share Image"));
    }

    public void downloadImageUri() {
        long downloadReference;
        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(getString(R.string.downloadImageTitle));
        request.setDescription(getString(R.string.downloadImageDescription));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "qr.jpg");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);


        if (downloadManager != null) {
            downloadReference = downloadManager.enqueue(request);
            Uri.parse(url);
        }
    }
}

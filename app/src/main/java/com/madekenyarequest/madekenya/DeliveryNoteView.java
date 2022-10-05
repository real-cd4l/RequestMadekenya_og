package com.madekenyarequest.madekenya;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.LruCache;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.madekenyarequest.madekenya.adapter.DeliveryNoteAdapter;
import com.madekenyarequest.madekenya.pojos.Customer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Objects;

public class DeliveryNoteView extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 56;
    private static final String TAG = "DeliveryNoteView";
    private RecyclerView recyclerView;
    private DeliveryNoteAdapter adapter;
    private Customer customer;
    private ProgressBar progressBar;
    private FloatingActionButton fabShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_RequestMadekenya_NoActionBar);
        setContentView(R.layout.activity_delivery_note_view);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        fabShare = findViewById(R.id.fabShare);

        fabShare.setOnClickListener(v -> {
            Toast.makeText(this, "please wait..!", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.VISIBLE);
            generatePdfFromRecylerView();
        });

        if (getIntent().hasExtra("customer")) {
            customer = getIntent().getParcelableExtra("customer");
            if (customer != null) {
                adapter = new DeliveryNoteAdapter(DeliveryNoteView.this, customer);
                recyclerView.setAdapter(adapter);
                if (ContextCompat.checkSelfPermission(DeliveryNoteView.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(DeliveryNoteView.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // You can use the API that requires the permission.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale("This permission is essential for allowing printing pdf mechanisms")) {
                        } else {
                            // You can directly ask for the permission.
                            String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            requestPermissions(permissions, PERMISSION_REQUEST_CODE);
                        }
                    }
                }
            } else {
                finish();
            }
        }
    }


    private void generatePdfFromRecylerView() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            Log.d(TAG, "generatePdfFromRecylerView: Permission granted...!!");
            generatePDF(recyclerView);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale("This permission is essential for allowing printing pdf mechanisms")) {
                } else {
                    // You can directly ask for the permission.
                    String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(permissions, PERMISSION_REQUEST_CODE);
                }
            }
        }
    }

    public void generatePDF(RecyclerView view) {
        Bitmap bigBitmap = null;
        if (adapter != null) {
            int size = adapter.getItemCount(); //3
            int height = 0;
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            // Use 1/8th of the available memory for this memory cache.
            final int cacheSize = maxMemory / 8;
            LruCache<String, Bitmap> bitmaCache = new LruCache<>(cacheSize);
            int bitmapCachePointer = 0;

            int internalListSize = adapter.getItemCount();
            if (adapter.getDeliveryNoteListAdapter() != null) {
                for (int i = 0; i < internalListSize; i++) {
                    RecyclerView.ViewHolder holder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(i));
                    adapter.onBindViewHolder(holder, i);
                    holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(), holder.itemView.getMeasuredHeight());
                    holder.itemView.setDrawingCacheEnabled(true);
                    holder.itemView.buildDrawingCache();
                    Bitmap drawingCache = holder.itemView.getDrawingCache();
                    if (drawingCache != null) {
                        bitmaCache.put(String.valueOf(bitmapCachePointer), drawingCache);
                        bitmapCachePointer++;
                    } else {
                        Log.d(TAG, "generatePDF: drawingCache == null");
                    }
                    height += holder.itemView.getMeasuredHeight();
                }
                Log.d(TAG, "generatePDF: height == " + height);
                bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), height, Bitmap.Config.ARGB_8888);
                Canvas bigCanvas = new Canvas(bigBitmap);
                bigCanvas.drawColor(Color.WHITE);

                Document document = new Document(PageSize.A4);
                File f = new File(Environment.getExternalStorageDirectory(), "DeliveryNote");
                if (!f.exists()) {
                    Toast.makeText(this, "file not exists", Toast.LENGTH_SHORT).show();
                    if (!f.mkdirs()) {
                        Toast.makeText(this, "opps!", Toast.LENGTH_SHORT).show();
                    }
                }
                String sharedPdfName = "doc_" + customer.getUserDeliveredID() + ".pdf";
                final File file = new File(Environment.getExternalStorageDirectory(), "DeliveryNote/" + sharedPdfName);
                try {
                    PdfWriter.getInstance(document, new FileOutputStream(file));
                } catch (DocumentException | FileNotFoundException e) {
                    e.printStackTrace();
                    Log.d(TAG, "generatePDF: ex " + e.getMessage());
                    Log.d(TAG, "generatePDF: throw " + Objects.requireNonNull(e.getCause()));
                }

                Log.d(TAG, "generatePDF: bitmapCachePointer size ==> " + bitmapCachePointer);

                for (int i = 0; i < bitmapCachePointer; i++) {
                    try {
                        //Adding the content to the document
                        Bitmap bmp = bitmaCache.get(String.valueOf(i));
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        if (bmp != null) {
                            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        }
                        Image image = Image.getInstance(stream.toByteArray());
                        image.scalePercent(100);
                        image.setAlignment(Image.ALIGN_CENTER);
                        if (!document.isOpen()) {
                            document.open();
                        }
                        document.add(image);
                        Log.d(TAG, "generatePDF: documment added");

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Log.d(TAG, "generatePDF: error");
                        Log.e("TAG-ORDER PRINT ERROR", ex.getMessage());
                    }
                }
                if (document.isOpen()) {
                    document.close();
                }
                Log.d(TAG, "generatePDF: document.getPageNumber() " + document.getPageNumber());
                // Set on UI Thread
//                progressBar.setVisibility(View.INVISIBLE);

                Uri uri = FileProvider.getUriForFile(DeliveryNoteView.this, "com.madekenyarequest.madekenya.fileprovider", file, sharedPdfName);

                PackageManager pm = getPackageManager();
                try {

                    Intent waIntent = new Intent(Intent.ACTION_SEND);
                    waIntent.setType("application/pdf");
                    waIntent.putExtra(Intent.EXTRA_STREAM, uri);

                    PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                    //Check if package exists or not. If not then code
                    //in catch block will be called
                    waIntent.setPackage("com.whatsapp");

                    startActivity(Intent.createChooser(waIntent, "Share with"));

                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(DeliveryNoteView.this, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d(TAG, "generatePDF: customer adapter is null");
            }
        } else {
            Log.d(TAG, "generatePDF: adapter is null");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delivery_note_view_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
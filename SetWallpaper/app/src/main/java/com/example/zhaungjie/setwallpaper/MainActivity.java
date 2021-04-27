package com.example.zhaungjie.setwallpaper;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.WallpaperManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final int TAKE_PHOTO = 1;
    private ImageView picture;
    private Uri imageUri;
    public static final int CHOOSE_PHOTO = 2;
    private WallpaperManager wallpaperManager;
    private Drawable wallpaperDrawable;
    NotificationManager notificationManager;
    Notification notification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        picture = (ImageView) findViewById(R.id.picture);
        //定义打开相机按钮操作
        FloatingActionButton ftakepho=(FloatingActionButton)findViewById(R.id.ftake_photo);
        ftakepho.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= 24) {
                    imageUri = FileProvider.getUriForFile(MainActivity.this,
                            "com.example.zhaungjie.setwallpaper.fileprovider", outputImage);
                } else {
                    imageUri = Uri.fromFile(outputImage);
                }

                Intent intent1 = new Intent("android.media.action.IMAGE_CAPTURE");
                intent1.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent1, TAKE_PHOTO);
            }
        });

        //定义打开相册按钮操作
        FloatingActionButton fopalb=(FloatingActionButton)findViewById(R.id.fopen_album);
        fopalb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
            }
        });


        wallpaperManager = WallpaperManager.getInstance(this);
        wallpaperDrawable = wallpaperManager.getDrawable();// 获得当前系统的壁纸
        picture = (ImageView) findViewById(R.id.picture);
        picture.setDrawingCacheEnabled(true);
        picture.setImageDrawable(wallpaperDrawable);
        picture.setDrawingCacheEnabled(false);

        //设置壁纸操作
        FloatingActionButton fwallpaper=(FloatingActionButton)findViewById(R.id.wallpaper);
        fwallpaper.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    picture.setDrawingCacheEnabled(true);
                    wallpaperManager.setBitmap(picture.getDrawingCache());
                    picture.setDrawingCacheEnabled(false);
                    Toast.makeText(MainActivity.this,"壁纸设置成功",Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        Intent intent=getIntent();
        Intent[] intents= new Intent[1];
        intents[0]=intent;
        PendingIntent pi= PendingIntent.getActivities(this,0,intents,0);

        notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notification=new NotificationCompat.Builder(this)
                .setContentTitle("欢迎使用壁纸2.0").setContentText("以后将为您提供更优质服务")
                .setWhen(System.currentTimeMillis()).setSmallIcon(R.mipmap.logo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.logo))
                .setContentIntent(pi)
                .build();
        notificationManager.notify(1,notification);
    }




    private void openAlbum() {
        Intent intent2 = new Intent("android.intent.action.GET_CONTENT");
        intent2.setType("image/*");
        startActivityForResult(intent2, CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "你没有相关权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        //安卓4.0以上版本
                        handleImageOnKitKat(data);
                    } else {
                        //安卓4.0以下版本
                        handleImageBeforeKitKat(data);
                    }
                }
                break;

        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "获取图片失败", Toast.LENGTH_SHORT).show();
        }
    }

}
package test.teamk.com.imagetextrecognizer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ImageView;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.IOException;

public class MainActivity extends ActionBarActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView)findViewById(R.id.imageView);

        String _path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/tesseract/tessdata/good.jpg";
        Bitmap bitmap = BitmapFactory.decodeFile(_path);

        // _path = path to the image to be OCRed
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(_path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);

        int rotate = 0;

        Log.i("rotate", exifOrientation+"");

        switch (exifOrientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotate = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotate = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotate = 270;
                break;
        }

        if (rotate != 0) {
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();

            // Setting pre rotate
            Matrix mtx = new Matrix();
            mtx.postRotate(rotate);

            // Rotating Bitmap & convert to ARGB_8888, required by tess
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
        }
        imageView.setImageBitmap(bitmap);

        TessBaseAPI tessBaseAPI = new TessBaseAPI();
        tessBaseAPI.init(Environment.getExternalStorageDirectory().getAbsolutePath() + "/tesseract", "kor");
        tessBaseAPI.setImage(bitmap);

        Log.i("test", tessBaseAPI.getUTF8Text());

        tessBaseAPI.end();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}

package com.ts.mobilelab.goggles4u;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ts.mobilelab.goggles4u.data.AppConstants;
import com.ts.mobilelab.goggles4u.data.PreferenceData;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Abhishek on 02-Oct-16.
 */
public class UploadPrescriptionActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = UploadPrescriptionActivity.class.getSimpleName();
    private Button buttonChoose;
    private Button buttonClick;
    private Button buttonUpload;
    private EditText editTextName;
    private EditText editTextEmail;
    private Bitmap bitmap;
    private int IMAGE_GALLERY_REQUEST = 1;
    private int IMAGE_CAMERA_REQUEST = 2;
    private final String EMAIL = "email";
    private final String NAME = "name";
    private final String CUSTOMER_ID = "customer_id";
    private final String PRESCRIPTION_IMG = "prescriptionimg";
    private final String DEVICE = "device";
    private PreferenceData mPreferenceData;
    private ImageView imageView;
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pres);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setLogo(R.drawable.ic_actionbar_logo);
        toolbar.setTitle("Upload Prescription");

        mPreferenceData = new PreferenceData();

        initUI();

    }

    /**
     * Initialize the UI widgets
     */
    private void initUI() {
        editTextName = (EditText) findViewById(R.id.prescription_name);
        editTextEmail = (EditText) findViewById(R.id.prescription_email);
        buttonChoose = (Button) findViewById(R.id.btn_upload_gallery);
        buttonClick = (Button) findViewById(R.id.btn_upload_camera);
        buttonUpload = (Button) findViewById(R.id.btn_prescription_submit);
        imageView = (ImageView) findViewById(R.id.image_upload);

        buttonClick.setOnClickListener(this);
        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);

        editTextName.setText(mPreferenceData.getCustomerFName());
        editTextEmail.setText(mPreferenceData.getCustomerMailId());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage() {
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.UPLOAD_IMAGE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(UploadPrescriptionActivity.this, "Prescription uploaded successfully", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(UploadPrescriptionActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);

                //Getting Image Name
                String name = editTextName.getText().toString().trim();

                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(PRESCRIPTION_IMG, image);
                params.put(NAME, mPreferenceData.getCustomerFName());
                params.put(EMAIL, mPreferenceData.getCustomerMailId());
                params.put(CUSTOMER_ID, mPreferenceData.getCustomerId());
                params.put(DEVICE, "android");

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_GALLERY_REQUEST);
    }

    private void openCamera() {
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
            Log.i(TAG, "IOException");
        }
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (photoFile != null) {
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            startActivityForResult(cameraIntent, IMAGE_CAMERA_REQUEST);
        }
    }

    /**
     *
     // Create an image file name
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == IMAGE_CAMERA_REQUEST && resultCode == RESULT_OK ) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_upload_camera:
                openCamera();
                break;

            case R.id.btn_upload_gallery:
                showFileChooser();
                break;

            case R.id.btn_prescription_submit:
                uploadImage();
                break;

            default:
                break;

        }

    }
}

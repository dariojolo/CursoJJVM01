package com.dariojolo.pruebadeintents;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class ActivityThird extends AppCompatActivity {

    private EditText txtPhone;
    private EditText txtWeb;
    private ImageButton btnPhone;
    private ImageButton btnWeb;
    private ImageButton btnCam;

    private final int CALL_PHONE_CODE = 100;
    private final int PICTURE_FROM_CAMERA = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        //Activar flecha ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtPhone = (EditText) findViewById(R.id.txtPhone);
        txtWeb = (EditText) findViewById(R.id.txtWeb);
        btnPhone = (ImageButton) findViewById(R.id.btnCall);
        btnWeb = (ImageButton) findViewById(R.id.btnWeb);
        btnCam = (ImageButton) findViewById(R.id.btnCam);

        //Boton para el intent del telefono
        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = txtPhone.getText().toString();
                if (phoneNumber != null && !phoneNumber.isEmpty()) {
                    //Comprobando version Android del telefono
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        //Comprobar si ha aceptado o no ha aceptado o si nunca se le pregunto
                        if (checkPermission(Manifest.permission.CALL_PHONE)){
                            Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                            if (ActivityCompat.checkSelfPermission(ActivityThird.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                        }else{
                            //No ha aceptado o nunca se le pregunto
                            if (!shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)){
                                //No se le ha preguntado aun
                                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE_CODE);
                            }else{
                            //Ha denegado el acceso
                                Toast.makeText(ActivityThird.this, "Por favor, otorgue los permisos", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                i.addCategory(Intent.CATEGORY_DEFAULT);
                                i.setData(Uri.parse("package:"+ getPackageName()));
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

                                startActivity(i);
                            }
                        }
                    } else {
                        OlderPhone(phoneNumber);
                    }
                }
            }

            private void OlderPhone(String phoneNumber) {
                Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                if (checkPermission(Manifest.permission.CALL_PHONE)) {
                    startActivity(intentCall);
                } else {
                    Toast.makeText(ActivityThird.this, "No tiene los permisos aceptados", Toast.LENGTH_LONG).show();
                }

            }
        });

        //Boton para el intent de internet
        btnWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = txtWeb.getText().toString();
                if (url != null && !url.isEmpty()){
                    //Intent intentWeb = new Intent(Intent.ACTION_VIEW,Uri.parse("http://"+url)); <= esto es lo mismo que esta abajo en 2 pasos
                    Intent intentWeb = new Intent();
                    intentWeb.setAction(Intent.ACTION_VIEW);
                    intentWeb.setData(Uri.parse("http://"+url));
                    //startActivity(intentWeb);

                    //Acceso a ventana de contactos
                    Intent intentContacts = new Intent(Intent.ACTION_VIEW,Uri.parse("content://contacts/people"));
                    startActivity(intentContacts);

                    String email = "dariojolo@gmail.com";
                    //Email rapido
                    Intent intentEmailTo = new Intent(Intent.ACTION_SENDTO,Uri.parse("mailto:"+email));
                    startActivity(intentEmailTo);
                    //Email completo
                    Intent intentEmail = new Intent(Intent.ACTION_VIEW,Uri.parse(email));
                    intentEmail.setClassName("com.google.android.gm","com.google.android.gm.ComposeActivityGmail");
                    //intentEmail.setType("plain/text");
                    intentEmail.setType("message/rfc822");
                    intentEmail.putExtra(Intent.EXTRA_SUBJECT, "Titulo del mail");
                    intentEmail.putExtra(Intent.EXTRA_TEXT,"Texto enviado por mail");
                    intentEmail.putExtra(Intent.EXTRA_EMAIL,new String[]{"dariojolo@gmail.com","otroemail@gmail.com"});
                    startActivity(Intent.createChooser(intentEmail, "Elige cliente de correo"));
                    //startActivity(intentEmail);

                    //Telefono 2 sin permisos requeridos
                   // Intent intentPhone = new Intent(Intent.ACTION_CALL,Uri.parse("tel:23423423"));
                   // startActivity(intentPhone);
                }
            }
        });

        btnCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Abrir camara
                Intent intentCam = new Intent("android.media.action.IMAGE_CAPTURE");
               // startActivity(intentCam);
                startActivityForResult(intentCam,PICTURE_FROM_CAMERA);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){
            case PICTURE_FROM_CAMERA:
                if (resultCode ==  Activity.RESULT_OK){
                    String result = data.toUri(0);
                    Toast.makeText(this,"Result "+ result,Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(this,"hubo un error con la imagen",Toast.LENGTH_LONG).show();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CALL_PHONE_CODE:
                String permission = permissions[0];
                int result = grantResults[0];

                if (permission.equals(Manifest.permission.CALL_PHONE)) {
                    //Comprobar si ha sido aceptado el permiso o no
                    if (result == PackageManager.PERMISSION_GRANTED) {
                        //Concedio los permisos
                        String phoneNumber = txtPhone.getText().toString();
                        Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        startActivity(intentCall);
                    } else {
                        //No Concedio los permisos
                        Toast.makeText(ActivityThird.this, "No otorgo los permisos", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }
    }

    private boolean checkPermission(String permission) {
        int result = this.checkCallingOrSelfPermission(permission);
        return result == PackageManager.PERMISSION_GRANTED;

    }
}

package com.example.antonio.manejoarchivos;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;


public class MainActivity extends Activity {

    private static final int REQUEST_ID_READ_PERMISSION = 100;
    private static final int REQUEST_ID_WRITE_PERMISSION = 200;

    File archivos[];
    private String nombres[]; // Nombre de las listas sin extension

    Button BAgregar;
    Button BEliminar;
    Button BGuardar;
    Button BSalir;
    EditText ET1;
    EditText ET2;
    EditText ET3;
    Context CX;

    String Ruta;
    final String NombreDirectorio ="ManejoArchivos_Demo2018";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Esto manda a llamar a una función que le pide confirmación al usuario
        // acerca del acceso a los archivos por parte de la aplicación
        // No es necesario en versiones anteriores a la 6.0, pero se incluye para
        // que sea compatible con todo dispositivo viejo o nuevo...
        
        askPermissionOnly();

        Ruta= Environment.getExternalStorageDirectory().getAbsolutePath().toString()+"/"+NombreDirectorio;

        CX= this;

        BAgregar = (Button) findViewById(R.id.button_agregar_lista);
        BEliminar = (Button) findViewById(R.id.button_eliminar_lista);
        BGuardar =  (Button) findViewById(R.id.button_guardar_cambios);
        BSalir = (Button) findViewById(R.id.button_salir);

        ET1 = (EditText) findViewById(R.id.editText1);
        ET2 = (EditText) findViewById(R.id.editText2);
        ET3 = (EditText) findViewById(R.id.editText3);



        BAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Cadena=ET1.getText().toString();
                createFile(Cadena);
                cargarControles(); // Actualizar listas ...
                ET1.setText(""); // Limpiar el EditTExt
                ET3.setText(""); // Limpiar el EditTExt

            }
        });

        BGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Cadena=ET1.getText().toString();
                try {
                    GuardarArchivoTexto(Cadena);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cargarControles(); // Actualizar listas ...
                ET1.setText(""); // Limpiar el EditTExt
                ET3.setText(""); // Limpiar el EditTExt
            }
        });

        BEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    LeerArchivoTexto(ET1.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cargarControles(); // Actualizar listas ...
                //ET3.setText(""); // Limpiar el EditTExt

            }
        });

        // Declaracion de Controles

        BSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // En las versiones "viejas" no es tanto problema!!!
        if (android.os.Build.VERSION.SDK_INT < 23) {
            cargarControles();
        }
        else
        { // Do nothing => Esta carga de controles se hace en el momento que el usuario "Autoriza"
           // el permiso (Para versiones 6.0 en adelante)

        }


    }

    private void GuardarArchivoTexto (String NombreArchivo) throws IOException {
        Ruta= Environment.getExternalStorageDirectory().getAbsolutePath().toString()+"/"+NombreDirectorio;
        File F = new File (Ruta);
        if (F.exists()) { // Ya existe el directorio
            File F2 = new File (Ruta, NombreArchivo+".txt");

            // Si existe o no existe se crea...
            FileOutputStream fOut = new FileOutputStream(F2);
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);
            myOutWriter.append(ET3.getText().toString());
            myOutWriter.close();
            fOut.close();

            // Se informa al usuario que va a sobreescibirse
            if (F2.exists()) { // Checar si el archivo Existe
                Toast.makeText(getBaseContext(),
                        "Se guardó el archivo",
                        Toast.LENGTH_SHORT).show();


            }
            else { // Se informa al usuario qeu se creo uno nuevo
                //Toast.makeText(getApplicationContext(),"No EXISTE tal ARCHIVO en el Directorio: "+Ruta+" Pero se Creará",Toast.LENGTH_SHORT).show();
            }
        }
        else { // No existe, crea el directorio
            Toast.makeText(getApplicationContext(),"Se ha creado la ruta RUTA:"+Ruta,Toast.LENGTH_SHORT).show();
            F.mkdir();
        }
    }


    private void LeerArchivoTexto (String NombreArchivo) throws IOException {
        Ruta= Environment.getExternalStorageDirectory().getAbsolutePath().toString()+"/"+NombreDirectorio;
        File F = new File (Ruta);
        if (F.exists()) { // Ya existe el directorio
            File F2 = new File (Ruta, NombreArchivo+".txt");

            if (F2.exists()) { // Checar si el archivo Existe

                FileInputStream fIn = new FileInputStream(F2);
                BufferedReader myReader = new BufferedReader(
                        new InputStreamReader(fIn));
                String aDataRow = "";
                String aBuffer = "";
                while ((aDataRow = myReader.readLine()) != null) {
                    aBuffer += aDataRow + "\n";
                }
                //txtData.setText(aBuffer);

                ET3.setText(aBuffer);
                myReader.close();
                Toast.makeText(getBaseContext(),
                        "Se leyó el archivo",
                        Toast.LENGTH_SHORT).show();


            }
            else {
                Toast.makeText(getApplicationContext(),"No EXISTE tal ARCHIVO en el Directorio: "+Ruta,Toast.LENGTH_SHORT).show();
            }
        }
        else { // No existe, crea el directorio
            Toast.makeText(getApplicationContext(),"Se ha creado la ruta RUTA:"+Ruta,Toast.LENGTH_SHORT).show();
            F.mkdir();
        }
    }

    //  Crea un nuevo archovo-Lista, recibe el nombre de esta.
    public void createFile(String file_name){ // Crea un nuevo archivo de texto
        File extStore = Environment.getExternalStorageDirectory();
        String path = extStore.getAbsolutePath() + "/"+NombreDirectorio+"/";
        //File f  = new File(path+date.replace(":","-")+" "+file_name+".txt");
        //File f  = new File(path+date.replace(":","-")+"_"+file_name+".txt");
        File f  = new File(path+file_name+".txt");

        if (f.exists()) {
            Toast.makeText(CX,"El archivo ya existe",Toast.LENGTH_SHORT).show();
        } else {
            try {
                f.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // recomiendo limpiar el EditText para Evitar duplicar el archivo
        }
    }

    public void cargarControles() {
        File extStore = Environment.getExternalStorageDirectory();
        String path = extStore.getAbsolutePath() + "/" + NombreDirectorio;
        File carpeta = new File(path);


        // Comprobar si existe la carpeta
        if (!carpeta.exists()) {
            carpeta.mkdir();
            return;
        }
        else {
            //Toast.makeText(this, "Carpeta EXISTE + "+carpeta.listFiles().toString(), Toast.LENGTH_LONG).show();
        }

        // Se guardan los archivos
        String Cadena="";
        File archivos[] = carpeta.listFiles();
        nombres = new String[archivos.length];

        for (int i = 0; i < nombres.length; i++){
            String name = archivos[i].getName().toUpperCase().replace(".",";");
            nombres[i] = name.split(";")[0];
            if (nombres[i].equals("")) { } else {Cadena+=nombres[i]+"\n"; }
        }

        ET2.setText(Cadena);

    }

/*
*   Funciones especializadas en la obtanción de Permisos de USUARIO !!!!!
*   Sacadas de algún lado de StackOverFlow...
*
* */

    private void askPermissionOnly() {
        this.askPermission(REQUEST_ID_WRITE_PERMISSION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        this.askPermission(REQUEST_ID_READ_PERMISSION,
                Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    // With Android Level >= 23, you have to ask the user
    // for permission with device (For example read/write data on the device).
    private boolean askPermission(int requestId, String permissionName) {
        if (android.os.Build.VERSION.SDK_INT >= 23) {

            // Check if we have permission
            int permission = ActivityCompat.checkSelfPermission(this, permissionName);


            if (permission != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                this.requestPermissions(
                        new String[]{permissionName},
                        requestId
                );
                return false;
            }
        }
        return true;
    }

    // When you have the request results
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //
        // Note: If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0) {
            switch (requestCode) {
                case REQUEST_ID_READ_PERMISSION: {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        //cargarControles();
                        Toast.makeText(getApplicationContext(), "Permission Lectura Concedido!", Toast.LENGTH_SHORT).show();
                        cargarControles();
                    }
                }
                case REQUEST_ID_WRITE_PERMISSION: {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        //writeFile();
                        //
                        Toast.makeText(getApplicationContext(), "Permission Escritura Concedido!", Toast.LENGTH_SHORT).show();
                        cargarControles();
                    }
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "Permission Cancelled!", Toast.LENGTH_SHORT).show();
        }
    }

}

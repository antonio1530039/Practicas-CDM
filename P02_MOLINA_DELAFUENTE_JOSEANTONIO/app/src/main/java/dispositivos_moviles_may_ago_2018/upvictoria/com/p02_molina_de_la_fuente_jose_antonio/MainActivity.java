package dispositivos_moviles_may_ago_2018.upvictoria.com.p02_molina_de_la_fuente_jose_antonio;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //Variables necesarias para la peticion de permisos de lectura y escritura de archivos
    private static final int REQUEST_ID_READ_PERMISSION = 100;
    private static final int REQUEST_ID_WRITE_PERMISSION = 200;


    String Ruta;
    //Variables de nombres de archivos establecidas como finales debido a que no cambiaran
    final String NombreDirectorio ="Problema3_CDM";

    ListView LV1;
    Button addEncuesta_main;
    EditText nombreEncuesta;

    Context CX;
    private String nombres[]; // Nombre de las encuestas


    ArrayAdapter<String> adapter;
    List<String> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //peticion de permisos de lectura y escritura de archivos
        askPermissionOnly();
        CX= this;

        //Se asocian los id de los elementos a las variables
        addEncuesta_main = (Button) findViewById(R.id.btn_agregar);

        //crear directorio
        try {
            createDirectory();
        } catch (IOException e) {
            e.printStackTrace();
        }

        LV1 = (ListView) findViewById(R.id.listView);
        //lista contenedora de los nombres de las encuestas

        cargarEncuestas();


        //Agregar una nueva encuesta
        addEncuesta_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mostrar dialogo para agregar encuesta
                //Se abre una ventana par llenar los campos
                final Dialog dialog = new Dialog(CX);
                dialog.setContentView(R.layout.add);
                dialog.setTitle("Registrar encuesta");

                //inicializar controles del dialog crear encuesta (add)
                nombreEncuesta = (EditText) dialog.findViewById(R.id.nombre_encuesta_txt);



                //Boton guardar del dialogo
                Button dialogButtonOK = (Button) dialog.findViewById(R.id.dialogButtonOK);
                // if button is clicked, close the custom dialog
                dialogButtonOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //crear archivo con el nombre propuesto
                        if(!nombreEncuesta.getText().toString().equals("")){//verificar que no este vacio el edittext
                            final String encuesta_en_proceso = nombreEncuesta.getText().toString();
                            createFile(encuesta_en_proceso);
                            cargarEncuestas();
                            dialog.dismiss();

                            //Abrir dialog de preguntas

                            //Se abre una ventana par llenar los campos
                            final Dialog dialog_preguntas = new Dialog(CX);
                            dialog_preguntas.setContentView(R.layout.pregunta);
                            dialog_preguntas.setTitle("Registrar preguntas a encuesta");
                            //Enlazar elementos del dialog de preguntas
                            Button btn_guardarPregunta = dialog_preguntas.findViewById(R.id.guardar_pregunta_btn);
                            Button completarEncuesta = dialog_preguntas.findViewById(R.id.confirmar_encuesta_btn);
                            final EditText pregunta = dialog_preguntas.findViewById(R.id.pregunta_txt);
                            final EditText respuesta1 = dialog_preguntas.findViewById(R.id.respuesta1_txt);
                            final EditText respuesta2 = dialog_preguntas.findViewById(R.id.respuesta2_txt);
                            final EditText respuesta3 = dialog_preguntas.findViewById(R.id.respuesta3_txt);
                            final EditText respuesta4 = dialog_preguntas.findViewById(R.id.respuesta4_txt);


                            btn_guardarPregunta.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //validar que no esten vacios los campos
                                    if(!pregunta.getText().toString().equals("") &&
                                            !respuesta1.getText().toString().equals("") &&
                                            !respuesta2.getText().toString().equals("") &&
                                            !respuesta3.getText().toString().equals("") &&
                                            !respuesta4.getText().toString().equals("")){


                                        //guardar pregunta en archivo txt de la encuesta

                                        //limpiar campos
                                        pregunta.setText("");
                                        respuesta1.setText("");
                                        respuesta2.setText("");
                                        respuesta3.setText("");
                                        respuesta4.setText("");

                                        //mostrar mensaje a usuario
                                        Toast.makeText(CX, "Guardaste pregunta en encuesta " + encuesta_en_proceso, Toast.LENGTH_LONG).show();



                                    }
                                }
                            });

                            completarEncuesta.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(CX, "Guardaste la encuesta" + encuesta_en_proceso, Toast.LENGTH_LONG).show();
                                    dialog_preguntas.dismiss();
                                }
                            });
                            dialog_preguntas.show();



                        }else{
                            Toast.makeText(getApplicationContext(), "Ingresa el nombre de la encuesta",Toast.LENGTH_LONG).show();

                        }
                    }
                });


                //Boton cacelar del dialogo
                Button dialogButtonCancel = (Button) dialog.findViewById(R.id.dialogButtonCancel);
                // if button is clicked, close the custom dialog
                dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "presionaste boton cancelar",Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });


        LV1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),
                        ((TextView) view).getText()+"- Tocaste : "+lista.get(position), Toast.LENGTH_SHORT).show();
            }
        });


    }


    // dado un nombre de archivo, se crea uno en el directorio especificado
    public void createFile(String file_name){ // Crea un nuevo archivo de texto
        File extStore = Environment.getExternalStorageDirectory();
        String path = extStore.getAbsolutePath() + "/"+NombreDirectorio+"/";
        File f  = new File(path+file_name+".txt");
        if (f.exists()) {
            Toast.makeText(CX, "Ya existe una encuesta con el nombre: "+file_name,Toast.LENGTH_LONG).show(); //aviso al usuario del registro


        } else {
            try {
                Toast.makeText(CX, "Registraste la encuesta con nombre: "+file_name,Toast.LENGTH_LONG).show(); //aviso al usuario del registro
                f.createNewFile(); //se crea el archivo en caso e no existir
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //Funcion que crea la carpeta contenedora de las encuestas
    private void createDirectory () throws IOException {
        //Se realiza la lectura del archivo de la lista enlazada
        Ruta= Environment.getExternalStorageDirectory().getAbsolutePath().toString()+"/"+NombreDirectorio;
        File F = new File (Ruta);
        if (F.exists()) { // Ya existe el directorio
        }
        else { // No existe, crea el directorio
            Toast.makeText(getApplicationContext(),"Se ha creado la ruta:"+Ruta,Toast.LENGTH_SHORT).show();
            F.mkdir();
        }
    }

    //Metodo para cargar controles (actualizar la lista)
    public void cargarEncuestas()
    {
        File extStore = Environment.getExternalStorageDirectory();
        String path = extStore.getAbsolutePath() + "/" + NombreDirectorio;
        File carpeta = new File(path);

        lista = new ArrayList<String>();//lista contenedora de los nombres de las encuestas
        // Comprobar si existe la carpeta
        if (!carpeta.exists())
        {
            carpeta.mkdir();
            return;
        }

        // Se guardan los archivos
        String Cadena="";
        File archivos[] = carpeta.listFiles();
        nombres = new String[archivos.length];

        for (int i = 0; i < nombres.length; i++)
        {
            String name = archivos[i].getName().toUpperCase().replace(".",";");
            nombres[i] = name.split(";")[0];
            if (nombres[i].equals("")) { } else {lista.add(nombres[i]); }
        }
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, lista); //crear adaptador para adjuntar lista de nombres de archivos
        LV1.setAdapter(adapter); //colocar la lista en el adaptador


    }




    //Funcion para la peticion de permisos de lectura y escritura de archivos
    private void askPermissionOnly() {
        this.askPermission(REQUEST_ID_WRITE_PERMISSION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        this.askPermission(REQUEST_ID_READ_PERMISSION,
                Manifest.permission.READ_EXTERNAL_STORAGE);
    }
    //Funcion para la peticion de permisos de lectura y escritura de archivos
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

    //Funcion para la peticion de permisos de lectura y escritura de archivos
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
                        Toast.makeText(getApplicationContext(), "Permiso de lectura concedido!", Toast.LENGTH_SHORT).show();
                        /*try {
                            //showList();
                            x = 10;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                    }
                }
                case REQUEST_ID_WRITE_PERMISSION: {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        //writeFile();
                        //
                        Toast.makeText(getApplicationContext(), "Permiso de escritura concedido!", Toast.LENGTH_SHORT).show();
                        /*try {
                           // showList();
                            int x = 10;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                    }
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "Permission Cancelled!", Toast.LENGTH_SHORT).show();
        }
    }
}

package dispositivos_moviles_may_ago_2018.upvictoria.com.p01_molina_de_la_fuente_jose_antonio;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ID_READ_PERMISSION = 100;
    private static final int REQUEST_ID_WRITE_PERMISSION = 200;

    Button insertF;
    Button insertL;
    Button deleteF;
    Button deleteL;
    Button buscarE;
    EditText element;
    EditText busqueda;
    EditText visualizacion;
    Context mensaje;

    String Ruta;
    final String NombreDirectorio ="Problema8_CDM";
    final String NombreArchivo = "lista_enlazada";

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
        mensaje=this;

        //Asignar controles a variables
        insertF = (Button) findViewById(R.id.btn_if);
        insertL = (Button) findViewById(R.id.btn_il);
        deleteF = (Button) findViewById(R.id.btn_df);
        deleteL = (Button) findViewById(R.id.btn_dl);
        buscarE = (Button) findViewById(R.id.btn_buscar);
        element = (EditText) findViewById(R.id.txt_elemento);

        visualizacion = (EditText) findViewById(R.id.txt_visualizacion);
        busqueda = (EditText) findViewById(R.id.txt_resultadoB);

        //Crear el archivo en el directorio
        createFile(NombreArchivo);
        //Mostrar lista en el textview
        try {
            showList();
        } catch (IOException e) {
            e.printStackTrace();
        }

        insertF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(validarElemento(element.getText().toString())){
                        f_insertF(element.getText().toString());
                        element.setText(""); busqueda.setText("");
                        showList();
                    }else{
                        Toast.makeText(getBaseContext(),
                                "Ingresa el elemento por favor",
                                Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        insertL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarElemento(element.getText().toString())){
                    try {
                        f_insertL(element.getText().toString());
                        showList();
                        element.setText(""); busqueda.setText("");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getBaseContext(),
                            "Ingresa el elemento por favor",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        deleteF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    f_deleteF();
                    showList();
                    busqueda.setText("");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        deleteL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    f_deleteL();
                    showList();
                    busqueda.setText("");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        buscarE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    f_buscarE(element.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    //Funcion que lee la lista enlazada y encuentra las posiciones que coinciden con el elemento ingresado de busqueda
    private void f_buscarE(String e) throws IOException{
        if(!e.equals("")){
            ArrayList<String> list = new ArrayList<String>();
            list = readList();
            String match = "";
            for(int i=0; i<list.size();i++){
                if(list.get(i).equals(e)){
                    match += i + " , ";
                }
            }
            if(!match.equals("")){
                int x = match.lastIndexOf(",");
                match = match.substring(0, x -1);
                busqueda.setText("Match en posiciones: "+match);
            }else{
                busqueda.setText("No se encontraron coincidencias");
            }

        }else{
            Toast.makeText(getBaseContext(),
                    "Ingresa un elemento para poder buscarlo",
                    Toast.LENGTH_SHORT).show();
        }

    }
    private void f_insertF(String e) throws IOException{
        ArrayList<String> old_list = new ArrayList<String>();
        ArrayList<String> new_list = new ArrayList<String>();
        old_list = readList();
        new_list.add(e);
        for(int i=1; i <= old_list.size(); i++){
            new_list.add(old_list.get(i-1));
        }
        if(saveList(new_list)) Toast.makeText(getBaseContext(),
                "Se inserto al principio de la lista",
                Toast.LENGTH_SHORT).show();
    }

    private void f_insertL(String e) throws IOException{
        ArrayList<String> old_list = new ArrayList<String>();
        ArrayList<String> new_list = new ArrayList<String>();
        old_list = readList();
        for(int i=0; i < old_list.size(); i++){
            new_list.add(old_list.get(i));
        }
        new_list.add(e);
        if(saveList(new_list)) Toast.makeText(getBaseContext(),
                "Se inserto al final de la lista",
                Toast.LENGTH_SHORT).show();
    }

    private void f_deleteF() throws IOException{
        ArrayList<String> old_list = new ArrayList<String>();
        ArrayList<String> new_list = new ArrayList<String>();
        old_list = readList();
        if(old_list.size()!=0){
            for(int i=1; i < old_list.size(); i++){
                new_list.add(old_list.get(i));
            }
            if(saveList(new_list))
                Toast.makeText(getBaseContext(),
                        "Se borro el primer elemento de la lista",
                        Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getBaseContext(),
                    "La lista esta vacia, no se puede borrar el primer elemento",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void f_deleteL() throws IOException{
        ArrayList<String> old_list = new ArrayList<String>();
        ArrayList<String> new_list = new ArrayList<String>();
        old_list = readList();
        if(old_list.size()!=0){
            for(int i=0; i < old_list.size() - 1; i++){
                new_list.add(old_list.get(i));
            }
            if(saveList(new_list))Toast.makeText(getBaseContext(),
                    "Se borro el ultimo elemento de la lista",
                    Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getBaseContext(),
                    "La lista esta vacia, no se puede borrar el ultimo elemento",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private boolean saveList (ArrayList<String> lista) throws IOException {
        Ruta= Environment.getExternalStorageDirectory().getAbsolutePath().toString()+"/"+NombreDirectorio;
        File F = new File (Ruta);
        if (F.exists()) { // Ya existe el directorio
            File F2 = new File (Ruta, NombreArchivo+".txt");

            // Si existe o no existe se crea...
            FileOutputStream fOut = new FileOutputStream(F2);
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);
            String listaEnTexto="";
            for(int i=0; i < lista.size(); i++){
                listaEnTexto+= lista.get(i).toString() + "\n";
            }
            myOutWriter.append(listaEnTexto);
            myOutWriter.close();
            fOut.close();
            return true;
        }
        return false;
    }

    //Funcion que lee el archivo y retorna un ArrayList con los elementos de la lista
    private ArrayList<String> readList()  throws IOException {
        ArrayList<String> lista = new ArrayList<>();
        Ruta= Environment.getExternalStorageDirectory().getAbsolutePath().toString()+"/"+NombreDirectorio;
        File F = new File (Ruta);
        if (F.exists()) { // Ya existe el directorio
            File F2 = new File (Ruta, NombreArchivo+".txt");

            if (F2.exists()) { // Checar si el archivo Existe

                FileInputStream fIn = new FileInputStream(F2);
                BufferedReader myReader = new BufferedReader(
                        new InputStreamReader(fIn));
                String aDataRow = "";

                while ((aDataRow = myReader.readLine()) != null) {
                    lista.add(aDataRow);
                }
                myReader.close();
            }
            else {
                Toast.makeText(getApplicationContext(),"No EXISTE tal ARCHIVO en el Directorio: "+Ruta,Toast.LENGTH_SHORT).show();
            }
        }
        else { // No existe, crea el directorio
            Toast.makeText(getApplicationContext(),"Se ha creado la ruta RUTA:"+Ruta,Toast.LENGTH_SHORT).show();
            F.mkdir();
        }
        return lista;
    }

    private boolean validarElemento(String e){
        if(!e.equals(""))
            return true;
        else
            return false;
    }

    //  Crea un nuevo archovo-Lista, recibe el nombre de esta.
    public void createFile(String file_name){ // Crea un nuevo archivo de texto
        File extStore = Environment.getExternalStorageDirectory();
        String path = extStore.getAbsolutePath() + "/"+NombreDirectorio+"/";
        File f  = new File(path+file_name+".txt");
        if (f.exists()) {
            //Toast.makeText(mensaje,"El archivo ya existe",Toast.LENGTH_SHORT).show();
        } else {
            try {
                f.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // recomiendo limpiar el EditText para Evitar duplicar el archivo
        }
    }

    //Mostrar visualizacion de lista
    private void showList () throws IOException {
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
                int c=0;
                while ((aDataRow = myReader.readLine()) != null) {
                    aBuffer += aDataRow + " -> ";
                    c++;
                }
                if(aBuffer!="") aBuffer+="null";
                visualizacion.setText(aBuffer + "\n\nLista con "+c+" elementos");
                myReader.close();

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
                        Toast.makeText(getApplicationContext(), "Permiso de lectura concedido!", Toast.LENGTH_SHORT).show();
                        try {
                            showList();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                case REQUEST_ID_WRITE_PERMISSION: {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        //writeFile();
                        //
                        Toast.makeText(getApplicationContext(), "Permiso de escritura concedido!", Toast.LENGTH_SHORT).show();
                        try {
                            showList();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "Permission Cancelled!", Toast.LENGTH_SHORT).show();
        }
    }
}

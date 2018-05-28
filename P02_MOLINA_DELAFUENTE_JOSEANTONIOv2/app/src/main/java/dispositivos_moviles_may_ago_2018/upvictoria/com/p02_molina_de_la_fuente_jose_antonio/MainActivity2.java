package dispositivos_moviles_may_ago_2018.upvictoria.com.p02_molina_de_la_fuente_jose_antonio;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {


    Context CX;
    private String nombres[]; // Nombre de las encuestas en el directorio (solo para mostrar en el list view)
    String Ruta;
    //Variables de nombres de archivos establecidas como finales debido a que no cambiaran
    final String NombreDirectorio ="Problema3_CDM";

    //nombre de encuesta
    String nombreEncuesta = "";
    String contenidoEncuesta = "";

    //Variables enlazadas con los controles para manipularlas
    EditText nombreEDITTEXT;
    EditText contenidoEDITTEXT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showencuesta);

        CX= this;

        nombreEncuesta = getIntent().getStringExtra("EXTRA_N_ENCUESTA");
        contenidoEncuesta = getIntent().getStringExtra("EXTRA_CONTENIDO_ENCUESTA");

        nombreEDITTEXT = (EditText) findViewById(R.id.nombreE_txt);

        contenidoEDITTEXT = (EditText) findViewById(R.id.contenidoE_txt);

        nombreEDITTEXT.setText(nombreEncuesta);

        contenidoEDITTEXT.setText(contenidoEncuesta);

        try {
            createControles(contenidoEncuesta);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    //crear controles de la encuesta (mostrar preguntas y respuestas)
    public void createControles(String contenido) throws IOException {
        //meter el contenido de la encuesta en un buffer para poder iterar sobre las lineas
        BufferedReader bufReader = new BufferedReader(new StringReader(contenido));
        //cadena de cada linea
        String line = "";
        int iterate = 0;
        //contar numero de lineas del contenido de la encuesta
        while( (line = bufReader.readLine()) != null){
            iterate++;
        }

        //La linea 0 es una pregunta, igual 0 + 5 + 5 + 5
        line = "";
        int i = 0;
        TextView[] preguntas_TV = new TextView[iterate / 5]; //definir los textview para las preguntas
        RadioButton[] respuestas_RB = new RadioButton[ (iterate / 5) * 4]; //definir los edittext para las respuestas

        //Enlazar linearLayout para colocar los botones
        LinearLayout mLinearLayout = new LinearLayout(this);

        mLinearLayout.setOrientation(LinearLayout.VERTICAL);
        int nTextView= 0, nRadios = 0;

        BufferedReader bufReader2 = new BufferedReader(new StringReader(contenido));
        ArrayList<String> contenido_real = new ArrayList<>(); //se guarda el contenido de la encuesta
        while( (line=bufReader2.readLine()) != null ){
            contenido_real.add(line);
            if(i % 5 == 0 && i != 0){ //linea representa una pregunta
                preguntas_TV[nTextView] = new TextView(this);
                preguntas_TV[nTextView].setText((i-4) + "." + contenido_real.get(i-5));

                respuestas_RB[nRadios] = new RadioButton(this); //respuesta1
                respuestas_RB[nRadios].setText( contenido_real.get(i-4) );

                respuestas_RB[nRadios+1] = new RadioButton(this); //respuesta2
                respuestas_RB[nRadios+1].setText( contenido_real.get(i-3) );

                respuestas_RB[nRadios+2] = new RadioButton(this); //respuesta3
                respuestas_RB[nRadios+2].setText( contenido_real.get(i-2) );

                respuestas_RB[nRadios+3] = new RadioButton(this); //respuesta4
                respuestas_RB[nRadios+3].setText( contenido_real.get(i-1) );

                preguntas_TV[nTextView].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                       LinearLayout.LayoutParams.WRAP_CONTENT));
                respuestas_RB[nRadios].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                respuestas_RB[nRadios+1].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                respuestas_RB[nRadios+2].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                respuestas_RB[nRadios+3].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

                //AGREGAR AL LAYOUT
                mLinearLayout.addView(preguntas_TV[nTextView]);
                mLinearLayout.addView(respuestas_RB[nRadios]);
                mLinearLayout.addView(respuestas_RB[nRadios+1]);
                mLinearLayout.addView(respuestas_RB[nRadios+2]);
                mLinearLayout.addView(respuestas_RB[nRadios+3]);

                nTextView++;
                nRadios+=4;
                //reiniciar contenido real
                contenido_real.clear();
                i=0;

                //pos i - 5 = pregunta
                //pos i - 4 = res 1
                //pos i - 3 = res 2
                //pos i - 2 = res 3
                //pos i - 1 = res 4



            }
            i++;
        }
        //mostrar en pantalla lo agregado al layout
        Toast.makeText(CX, "Numero de hijos: "+mLinearLayout.getChildCount(),Toast.LENGTH_LONG).show(); //aviso al usuario del registro

        this.setContentView(mLinearLayout);

       /* ScrollView sv = new ScrollView(this);

        LinearLayout ll = new LinearLayout(this);

        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);

        EditText et = new EditText(this);

        et.setText("weeeeeeeeeee~!");

        ll.addView(et);
*/

        //this.setContentView(sv);



        Toast.makeText(CX, "La encuesta tiene: "+ iterate + " lineas",Toast.LENGTH_LONG).show(); //aviso al usuario del registro

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


    //Funcion que dada un string con el contenido de una pregunta y sus 4 respuestas, la guarda en su correspondiente archivo
    private boolean saveQuestion ( ArrayList<String>  pregunta,String archivo) throws IOException {
        String contentLog="";
        Ruta= Environment.getExternalStorageDirectory().getAbsolutePath().toString()+"/"+NombreDirectorio;
        //Se lee el contenido del archivo y de log y se guarda en la variable contentLog
        File F = new File (Ruta);
        if (F.exists()) { // Ya existe el directorio
            File F2 = new File (Ruta, archivo+".txt");
            if (F2.exists()) { // Checar si el archivo Existe
                FileInputStream fIn = new FileInputStream(F2);
                BufferedReader myReader = new BufferedReader(
                        new InputStreamReader(fIn));
                String aDataRow = "";
                while ((aDataRow = myReader.readLine()) != null) {
                    contentLog+=aDataRow+"\n"; //se guarda cada linea en contentLog del archivo txt
                }
                myReader.close();
            }
        }
        //Se procede a guardar remplazando el archivo que ya se tenia
        File Fwrite = new File (Ruta);
        if (Fwrite.exists()) { // Ya existe el directorio
            File F2 = new File (Ruta, archivo+".txt");
            // Si existe o no existe se crea...
            FileOutputStream fOut = new FileOutputStream(F2);
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);
            //Guardar el contenido leido del archivo y la primera pregunta
            String contenidoArchivo = contentLog + pregunta.get(0);
            //iterar para guardar el resto de las preguntas
            for(int i=1; i< pregunta.size(); i++){
                contenidoArchivo += pregunta.get(i);
            }
            myOutWriter.append(contenidoArchivo); //escritura de la cadena en el archivo
            myOutWriter.close(); //se cierra la conexion de la escritura del archivo
            fOut.close(); //se cierra la conexion con el archivo
            return true;
        }
        return false;
    }



    //Funcion que lee el archivo de una encuesta dada por el param NombreArchivo y retorna un String con el contenido de esta
    private String readFile(String NombreArchivo)  throws IOException {
        String content = ""; //var donde se guardara el contenido del archivo
        Ruta= Environment.getExternalStorageDirectory().getAbsolutePath().toString()+"/"+NombreDirectorio; //definicion de la ruta
        File F = new File (Ruta); //se crea el directorio si no existe
        if (F.exists()) { // Ya existe el directorio
            File F2 = new File (Ruta, NombreArchivo+".txt"); //se crea la variable de tipo archivo

            if (F2.exists()) { // Checar si el archivo Existe

                FileInputStream fIn = new FileInputStream(F2);
                BufferedReader myReader = new BufferedReader(
                        new InputStreamReader(fIn));
                String aDataRow = "";

                while ((aDataRow = myReader.readLine()) != null) {
                    content += aDataRow + "\n";
                }
                myReader.close();
            }
        }
        else { // No existe, crea el directorio
            Toast.makeText(getApplicationContext(),"Se ha creado la ruta :"+Ruta + " y los archivos necesarios",Toast.LENGTH_LONG).show();
            F.mkdir();
        }
        return content; //se retorna el string con el contenido del archivo
    }

}

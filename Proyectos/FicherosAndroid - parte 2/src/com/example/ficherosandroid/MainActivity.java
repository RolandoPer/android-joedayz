package com.example.ficherosandroid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import com.example.ficherosandroid.util.Constantes;
import com.example.ficherosandroid.util.Utilitarios;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener{

	//MEM EXT.
	Button btnWriteMemExt;
	Button btnReadMemExt;
	
	//PARA AGREGAR TITULO Y CONTENIDO
	EditText editTitulo;
	EditText editContenido;
	
	//RECURSO
	Button btnRecurso;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Map<String, Object> valida = validacionMemExt();
        System.out.println("valida.disponible "+valida.get("DISPONIBLE"));
        System.out.println("valida.escritura "+valida.get("ESCRITURA"));
        
        btnWriteMemExt = (Button)findViewById(R.id.btnWriteMenExt);
        btnWriteMemExt.setOnClickListener(this);
        btnReadMemExt = (Button)findViewById(R.id.btnReadMenExt);
        btnReadMemExt.setOnClickListener(this);
        
        //RECURSO
        btnRecurso =  (Button)findViewById(R.id.btnRecurso);
        btnRecurso.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }    

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnWriteMenExt:
			String message="";
			editTitulo = (EditText)findViewById(R.id.editTitulo);
	        String titulo = editTitulo.getText().toString();
	        editContenido =  (EditText)findViewById(R.id.editContenido);
	        String contenido = editContenido.getText().toString();
	        
	        message = writeMemExt(titulo, contenido);
			Utilitarios.message(this, message);
			break;
		case R.id.btnReadMenExt:
			String contenidoSD = readMemExt();
			Utilitarios.message(this, contenidoSD);
			break;
			
		case R.id.btnRecurso:
			
			String contenidoRes = readRecurso();
			Utilitarios.message(this, contenidoRes);
			
			break;
		default:
			break;
		}
		
	}
   
	private String readRecurso(){
		String contentidoRes = "";
		
		try{
		    InputStream fraw = getResources().openRawResource(R.raw.constante_res);
		    BufferedReader brin = new BufferedReader(new InputStreamReader(fraw));
		 
		    contentidoRes = brin.readLine();
		    fraw.close();
		}
		catch (Exception ex){
		    Log.e("Ficheros", "Error al leer fichero desde recurso raw");
		    contentidoRes = Constantes.MESSAGE_ERROR+ex;
		}

		return contentidoRes;
	}
	
	
	private String readMemExt(){
		String content="";
		try{
		    File ruta_sd = Environment.getExternalStorageDirectory();
		    File f = new File(ruta_sd.getAbsolutePath(), "/pruebaSD/"+"prueba_sd"+Constantes.EXT_DOC);
		    BufferedReader fin =   new BufferedReader(new InputStreamReader(new FileInputStream(f)));
		    content = fin.readLine();
		    fin.close();
		}catch (Exception ex){
		    Log.e("Ficheros", "Error al leer fichero desde tarjeta SD");
		    content = Constantes.MESSAGE_ERROR+ex;
		}
		return content;
	}
	
	
	private String writeMemExt(String titulo, String contenido){
		String message="";
		Map<String, Object> valida = validacionMemExt();
		if(valida.get("DISPONIBLE").toString().equalsIgnoreCase("true") && 
				valida.get("ESCRITURA").toString().equalsIgnoreCase("true")){
			try{
		       File ruta_sd = Environment.getExternalStorageDirectory(); 
		       
		       String rutaBase = ruta_sd.getAbsolutePath();
		       //CREAR CARPETA ADICIONAL LLAMADA "pruebaSD"
		       File dir = new File(rutaBase+"/pruebaSD");
		       dir.mkdir();
		       File f = new File(dir.toString(), titulo+Constantes.EXT_DOC);
		       
		       
		       OutputStreamWriter fout = new OutputStreamWriter( new FileOutputStream(f)); 
		    
		        fout.write(contenido);
		        fout.close();
		        message = Constantes.MESSAGE_OK;
		    }
		    catch (Exception ex){
		        Log.e("Ficheros", "Error al escribir fichero a tarjeta SD");
		        message = Constantes.MESSAGE_ERROR+ex;
		    }
  
		}
		return message;
	}
	
	private Map<String, Object> validacionMemExt(){
		Map<String, Object> validate = new HashMap<String, Object>();
		
		boolean sdDisponible = false; 
		boolean sdAccesoEscritura = false; 

		//Comprobamos el estado de la memoria externa (tarjeta SD) 
		String estado = Environment.getExternalStorageState(); 
		if (estado.equals(Environment.MEDIA_MOUNTED)) { 
			sdDisponible = true; 
			sdAccesoEscritura = true; 
		}else if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) { 
			sdDisponible = true; 
			sdAccesoEscritura = false; 
		}else{
			sdDisponible = false; 
			sdAccesoEscritura = false;
		}    
 
		validate.put("DISPONIBLE", sdDisponible);
		validate.put("ESCRITURA", sdAccesoEscritura);
		
		return validate;
	}
	
}

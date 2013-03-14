package com.joedayz.com;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.joedayz.com.util.Constantes;
import com.joedayz.com.util.Utilitarios;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class FicherosActivity extends Activity implements OnClickListener{

	//MANEJO MEM INT.
	Button btnWriteInt;
	Button btnReadInt;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficheros);
        
        //VINCULO CON LA VISTA
        btnWriteInt = (Button)findViewById(R.id.btnWriteInt);
        btnWriteInt.setOnClickListener(this);
        
        btnReadInt =  (Button)findViewById(R.id.btnReadInt);
        btnReadInt.setOnClickListener(this);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_ficheros, menu);
        return true;
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnWriteInt:
			String message = writeInt();
			Utilitarios.message(this, message);
			
			break;
		case R.id.btnReadInt:
			String content = readInt();
			Utilitarios.message(this, content);
			break;
			
		default:
			break;
		}
		
	}
    
	private String writeInt(/*String nameArc*/){
		String message = "";
		
		try{
	        OutputStreamWriter fout= new OutputStreamWriter(openFileOutput("prueba_int"+Constantes.EXT_TXT, Context.MODE_PRIVATE));
	        fout.write("texto dentro del archivo");
	        fout.close();
	        message = Constantes.MESSAGE_OK;
	    }
	    catch (Exception ex){
	        Log.e("Ficheros", "Error al escribir fichero a memoria interna");
	        message = Constantes.MESSAGE_ERROR;
	    }
		
		return message;
	}
	
	private String readInt(){
		String contenido="";
		try{
			BufferedReader fin = new BufferedReader(new InputStreamReader(openFileInput("prueba_int"+Constantes.EXT_TXT)));
			contenido = fin.readLine();
			fin.close();
		}
		catch (Exception ex){
			Log.e("Ficheros", "Error al leer fichero desde memoria interna");
			contenido = Constantes.MESSAGE_ERROR+ex;
		}

		return contenido;
	}
	
}

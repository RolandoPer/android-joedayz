package com.joedayz.com.util;

import com.joedayz.com.R;

import android.content.Context;
import android.widget.Toast;

public class Utilitarios {

	public static void message(Context context, String msg){
		int duracion = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, msg, duracion);
		toast.show();
	}
	
}

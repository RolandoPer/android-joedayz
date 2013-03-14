package com.joedayz.com;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.joedayz.com.model.BNTime;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TimeActivity extends Activity implements OnClickListener{

	Button btnTime;
	TextView txtDetalle;
	List<BNTime> times = new ArrayList<BNTime>();
	String cadena;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        
        System.out.println("MUESTRA INFORMACION");
        
        btnTime = (Button)findViewById(R.id.btnTime);
        btnTime.setOnClickListener(this);
        
        txtDetalle = (TextView)findViewById(R.id.txtDetalle);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_time, menu);
        return true;
    }

	@Override
	public void onClick(View v) {
		 switch (v.getId()) {
		case R.id.btnTime:
			List<BNTime> listTimes = updateTime();
			cadena = "";
			
			for (int i = 0; i < listTimes.size(); i++) {
				cadena += listTimes.get(i).getTextTime()+"\n";
			}
			
			txtDetalle.setText(cadena);
			
//			btnTime.setText(new Date().toString());
			break;

		default:
			break;
		}
		
	}
    
	
	private List<BNTime> updateTime(){
		
		BNTime time = new BNTime();
		time.setTextTime(new Date().toString());
		times.add(time);
		return times;
	}
	
}

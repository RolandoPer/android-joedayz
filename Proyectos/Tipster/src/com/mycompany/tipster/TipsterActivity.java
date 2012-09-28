package com.mycompany.tipster;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class TipsterActivity extends Activity {
	
	//Widgets en la aplicacion
	private EditText txtAmount;
	private EditText txtPeople;
	private EditText txtTipOther;
	private RadioGroup rdoGroupTips;
	private Button btnCalculate;
	private Button btnReset;
	
	private TextView txtTipAmount;
	private TextView txtTotalToPay;
	private TextView txtTipPerPerson;
	
	//para manejar el id del radio button seleccionado
	private int radioCheckedId = -1;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        //acceder a los widgets segun su id que encontramos en R.java
        txtAmount = (EditText) findViewById(R.id.txtAmount);
        //cuando la app cargue, el cursor se posicione en el campo Amount
        txtAmount.requestFocus();
        
        txtPeople = (EditText) findViewById(R.id.txtPeople);
        txtTipOther = (EditText) findViewById(R.id.txtTipOther);
        
        rdoGroupTips = (RadioGroup) findViewById(R.id.RadioGroupTips);
        
        btnCalculate = (Button) findViewById(R.id.btnCalculate);
        //cuando la app cargue, el boton Calculate este deshabilitado
        btnCalculate.setEnabled(false);
        
        btnReset = (Button) findViewById(R.id.btnReset);
        
        txtTipAmount = (TextView) findViewById(R.id.txtTipAmount);
        txtTotalToPay = (TextView) findViewById(R.id.txtTotalToPay);
        txtTipPerPerson = (TextView) findViewById(R.id.txtTipPerPerson);
        
        //cuando la app cargue, deshabilitar el campos Otro porcentaje
        txtTipOther.setEnabled(false);
        
        rdoGroupTips.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// enabled/disabled el campo Other
				if(checkedId == R.id.radioFifteen || 
						checkedId == R.id.radioTwenty){
					txtTipOther.setEnabled(false);
				
					/*habilitar el boton calculate si el Total y numero de personas tiene
					valores validos*/
					btnCalculate.setEnabled(txtAmount.getText().length()>0 &&
						txtPeople.getText().length()>0);
				}
				
				if(checkedId == R.id.radioOther){
					txtTipOther.setEnabled(true);
					txtTipOther.requestFocus();
					btnCalculate.setEnabled(txtAmount.getText().length()>0 &&
							txtPeople.getText().length()>0 &&
							txtTipOther.getText().length()>0);
				}
				//para determinar la seleccion
				radioCheckedId = checkedId;
				
			}
		});
        
        //listeners para las cajitas de texto
        txtAmount.setOnKeyListener(mKeyListener);
        txtPeople.setOnKeyListener(mKeyListener);
        txtTipOther.setOnKeyListener(mKeyListener);
        
        //a–adir los listeners para los botones
        btnCalculate.setOnClickListener(mClickListener);
        btnReset.setOnClickListener(mClickListener);
  
        
    }
    
    
    private OnKeyListener mKeyListener = new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				
				switch (v.getId()){
					case R.id.txtAmount:
					case R.id.txtPeople:
						btnCalculate.setEnabled(txtAmount.getText().length()>0 
								&& txtPeople.getText().length()>0);
						break;
					case R.id.txtTipOther:
						btnCalculate.setEnabled(txtAmount.getText().length()>0 
								&& txtPeople.getText().length()>0 
								&& txtTipOther.getText().length()>0);
						break;						
				}
				
				
				return false;
			}


      	
      };  
      
      
     private OnClickListener mClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.btnCalculate){
				calculate();
			}else{
				reset();
			}
			
		}

    	 
     };
      
     

	private void reset() {
		txtTipAmount.setText("");
		txtTotalToPay.setText("");
		txtTipPerPerson.setText("");
		txtAmount.setText("");
		txtPeople.setText("");
		txtTipOther.setText("");
		rdoGroupTips.clearCheck();
		rdoGroupTips.check(R.id.radioFifteen);
		//focus en el primer campo
		txtAmount.requestFocus();
	}   

	
	private void calculate(){
		Double billAmount = Double.parseDouble(txtAmount.getText().toString());
		Double totalPeople = Double.parseDouble(txtPeople.getText().toString());
		Double percentage = null;
		boolean isError = false;
		
		if(billAmount < 1.0){
			showErrorAlert("Ingresa una cantidad Total valida.", txtAmount.getId());
			isError = true;
			
		}
		
		if(totalPeople < 1.0){
			showErrorAlert("Ingresa una cantidad de Nro. de personas valida.",
					txtPeople.getId());
			isError = true;
			
		}
		
		/*si el usuario nunca cambia su radio selection, entonces el valor por
		defecto es 15%. Pero hay que verificar*/
		if (radioCheckedId == -1){
			radioCheckedId = rdoGroupTips.getCheckedRadioButtonId();
		}
		
		if(radioCheckedId == R.id.radioFifteen){
			percentage = 15.00;
		} else if(radioCheckedId == R.id.radioTwenty){
			percentage = 20.00;
		} else if(radioCheckedId == R.id.radioOther){
			percentage = Double.parseDouble(txtTipOther.getText().toString());
		}
		
		
		if(percentage < 1.0){
			showErrorAlert("Ingresa una cantidad de Porcentaje de Propina valida.",
					txtTipOther.getId());
			isError = true;
			
		}
				
			
		if(!isError){
			Double tipAmount =  ( (billAmount * percentage) / 100);
			Double totalToPay = billAmount + tipAmount;
			Double perPersonPays = totalToPay / totalPeople;
			
			txtTipAmount.setText(tipAmount.toString());
			txtTotalToPay.setText(totalToPay.toString());
			txtTipPerPerson.setText(perPersonPays.toString());
		}
		
	}


	private void showErrorAlert(String errorMessage, final int fieldId) {
		new AlertDialog.Builder(this).setTitle("Error").
			setMessage(errorMessage).setNeutralButton("Close",
					new DialogInterface.OnClickListener() {
				
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							findViewById(fieldId).requestFocus();
							
						}
					}).show();
		
	}
}
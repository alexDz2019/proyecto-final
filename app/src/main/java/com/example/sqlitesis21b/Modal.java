package com.example.sqlitesis21b;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Modal {
    Dialog myDialog;
    AlertDialog.Builder dialogo;
    boolean validaInput = false;
    String codigo;
    String descripcion="";
    String precio;
    SQLiteDatabase bd = null;
    Dto datos = new Dto();

    public void VentanaEmergente(final Context context) {
        myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.ventana1);
        myDialog.setTitle("Search");
        myDialog.setCancelable(false);

        final AdminSQLiteOpenHelper conexion = new AdminSQLiteOpenHelper(context);
        final EditText et_cod = (EditText)myDialog.findViewById(R.id.et_cod);
        Button btn_buscar = (Button)myDialog.findViewById(R.id.btn_buscar);
        TextView tv_close = (TextView)myDialog.findViewById(R.id.tv_close);

        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        btn_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_cod.getText().toString().length()==0){
                    validaInput = false;
                    et_cod.setError("Campo Obligatorio");
                }else{
                    validaInput = true;
                }
                if (validaInput){
                    String cod = et_cod.getText().toString();
                    datos.setCodigo(Integer.parseInt(cod));
                    if (conexion.consultaCodigo(datos)){
                        codigo = String.valueOf(datos.getCodigo());
                        descripcion = datos.getDescripcion();
                        precio = String.valueOf(datos.getPrecio());
                        String action;
                        Intent intent = new Intent(context,MainActivity.class);
                        intent.putExtra("senal","1");
                        intent.putExtra("codigo",codigo);
                        intent.putExtra("descripcion",descripcion);
                        intent.putExtra("precio",precio);
                        context.startActivity(intent);
                    }else{
                        Toast.makeText(context, "No se han encontrado resultados",Toast.LENGTH_SHORT);
                    }
                }else {
                    Toast.makeText(context, "No se ha especificado lo que desea buscar",Toast.LENGTH_SHORT);
                }
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
}


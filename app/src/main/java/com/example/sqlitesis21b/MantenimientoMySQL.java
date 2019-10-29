package com.example.sqlitesis21b;

import android.app.DownloadManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MantenimientoMySQL {

    Boolean estadoGuardar = false;

    public void guardar (final Context context, final  String codigo, final String precio, final String descricion){
        String url = "http://mjql.com.sv/mysql_crud/guardar.php";
        StringResquest resquest = new StringRequest { Request.Method.POST, url,
        new Readable.Listener<>(){

            @Override

            public void onResponse (String response) {
                try {

                    JSONObject resquestJSON = new JSONObject(response.toString());
                    String estado = requestJSON.getString(name"estado");
                    String mensaje = requestJSON.getString(name"mensaje");

                    if (estado.equals("1")) {
                        makeText(context, mensaje, Toast.LENGTH_SHORT).show();
                    } else if (mensaje.equals("2")) {
                        makeText(context, text: "Error, no se pudo guardar. \n" + "Intentelo mas tarde.", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }, new Response.ErrorListener(){
            @Override

               public void onErrorResponse (VollyError error){
                makeText (context, text: "Error, no se pudo guardar. \n " + "verifique su acceso a internet.", Toast.LENGTH_SHORT).show();
                }
            }
         protected  map<String, String  > getParams() throws AuthFailureError {
             map.put("content-type", "application/json; charset=utf-8");
             map.put("Accept", "aplication/json");
             map.put("codigo", codigo);
             map.put("descripcion", descricion);
             map.put("precio", precio);
             return map;
         }
};



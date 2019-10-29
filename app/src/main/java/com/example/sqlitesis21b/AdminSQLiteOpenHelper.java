package com.example.sqlitesis21b;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    ArrayList<String> listaArticulos;
    ArrayList<Dto>articulosLista;
    boolean estadoDelete = false;

    /*
    public AdminSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }*/

    public AdminSQLiteOpenHelper(Context context) {
        super(context, "Administracion.db", null, 1);
    }




    public SQLiteDatabase bd(){
        SQLiteDatabase bd = this.getWritableDatabase();
        return  bd;
    }

    public boolean InsertTradicional(Dto datos){  //OBLIGADO A MANDARLE UN OBJETO
        boolean estado = true;
        int resultado;
        try {

            int codigo = datos.getCodigo();
            String descripcion = datos.getDescripcion();
            double precio = datos.getPrecio();

            //si la fila se mueve a una posicion y no esta ese registro lo guarda sino no hace el insert
            Cursor fila = bd().rawQuery("select codigo from articulos where codigo='" + codigo+"'", null);
            if ( fila.moveToFirst()==true){
                estado = false;
            }else {
                String SQL = "INSERT INTO articulos \n"+
                        "(codigo, descripcion,precio)\n"+
                        "VALUES \n" +
                        "('"+ String.valueOf(codigo) +"', '"+ descripcion + "','"+ String.valueOf(precio)+ "');"; //String.Valueof para convertir a cadena(texto)

                bd().execSQL(SQL); //ejecuta el insert
                bd().close();

                estado=true; //se pudo guardar
            }
        }catch (Exception e){
            estado = false;
            Log.e("error.",e.toString());
        }

        return estado;
    }



    public boolean consultaCodigo(Dto datos){
        boolean estado= true;
        int resultado;
        SQLiteDatabase bd = this.getWritableDatabase();
        try {
            int codigo = datos.getCodigo();
            Cursor fila = bd.rawQuery("select codigo, descripcion, precio from articulos where codigo="+codigo,null);
            if (fila.moveToFirst()) {
                datos.setCodigo(Integer.parseInt(fila.getString(0)));
                datos.setDescripcion(fila.getString(1));
                datos.setPrecio(Double.parseDouble(fila.getString(2)));
                estado=true;
            }else {
                estado=false;
            }
            bd.close();
        }catch (Exception e){
            estado=false;
            Log.e("error",e.toString());
        }
        return estado;

    }



    public boolean consultaDescripcion(Dto datos){
        boolean estado = true;
        int resultado;
        SQLiteDatabase bd = this.getWritableDatabase();
        try {
            String descripcion = datos.getDescripcion();
            Cursor fila = bd.rawQuery("select codigo, descripcion, precio from articulos where descripcion ='" + descripcion + "'", null);
            if (fila.moveToFirst()){
                datos.setCodigo(Integer.parseInt(fila.getString(0)));
                datos.setDescripcion(fila.getString(1));
                datos.setPrecio(Double.parseDouble(fila.getString(2)));
                estado = true;
            }else {
                estado = false;
            }
            bd.close();
        }catch (Exception e){
            estado = false;
            Log.e("error.",e.toString());
        }
        return estado;

    }



    public boolean bajapocodigo(final Context context, final Dto datos){
        estadoDelete = true;
        try {
            int codigo = datos.getCodigo();
            Cursor fila = bd().rawQuery("select * from articulos where codigo=" + codigo, null);
                if (fila.moveToFirst()) {
                datos.setCodigo(Integer.parseInt(fila.getString(0)));
                datos.setDescripcion(fila.getString(1));
                datos.setPrecio(Double.parseDouble(fila.getString(2)));
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.ic_back);
                builder.setTitle("warning");
                builder.setMessage("¿ Esta seguro de borrar el registro? \nCodigo:" + datos.getCodigo()+"\nDescripcion:"+datos.getDescripcion());
                builder.setCancelable(false);
                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        int codigo = datos.getCodigo();
                        int cant = bd().delete("articulos", "codigo=" + codigo, null );

                        if(cant > 0){
                            estadoDelete = true;
                            Toast.makeText(context,"Registro eliminado satisfactoriamente.", Toast.LENGTH_SHORT).show();

                        }else {
                            estadoDelete = false;
                        }
                        bd().close();
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                  AlertDialog dialog = builder.create();
                  dialog.show();

                }else {
                Toast.makeText(context,"No hay resultados encontrados para la busqueda ", Toast.LENGTH_SHORT).show();
                }
                 }catch (Exception e){
                 estadoDelete = false;
               Log.e("Error",e.toString());
                }

              return estadoDelete;
               }




         public  boolean modificar(Dto datos){
           boolean estado = true;
           int resultados;
             SQLiteDatabase bd = this.getWritableDatabase();
           try{
            int codigo = datos.getCodigo();
            String desripcion = datos.getDescripcion();
            double precio = datos.getPrecio();

               ContentValues registro = new ContentValues();
               registro.put("codigo", codigo);
               registro.put("descripcion",desripcion);
               registro.put("precio", precio);

               int cant = (int) bd.update("articulos", registro,"codigo=" + codigo, null);

               bd.close();
               if (cant>0) estado= true;
               else estado= false;
           }catch (Exception e){
               estado = false;
               Log.e("error",e.toString());
           }
             return estado;
           }


           public ArrayList <Dto> consultarListaArticulos() {
               boolean estado = false;
               SQLiteDatabase bd = this.getWritableDatabase();

               Dto articulos = null;      //creamos la instancia vacia
               articulosLista = new ArrayList<Dto>();

               try {
                   Cursor fila = bd.rawQuery("select * from articulos", null);
                   while (fila.moveToNext()) {
                       articulos = new Dto();
                       articulos.setCodigo(fila.getInt(0));
                       articulos.setDescripcion(fila.getString(1));
                       articulos.setPrecio(fila.getDouble(2));

                       articulosLista.add(articulos);
                    Log.i("codigo", String.valueOf(articulos.getCodigo()));
                    Log.i("descripcion ", articulos.getDescripcion().toString());
                    Log.i("precio",String.valueOf(articulos.getPrecio()));
                   }
                   obtenerListaArticulos();
               }catch (Exception e){

               }
               return  articulosLista;

           }


             public ArrayList<String> obtenerListaArticulos(){
        listaArticulos = new ArrayList<>();
        listaArticulos.add("seleccione");

           for (int i=0;i<articulosLista.size();i++){
               listaArticulos.add(articulosLista.get(i).getCodigo()+"~"+articulosLista.get(i).getDescripcion());

           }
           return listaArticulos;

             }



        @Override
        public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table articulos(codigo integer not null primary key, descripcion text, precio real)");
    }

         @Override
         public void onUpgrade(SQLiteDatabase db, int i, int i1) {
         db.execSQL("drop table if exists articulos");
         onCreate(db);
    }
}

package com.example.sqlitesis21b;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnGuardar, btnConsultar1, btnConsultar2, btnEliminar, btnActualizar;
    private EditText et_codigo, et_description, et_precio;
    boolean estadoCodigo = false;
    boolean estadoDescripcion = false;
    boolean estadoPrecio = false;
    int estadoInsert=0;

    Modal ventanas = new Modal();
    AdminSQLiteOpenHelper conexion = new AdminSQLiteOpenHelper(this);
    Dto datos = new Dto();
    AlertDialog.Builder dialogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnConsultar1 = (Button) findViewById(R.id.btnConsultar1);
        btnConsultar2 = (Button) findViewById(R.id.btnConsultar2);
        btnEliminar = (Button) findViewById(R.id.btnEliminar);
        btnActualizar = (Button) findViewById(R.id.btnEditar);

        et_codigo = (EditText) findViewById(R.id.et_codigo);
        et_description = (EditText) findViewById(R.id.et_descripcion);
        et_precio = (EditText) findViewById(R.id.et_precio);

        String senal = "";
        String codigo = "";
        String descripcion = "";
        String precio = "";

        try{
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            if (bundle != null);
            codigo = bundle.getString("codigo");
            senal = bundle.getString("senal");
            descripcion = bundle.getString("descripcion");
            precio = bundle.getString("precio");
            if (senal.equals("1")){
                et_codigo.setText(codigo);
                et_description.setText(descripcion);
                et_precio.setText(precio);
            }
        }catch (Exception e){

        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                // .setAction("Action", null).show();
                ventanas.VentanaEmergente(MainActivity.this);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_limpiar) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void guardar(View view) {

        if (et_codigo.getText().toString().length() == 0) {
            estadoCodigo = false;
            et_codigo.setError("Campo obligatorio");
        } else {
            estadoCodigo = true;
        }
        if (et_description.getText().toString().length() == 0) {
            estadoDescripcion = false;
            et_description.setError("Campo obligatorio");
        } else {
            estadoDescripcion = true;
        }
        if (et_precio.getText().toString().length() == 0) {
            estadoPrecio = false;
            et_precio.setError("Campo obligatorio");
        } else {
            estadoPrecio = true;
        }
        if (estadoCodigo && estadoDescripcion && estadoPrecio) {
            //Toast.makeText(this, "Estamos bien", Toast.LENGTH_SHORT).show();

            try {

                datos.setCodigo(Integer.parseInt(et_codigo.getText().toString()));
                datos.setDescripcion(et_description.getText().toString());
                datos.setPrecio(Double.parseDouble(et_precio.getText().toString()));

                if (conexion.InsertTradicional(datos)) {
                    Toast.makeText(this, "Registro agregado satisfactoriamente", Toast.LENGTH_SHORT).show();
                    limpiarDatos();
                } else {
                    Toast.makeText(getApplicationContext(), "Error. ya existe un registro\n" + "codigo: " + et_codigo.getText().toString(), Toast.LENGTH_SHORT).show();
                    limpiarDatos();
                }
            }catch (Exception e) {
                Toast.makeText(this, "Error ya existe", Toast.LENGTH_SHORT).show();



            }
        }
    }
            public  void mensaje (String mensaje){
        Toast.makeText(this, ""+mensaje,Toast.LENGTH_SHORT).show();
            }

           public void limpiarDatos(){
                et_codigo.setText(null);
                et_description.setText(null);
                et_precio.setText(null);
                et_codigo.requestFocus();

    }



           public void consultarporcodigo(View view) {


        if (et_codigo.getText().toString().length() == 0) {
            et_codigo.setError("Campo obligatorio");

            estadoCodigo = false;
        } else {
            estadoCodigo = true;
        }
            if (estadoCodigo) {

                String codigo = et_codigo.getText().toString();
                datos.setCodigo(Integer.parseInt(codigo));

                if (conexion.consultaCodigo(datos)==true) {
                    et_description.setText(datos.getDescripcion());
                    et_precio.setText(""+datos.getPrecio());
                } else {
                    Toast.makeText(this, "No existe un artículo con dicho código", Toast.LENGTH_SHORT).show();
                    limpiarDatos();
                }
            } else {
                Toast.makeText(this, "Ingrese el codigo del articulo a buscar", Toast.LENGTH_SHORT).show();
            }
        }



    public void consultarpordescripcion(View view) {
        if (et_description.getText().toString().length() == 0) {
            et_description.setError("campo obligatorio");
            estadoDescripcion = false;
        } else {
            estadoDescripcion = true;

        }
            if (estadoDescripcion) {

                String descri = et_description.getText().toString();
               datos.setDescripcion(descri);


                if (conexion.consultaDescripcion(datos)) {
                    et_codigo.setText(""+datos.getCodigo());
                    et_description.setText(datos.getDescripcion());
                    et_precio.setText(""+datos.getPrecio());
                } else {
                    Toast.makeText(this, "No existe un artículo con dicha descripción", Toast.LENGTH_SHORT).show();
                  limpiarDatos();
                }

            } else {
                Toast.makeText(this, "Ingrese la descripcion del articulo a buscar", Toast.LENGTH_SHORT).show();
            }
        }



     public void bajaporcodigo(View v) {
        if (et_codigo.getText().toString().length()==0) {
            et_codigo.setError("Campo obligatorio");
            estadoCodigo = false;
        } else {
            estadoCodigo = true;

        }if (estadoCodigo) {
            String cod = et_codigo.getText().toString();
               datos.setCodigo(Integer.parseInt(cod));
                if (conexion.bajapocodigo(MainActivity.this,datos)){
                    //Toast.makeText(this, "se borro el articulo con dicho codigo", Toast.LENGTH_SHORT).show();
                    limpiarDatos();
            } else {
                Toast.makeText(this, "No existe un articulo con dicho codigo", Toast.LENGTH_SHORT).show();
                limpiarDatos();
            }
        }

    }


    public void modificacion(View v) {
        if (et_codigo.getText().toString().length() == 0) {
            et_codigo.setError("Campo obligatorio");
            estadoCodigo = false;
        } else {
            estadoCodigo = true;
        }
            if (estadoCodigo) {
                String cod = et_codigo.getText().toString();
                String descri = et_description.getText().toString();
                double pre = Double.parseDouble(et_precio.getText().toString());

                datos.setCodigo(Integer.parseInt(cod));
                datos.setDescripcion(descri);
                datos.setPrecio(pre);

                if (conexion.modificar(datos)){
                    Toast.makeText(this, "se modificaron los datos", Toast.LENGTH_SHORT).show();
                } else {
                Toast.makeText(this, "No existe un articulo con dicho codigo", Toast.LENGTH_SHORT).show();
            }
        }
    }
}





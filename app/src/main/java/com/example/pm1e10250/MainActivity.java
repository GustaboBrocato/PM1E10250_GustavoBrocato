package com.example.pm1e10250;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pm1e10250.Config.SQLiteConnection;
import com.example.pm1e10250.Config.Transacciones;
import com.example.pm1e10250.Models.Paises;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SQLiteConnection conexion;
    static final int peticion_acceso_camera = 101;
    static final int peticion_toma_fotografia = 102;
    String currentPhotoPath;
    EditText nombre, telefono, nota;
    Spinner paises;
    ImageButton imagenPerfil;
    Button guardar, contactos;
    String paisSeleccionado, codigoSeleccionado;
    Bitmap imageBitmap = null;
    byte[] imagenPerfilByteArray;
    ArrayList<Paises> listPais;
    ArrayList<String> arregloPaises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        nombre = (EditText) findViewById(R.id.txt_nombre);
        telefono = (EditText) findViewById(R.id.txt_telefono);
        nota = (EditText) findViewById(R.id.txt_nota);
        paises = (Spinner) findViewById(R.id.cmb_paises);
        imagenPerfil = (ImageButton) findViewById(R.id.img_perfil);
        guardar = (Button) findViewById(R.id.btn_Guardar);
        contactos = (Button) findViewById(R.id.btn_Contactos);

        getPaises();

        ArrayAdapter<CharSequence> adp = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arregloPaises);
        paises.setAdapter(adp);

        paises.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                paisSeleccionado = listPais.get(i).getPais();
                codigoSeleccionado = listPais.get(i).getCodigo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        imagenPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permisos();
            }
        });

        View.OnClickListener buttonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class<?> actividad = null;
                if (view.getId()==R.id.btn_Contactos) {
                    actividad = ActivityContactos.class;
                }
                if (actividad != null) {
                    moveActivity(actividad);
                }
            }
        };

        contactos.setOnClickListener(buttonClick);

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nombre.getText().toString().trim().isEmpty() || telefono.getText().toString().trim().isEmpty() || nota.getText().toString().trim().isEmpty()){
                    nombre.setError("Porfavor ingrese un nombre para el contacto, no se permiten campos vacios!!!");
                    telefono.setError("Porfavor ingrese un numero telefonico, no se permiten campos vacios!!!");
                    nota.setError("Porfavor ingrese una nota para el contacto, no se permiten campos vacios!!!");
                } else if (imageBitmap == null) {
                    Toast.makeText(getApplicationContext(), "Porfavor tome una foto al contacto!", Toast.LENGTH_LONG).show();
                }else if (paises.getSelectedItemPosition() == 0){
                    Toast.makeText(getApplicationContext(), "Porfavor seleccione un pais de la lista!", Toast.LENGTH_LONG).show();
                }else{
                    addContact();
                }
            }
        });
    }

    private void addContact() {
        try {
            SQLiteDatabase db = conexion.getWritableDatabase();

            ContentValues valores = new ContentValues();
            valores.put(Transacciones.nombres, nombre.getText().toString());
            valores.put(Transacciones.pais, paisSeleccionado);
            valores.put(Transacciones.codigo, codigoSeleccionado);
            valores.put(Transacciones.telefono, telefono.getText().toString());
            valores.put(Transacciones.nota, nota.getText().toString());
            valores.put(Transacciones.imagen, imagenPerfilByteArray);

            Long result = db.insert(Transacciones.tablaContactos, Transacciones.id, valores);

            Toast.makeText(this, getString(R.string.respuesta), Toast.LENGTH_SHORT).show();
            db.close();
            recreate();
            nombre.setText("");
            telefono.setText("");
            nota.setText("");
            paises.setSelection(0);
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.errorIngreso), Toast.LENGTH_SHORT).show();
        }
    }

    private void getPaises() {
        try {
            conexion = new SQLiteConnection(this, Transacciones.namedb, null, 1);
            SQLiteDatabase db = conexion.getReadableDatabase();
            Paises pais = null;
            listPais = new ArrayList<Paises>();

            Cursor cursor = db.rawQuery(Transacciones.SelectTablePais, null);
            while (cursor.moveToNext()) {
                pais = new Paises();
                pais.setId(cursor.getInt(0));
                pais.setPais(cursor.getString(1));
                pais.setCodigo(cursor.getString(2));

                listPais.add(pais);
            }
            cursor.close();
            fillCombo();
        } catch (Exception ex) {
            ex.toString();
        }
    }

    private void fillCombo() {
        arregloPaises = new ArrayList<String>();
        for (int i = 0; i < listPais.size(); i++) {
            arregloPaises.add(listPais.get(i).getId() + " - " +
                    listPais.get(i).getPais() + " - " +
                    listPais.get(i).getCodigo());
        }
    }

    private void moveActivity(Class<?> actividad) {
        Intent intent = new Intent(getApplicationContext(), actividad);
        startActivity(intent);
    }

    private void permisos() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, peticion_acceso_camera);
        } else {
            tomarFoto();
        }
    }

    private void tomarFoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, peticion_toma_fotografia);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == peticion_acceso_camera) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                tomarFoto();
            } else {
                Toast.makeText(getApplicationContext(), "Permiso Denegado!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == peticion_toma_fotografia && resultCode == RESULT_OK) {
            try {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                imagenPerfil.setImageBitmap(imageBitmap);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                imagenPerfilByteArray = stream.toByteArray();

            }catch (Exception ex){
                ex.toString();
            }
        }
    }
}
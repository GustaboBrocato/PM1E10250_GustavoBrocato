package com.example.pm1e10250;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pm1e10250.Config.SQLiteConnection;
import com.example.pm1e10250.Config.Transacciones;
import com.example.pm1e10250.Models.Paises;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ActivityEditar extends AppCompatActivity {

    SQLiteConnection conexion;
    static final int peticion_acceso_camera = 101;
    static final int peticion_toma_fotografia = 102;
    String currentPhotoPath;
    EditText nombre, telefono, nota;
    Spinner paises;
    ImageButton imagenPerfil, btn_addPais;
    Button guardar, contactos;
    String paisSeleccionado, codigoSeleccionado;
    Bitmap imageBitmap = null;
    byte[] imagenPerfilByteArray;
    ArrayList<Paises> listPais;
    ArrayList<String> arregloPaises;
    ArrayAdapter<CharSequence> adp;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        String nombreIntent = intent.getStringExtra("nombres");
        String paisIntent = intent.getStringExtra("pais");
        String telefonoIntent = intent.getStringExtra("telefono");
        String notaIntent = intent.getStringExtra("nota");
        byte[] imagenIntent = intent.getByteArrayExtra("imagen");

        conexion = new SQLiteConnection(this, Transacciones.namedb, null, 1);
        nombre = (EditText) findViewById(R.id.txt_actualizarNombre);
        telefono = (EditText) findViewById(R.id.txt_actualizarTelefono);
        nota = (EditText) findViewById(R.id.txt_actualizarNota);
        paises = (Spinner) findViewById(R.id.cmb_actualizarPaises);
        imagenPerfil = (ImageButton) findViewById(R.id.img_actualizarPerfil);
        btn_addPais = (ImageButton) findViewById(R.id.btn_addactualizarAddPais);
        guardar = (Button) findViewById(R.id.btn_actualizarContacto);

        getPaises();

        adp = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arregloPaises);
        paises.setAdapter(adp);

        nombre.setText(nombreIntent);
        telefono.setText(telefonoIntent);
        nota.setText(notaIntent);

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
    }

    private void getPaises() {
        try {
            SQLiteDatabase db = conexion.getReadableDatabase();
            Paises pais = null;
            listPais = new ArrayList<Paises>();
            db.rawQuery(Transacciones.SelectTablePais, null);

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

    public void showAddPaisDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Pais");

        View dialogView = getLayoutInflater().inflate(R.layout.add_pais_dialog, null);
        builder.setView(dialogView);

        final EditText paisEditText = dialogView.findViewById(R.id.editPais);
        final EditText areaCodeEditText = dialogView.findViewById(R.id.editAreaCode);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String pais = paisEditText.getText().toString();
                String areaCode = "("+areaCodeEditText.getText().toString()+")";

                addPais(pais, areaCode);
                updateSpinner();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.create().show();
    }

    private void addPais(String pais, String codigo) {
        try {
            SQLiteDatabase db = conexion.getWritableDatabase();

            ContentValues valores = new ContentValues();
            valores.put(Transacciones.pais, pais);
            valores.put(Transacciones.codigoArea, codigo);

            long result = db.insert(Transacciones.tablaPaises, null, valores);

            if (result != -1) {
                Log.d("DatabaseSuccess", "Inserted data with row ID: " + result);
                Toast.makeText(this, getString(R.string.respuesta), Toast.LENGTH_SHORT).show();
            } else {
                Log.e("DatabaseError", "Error inserting data");
                Toast.makeText(this, getString(R.string.errorIngreso), Toast.LENGTH_SHORT).show();
            }

            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DatabaseError", "Error inserting data: " + e.getMessage());
            Toast.makeText(this, getString(R.string.errorIngreso), Toast.LENGTH_SHORT).show();
        }
    }


    private void updateSpinner() {
        // Step 1: Retrieve the updated data from the database
        paises.setAdapter(null);
        getPaises();
        adp = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arregloPaises);
        paises.setAdapter(adp);
    }

    private int getPositionOfCountryName(List<String> countryList, String specificCountryName) {
        for (int i = 0; i < countryList.size(); i++) {
            if (countryList.get(i).equals(specificCountryName)) {
                return i;
            }
        }
        return -1; // Country name not found
    }
}
package com.example.pm1e10250.Config;

public class Transacciones {
    //Nombre de la base de datos
    public static final String namedb = "PM1E10250";

    //Tabla de Contactos
    public static final String tablaContactos = "contactos";

    //Campos de la tabla de Contactos
    public static final String id = "id";
    public static final String nombres = "nombres";
    public static final String pais = "pais";
    public static final String codigo = "codigo";
    public static final String telefono = "telefono";
    public static final String nota = "nota";
    public static final String imagen= "imagen";




    //Consultas de tabla contactos
    public static final String CreateTableContactos = "CREATE TABLE " + tablaContactos +"( id INTEGER PRIMARY KEY AUTOINCREMENT,"+ nombres + " TEXT," + pais + " TEXT," + codigo +" TEXT,"
            + telefono + " TEXT," + nota + " TEXT, "+ imagen + " BLOB )";
    public static final String DropTableContactos = "DROP TABLE IF EXISTS" + tablaContactos;
    public static final String SelectTablePersonas = "SELECT * FROM "+ Transacciones.tablaContactos;

    //Tabla de Paises
    public static final String tablaPaises = "paises";

    //Campos de la tabla de paises
    public static final String nombrePais = "pais";
    public static final String codigoArea = "codigo_Area";

    //Consultas de la tabla paises

    public static final String CreateTablePais = "CREATE TABLE " + tablaPaises +"( id INTEGER PRIMARY KEY AUTOINCREMENT,"+ nombrePais + " TEXT," + codigoArea + " TEXT)";
    public static final String DropTablePais = "DROP TABLE IF EXISTS" + tablaPaises;
    public static final String SelectTablePais = "SELECT * FROM "+ Transacciones.tablaPaises;
    public static final String InsertPaises = "INSERT INTO " + tablaPaises + "(pais, codigo_area) VALUES" +
            "    ('Seleccione un Campo', '(0)')," +
            "    ('Honduras', '(504)')," +
            "    ('Costa Rica', '(506)')," +
            "    ('Guatemala', '(502)')," +
            "    ('El Salvador', '(503)');";


}

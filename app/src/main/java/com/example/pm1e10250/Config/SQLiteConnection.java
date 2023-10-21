package com.example.pm1e10250.Config;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteConnection extends SQLiteOpenHelper
{
    public SQLiteConnection(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Transacciones.CreateTableContactos);
        sqLiteDatabase.execSQL(Transacciones.CreateTablePais);
        sqLiteDatabase.execSQL(Transacciones.InsertPaises);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(Transacciones.DropTableContactos);
        sqLiteDatabase.execSQL(Transacciones.DropTablePais);
        onCreate(sqLiteDatabase);
    }
}

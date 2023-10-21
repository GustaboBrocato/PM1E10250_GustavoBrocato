package com.example.pm1e10250.Models;

public class Contactos {
    private Integer id;
    private String nombre;
    private String pais;
    private String codigo;
    private String telefono;
    private String nota;
    private byte[] imagen;

    public Contactos(Integer id, String nombre, String pais, String codigo, String telefono, String nota, byte[] imagen) {
        this.id = id;
        this.nombre = nombre;
        this.pais = pais;
        this.codigo = codigo;
        this.telefono = telefono;
        this.nota = nota;
        this.imagen = imagen;
    }

    public Contactos() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }
}

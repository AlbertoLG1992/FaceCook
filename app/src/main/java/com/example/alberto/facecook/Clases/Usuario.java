package com.example.alberto.facecook.Clases;

import java.io.File;

public class Usuario {

    private FotoUsuario fotoUsuario;
    private String  nick,
                    pass,
                    nombre,
                    apellidos,
                    fechaNacimiento,
                    correoElectronico,
                    tlf,
                    comentarios;
    private double longitud, latitud;
    private String urlFoto;
    private String fechaAlta;

    /**
     * Constructor con foto de usuario
     *
     * @param fotoUsuario :FotoUsuario
     * @param nick :String
     * @param pass :String
     * @param nombre :String
     * @param apellidos :String
     * @param fechaNacimiento :String
     * @param correoElectronico :String
     * @param tlf :String
     * @param comentarios :String
     * @param longitud :double
     * @param latitud :double
     */
    public Usuario(FotoUsuario fotoUsuario, String nick, String pass, String nombre, String apellidos,
                   String fechaNacimiento, String correoElectronico, String tlf, String comentarios,
                   double longitud, double latitud) {
        this.fotoUsuario = fotoUsuario;
        this.nick = nick;
        this.pass = pass;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.correoElectronico = correoElectronico;
        this.tlf = tlf;
        this.comentarios = comentarios;
        this.longitud = longitud;
        this.latitud = latitud;
    }

    /**
     * Constructor con urlFoto
     *
     * @param urlFoto :String
     * @param nick :String
     * @param nombre :String
     * @param apellidos :String
     * @param fechaNacimiento :String
     * @param correoElectronico :String
     * @param tlf :String
     * @param comentarios :String
     * @param longitud :double
     * @param latitud :double
     */
    public Usuario(String urlFoto, String nick, String nombre, String apellidos,
                   String fechaNacimiento, String correoElectronico, String tlf,
                   String fechaAlta ,String comentarios,
                   double longitud, double latitud) {
        this.urlFoto = urlFoto;
        this.nick = nick;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.correoElectronico = correoElectronico;
        this.tlf = tlf;
        this.fechaAlta = fechaAlta;
        this.comentarios = comentarios;
        this.longitud = longitud;
        this.latitud = latitud;
    }

    public String getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(String fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public FotoUsuario getFotoUsuario() {
        return fotoUsuario;
    }

    public void setFotoUsuario(FotoUsuario fotoUsuario) {
        this.fotoUsuario = fotoUsuario;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getTlf() {
        return tlf;
    }

    public void setTlf(String tlf) {
        this.tlf = tlf;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }
}

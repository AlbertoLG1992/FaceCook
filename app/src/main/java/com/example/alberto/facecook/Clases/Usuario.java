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

    public Usuario(FotoUsuario fotoUsuario, String nick, String pass, String nombre, String apellidos,
                   String fechaNacimiento, String correoElectronico, String tlf, String comentarios) {
        this.fotoUsuario = fotoUsuario;
        this.nick = nick;
        this.pass = pass;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.correoElectronico = correoElectronico;
        this.tlf = tlf;
        this.comentarios = comentarios;
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
}

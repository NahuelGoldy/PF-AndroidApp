package com.example.puchoo.mapmaterial.Dto;

import com.example.puchoo.mapmaterial.Modelo.Usuario;

/**
 * Created by Puchoo on 15/10/2017.
 */

public class UsuarioDTO {
    String apellido;
    String email;
    int idUsuario;
    String nombre;
    String nroTelefono;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNroTelefono() {
        return nroTelefono;
    }

    public void setNroTelefono(String nroTelefono) {
        this.nroTelefono = nroTelefono;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Usuario toUsuario() {
        Usuario userResult = new Usuario();

        userResult.setEmail(this.email);
        userResult.setApellidoUsuario(this.apellido);
        userResult.setNombreUsuario(this.nombre);
        userResult.setTelefono(this.nroTelefono);

        return userResult;
    }
}

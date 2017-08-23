package com.example.puchoo.mapmaterial.Dto;


public class ResultadoRegistroDTO {

    private String msgResultado;
    private Integer idUsuarioCreado;
    private String nombreUsuario;
    private String apellidoUsuario;
    private String nroTelefono;
    private String email;
    private String nroPatente;
    private String modeloVehiculo;
    private String colorVehiculo;
    private String anioVehiculo;

    public ResultadoRegistroDTO() {
    }

    public String getMsgResultado() {
        return msgResultado;
    }

    public void setMsgResultado(String msgResultado) {
        this.msgResultado = msgResultado;
    }

    public Integer getIdUsuarioCreado() {
        return idUsuarioCreado;
    }

    public void setIdUsuarioCreado(Integer idUsuarioCreado) {
        this.idUsuarioCreado = idUsuarioCreado;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getApellidoUsuario() {
        return apellidoUsuario;
    }

    public void setApellidoUsuario(String apellidoUsuario) {
        this.apellidoUsuario = apellidoUsuario;
    }

    public String getNroTelefono() {
        return nroTelefono;
    }

    public void setNroTelefono(String nroTelefono) {
        this.nroTelefono = nroTelefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNroPatente() {
        return nroPatente;
    }

    public void setNroPatente(String nroPatente) {
        this.nroPatente = nroPatente;
    }

    public String getModeloVehiculo() {
        return modeloVehiculo;
    }

    public void setModeloVehiculo(String modeloVehiculo) {
        this.modeloVehiculo = modeloVehiculo;
    }

    public String getColorVehiculo() {
        return colorVehiculo;
    }

    public void setColorVehiculo(String colorVehiculo) {
        this.colorVehiculo = colorVehiculo;
    }

    public String getAnioVehiculo() {
        return anioVehiculo;
    }

    public void setAnioVehiculo(String anioVehiculo) {
        this.anioVehiculo = anioVehiculo;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.luisvaquin.bean;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;

/**
 *
 * @author luisb
 */
public class Servicio {

    private int codigoServicio;
    private LocalDate fechaServicio;
    private String tipoServicio;
    private Time horaServicio;
    private String lugarServicio;
    private String telefonoContacto;
    private int codigoEmpresa;

    public Servicio() {
    }

    public Servicio(int codigoServicio, LocalDate fechaServicio, String tipoServicio, Time horaServicio, String lugarServicio, String telefonoContacto, int codigoEmpresa) {
        this.codigoServicio = codigoServicio;
        this.fechaServicio = fechaServicio;
        this.tipoServicio = tipoServicio;
        this.horaServicio = horaServicio;
        this.lugarServicio = lugarServicio;
        this.telefonoContacto = telefonoContacto;
        this.codigoEmpresa = codigoEmpresa;
    }

    public int getCodigoServicio() {
        return codigoServicio;
    }

    public void setCodigoServicio(int codigoServicio) {
        this.codigoServicio = codigoServicio;
    }

    public LocalDate getFechaServicio() {
        return fechaServicio;
    }

    public void setFechaServicio(LocalDate fechaServicio) {
        this.fechaServicio = fechaServicio;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public Time getHoraServicio() {
        return horaServicio;
    }

    public void setHoraServicio(Time horaServicio) {
        this.horaServicio = horaServicio;
    }

    public String getLugarServicio() {
        return lugarServicio;
    }

    public void setLugarServicio(String lugarServicio) {
        this.lugarServicio = lugarServicio;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public int getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public void setCodigoEmpresa(int codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }

    @Override
    public String toString() {
        return codigoServicio + " | " + tipoServicio;
    }

}

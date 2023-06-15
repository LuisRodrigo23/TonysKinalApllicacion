/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.luisvaquin.bean;

import java.sql.Time;
import java.time.LocalDate;

public class Servicios_has_empleado {

    private int Servicios_codigoServicio;
    private int codigoServicio;
    private int codigoEmpleado;
    private LocalDate fechaEvento;
    private Time horaEvento;
    private String lugarEvento;

    public Servicios_has_empleado() {
    }

    public Servicios_has_empleado(int Servicios_codigoServicio, int codigoServicio, int codigoEmpleado, LocalDate fechaEvento, Time horaEvento, String lugarEvento) {
        this.Servicios_codigoServicio = Servicios_codigoServicio;
        this.codigoServicio = codigoServicio;
        this.codigoEmpleado = codigoEmpleado;
        this.fechaEvento = fechaEvento;
        this.horaEvento = horaEvento;
        this.lugarEvento = lugarEvento;
    }

    public int getServicios_codigoServicio() {
        return Servicios_codigoServicio;
    }

    public void setServicios_codigoServicio(int Servicios_codigoServicio) {
        this.Servicios_codigoServicio = Servicios_codigoServicio;
    }

    public int getCodigoServicio() {
        return codigoServicio;
    }

    public void setCodigoServicio(int codigoServicio) {
        this.codigoServicio = codigoServicio;
    }

    public int getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(int codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    public LocalDate getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(LocalDate fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    public Time getHoraEvento() {
        return horaEvento;
    }

    public void setHoraEvento(Time horaEvento) {
        this.horaEvento = horaEvento;
    }

    public String getLugarEvento() {
        return lugarEvento;
    }

    public void setLugarEvento(String lugarEvento) {
        this.lugarEvento = lugarEvento;
    }

}

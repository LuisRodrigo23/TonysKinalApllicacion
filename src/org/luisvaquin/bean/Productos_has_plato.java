/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.luisvaquin.bean;

/**
 *
 * @author luisb
 */
public class Productos_has_plato {

    private int Productos_codigoProducto;
    private int codigoPlato;
    private int codigoProducto;

    public Productos_has_plato() {
    }

    public Productos_has_plato(int Productos_codigoProducto, int codigoPlato, int codigoProducto) {
        this.Productos_codigoProducto = Productos_codigoProducto;
        this.codigoPlato = codigoPlato;
        this.codigoProducto = codigoProducto;
    }

    public int getProductos_codigoProducto() {
        return Productos_codigoProducto;
    }

    public void setProductos_codigoProducto(int Productos_codigoProducto) {
        this.Productos_codigoProducto = Productos_codigoProducto;
    }

    public int getCodigoPlato() {
        return codigoPlato;
    }

    public void setCodigoPlato(int codigoPlato) {
        this.codigoPlato = codigoPlato;
    }

    public int getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(int codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

}

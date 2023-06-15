package org.luisvaquin.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javax.swing.JOptionPane;
import org.luisvaquin.bean.Empresa;
import org.luisvaquin.main.Principal;
import org.luisvaquin.report.GenerarReporte;

public class MenuPrincipalController implements Initializable {

    private Principal escenarioPrincipal;

    @FXML
    private Button btnSalir;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void ventanaProgramador() {
        escenarioPrincipal.ventanaProgramador();
    }

    public void ventanaEmpresa() {
        escenarioPrincipal.ventanaEmpresa();
    }

    public void ventanaTipoEmpleado() {
        escenarioPrincipal.ventanaTipoEmpleado();
    }

    public void ventanaProducto() {
        escenarioPrincipal.ventanaProducto();
    }

    public void ventanaEmpleado() {
        escenarioPrincipal.ventanaEmpleado();
    }

    public void ventanaPresupuesto() {
        escenarioPrincipal.ventanaPresupuesto();
    }

    public void login() {
        escenarioPrincipal.login();
    }

    public void ventanaUsuario() {
        escenarioPrincipal.ventanaUsuario();
    }

    public void ventanaTipoPlato() {
        escenarioPrincipal.ventanaTipoPlato();
    }

    public void ventanaPlato() {
        escenarioPrincipal.ventanaPlato();
    }

    public void ventanaServicio() {
        escenarioPrincipal.ventanaServicio();
    }

    public void ventanaServicios_has_Empleados() {
        escenarioPrincipal.ventanaServicios_has_Empleados();
    }

    public void ventanaProductos_has_Platos() {
        escenarioPrincipal.ventanaProductos_has_Platos();
    }

    @FXML
    private void salir(ActionEvent evento) {
        int salir = JOptionPane.showOptionDialog(null, "¿Seguro que desea salir? \n"
                + "Se quitara su usuario previamente ", "Tonys Kinal", 0, 0, null, null, this);
        if (salir == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(null, "Gracias por usar mi programa :)");
            System.exit(0);
        } else if (salir == JOptionPane.NO_OPTION) {
            JOptionPane.showMessageDialog(null, "Sigue en el programa");
        }
    }

}

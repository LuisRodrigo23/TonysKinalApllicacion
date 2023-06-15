/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.luisvaquin.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import org.luisvaquin.bean.Login;
import org.luisvaquin.bean.Usuario;
import org.luisvaquin.db.Conexion;
import org.luisvaquin.main.Principal;

/**
 *
 * @author informatica
 */
public class LoginController implements Initializable {

    private Principal escenarioPrincipal;
    private ObservableList<Usuario> listaUsuario;
    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtPassword;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public ObservableList<Usuario> getUsuario() {
        ArrayList<Usuario> lista = new ArrayList<Usuario>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarUsuarios()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Usuario(resultado.getInt("codigoUsuario"),
                        resultado.getString("nombreUsuario"),
                        resultado.getString("apellidoUsuario"),
                        resultado.getString("usuarioLogin"),
                        resultado.getString("contrasena")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaUsuario = FXCollections.observableArrayList(lista);
    }

    //Corregir metoodo
    @FXML
    private void login() {
        Login login = new Login();
        int x = 0;
        boolean bandera = false;
        login.setUsuarioMaster(txtUsuario.getText());
        login.setPasswordLogin(txtPassword.getText());

        while (x < getUsuario().size()) {
            String user = getUsuario().get(x).getUsuarioLogin();
            String pass = getUsuario().get(x).getContrasena();
            if (user.equals(login.getUsuarioMaster()) && pass.equals(login.getPasswordLogin())) {
                JOptionPane.showMessageDialog(null, "Sesion iniciada\n"
                        + getUsuario().get(x).getNombreUsuario() + " "
                        + getUsuario().get(x).getApellidoUsuario() + "\n" + "Bienvenido");
                escenarioPrincipal.menuPrincipal();
                x = getUsuario().size();
                bandera = true;
            }

            x++;
        }

        if (bandera == false) {
            JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrecta");
        }

    }

    public LoginController() {

    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void ventanaUsuario() {
        escenarioPrincipal.ventanaUsuario();
    }

//    public LoginController(Principal escenarioPrincipal, ObservableList<Usuario> listaUsuario, TextField txtUsuario, PasswordField txtPassword) {
//        this.escenarioPrincipal = escenarioPrincipal;
//        this.listaUsuario = listaUsuario;
//        this.txtUsuario = txtUsuario;
//        this.txtPassword = txtPassword;
//    }
}

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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.luisvaquin.bean.Plato;
import org.luisvaquin.bean.Producto;
import org.luisvaquin.bean.Productos_has_plato;
import org.luisvaquin.db.Conexion;
import org.luisvaquin.main.Principal;

/**
 *
 * @author luisb
 */
public class Productos_has_PlatosController implements Initializable {

    private Principal escenarioPrincipal;

    private enum operaciones {
        GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO
    }
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Plato> listaPlatos;
    private ObservableList<Producto> listaProductos;
    private ObservableList<Productos_has_plato> listaProductos_has_platoController;

    @FXML
    private TableView tblProductos_has_platos;
    @FXML
    private TextField txtProductosCodigoProducto;
    @FXML
    private TableColumn colProductosCodigoProducto;
    @FXML
    private TableColumn colCodigoPlato;
    @FXML
    private TableColumn colCodigoProducto;
    @FXML
    private ComboBox cmbCodigoProducto;
    @FXML
    private ComboBox cmbCodigoPlato;
    @FXML
    private Button btnNuevo;

    @FXML
    private ImageView imgNuevo;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgReporte;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoPlato.setItems(getPlato());
        cmbCodigoProducto.setItems(getProducto());
        //desactivarControles();
    }

    public void cargarDatos() {
        tblProductos_has_platos.setItems(getProductos_has_plato());
        colProductosCodigoProducto.setCellValueFactory(new PropertyValueFactory<Productos_has_plato, Integer>("Productos_codigoProducto"));
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<Productos_has_plato, Integer>("codigoPlato"));
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<Productos_has_plato, Integer>("codigoProducto"));
    }

    public ObservableList<Productos_has_plato> getProductos_has_plato() {
        ArrayList<Productos_has_plato> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarProductos_has_Platos();");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Productos_has_plato(
                        resultado.getInt("Productos_codigoProducto"),
                        resultado.getInt("codigoPlato"),
                        resultado.getInt("codigoProducto")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProductos_has_platoController = FXCollections.observableArrayList(lista);
    }

    public ObservableList<Plato> getPlato() {
        ArrayList<Plato> lista = new ArrayList<Plato>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPlatos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Plato(resultado.getInt("codigoPlato"),
                        resultado.getInt("cantidad"),
                        resultado.getString("nombrePlato"),
                        resultado.getString("descripcionPlato"),
                        resultado.getInt("precioPlato"),
                        resultado.getInt("codigoTipoPlato")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaPlatos = FXCollections.observableArrayList(lista);
    }

    public Plato buscarPlato(int codigoPlato) {
        Plato resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarPlato(?)}");
            procedimiento.setInt(1, codigoPlato);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Plato(registro.getInt("codigoPlato"),
                        registro.getInt("cantidad"),
                        registro.getString("nombrePlato"),
                        registro.getString("descripcion"),
                        registro.getInt("precioPlato"),
                        registro.getInt("codigoTipoPlato"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public ObservableList<Producto> getProducto() {
        ArrayList<Producto> lista = new ArrayList<Producto>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarProductos()");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Producto(resultado.getInt("codigoProducto"),
                        resultado.getString("nombreProducto"),
                        resultado.getInt("cantidad")));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProductos = FXCollections.observableArrayList(lista);
    }

    public Producto buscarProducto(int codigoProducto) {
        Producto resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarProducto(?)");
            procedimiento.setInt(1, codigoProducto);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Producto(registro.getInt("codigoProducto"),
                        registro.getString("nombreProducto"),
                        registro.getInt("cantidad"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public void seleccionarElemento() {
        if (tblProductos_has_platos.getSelectionModel().getSelectedItem() != null) {
            txtProductosCodigoProducto.setText(String.valueOf(((Productos_has_plato) tblProductos_has_platos.getSelectionModel().getSelectedItem()).getProductos_codigoProducto()));
            cmbCodigoPlato.getSelectionModel().select(buscarPlato(((Productos_has_plato) tblProductos_has_platos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
            cmbCodigoProducto.getSelectionModel().select(buscarProducto(((Productos_has_plato) tblProductos_has_platos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
        }
    }

    public void nuevo() {
        switch (tipoDeOperaciones) {
            case NINGUNO:
                dseleccionar();
                activarControles();
                btnNuevo.setText("Guardar");
                imgNuevo.setImage(new Image("/org/luisvaquin/image/IconGuardar.png"));
                imgEliminar.setImage(new Image("/org/luisvaquin/image/IconCancelar.png"));
                tipoDeOperaciones = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                imgNuevo.setImage(new Image("/org/luisvaquin/image/IconGuardar.png"));
                imgEliminar.setImage(new Image("/org/luisvaquin/image/IconCancelar.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                limpiarControles();
                break;
        }
    }

    public void guardar() {
        Productos_has_plato registro = new Productos_has_plato();
        registro.setProductos_codigoProducto(Integer.parseInt(txtProductosCodigoProducto.getText()));
        registro.setCodigoPlato(((Plato) cmbCodigoPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());
        registro.setCodigoProducto(((Producto) cmbCodigoProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarProducto_has_Plato(?,?,?)}");
            procedimiento.setInt(1, registro.getProductos_codigoProducto());
            procedimiento.setInt(2, registro.getCodigoPlato());
            procedimiento.setInt(3, registro.getCodigoProducto());
            procedimiento.execute();
            listaProductos_has_platoController.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dseleccionar() {
        tblProductos_has_platos.getSelectionModel().clearSelection();
        limpiarControles();
    }

    public void desactivarControles() {
        txtProductosCodigoProducto.setEditable(false);
        cmbCodigoPlato.setDisable(true);
        cmbCodigoProducto.setDisable(true);
    }

    public void activarControles() {
        txtProductosCodigoProducto.setEditable(true);
        cmbCodigoPlato.setDisable(false);
        cmbCodigoProducto.setDisable(false);
    }

    public void limpiarControles() {
        txtProductosCodigoProducto.clear();
        cmbCodigoPlato.setValue(null);
        cmbCodigoProducto.setValue(null);
    }

    //Agregar metodo nuevo
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }
}

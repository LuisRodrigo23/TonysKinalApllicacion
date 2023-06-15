package org.luisvaquin.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.luisvaquin.bean.TipoPlato;
import org.luisvaquin.db.Conexion;
import org.luisvaquin.main.Principal;

public class TipoPlatoController implements Initializable {

    private Principal escenarioPrincipal;
    private ObservableList<TipoPlato> listaTipoPlato;

    private enum operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
    };

    private operaciones tipoDeOperacion = operaciones.NINGUNO;

    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnReporte;
    @FXML
    private TextField txtCodigoTipoPlato;
    @FXML
    private TextField txtDescripcionTipoPlato;
    @FXML
    private TableView tblTipoPlatos;
    @FXML
    private TableColumn colCodigoTipoPlato;
    @FXML
    private TableColumn colDescripcionTipoPlato;
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
    }

    public void cargarDatos() {
        tblTipoPlatos.setItems(getTipoPlato());
        colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory("codigoTipoPlato"));
        colDescripcionTipoPlato.setCellValueFactory(new PropertyValueFactory("descripcionTipo"));
    }

    public ObservableList<TipoPlato> getTipoPlato() {
        ArrayList<TipoPlato> lista = new ArrayList<TipoPlato>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarTipoPlato");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new TipoPlato(resultado.getInt("codigoTipoPlato"),
                        resultado.getString("descripcionTipo")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTipoPlato = FXCollections.observableArrayList(lista);
    }

    public void seleccionarElemento() {
        if (tblTipoPlatos.getSelectionModel().getSelectedItem() != null) {
            txtCodigoTipoPlato.setText(String.valueOf(((TipoPlato) tblTipoPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
            txtDescripcionTipoPlato.setText(((TipoPlato) tblTipoPlatos.getSelectionModel().getSelectedItem()).getDescripcionTipo());
        } else {
            JOptionPane.showMessageDialog(null, "Selecciona un campo que contenga registros");
        }
    }

    public void nuevo() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                dseleccionar();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/luisvaquin/image/IconGuardar.png"));
                imgEliminar.setImage(new Image("/org/luisvaquin/image/IconCancelar.png"));
                tipoDeOperacion = TipoPlatoController.operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/luisvaquin/image/IconGuardar.png"));
                imgEliminar.setImage(new Image("/org/luisvaquin/image/IconCancelar.png"));
                tipoDeOperacion = TipoPlatoController.operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }

    public void guardar() {
        if (txtDescripcionTipoPlato.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Todos los campos deben ser llenados");
        } else {
            TipoPlato registro = new TipoPlato();
            registro.setDescripcionTipo(txtDescripcionTipoPlato.getText());
            try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarTipoPlato(?)");
                procedimiento.setString(1, registro.getDescripcionTipo());
                procedimiento.executeQuery();
                listaTipoPlato.add(registro);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void eliminar() {
        switch (tipoDeOperacion) {
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image(""));
                imgEliminar.setImage(new Image(""));
                tipoDeOperacion = TipoPlatoController.operaciones.NINGUNO;
                break;
            default:
                if (tblTipoPlatos.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Está seguro de eliminar el registro?", "Eliminar Producto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarTipoPlato(?)");
                            procedimiento.setInt(1, ((TipoPlato) tblTipoPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
                            procedimiento.execute();
                            listaTipoPlato.remove(tblTipoPlatos.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, "No se puede eliminar, esta relacionado con otro dato");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (respuesta == JOptionPane.NO_OPTION) {
                        desactivarControles();
                        dseleccionar();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento registrado) ");
                }
        }
    }

    //Revisar Rutas externad de metodo editar
    //Rutas para imagene de este mismo metodo
    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblTipoPlatos.getSelectionModel().getSelectedItem() != null) {
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/luisvaquin/image/IconActualizar.png"));
                    imgReporte.setImage(new Image("/org/luisvaquin/image/IconCancelar.png"));
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento;) ");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                dseleccionar();
                desactivarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                imgEditar.setImage(new Image("/org/luisvaquin/image/IconActualizar.png"));
                imgReporte.setImage(new Image("/org/luisvaquin/image/IconCancelar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }

    //Revisar procedimiento almacenado de metodo actualizar
    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ActualizarTipoPlato(?, ?)");
            TipoPlato registro = (TipoPlato) tblTipoPlatos.getSelectionModel().getSelectedItem();
            registro.setDescripcionTipo(txtDescripcionTipoPlato.getText());
            procedimiento.setInt(1, registro.getCodigoTipoPlato());
            procedimiento.setString(2, registro.getDescripcionTipo());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Revisar Rutas externas de metod reporte
    public void reporte() {
        switch (tipoDeOperacion) {
            case ACTUALIZAR:
                limpiarControles();
                desactivarControles();
                dseleccionar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/luisvaquin/image/IconActualizar.png"));
                imgReporte.setImage(new Image("/org/luisvaquin/image/IconCancelar.png"));
                tipoDeOperacion = TipoPlatoController.operaciones.NINGUNO;
                tblTipoPlatos.getSelectionModel().clearSelection();
                break;
        }
    }

    public void dseleccionar() {
        tblTipoPlatos.getSelectionModel().clearSelection();
        limpiarControles();
    }

    public void desactivarControles() {
        txtCodigoTipoPlato.setEditable(false);
        txtDescripcionTipoPlato.setEditable(false);
    }

    public void activarControles() {
        txtCodigoTipoPlato.setEditable(false);
        txtDescripcionTipoPlato.setEditable(true);
    }

    public void limpiarControles() {
        txtCodigoTipoPlato.clear();
        txtDescripcionTipoPlato.clear();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    //AgregarMetoRegresarMenuPrincipal
    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }

}

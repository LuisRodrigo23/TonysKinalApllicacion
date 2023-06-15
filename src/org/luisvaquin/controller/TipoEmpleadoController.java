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
import org.luisvaquin.bean.TipoEmpleado;
import org.luisvaquin.db.Conexion;
import org.luisvaquin.main.Principal;

public class TipoEmpleadoController implements Initializable {

    private enum operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
    }

    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<TipoEmpleado> listaTipoEmpleado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }

    @FXML
    private Button btnRegresar;
    @FXML
    private TextField txtCodigoTipoEmpleado;
    @FXML
    private TextField txtDescripcionTipoEmpleado;
    @FXML
    private TableView tblTipoEmpleados;
    @FXML
    private TableColumn colCodigoTipoEmpleado;
    @FXML
    private TableColumn colDescripcionTipoEmpleado;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnReporte;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgReporte;

    public void cargarDatos() {

        tblTipoEmpleados.setItems(getTipoEmpleado());
        colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory("codigoTipoEmpleado"));
        colDescripcionTipoEmpleado.setCellValueFactory(new PropertyValueFactory("descripcion"));
    }

    public ObservableList<TipoEmpleado> getTipoEmpleado() {
        ArrayList<TipoEmpleado> lista = new ArrayList<TipoEmpleado>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarTipoEmpleado");
            ResultSet resultado = procedimiento.executeQuery();

            while (resultado.next()) {
                lista.add(new TipoEmpleado(resultado.getInt("codigoTipoEmpleado"),
                        resultado.getString("descripcion")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTipoEmpleado = FXCollections.observableArrayList(lista);
    }

    public void seleccionarElemento() {
        if (tblTipoEmpleados.getSelectionModel().getSelectedItem() != null) {
            txtCodigoTipoEmpleado.setText(String.valueOf(((TipoEmpleado) tblTipoEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
            txtDescripcionTipoEmpleado.setText(((TipoEmpleado) tblTipoEmpleados.getSelectionModel().getSelectedItem()).getDescripcion());
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un campo con registros");
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
                tipoDeOperacion = TipoEmpleadoController.operaciones.GUARDAR;
                break;

            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/luisvaquin/image/Logo Nuevo.png"));
                imgEliminar.setImage(new Image("/org/luisvaquin/image/Logo Eliminar.png"));
                tipoDeOperacion = TipoEmpleadoController.operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }

    public void guardar() {
        if (txtDescripcionTipoEmpleado.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Todos los campos deben ser llenados");
        } else {
            TipoEmpleado registro = new TipoEmpleado();
            registro.setDescripcion(txtDescripcionTipoEmpleado.getText());
            try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarTipoEmpleado(?)");
                procedimiento.setString(1, registro.getDescripcion());
                procedimiento.executeQuery();
                listaTipoEmpleado.add(registro);
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
                imgNuevo.setImage(new Image("/org/luisvaquin/image/Logo Nuevo.png"));
                imgEliminar.setImage(new Image("/org/luisvaquin/image/Logo Eliminar.png"));
                tipoDeOperacion = TipoEmpleadoController.operaciones.NINGUNO;
                break;
            default:
                if (tblTipoEmpleados.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Está seguro de eliminar el registro?", "Eliminar Producto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarTipoEmpleado(?)");
                            procedimiento.setInt(1, ((TipoEmpleado) tblTipoEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
                            procedimiento.execute();
                            listaTipoEmpleado.remove(tblTipoEmpleados.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, "No se puede eliminar porque esta relacionado con otro dato");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (respuesta == JOptionPane.NO_OPTION) {
                        desactivarControles();
                        dseleccionar();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento;) ");
                }
        }
    }

    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblTipoEmpleados.getSelectionModel().getSelectedItem() != null) {
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/luisvaquin/image/IconActualizar.png"));
                    imgReporte.setImage(new Image("/org/luisvaquin/image/IconCancelar.png"));
                    activarControles();
                    tipoDeOperacion = TipoEmpleadoController.operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                imgEditar.setImage(new Image("/org/luisvaquin/image/Logo Editar.png"));
                imgReporte.setImage(new Image("/org/luisvaquin/image/Logo Reporte.png"));
                tipoDeOperacion = TipoEmpleadoController.operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }

    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ActualizarTipoEmpleado(?, ?)");
            TipoEmpleado registro = (TipoEmpleado) tblTipoEmpleados.getSelectionModel().getSelectedItem();
            registro.setDescripcion(txtDescripcionTipoEmpleado.getText());
            procedimiento.setInt(1, registro.getCodigoTipoEmpleado());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
                imgEditar.setImage(new Image("/org/luisvaquin/image/Logo Editar.png"));
                imgReporte.setImage(new Image("/org/luisvaquin/image/Logo Reporte.png"));
                tipoDeOperacion = TipoEmpleadoController.operaciones.NINGUNO;
                tblTipoEmpleados.getSelectionModel().clearSelection();
                break;
        }
    }

    public void dseleccionar() {
        tblTipoEmpleados.getSelectionModel().clearSelection();
        limpiarControles();
    }

    public void activarControles() {
        txtCodigoTipoEmpleado.setEditable(false);
        txtDescripcionTipoEmpleado.setEditable(true);
    }

    public void desactivarControles() {
        txtCodigoTipoEmpleado.setEditable(false);
        txtDescripcionTipoEmpleado.setEditable(false);
    }

    public void limpiarControles() {
        txtCodigoTipoEmpleado.clear();
        txtDescripcionTipoEmpleado.clear();
    }

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

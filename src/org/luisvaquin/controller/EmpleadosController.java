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
import javax.swing.JOptionPane;
import org.luisvaquin.bean.Empleado;
import org.luisvaquin.bean.TipoEmpleado;
import org.luisvaquin.db.Conexion;
import org.luisvaquin.main.Principal;

public class EmpleadosController implements Initializable {

    private enum operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;

    //Arreglo para la clase de modelado
    private ObservableList<Empleado> listaEmpleado;
    private ObservableList<TipoEmpleado> listaTipoEmpleado;

    @FXML
    private TableView tblEmpleados;
    @FXML
    private TableColumn colCodEmpleado;
    @FXML
    private TableColumn colNumeroEmpleado;
    @FXML
    private TableColumn colApellidoEmpleado;
    @FXML
    private TableColumn colNombreEmpleado;
    @FXML
    private TableColumn colDireccionEmpleado;
    @FXML
    private TableColumn colTelefonoContacto;
    @FXML
    private TableColumn colGradoCocinero;
    @FXML
    private TableColumn colCodigoTipoEmpleado;
    @FXML
    private TextField txtCodEmpleado;
    @FXML
    private TextField txtApellidoEmpleado;
    @FXML
    private TextField txtDireccionEmpleado;
    @FXML
    private TextField txtGradoCocinero;
    @FXML
    private TextField txtNumeroEmpleado;
    @FXML
    private TextField txtNombreEmpleado;
    @FXML
    private TextField txtTelefonoContacto;
    @FXML
    private ComboBox cmbCodTipoEmpleado;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnEditar;
    ;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        desactivarControles();
        cmbCodTipoEmpleado.setItems(getTipoEmpleado());

    }

    public void cargarDatos() {
        tblEmpleados.setItems(getEmpleado());
        colCodEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("codigoEmpleado"));
        colNumeroEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("numeroEmpleado"));
        colApellidoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("apellidosEmpleado"));
        colNombreEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombresEmpleado"));
        colDireccionEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("direccionEmpleado"));
        colTelefonoContacto.setCellValueFactory(new PropertyValueFactory<Empleado, String>("telefonoContacto"));
        colGradoCocinero.setCellValueFactory(new PropertyValueFactory<Empleado, String>("gradoCocinero"));
        colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("codigoTipoEmpleado"));
    }

    public ObservableList<Empleado> getEmpleado() {
        ArrayList<Empleado> lista = new ArrayList<Empleado>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpleado()");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Empleado(resultado.getInt("codigoEmpleado"),
                        resultado.getInt("numeroEmpleado"),
                        resultado.getString("apellidosEmpleado"),
                        resultado.getString("nombresEmpleado"),
                        resultado.getString("direccionEmpleado"),
                        resultado.getString("telefonoContacto"),
                        resultado.getString("gradoCocinero"),
                        resultado.getInt("codigoTipoEmpleado")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaEmpleado = FXCollections.observableArrayList(lista);
    }

    public void seleccionarElemento() {
        if (tblEmpleados.getSelectionModel().getSelectedItem() != null) {
            txtCodEmpleado.setText(String.valueOf(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
            txtNumeroEmpleado.setText(String.valueOf(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getNumeroEmpleado()));
            txtApellidoEmpleado.setText(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getApellidosEmpleado());
            txtNombreEmpleado.setText(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getNombresEmpleado());
            txtDireccionEmpleado.setText(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getDireccionEmpleado());
            txtTelefonoContacto.setText(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getTelefonoContacto());
            txtGradoCocinero.setText(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getGradoCocinero());
            cmbCodTipoEmpleado.getSelectionModel().select(buscarTipoEmpleado(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
        }
    }

    public TipoEmpleado buscarTipoEmpleado(int codigoTipoEmpleado) {
        TipoEmpleado registro = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarTipoEmpleado(?)");
            procedimiento.setInt(1, codigoTipoEmpleado);
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                registro = new TipoEmpleado(resultado.getInt("codigoTipoEmpleado"),
                        resultado.getString("descripcion"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return registro;
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
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/luisvaquin/image/IconGuardar.png"));
                imgEliminar.setImage(new Image("/org/luisvaquin/image/IconCancelar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                limpiarControles();
                break;
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
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if (tblEmpleados.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Está seguro de eliminar el registro?", "Eliminar Empresa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarEmpleado(?)");
                            procedimiento.setInt(1, ((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
                            procedimiento.execute();
                            listaEmpleado.remove(tblEmpleados.getSelectionModel().getFocusedIndex());
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (respuesta == JOptionPane.NO_OPTION) {
                        desactivarControles();
                        dseleccionar();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemnto");
                }
        }
    }

    public void guardar() {
        Empleado registro = new Empleado();
        registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
        registro.setNombresEmpleado(txtNombreEmpleado.getText());
        registro.setApellidosEmpleado(txtApellidoEmpleado.getText());
        registro.setDireccionEmpleado(txtDireccionEmpleado.getText());
        registro.setTelefonoContacto(txtTelefonoContacto.getText());
        registro.setGradoCocinero(txtGradoCocinero.getText());
        registro.setCodigoTipoEmpleado(((TipoEmpleado) cmbCodTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarEmpleado(?,?,?,?,?,?,?)");
            procedimiento.setInt(1, registro.getNumeroEmpleado());
            procedimiento.setString(2, registro.getApellidosEmpleado());
            procedimiento.setString(3, registro.getNombresEmpleado());
            procedimiento.setString(4, registro.getDireccionEmpleado());
            procedimiento.setString(5, registro.getTelefonoContacto());
            procedimiento.setString(6, registro.getGradoCocinero());
            procedimiento.setInt(7, registro.getCodigoTipoEmpleado());
            procedimiento.execute();
            listaEmpleado.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblEmpleados.getSelectionModel().getSelectedItem() != null) {
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
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                imgEditar.setImage(new Image("/org/luisvaquin/image/Logo Editar.png"));
                imgReporte.setImage(new Image("/org/luisvaquin/image/Logo Reporte.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                desactivarControles();
                dseleccionar();
                break;
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
                imgEditar.setImage(new Image("/org/luisvaquin/image/IconActualizar.png"));
                imgReporte.setImage(new Image("/org/luisvaquin/image/IconCancelar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                tblEmpleados.getSelectionModel().clearSelection();
                break;
        }
    }

    public void actualizar() {
        Empleado registro = (Empleado) tblEmpleados.getSelectionModel().getSelectedItem();
        registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
        registro.setApellidosEmpleado(txtApellidoEmpleado.getText());
        registro.setNombresEmpleado(txtNombreEmpleado.getText());
        registro.setDireccionEmpleado(txtDireccionEmpleado.getText());
        registro.setTelefonoContacto(txtTelefonoContacto.getText());
        registro.setGradoCocinero(txtGradoCocinero.getText());
        registro.setCodigoTipoEmpleado(((TipoEmpleado) cmbCodTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ActualizarEmpleado(?,?,?,?,?,?,?,?)");
            procedimiento.setInt(1, registro.getCodigoEmpleado());
            procedimiento.setInt(2, registro.getNumeroEmpleado());
            procedimiento.setString(3, registro.getApellidosEmpleado());
            procedimiento.setString(4, registro.getNombresEmpleado());
            procedimiento.setString(5, registro.getDireccionEmpleado());
            procedimiento.setString(6, registro.getTelefonoContacto());
            procedimiento.setString(7, registro.getGradoCocinero());
            procedimiento.setInt(8, registro.getCodigoTipoEmpleado());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void desactivarControles() {
        txtCodEmpleado.setEditable(false);
        txtNumeroEmpleado.setEditable(false);
        txtApellidoEmpleado.setEditable(false);
        txtNombreEmpleado.setEditable(false);
        txtDireccionEmpleado.setEditable(false);
        txtTelefonoContacto.setEditable(false);
        txtGradoCocinero.setEditable(false);
        cmbCodTipoEmpleado.setDisable(true);
    }

    public void activarControles() {
        txtCodEmpleado.setEditable(false);
        txtNumeroEmpleado.setEditable(true);
        txtApellidoEmpleado.setEditable(true);
        txtNombreEmpleado.setEditable(true);
        txtDireccionEmpleado.setEditable(true);
        txtTelefonoContacto.setEditable(true);
        txtGradoCocinero.setEditable(true);
        cmbCodTipoEmpleado.setDisable(false);
    }

    public void limpiarControles() {
        txtCodEmpleado.clear();
        txtNumeroEmpleado.clear();
        txtApellidoEmpleado.clear();
        txtNombreEmpleado.clear();
        txtDireccionEmpleado.clear();
        txtTelefonoContacto.clear();
        txtGradoCocinero.clear();
        cmbCodTipoEmpleado.setValue(null);
    }

    public void dseleccionar() {
        tblEmpleados.getSelectionModel().clearSelection();
        limpiarControles();
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

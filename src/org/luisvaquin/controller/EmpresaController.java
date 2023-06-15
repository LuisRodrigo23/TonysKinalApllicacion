package org.luisvaquin.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import org.luisvaquin.bean.Empresa;
import org.luisvaquin.db.Conexion;
import org.luisvaquin.main.Principal;
import org.luisvaquin.report.GenerarReporte;

public class EmpresaController implements Initializable {

    private enum operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenaPrincipal;
    private ObservableList<Empresa> listaEmpresa;

    //@FXML private Button btnSalir;
    @FXML
    private Button btnRegresar;
    @FXML
    private TextField txtCodigoEmpresa;
    @FXML
    private TextField txtNombreEmpresa;
    @FXML
    private TextField txtDireccionEmpresa;
    @FXML
    private TextField txtTelefonoEmpresa;
    @FXML
    private TableView tblEmpresas;
    @FXML
    private TableColumn colCodigoEmpresa;
    @FXML
    private TableColumn colNombreEmpresa;
    @FXML
    private TableColumn colDireccionEmpresa;
    @FXML
    private TableColumn colTelefonoEmpresa;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnEditar;
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
    }

    public void cargarDatos() {
        tblEmpresas.setItems(getEmpresa());
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, Integer>("codigoEmpresa"));
        colNombreEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("nombreEmpresa"));
        colDireccionEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("direccion"));
        colTelefonoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("telefono"));
    }

    public ObservableList<Empresa> getEmpresa() {
        ArrayList<Empresa> lista = new ArrayList<Empresa>();

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpresa");
            ResultSet resultado = procedimiento.executeQuery();

            while (resultado.next()) {
                lista.add(new Empresa(resultado.getInt("codigoEmpresa"),
                        resultado.getString("nombreEmpresa"),
                        resultado.getString("direccion"),
                        resultado.getString("telefono")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaEmpresa = FXCollections.observableArrayList(lista);
    }

    public void nuevo() {
        switch (tipoDeOperacion) {
            case NINGUNO:
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
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDefaultButton(false);
                btnReporte.setDefaultButton(false);
                imgNuevo.setImage(new Image("/org/luisvaquin/image/Logo Nuevo.png"));
                imgEliminar.setImage(new Image("/org/luisvaquin/image/Logo Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }

    public void seleccionarEliminar() {
        txtCodigoEmpresa.setText(String.valueOf(((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
        txtDireccionEmpresa.setText(((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getDireccion());
        txtNombreEmpresa.setText(((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getNombreEmpresa());
        txtTelefonoEmpresa.setText(String.valueOf(((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getTelefono()));

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
                if (tblEmpresas.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?", "Eliminar Empresa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarEmpresa(?)");
                            procedimiento.setInt(1, ((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
                            procedimiento.execute();
                            listaEmpresa.remove(tblEmpresas.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            cargarDatos();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                    }
                }
        }
    }

    public void seleccionarElemento() {
//        txtCodigoEmpresa.setText(String.valueOf((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()));
        if (tblEmpresas.getSelectionModel().getSelectedItem() != null) {
            txtCodigoEmpresa.setText(String.valueOf(((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
            txtNombreEmpresa.setText(((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getNombreEmpresa());
            txtDireccionEmpresa.setText(((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getDireccion());
            txtTelefonoEmpresa.setText(((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getTelefono());
        } else {
            JOptionPane.showMessageDialog(null, "Selecciona un campo que tenga datos.");
        }
    }

    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblEmpresas.getSelectionModel().getSelectedItem() != null) {
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
                desactivarControles();
                dseleccionar();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                imgEditar.setImage(new Image("/org/luisvaquin/image/Logo Editar.png"));
                imgReporte.setImage(new Image("/org/luisvaquin/image/Logo Reporte.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }

    public void actualizar() {
        try {
            // Revisar PreparedStatement con la base de datos
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ActualizarEmpresa(?, ?, ?, ?)");
            Empresa registro = (Empresa) tblEmpresas.getSelectionModel().getSelectedItem();
            registro.setNombreEmpresa(txtNombreEmpresa.getText());
            registro.setDireccion(txtDireccionEmpresa.getText());
            registro.setTelefono(txtTelefonoEmpresa.getText());
            procedimiento.setInt(1, registro.getCodigoEmpresa());
            procedimiento.setString(2, registro.getNombreEmpresa());
            procedimiento.setString(3, registro.getDireccion());
            procedimiento.setString(4, registro.getTelefono());
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
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                imgEditar.setImage(new Image(""));
                imgReporte.setImage(new Image(""));
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }

    //Este metodo generar reporte se concatena en el button reporte para Empresa
    //En el On Action   
    // REPORTE DE ENNTIDAD EMPRESAS
    public void generarReporte() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                imprimirReporte();
                break;
            case ACTUALIZAR:
                break;
        }
    }

    public void imprimirReporte() {
        try {
            Map parametros = new HashMap();
            parametros.put("codigoEmpresa", null);
            GenerarReporte.mostrarReporte("ReporteEmpresas.jasper", "Reporte de empresas", parametros);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Seleccione algun registro");
            e.printStackTrace();
        }

    }
// ---------------------------------------------------------------------------------------------------

    public void generarReporteGeneral() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                imprimirReporteGeneral();
                break;
        }
    }

    public void imprimirReporteGeneral() {
        try {
            Map parametros = new HashMap();
            parametros.put("codigoEmpresa", null);
            GenerarReporte.mostrarReporte("ReporteGeneral.jasper", "Reporte de general", parametros);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar algun registro");
            e.printStackTrace();
        }
    }

    public void guardar() {
        Empresa registro = new Empresa();
        //registro.setCodigoEmpresa(Integer.parseInt(txtCodigoEmpresa.getText()));
        registro.setNombreEmpresa(txtNombreEmpresa.getText());
        registro.setDireccion(txtDireccionEmpresa.getText());
        registro.setTelefono(txtTelefonoEmpresa.getText());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarEmpresa(?,?,?)");
            procedimiento.setString(1, registro.getNombreEmpresa());
            procedimiento.setString(2, registro.getDireccion());
            procedimiento.setString(3, registro.getTelefono());
            procedimiento.execute();
            listaEmpresa.add(registro);
            cargarDatos();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dseleccionar() {
        tblEmpresas.getSelectionModel().clearSelection();
        limpiarControles();
    }

    public void desactivarControles() {
        txtCodigoEmpresa.setEditable(false);
        txtNombreEmpresa.setEditable(false);
        txtDireccionEmpresa.setEditable(false);
        txtTelefonoEmpresa.setEditable(false);
    }

    public void activarControles() {
        txtCodigoEmpresa.setEditable(false);
        txtNombreEmpresa.setEditable(true);
        txtDireccionEmpresa.setEditable(true);
        txtTelefonoEmpresa.setEditable(true);
    }

    public void limpiarControles() {
        txtCodigoEmpresa.clear();
        txtNombreEmpresa.clear();
        txtDireccionEmpresa.clear();
        txtTelefonoEmpresa.clear();
    }

    public Principal getEscenaPrincipal() {
        return escenaPrincipal;
    }

    public void setEscenaPrincipal(Principal escenaPrincipal) {
        this.escenaPrincipal = escenaPrincipal;
    }

    @FXML
    private void menuPrincipal(ActionEvent evento) {
        if (evento.getSource() == btnRegresar) {
            escenaPrincipal.menuPrincipal();
        }
    }

    public void ventanaPresupuesto() {
        escenaPrincipal.ventanaPresupuesto();
    }

}

package org.luisvaquin.controller;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import static org.apache.poi.hssf.usermodel.HeaderFooter.date;
import org.luisvaquin.bean.Empresa;
import org.luisvaquin.bean.Servicio;
import org.luisvaquin.db.Conexion;
import org.luisvaquin.main.Principal;

public class ServiciosController implements Initializable {

    private Principal escenarioPrincipal;

    private enum operaciones {
        GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO
    }

    Alert alerta = new Alert(Alert.AlertType.WARNING);
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Servicio> listaServicio;
    private ObservableList<Empresa> listaEmpresa;
    LocalDate localDate = LocalDate.now();

    @FXML
    private GridPane grpFechaHora;
    @FXML
    private JFXTimePicker hora;
    @FXML
    private JFXDatePicker fecha;
    @FXML
    private TableView tblServicios;
    @FXML
    private ComboBox cmbCodigoEmpresa;
    @FXML
    private TextField txtCodigoServicio;
    @FXML
    private TextField txtTipoServicio;
    @FXML
    private TextField txtLugarServicio;
    @FXML
    private TextField txtTelefonoContacto;
    @FXML
    private TableColumn colCodigoServicio;
    @FXML
    private TableColumn colFechaServicio;
    @FXML
    private TableColumn colTipoServicio;
    @FXML
    private TableColumn colHoraServicio;
    @FXML
    private TableColumn colLugarServicio;
    @FXML
    private TableColumn colTelefonoContacto;
    @FXML
    private TableColumn colCodigoEmpresa;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnReporte;
    @FXML
    private Button btnEditar;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgReporte;

    //Asignar FXML para agregar a metodos
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        cargarDatos();
        fecha = new JFXDatePicker(LocalDate.now());
        hora = new JFXTimePicker();
        grpFechaHora.add(fecha, 3, 0);
        grpFechaHora.add(hora, 3, 1);
        cmbCodigoEmpresa.setItems(getEmpresa());
        desactivarControles();
    }

    public void cargarDatos() {
        tblServicios.setItems(getServicios());
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicio, Integer>("codigoServicio"));
        colFechaServicio.setCellValueFactory(new PropertyValueFactory<Servicio, Date>("fechaServicio"));
        colTipoServicio.setCellValueFactory(new PropertyValueFactory<Servicio, String>("tipoServicio"));
        colHoraServicio.setCellValueFactory(new PropertyValueFactory<Servicio, String>("horaServicio"));
        colLugarServicio.setCellValueFactory(new PropertyValueFactory<Servicio, String>("lugarServicio"));
        colTelefonoContacto.setCellValueFactory(new PropertyValueFactory<Servicio, String>("telefonoContacto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Servicio, Integer>("codigoEmpresa"));
    }

    public ObservableList<Servicio> getServicios() {
        ArrayList<Servicio> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarServicios");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Servicio(resultado.getInt("codigoServicio"),
                        resultado.getDate("fechaServicio").toLocalDate(),
                        resultado.getString("tipoServicio"),
                        resultado.getTime("horaServicio"),
                        resultado.getString("lugarServicio"),
                        resultado.getString("telefonoContacto"),
                        resultado.getInt("codigoEmpresa")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaServicio = FXCollections.observableArrayList(lista);
    }

    public ObservableList<Empresa> getEmpresa() {
        ArrayList<Empresa> lista = new ArrayList<Empresa>();

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpresa()");
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

    public Empresa buscarEmpresa(int codigoEmpresa) {
        Empresa resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarEmpresa(?)");
            procedimiento.setInt(1, codigoEmpresa);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Empresa(registro.getInt("codigoEmpresa"),
                        registro.getString("nombreEmpresa"),
                        registro.getString("direccion"),
                        registro.getString("telefono"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public void seleccionarElemento() {
        if (tblServicios.getSelectionModel().getSelectedItem() != null) {
            txtCodigoServicio.setText(String.valueOf(((Servicio) tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio()));
            txtTipoServicio.setText(((Servicio) tblServicios.getSelectionModel().getSelectedItem()).getTipoServicio());
            hora.setValue(((Servicio) tblServicios.getSelectionModel().getSelectedItem()).getHoraServicio().toLocalTime());
            fecha.setValue(((Servicio) tblServicios.getSelectionModel().getSelectedItem()).getFechaServicio());
            txtLugarServicio.setText(((Servicio) tblServicios.getSelectionModel().getSelectedItem()).getLugarServicio());
            txtTelefonoContacto.setText(((Servicio) tblServicios.getSelectionModel().getSelectedItem()).getTelefonoContacto());
            cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Servicio) tblServicios.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione algun registro");
        }
    }

    public void nuevo() {
        switch (tipoDeOperaciones) {
            case NINGUNO:
                dseleccionar();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/luisvaquin/image/IconGuardar.png"));
                imgEliminar.setImage(new Image("/org/luisvaquin/image/IconCancelar.png"));
                tipoDeOperaciones = operaciones.GUARDAR;
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
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                limpiarControles();
                break;
        }
    }

    public void eliminar() {
        switch (tipoDeOperaciones) {
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/luisvaquin/image/Logo Nuevo.png"));
                imgEliminar.setImage(new Image("/org/luisvaquin/image/Logo Eliminar.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
            default:
                if (tblServicios.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Está seguro de eliminar el registro?", "Eliminar Empresa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarServicios(?)");
                            procedimiento.setInt(1, ((Servicio) tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio());
                            procedimiento.execute();
                            listaServicio.remove(tblServicios.getSelectionModel().getFocusedIndex());
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

    public void editar() {
        switch (tipoDeOperaciones) {
            case NINGUNO:
                if (tblServicios.getSelectionModel().getSelectedItem() != null) {
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/luisvaquin/image/IconActualizar.png"));
                    imgReporte.setImage(new Image("/org/luisvaquin/image/IconCancelar.png"));
                    activarControles();
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
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
                imgEditar.setImage(new Image("/org/luisvaquin/image/IconActualizar.png"));
                imgReporte.setImage(new Image("/org/luisvaquin/image/IconCancelar.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                desactivarControles();
                dseleccionar();
                break;
        }
    }

    public void actualizar() {
        Servicio registro = (Servicio) tblServicios.getSelectionModel().getSelectedItem();
        registro.setFechaServicio(fecha.getValue());
        registro.setTipoServicio(txtTipoServicio.getText());
        registro.setHoraServicio(Time.valueOf(hora.getValue()));
        registro.setLugarServicio(txtLugarServicio.getText());
        registro.setTelefonoContacto(txtTelefonoContacto.getText());
        registro.setCodigoEmpresa(((Empresa) cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ActualizarServicios(?,?,?,?,?,?,?);");
            procedimiento.setInt(1, registro.getCodigoServicio());
            procedimiento.setDate(2, Date.valueOf(registro.getFechaServicio()));
            procedimiento.setString(3, registro.getTipoServicio());
            procedimiento.setTime(4, registro.getHoraServicio());
            procedimiento.setString(5, registro.getLugarServicio());
            procedimiento.setString(6, registro.getTelefonoContacto());
            procedimiento.setInt(7, registro.getCodigoEmpresa());
            procedimiento.execute();
        } catch (Exception e) {
        }

    }

    public void guardar() {
        Servicio registro = new Servicio();
        registro.setFechaServicio(fecha.getValue());
        registro.setTipoServicio(txtTipoServicio.getText());
        registro.setHoraServicio(Time.valueOf(hora.getValue()));
        registro.setLugarServicio(txtLugarServicio.getText());
        registro.setTelefonoContacto(txtTelefonoContacto.getText());
        registro.setCodigoEmpresa(((Empresa) cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarServicios(?,?,?,?,?,?)");
            procedimiento.setDate(1, Date.valueOf(registro.getFechaServicio()));
            procedimiento.setString(2, registro.getTipoServicio());
            procedimiento.setTime(3, registro.getHoraServicio());
            procedimiento.setString(4, registro.getLugarServicio());
            procedimiento.setString(5, registro.getTelefonoContacto());
            procedimiento.setInt(6, registro.getCodigoEmpresa());
            procedimiento.execute();
            listaServicio.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reporte() {

    }

    public void dseleccionar() {
        tblServicios.getSelectionModel().clearSelection();
        limpiarControles();
    }

    public void desactivarControles() {
        txtCodigoServicio.setEditable(false);
        fecha.setDisable(true);
        txtTipoServicio.setEditable(false);
        hora.setDisable(true);
        txtLugarServicio.setEditable(false);
        txtTelefonoContacto.setEditable(false);
        cmbCodigoEmpresa.setDisable(true);
    }

    public void activarControles() {
        txtCodigoServicio.setEditable(false);
        fecha.setDisable(false);
        txtTipoServicio.setEditable(true);
        hora.setDisable(false);
        txtLugarServicio.setEditable(true);
        txtTelefonoContacto.setEditable(true);
        cmbCodigoEmpresa.setDisable(false);
    }

    public void limpiarControles() {
        txtCodigoServicio.clear();
        fecha.setValue(null);
        txtTipoServicio.clear();
        hora.setValue(null);
        txtLugarServicio.clear();
        txtTelefonoContacto.clear();
        cmbCodigoEmpresa.setValue(null);
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

package org.luisvaquin.controller;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import org.luisvaquin.bean.Empleado;
import org.luisvaquin.bean.Servicio;
import org.luisvaquin.bean.Servicios_has_empleado;
import org.luisvaquin.main.Principal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.luisvaquin.db.Conexion;

/**
 *
 * @author luisb
 */
public class Servicios_has_EmpleadosController implements Initializable {

    private Principal escenarioPrincipal;

    private enum operaciones {
        GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO
    };

    private operaciones tipoDeoOperaciones = operaciones.NINGUNO;
    private ObservableList<Servicio> listarServicios;
    private ObservableList<Empleado> listarEmpleados;
    private ObservableList<Servicios_has_empleado> listaServicios_has_platos;

    @FXML
    private GridPane grpFechaHora;
    @FXML
    private JFXTimePicker hora;
    @FXML
    private JFXDatePicker fecha;
    @FXML
    private TableView tblServicios_has_Empleados;
    @FXML
    private TextField txtLugarEvento;
    @FXML
    private TextField txtServicioCodigoServicio;
    @FXML
    private ComboBox cmbCodigoServicio;
    @FXML
    private ComboBox cmbCodigoEmpleado;
    @FXML
    private TableColumn colServicioCodigoServicio;
    @FXML
    private TableColumn colCodigoServicio;
    @FXML
    private TableColumn colCodigoEmpleado;
    @FXML
    private TableColumn colFechaEvento;
    @FXML
    private TableColumn colHoraEvento;
    @FXML
    private TableColumn colLugarEvento;
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
        fecha = new JFXDatePicker(LocalDate.now());
        hora = new JFXTimePicker();
        grpFechaHora.add(fecha, 3, 0);
        grpFechaHora.add(hora, 3, 1);
        cmbCodigoServicio.setItems(getServicios());
        cmbCodigoEmpleado.setItems(getEmpleado());
        desactivarControles();
    }

    public void cargarDatos() {
        tblServicios_has_Empleados.setItems(getServicios_has_Empleados());
        colServicioCodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicios_has_empleado, String>("Servicios_codigoServicio"));
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicios_has_empleado, String>("codigoServicio"));
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Servicios_has_empleado, String>("codigoEmpleado"));
        colFechaEvento.setCellValueFactory(new PropertyValueFactory<Servicios_has_empleado, String>("fechaEvento"));
        colHoraEvento.setCellValueFactory(new PropertyValueFactory<Servicios_has_empleado, String>("horaEvento"));
        colLugarEvento.setCellValueFactory(new PropertyValueFactory<Servicios_has_empleado, String>("lugarEvento"));

    }

    public ObservableList<Servicios_has_empleado> getServicios_has_Empleados() {
        ArrayList<Servicios_has_empleado> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios_has_Empleados()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Servicios_has_empleado(resultado.getInt("Servicios_codigoServicio"),
                        resultado.getInt("codigoServicio"),
                        resultado.getInt("codigoEmpleado"),
                        resultado.getDate("fechaEvento").toLocalDate(),
                        resultado.getTime("horaEvento"),
                        resultado.getString("lugarEvento")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaServicios_has_platos = FXCollections.observableArrayList(lista);
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
        return listarServicios = FXCollections.observableArrayList(lista);
    }

    public Servicio buscarServicio(int codigoServicio) {
        Servicio resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarServicios(?)}");
            procedimiento.setInt(1, codigoServicio);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Servicio(registro.getInt("codigoServicio"),
                        registro.getDate("fechaServicio").toLocalDate(),
                        registro.getString("tipoServicio"),
                        registro.getTime("horaServicio"),
                        registro.getString("lugarServicio"),
                        registro.getString("telefonoContacto"),
                        registro.getInt("codigoEmpresa"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
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
                        resultado.getString("telefonoContacto   "),
                        resultado.getString("gradoCocinero"),
                        resultado.getInt("codigoTipoEmpleado")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listarEmpleados = FXCollections.observableArrayList(lista);
    }

    public Empleado buscarEmpleado(int codigoEmpleado) {
        Empleado resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpleado(?)}");
            procedimiento.setInt(1, codigoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Empleado(registro.getInt("codigoEmpleado"),
                        registro.getInt("numeroEmpleado"),
                        registro.getString("apellidosEmpleado"),
                        registro.getString("nombresEmpleado"),
                        registro.getString("direccionEmpleado"),
                        registro.getString("telefonoContacto"),
                        registro.getString("gradoCocinero"),
                        registro.getInt("codigoTipoEmpleado"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public void seleccionarElementos() {
        if (tblServicios_has_Empleados.getSelectionModel().getSelectedItem() != null) {
            txtServicioCodigoServicio.setText(String.valueOf(((Servicios_has_empleado) tblServicios_has_Empleados.getSelectionModel().getSelectedItem()).getServicios_codigoServicio()));
            cmbCodigoServicio.getSelectionModel().select(buscarServicio(((Servicios_has_empleado) tblServicios_has_Empleados.getSelectionModel().getSelectedItem()).getCodigoServicio()));
            cmbCodigoEmpleado.getSelectionModel().select(buscarEmpleado(((Servicios_has_empleado) tblServicios_has_Empleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
            fecha.setValue(((Servicios_has_empleado) tblServicios_has_Empleados.getSelectionModel().getSelectedItem()).getFechaEvento());
            hora.setValue(((Servicios_has_empleado) tblServicios_has_Empleados.getSelectionModel().getSelectedItem()).getHoraEvento().toLocalTime());
            txtLugarEvento.setText(((Servicios_has_empleado) tblServicios_has_Empleados.getSelectionModel().getSelectedItem()).getLugarEvento());
        }
    }

    public void nuevo() {
        switch (tipoDeoOperaciones) {
            case NINGUNO:
                dseleccionar();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/luisvaquin/image/IconGuardar.png"));
                imgEliminar.setImage(new Image("/org/luisvaquin/image/IconCancelar.png"));
                tipoDeoOperaciones = operaciones.GUARDAR;
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
                tipoDeoOperaciones = operaciones.NINGUNO;
                cargarDatos();
                limpiarControles();
                break;
        }
    }

    public void guardar() {
        Servicios_has_empleado registro = new Servicios_has_empleado();
        registro.setServicios_codigoServicio(Integer.parseInt(txtServicioCodigoServicio.getText()));
        registro.setCodigoServicio(((Servicio) cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
        registro.setCodigoEmpleado(((Empleado) cmbCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
        registro.setFechaEvento(fecha.getValue());
        registro.setHoraEvento(Time.valueOf(hora.getValue()));
        registro.setLugarEvento(txtLugarEvento.getText());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarServicio_has_Empleado(?,?,?,?,?,?)");
            procedimiento.setInt(1, registro.getServicios_codigoServicio());
            procedimiento.setInt(2, registro.getCodigoServicio());
            procedimiento.setInt(3, registro.getCodigoEmpleado());
            procedimiento.setDate(4, Date.valueOf(registro.getFechaEvento()));
            procedimiento.setTime(5, registro.getHoraEvento());
            procedimiento.setString(6, registro.getLugarEvento());
            procedimiento.execute();
            listaServicios_has_platos.add(registro);
            cargarDatos();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dseleccionar() {
        tblServicios_has_Empleados.getSelectionModel().clearSelection();
        limpiarControles();

    }

    public void desactivarControles() {
        txtServicioCodigoServicio.setEditable(false);
        cmbCodigoServicio.setDisable(true);
        cmbCodigoEmpleado.setDisable(true);
        fecha.setDisable(true);
        hora.setDisable(true);
        txtLugarEvento.setEditable(false);
    }

    public void activarControles() {
        txtServicioCodigoServicio.setEditable(true);
        cmbCodigoServicio.setDisable(false);
        cmbCodigoEmpleado.setDisable(false);
        fecha.setDisable(false);
        hora.setDisable(false);
        txtLugarEvento.setEditable(true);
    }

    public void limpiarControles() {
        txtServicioCodigoServicio.clear();
        cmbCodigoServicio.setValue(null);
        cmbCodigoEmpleado.setValue(null);
        fecha.setValue(null);
        hora.setValue(null);
        txtLugarEvento.clear();
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

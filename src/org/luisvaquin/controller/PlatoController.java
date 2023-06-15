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
import org.luisvaquin.bean.Plato;
import org.luisvaquin.bean.TipoPlato;
import org.luisvaquin.db.Conexion;
import org.luisvaquin.main.Principal;

public class PlatoController implements Initializable {

    private enum operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO;
    }
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<Plato> listaPlato;
    private ObservableList<TipoPlato> listaTipoPlato;

    @FXML
    private ComboBox cmbCodigoTipoPlato;
    @FXML
    private TableView tblPlatos;
    @FXML
    private TableColumn colCodigoPlato;
    @FXML
    private TableColumn colCantidadPlato;
    @FXML
    private TableColumn colNombrePlato;
    @FXML
    private TableColumn colDescripcionPlato;
    @FXML
    private TableColumn colPrecioPlato;
    @FXML
    private TableColumn colCodigoTipoPlato;
    @FXML
    private TextField txtCodigoPlato;
    @FXML
    private TextField txtNombrePlato;
    @FXML
    private TextField txtPrecioPlato;
    @FXML
    private TextField txtCantidadPlato;
    @FXML
    private TextField txtDescripcionPlato;
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
    private ImageView imgEditar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgReporte;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        desactivarControles();
        cmbCodigoTipoPlato.setItems(getTipoPlato());
    }

    public void cargarDatos() {
        tblPlatos.setItems(getPlato());
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("codigoPlato"));
        colCantidadPlato.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("cantidad"));
        colNombrePlato.setCellValueFactory(new PropertyValueFactory<Plato, String>("nombrePlato"));
        colDescripcionPlato.setCellValueFactory(new PropertyValueFactory<Plato, String>("descripcionPlato"));
        colPrecioPlato.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("precioPlato"));
        colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("codigoTipoPlato"));
    }

    public ObservableList<Plato> getPlato() {
        ArrayList<Plato> lista = new ArrayList<Plato>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarPlatos");
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
        return listaPlato = FXCollections.observableArrayList(lista);
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
        }
        return listaTipoPlato = FXCollections.observableArrayList(lista);
    }

    public TipoPlato buscarTipoPlato(int codigoTipoPlato) {
        TipoPlato registro = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarTipoPlato(?);");
            procedimiento.setInt(1, codigoTipoPlato);
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                registro = new TipoPlato(resultado.getInt("codigoTipoPlato"),
                        resultado.getString("descripcionTipo"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return registro;
    }

    public void seleccionarElemento() {
        if (tblPlatos.getSelectionModel().getSelectedItem() != null) {
            txtCodigoPlato.setText(String.valueOf(((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
            txtCantidadPlato.setText(String.valueOf(((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getCantidad()));
            txtNombrePlato.setText(((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getNombrePlato());
            txtDescripcionPlato.setText(((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getDescripcionPlato());
            txtPrecioPlato.setText(String.valueOf(((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getPrecioPlato()));
            cmbCodigoTipoPlato.getSelectionModel().select(buscarTipoPlato(((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
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
                if (tblPlatos.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Está seguro de eliminar el registro?", "Eliminar Empresa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarPlatos(?)");
                            procedimiento.setInt(1, ((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato());
                            procedimiento.execute();
                            listaPlato.remove(tblPlatos.getSelectionModel().getFocusedIndex());
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
        Plato registro = new Plato();
        registro.setCantidad(Integer.parseInt(txtCantidadPlato.getText()));
        registro.setNombrePlato(txtNombrePlato.getText());
        registro.setDescripcionPlato(txtDescripcionPlato.getText());
        registro.setPrecioPlato(Integer.parseInt(txtPrecioPlato.getText()));
        registro.setCodigoTipoPlato(((TipoPlato) cmbCodigoTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarPlatos(?,?,?,?,?)");
            procedimiento.setInt(1, registro.getCantidad());
            procedimiento.setString(2, registro.getNombrePlato());
            procedimiento.setString(3, registro.getDescripcionPlato());
            procedimiento.setDouble(4, registro.getPrecioPlato());
            procedimiento.setInt(5, registro.getCodigoTipoPlato());
            procedimiento.execute();
            listaPlato.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblPlatos.getSelectionModel().getSelectedItem() != null) {
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/luisvaquin/image/IconActualizar.png"));
                    imgReporte.setImage(new Image("/org/luisvaquin/image/IconCancelar.png"));
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione algun elemento");
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
                tblPlatos.getSelectionModel().clearSelection();
                break;
        }
    }

    public void actualizar() {
        Plato registro = (Plato) tblPlatos.getSelectionModel().getSelectedItem();
        registro.setCantidad(Integer.parseInt(txtCantidadPlato.getText()));
        registro.setNombrePlato(txtNombrePlato.getText());
        registro.setDescripcionPlato(txtDescripcionPlato.getText());
        registro.setPrecioPlato(Integer.parseInt(txtPrecioPlato.getText()));
        registro.setCodigoTipoPlato(((TipoPlato) cmbCodigoTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ActualizarPlatos(?,?,?,?,?,?)");
            procedimiento.setInt(1, registro.getCodigoPlato());
            procedimiento.setInt(2, registro.getCantidad());
            procedimiento.setString(3, registro.getNombrePlato());
            procedimiento.setString(4, registro.getDescripcionPlato());
            procedimiento.setDouble(5, registro.getPrecioPlato());
            procedimiento.setInt(6, registro.getCodigoTipoPlato());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dseleccionar() {
        tblPlatos.getSelectionModel().clearSelection();
    }

    public void desactivarControles() {

        txtCodigoPlato.setEditable(false);
        txtCantidadPlato.setEditable(false);
        txtNombrePlato.setEditable(false);
        txtDescripcionPlato.setEditable(false);
        txtPrecioPlato.setEditable(false);
        cmbCodigoTipoPlato.setDisable(true);
    }

    public void activarControles() {

        txtCodigoPlato.setEditable(false);
        txtCantidadPlato.setEditable(true);
        txtNombrePlato.setEditable(true);
        txtDescripcionPlato.setEditable(true);
        txtPrecioPlato.setEditable(true);
        cmbCodigoTipoPlato.setDisable(false);
    }

    public void limpiarControles() {
        txtCodigoPlato.clear();
        txtCantidadPlato.clear();
        txtNombrePlato.clear();
        txtDescripcionPlato.clear();
        txtPrecioPlato.clear();
        cmbCodigoTipoPlato.setValue(null);
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

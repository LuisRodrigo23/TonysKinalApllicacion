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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.luisvaquin.bean.Producto;
import org.luisvaquin.db.Conexion;
import org.luisvaquin.main.Principal;

public class ProductoController implements Initializable {

    private enum operaciones {NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList <Producto> listarProducto;
    
    //Crear el FXML para los objetos

    @FXML private TextField txtCodigoProducto;
    @FXML private TextField txtNombreProducto;
    @FXML private TextField txtCantidadProducto;
    @FXML private TableView tblProductos;
    @FXML private TableColumn colCodigoProducto;
    @FXML private TableColumn colNombreProducto;
    @FXML private TableColumn colCantidadProducto;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblProductos.setItems(getProducto());
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("codigoProducto"));
        colNombreProducto.setCellValueFactory(new PropertyValueFactory<Producto, String>("nombreProducto"));
        colCantidadProducto.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("cantidad"));
    }
    
    public ObservableList<Producto> getProducto(){
        ArrayList <Producto> lista = new ArrayList <Producto>();
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall ("call sp_ListarProductos");
            ResultSet resultado = procedimiento.executeQuery();
            
            while (resultado.next()){
                lista.add(new Producto( resultado.getInt("codigoProducto"),
                        resultado.getString("nombreProducto"),
                        resultado.getInt("cantidad")));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }   
        
        return listarProducto = FXCollections.observableArrayList(lista);
    }
    
    public void nuevo(){
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
                imgNuevo.setImage(new Image ("/org/luisvaquin/image/Logo Nuevo.png"));
                imgEliminar.setImage(new Image ("/org/luisvaquin/image/Logo Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
        public void seleccionarElemento() {
        if (tblProductos.getSelectionModel().getSelectedItem() != null) {
            txtCodigoProducto.setText(String.valueOf(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
            txtNombreProducto.setText(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getNombreProducto());
            txtCantidadProducto.setText(String.valueOf(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getCantidad()));
        } else {
            JOptionPane.showMessageDialog(null, "Selecciona un campo que tenga datos.");
        }
    }

    
    //Verificar compativilidad de datos
    public void seleccionarEliminar (){
        txtCodigoProducto.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
        txtNombreProducto.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getNombreProducto()));
        txtCantidadProducto.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCantidad()));
    }
    
    public void eliminar(){
        switch (tipoDeOperacion) {
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image ("/org/luisvaquin/image/Logo Nuevo.png"));
                imgEliminar.setImage(new Image ("/org/luisvaquin/image/Logo Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if (tblProductos.getSelectionModel().getSelectedItem() !=null) {
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta seguro de eliminar el registro?","Eliminar Empresa",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try{
                               PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarProductos(?)");
                               procedimiento.setInt(1,((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
                               procedimiento.execute();
                               listarProducto.remove(tblProductos.getSelectionModel().getSelectedIndex());
                               limpiarControles();
                               cargarDatos();
                               
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        JOptionPane.showMessageDialog(null,"Seleccione un elemento");
                    }
                }
        }
    }
    
    public void editar(){
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblProductos.getSelectionModel().getSelectedItem() !=null) {
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image ("/org/luisvaquin/image/IconActualizar.png"));
                    imgReporte.setImage(new Image ("/org/luisvaquin/image/IconCancelar.png"));
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar algun espacio");
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
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;
                break ;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ActualizarProductos(?,?,?)");
            Producto registro = (Producto)tblProductos.getSelectionModel().getSelectedItem();
            registro.setNombreProducto(txtNombreProducto.getText());
            registro.setCantidad(Integer.parseInt(txtCantidadProducto.getText()));
            procedimiento.setInt(1,registro.getCodigoProducto());
            procedimiento.setString(2,registro.getNombreProducto());
            procedimiento.setInt(3, registro.getCantidad());
            procedimiento.execute();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void reporte(){
        JOptionPane.showMessageDialog(null,"Se trabaja tercer bim");
    }
    
    public void guardar(){
        Producto registro = new Producto ();
        registro.setNombreProducto(txtNombreProducto.getText());
        registro.setCantidad(Integer.parseInt(txtCantidadProducto.getText()));
        
        try{
            //Agregar Procedimiento almacenado en parametros del metodo para el trycatch
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarProductos(?,?)");
            procedimiento.setString(1,registro.getNombreProducto());
            procedimiento.setInt(2,registro.getCantidad());
            procedimiento.execute();
            listarProducto.add(registro);
            cargarDatos();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
     
    public void desactivarControles(){
        txtCodigoProducto.setEditable(false);
        txtNombreProducto.setEditable(false);
        txtCantidadProducto.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoProducto.setEditable(false);
        txtNombreProducto.setEditable(true);
        txtCantidadProducto.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoProducto.clear();
        txtNombreProducto.clear();
        txtCantidadProducto.clear();
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

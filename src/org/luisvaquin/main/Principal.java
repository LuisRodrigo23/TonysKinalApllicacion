/*
Luis Rodrigo Vaquin Bacajol
Codigo tecnico: IN5BM
Codigo Academico: 2022300
Fecha de inicio: 12/04/2023
 */
package org.luisvaquin.main;

import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.luisvaquin.controller.EmpleadosController;
import org.luisvaquin.controller.EmpresaController;
import org.luisvaquin.controller.LoginController;
import org.luisvaquin.controller.MenuPrincipalController;
import org.luisvaquin.controller.PlatoController;
import org.luisvaquin.controller.PresupuestoController;
import org.luisvaquin.controller.ProductoController;
import org.luisvaquin.controller.Productos_has_PlatosController;
import org.luisvaquin.controller.ProgramadorController;
import org.luisvaquin.controller.ServiciosController;
import org.luisvaquin.controller.Servicios_has_EmpleadosController;
import org.luisvaquin.controller.TipoEmpleadoController;
import org.luisvaquin.controller.TipoPlatoController;
import org.luisvaquin.controller.UsuarioController;

public class Principal extends Application {

    ProgramadorController Programador = new ProgramadorController();
    private Stage escenarioPrincipal;
    private Scene escena;
    private final String PAQUETE_VISTA = "/org/luisvaquin/view/";

    @Override
    public void start(Stage escenarioPrincipal) throws IOException {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Tony´s Kinal 2023");
        escenarioPrincipal.getIcons().add(new Image("/org/luisvaquin/image/Iconcarr.png"));
        //Parent root = FXMLLoader.load(getClass().getResource("/org/luisvaquin/view/EmpresaView.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("/org/luisvaquin/view/ProgramadorView.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("/org/luisvaquin/view/MenuPrincipalView.fxml"));

        //Scene escena = new Scene(root);
        //escenarioPrincipal.setScene(escena);
        login();
        escenarioPrincipal.show();
    }

    public void menuPrincipal() {
        try {
            MenuPrincipalController menu = (MenuPrincipalController) cambiarEscena("MenuPrincipalView.fxml", 974, 757);
            menu.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaProgramador() {
        try {
            ProgramadorController programador = (ProgramadorController) cambiarEscena("ProgramadorView.fxml", 695, 632);
            programador.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaEmpresa() {
        try {
            EmpresaController empresa = (EmpresaController) cambiarEscena("EmpresaView.fxml", 1045, 767);
            empresa.setEscenaPrincipal(this);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void ventanaTipoEmpleado() {
        try {
            TipoEmpleadoController tipoEmpleado = (TipoEmpleadoController) cambiarEscena("TipoEmpleado.fxml", 1045, 767);
            tipoEmpleado.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void ventanaProducto() {
        try {
            ProductoController producto = (ProductoController) cambiarEscena("ProductosView.fxml", 1045, 767);
            producto.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaEmpleado() {
        try {
            EmpleadosController empleados = (EmpleadosController) cambiarEscena("EmpleadosView.fxml", 1100, 767);
            empleados.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Agregar mas ventana para finalizar proyecto, desde ventanaPresupuesto
    public void ventanaPresupuesto() {
        try {
            PresupuestoController presupuesto = (PresupuestoController) cambiarEscena("PresupuestoView.fxml", 1045, 767);
            presupuesto.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaTipoPlato() {
        try {
            TipoPlatoController plato = (TipoPlatoController) cambiarEscena("TipoPlatoView.fxml", 1045, 767);
            plato.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void login() {
        try {
            LoginController login = (LoginController) cambiarEscena("LoginView.fxml", 660, 557);
            login.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaUsuario() {
        try {
            UsuarioController usuario = (UsuarioController) cambiarEscena("UsuarioView.fxml", 813, 567);
            usuario.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaPlato() {
        try {
            PlatoController plato = (PlatoController) cambiarEscena("PlatoView.fxml", 1045, 767);
            plato.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaServicio() {
        try {
            ServiciosController servicio = (ServiciosController) cambiarEscena("ServiciosView.fxml", 1045, 767);
            servicio.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaServicios_has_Empleados() {
        try {
            Servicios_has_EmpleadosController hasEmpleados = (Servicios_has_EmpleadosController) cambiarEscena("Servicios_has_Empleados.fxml", 1045, 767);
            hasEmpleados.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaProductos_has_Platos() {
        try {
            Productos_has_PlatosController hasProductos = (Productos_has_PlatosController) cambiarEscena("Productos_has_Platos.fxml", 1045, 767);
            hasProductos.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws IOException {
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA + fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA + fxml));
        escena = new Scene((AnchorPane) cargadorFXML.load(archivo), ancho, alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable) cargadorFXML.getController();
        return resultado;

    }

}

package org.example.views;

import database.DatabaseConnection; // Asegúrate de que apunte a la carpeta correcta de tu conexión
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Route("perfil")
public class PerfilUsuarioView extends VerticalLayout {

    /*Declaramos las variables*/
    private Span lblNombreCompleto;
    private Span lblCorreo;
    private Span lblCedula;
    private Span lblSueldo;

    private TextField txtNombre;
    private TextField txtApellido;
    private TextField txtCorreo;
    private TextField txtCedula;
    private TextField txtSueldo;

    private Button btnEditar;
    private Button btnGuardar;

    public PerfilUsuarioView() {
        /**Configuramos la pantalla completa*/
        this.setSizeFull();
        this.setAlignItems(Alignment.CENTER);
        this.setPadding(false);
        this.setSpacing(false);
        /**Creamos un contenedor padre para toda la vista*/
        VerticalLayout container = new VerticalLayout();
        container.setWidth("100%");
        //container.getStyle().set("max-width", "600px");
        container.setPadding(false);

        /**Creamos el primer contenedor para la vista del usuario*/
        VerticalLayout seccionPerfil = new VerticalLayout();
        seccionPerfil.getStyle().set("background-color", "#203a60");
        seccionPerfil.setWidth("100%");
        seccionPerfil.getStyle().set("border-radius", "15px");
        seccionPerfil.getStyle().set("border-top-left-radius","0px");
        seccionPerfil.getStyle().set("border-top-right-radius","0px");


        /**Creamos el bloque del titulo*/
        VerticalLayout mainTitle = new VerticalLayout();
        mainTitle.setWidth("100%");
        mainTitle.setPadding(false);

        H2 titulo = new H2("Mi Perfil");
        titulo.getStyle().set("color", "white");
        titulo.getStyle().set("margin", "0");
        mainTitle.add(titulo);

        /**Creamos la tarjeta principal donde va a estar la info del usuario*/
        HorizontalLayout userCard = new HorizontalLayout();
        userCard.getStyle().set("background-color", "rgba(255, 255, 255, 0.05)");
        userCard.getStyle().set("backdrop-filter", "blur(10px)");
        userCard.setWidth("100%");
        userCard.getStyle().set("border-radius", "10px");
        userCard.getStyle().set("padding", "15px");
        userCard.getStyle().set("flex-wrap", "wrap");
        userCard.setAlignItems(Alignment.CENTER);

        /**Inicializamos las variables que van a ir en la tarjeta*/
        String nombre="";
        String apellido="";
        String cedula="";
        String correo="";
        double sueldo=0.0;

        try{
            /**Realizamos la peticion para el id del usuario*/
            Integer idUsuario=(Integer) com.vaadin.flow.server.VaadinSession.getCurrent().getAttribute("usuarioId");
            if (idUsuario==null){
                idUsuario=1;
            }
            /**Realizamos la conexion con la base de datos*/
            Connection con =DatabaseConnection.getConnection();
            String query="Select nombre, apellido, cedula, correo, sueldo FROM usuarios WHERE id=?";
            PreparedStatement ps=con.prepareStatement(query);
            ps.setInt(1, idUsuario);

            ResultSet rs= ps.executeQuery();
            if (rs.next()){
                nombre=rs.getString("nombre");
                apellido=rs.getString("apellido");
                cedula=rs.getString("cedula");
                correo=rs.getString("correo");
                sueldo=rs.getDouble("sueldo");
            }

            /**Extraemos las iniciales para el recuadro del usuario*/
            String inicialNombre = (nombre !=null&& !nombre.isEmpty())?nombre.substring(0,1).toUpperCase():"";
            String inicialApellido = (apellido !=null && !apellido.isEmpty())?apellido.substring(0,1).toUpperCase():"";
            String iniciales = inicialNombre+inicialApellido;

            VerticalLayout avatar=new VerticalLayout();
            avatar.setWidth("75px");
            avatar.setHeight("75px");
            avatar.getStyle().set("background-color", "#2ecc71"); // Verde brillante
            avatar.getStyle().set("border-radius", "12px");
            avatar.setJustifyContentMode(JustifyContentMode.CENTER);
            avatar.setAlignItems(Alignment.CENTER);
            avatar.setPadding(false);

            Span txtIniciales = new Span(iniciales);
            txtIniciales.getStyle().set("color", "white");
            txtIniciales.getStyle().set("font-weight", "bold");
            txtIniciales.getStyle().set("font-size", "22px");
            avatar.add(txtIniciales);

            lblNombreCompleto =new Span(nombre + " " + apellido);
            lblNombreCompleto.getStyle().set("color", "white");
            lblNombreCompleto.getStyle().set("font-weight", "bold");
            lblNombreCompleto.getStyle().set("font-size", "20px");

            lblCorreo=new Span("Correo: "+correo);
            lblCorreo.getStyle().set("color", "#8fa3bf");

            lblCedula=new Span("Cedula: "+cedula);
            lblCedula.getStyle().set("color", "#8fa3bf");

            lblSueldo=new Span("Sueldo: $"+sueldo);
            lblSueldo.getStyle().set("color", "#8fa3bf");

            txtNombre=new TextField("Nombre");
            txtNombre.setValue(nombre);
            txtNombre.setVisible(false);

            txtApellido = new TextField("Apellido");
            txtApellido.setValue(apellido);
            txtApellido.setVisible(false);

            txtCorreo = new TextField("Correo");
            txtCorreo.setValue(correo);
            txtCorreo.setVisible(false);

            txtCedula = new TextField("Cédula");
            txtCedula.setValue(cedula);
            txtCedula.setVisible(false);

            txtSueldo = new TextField("Sueldo");
            txtSueldo.setValue(String.valueOf(sueldo));
            txtSueldo.setVisible(false);


        }catch (SQLException e){
            add(new Span("Error al conectar a la base de datos: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    // 14. Método auxiliar para activar o desactivar el modo edición de golpe
    private void toggleModoEdicion(boolean editar) {
        // Si editar es true, oculta las etiquetas y muestra las cajas. Si es false, al revés.
        lblNombreCompleto.setVisible(!editar);
        lblCorreo.setVisible(!editar);
        lblCedula.setVisible(!editar);
        lblSueldo.setVisible(!editar);
        btnEditar.setVisible(!editar);

        txtNombre.setVisible(editar);
        txtApellido.setVisible(editar);
        txtCorreo.setVisible(editar);
        txtCedula.setVisible(editar);
        txtSueldo.setVisible(editar);
        btnGuardar.setVisible(editar);
    }
}
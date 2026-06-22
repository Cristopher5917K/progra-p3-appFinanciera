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

    // 1. Declaración de variables globales de la vista
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
        // Configuración de la pantalla completa (Fondo exterior)
        this.setSizeFull();
        this.setAlignItems(Alignment.CENTER);

        // 2. CONTENEDOR PADRE RESPONSIVE (Controla el ancho máximo de la tarjeta)
        VerticalLayout container = new VerticalLayout();
        container.setWidth("100%");
        container.getStyle().set("max-width", "550px");
        container.setPadding(false);

        // 3. CONTENEDOR INTERMEDIO (El que da el fondo compartido a toda la sección)
        VerticalLayout seccionPerfil = new VerticalLayout();
        seccionPerfil.getStyle().set("background-color", "#203a60"); // El azul grisáceo de tu imagen
        seccionPerfil.setWidth("100%");
        seccionPerfil.getStyle().set("border-radius", "15px");
        seccionPerfil.getStyle().set("padding", "25px");

        // 4. BLOQUE CABECERA (Contiene el título)
        VerticalLayout mainTitle = new VerticalLayout();
        mainTitle.setWidth("100%");
        mainTitle.setPadding(false);

        H2 titulo = new H2("Mi Perfil");
        titulo.getStyle().set("color", "white");
        titulo.getStyle().set("margin", "0");
        mainTitle.add(titulo);

        // 5. LA TARJETA DE USUARIO (Contenedor horizontal translúcido)
        HorizontalLayout userCard = new HorizontalLayout();
        userCard.getStyle().set("background-color", "rgba(255, 255, 255, 0.05)"); // Ligera capa blanca semi-transparente
        userCard.getStyle().set("backdrop-filter", "blur(10px)");
        userCard.setWidth("100%");
        userCard.getStyle().set("border-radius", "10px");
        userCard.getStyle().set("padding", "15px");
        userCard.getStyle().set("flex-wrap", "wrap");
        userCard.setAlignItems(Alignment.CENTER); // Centra verticalmente el avatar con los textos

        // Variables locales para almacenar lo que viene de MySQL
        String nombre = "";
        String apellido = "";
        String correo = "";
        String cedula = "";
        double sueldo = 0.0;

        // 6. CONEXIÓN REAL A LA BASE DE DATOS
        try {
            Connection con = DatabaseConnection.getConnection();
            String query = "SELECT nombre, apellido, correo, sueldo, cedula FROM usuarios WHERE id = 1";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                nombre = rs.getString("nombre");
                apellido = rs.getString("apellido");
                correo = rs.getString("correo");
                sueldo = rs.getDouble("sueldo");
                cedula = rs.getString("cedula");
            }
        } catch (SQLException e) {
            add(new Span("Error al conectar a la base de datos: " + e.getMessage()));
            e.printStackTrace();
        }

        // 7. PROCESAR LAS INICIALES PARA EL AVATAR VERDE
        String inicialNombre = (nombre != null && !nombre.isEmpty()) ? nombre.substring(0, 1).toUpperCase() : "";
        String inicialApellido = (apellido != null && !apellido.isEmpty()) ? apellido.substring(0, 1).toUpperCase() : "";
        String iniciales = inicialNombre + inicialApellido;

        // 8. DISEÑO DEL AVATAR CUADRADO VERDE
        VerticalLayout avatar = new VerticalLayout();
        avatar.setWidth("75px");
        avatar.setHeight("75px");
        avatar.getStyle().set("background-color", "#2ecc71"); // El verde brillante de tu imagen
        avatar.getStyle().set("border-radius", "12px");
        avatar.setJustifyContentMode(JustifyContentMode.CENTER);
        avatar.setAlignItems(Alignment.CENTER);
        avatar.setPadding(false);

        Span txtIniciales = new Span(iniciales);
        txtIniciales.getStyle().set("color", "white");
        txtIniciales.getStyle().set("font-weight", "bold");
        txtIniciales.getStyle().set("font-size", "22px");
        avatar.add(txtIniciales);

        // 9. ASIGNACIÓN DE DATOS A COMPONENTES DE MODO LECTURA (Span)
        lblNombreCompleto = new Span(nombre + " " + apellido);
        lblNombreCompleto.getStyle().set("color", "white");
        lblNombreCompleto.getStyle().set("font-weight", "bold");
        lblNombreCompleto.getStyle().set("font-size", "20px");

        lblCorreo = new Span(correo);
        lblCorreo.getStyle().set("color", "#8fa3bf");

        lblCedula = new Span("Cédula: " + cedula);
        lblCedula.getStyle().set("color", "#8fa3bf");

        lblSueldo = new Span("Sueldo: $" + sueldo);
        lblSueldo.getStyle().set("color", "#8fa3bf");

        // 10. ASIGNACIÓN DE DATOS A COMPONENTES DE MODO EDICIÓN (TextField)
        txtNombre = new TextField("Nombre");
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

        // 11. CONFIGURACIÓN DE BOTONES
        btnEditar = new Button("Editar Perfil");
        btnGuardar = new Button("Guardar Cambios");
        btnGuardar.setVisible(false);

        btnEditar.addClickListener(event -> toggleModoEdicion(true));
        btnGuardar.addClickListener(event -> toggleModoEdicion(false));

        // 12. COLUMNA VERTICAL INTERNA PARA LA INFORMACIÓN DEL USUARIO
        VerticalLayout infoUsuario = new VerticalLayout();
        infoUsuario.setPadding(false);
        infoUsuario.setSpacing(true);
        infoUsuario.setWidth("auto"); // Se ajusta al tamaño del texto interno

        // Añadimos tanto las etiquetas como las cajas y botones a este bloque
        infoUsuario.add(lblNombreCompleto, lblCorreo, lblCedula, lblSueldo);
        infoUsuario.add(txtNombre, txtApellido, txtCorreo, txtCedula, txtSueldo);
        infoUsuario.add(new HorizontalLayout(btnEditar, btnGuardar));

        // =========================================================================
        // 13. EL ENSAMBLAJE FINAL EN CAPAS (EL SISTEMA MATRIOSHKA)
        // =========================================================================

        // Metemos el avatar verde (izq) y la info (der) dentro de la tarjeta horizontal
        userCard.add(avatar, infoUsuario);

        // Metemos el bloque del título y el userCard dentro del contenedor con fondo azul
        seccionPerfil.add(mainTitle, userCard);

        // Metemos la sección completa dentro de la caja con tamaño máximo (container)
        container.add(seccionPerfil);

        // Colgamos la caja central en la pantalla web real
        this.add(container);
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
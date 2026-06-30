package org.example.pantallas;

import org.example.backend.Conexiones;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.sql.Connection;
import java.util.Map;

@Route("perfil")
public class PerfilUsuarioView extends VerticalLayout {

    private Integer idUsuario;
    private Connection con;

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
    private Button btnCancelar;

    public PerfilUsuarioView() {
        this.setSizeFull();
        this.setAlignItems(Alignment.CENTER);
        this.setPadding(false);
        this.setSpacing(false);

        VerticalLayout container = new VerticalLayout();
        container.setWidth("100%");
        container.setPadding(false);

        VerticalLayout seccionPerfil = new VerticalLayout();
        seccionPerfil.getStyle().set("background-color", "#203a60");
        seccionPerfil.setWidth("100%");
        seccionPerfil.getStyle().set("border-radius", "15px");
        seccionPerfil.getStyle().set("border-top-left-radius","0px");
        seccionPerfil.getStyle().set("border-top-right-radius","0px");

        VerticalLayout mainTitle = new VerticalLayout();
        mainTitle.setWidth("100%");
        mainTitle.setPadding(false);

        H2 titulo = new H2("Mi Perfil");
        titulo.getStyle().set("color", "white");
        titulo.getStyle().set("margin", "0");
        mainTitle.add(titulo);

        HorizontalLayout userCard = new HorizontalLayout();
        userCard.getStyle().set("background-color", "rgba(255, 255, 255, 0.05)");
        userCard.getStyle().set("backdrop-filter", "blur(10px)");
        userCard.setWidth("100%");
        userCard.getStyle().set("border-radius", "10px");
        userCard.getStyle().set("padding", "15px");
        userCard.getStyle().set("flex-wrap", "wrap");
        userCard.setAlignItems(Alignment.CENTER);

        String nombre="";
        String apellido="";
        String cedula="";
        String correo="";
        double sueldo=0.0;

        try{
            idUsuario = (Integer) com.vaadin.flow.server.VaadinSession.getCurrent().getAttribute("usuarioId");
            if (idUsuario == null) {
                idUsuario = 1;
            }
            con = Conexiones.getConnection();
            Map<String, Object> profile = new Conexiones().userProfileInfo(idUsuario, con);
            
            if (profile != null){
                nombre = (String) profile.get("nombre");
                apellido = (String) profile.get("apellido");
                cedula = (String) profile.get("cedula");
                correo = (String) profile.getOrDefault("correo", "");
                sueldo = (Double) profile.get("sueldo");
            }

            String inicialNombre = (nombre != null && !nombre.isEmpty()) ? nombre.substring(0,1).toUpperCase() : "";
            String inicialApellido = (apellido != null && !apellido.isEmpty()) ? apellido.substring(0,1).toUpperCase() : "";
            String iniciales = inicialNombre + inicialApellido;

            VerticalLayout avatar = new VerticalLayout();
            avatar.setWidth("75px");
            avatar.setHeight("75px");
            avatar.getStyle().set("background-color", "#2ecc71");
            avatar.getStyle().set("border-radius", "12px");
            avatar.setJustifyContentMode(JustifyContentMode.CENTER);
            avatar.setAlignItems(Alignment.CENTER);
            avatar.setPadding(false);

            Span txtIniciales = new Span(iniciales);
            txtIniciales.getStyle().set("color", "white");
            txtIniciales.getStyle().set("font-weight", "bold");
            txtIniciales.getStyle().set("font-size", "22px");
            avatar.add(txtIniciales);

            lblNombreCompleto = new Span(nombre + " " + apellido);
            lblNombreCompleto.getStyle().set("color", "white");
            lblNombreCompleto.getStyle().set("font-weight", "bold");
            lblNombreCompleto.getStyle().set("font-size", "20px");

            lblCorreo = new Span("Correo: " + correo);
            lblCorreo.getStyle().set("color", "#8fa3bf");

            lblCedula = new Span("Cedula: " + cedula);
            lblCedula.getStyle().set("color", "#8fa3bf");

            lblSueldo = new Span("Sueldo: $" + sueldo);
            lblSueldo.getStyle().set("color", "#8fa3bf");

            // TextFields para editar
            txtNombre = new TextField("Nombre");
            txtNombre.setValue(nombre);
            txtNombre.setVisible(false);
            txtNombre.setWidthFull();

            txtApellido = new TextField("Apellido");
            txtApellido.setValue(apellido);
            txtApellido.setVisible(false);
            txtApellido.setWidthFull();

            txtCorreo = new TextField("Correo");
            txtCorreo.setValue(correo);
            txtCorreo.setVisible(false);
            txtCorreo.setWidthFull();

            txtCedula = new TextField("Cédula");
            txtCedula.setValue(cedula);
            txtCedula.setVisible(false);
            txtCedula.setWidthFull();

            txtSueldo = new TextField("Sueldo");
            txtSueldo.setValue(String.valueOf(sueldo));
            txtSueldo.setVisible(false);
            txtSueldo.setWidthFull();

            // Botones
            btnEditar = new Button(VaadinIcon.EDIT.create());
            btnEditar.getStyle().set("border-radius", "50%");
            btnEditar.getStyle().set("width", "40px");
            btnEditar.getStyle().set("height", "40px");
            btnEditar.getStyle().set("min-width", "40px");
            btnEditar.getStyle().set("background-color", "#28a745");
            btnEditar.getStyle().set("color", "white");
            btnEditar.getStyle().set("cursor", "pointer");

            btnGuardar = new Button("✓ Guardar");
            btnGuardar.getStyle().set("background-color", "#28a745");
            btnGuardar.getStyle().set("color", "white");
            btnGuardar.getStyle().set("font-weight", "bold");
            btnGuardar.setVisible(false);
            btnGuardar.setWidthFull();

            btnCancelar = new Button("✕ Cancelar");
            btnCancelar.getStyle().set("background-color", "#dc3545");
            btnCancelar.getStyle().set("color", "white");
            btnCancelar.getStyle().set("font-weight", "bold");
            btnCancelar.setVisible(false);
            btnCancelar.setWidthFull();

            // Listeners
            btnEditar.addClickListener(event -> toggleModoEdicion(true));
            btnGuardar.addClickListener(event -> guardarCambios());
            btnCancelar.addClickListener(event -> toggleModoEdicion(false));

            VerticalLayout infoUsuario = new VerticalLayout();
            infoUsuario.setPadding(false);
            infoUsuario.setSpacing(true);
            infoUsuario.setWidth("100%");

            infoUsuario.add(lblNombreCompleto, lblCorreo, lblCedula, lblSueldo);
            infoUsuario.add(txtNombre, txtApellido, txtCorreo, txtCedula, txtSueldo);
            
            HorizontalLayout botonesAccion = new HorizontalLayout();
            botonesAccion.setSpacing(true);
            botonesAccion.setWidthFull();
            botonesAccion.add(btnGuardar, btnCancelar);
            botonesAccion.setVisible(false);
            
            infoUsuario.add(botonesAccion);
            
            userCard.add(avatar, infoUsuario);
            seccionPerfil.add(mainTitle, userCard, btnEditar);
            container.add(seccionPerfil);
            this.add(container);

        } catch (Exception e) {
            add(new Span("Error al conectar a la base de datos: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    private void toggleModoEdicion(boolean editar) {
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
        btnCancelar.setVisible(editar);
    }

    private void guardarCambios() {
        try {
            String nombre = txtNombre.getValue();
            String apellido = txtApellido.getValue();
            String cedula = txtCedula.getValue();
            double sueldo = Double.parseDouble(txtSueldo.getValue());

            if (nombre.isEmpty() || apellido.isEmpty() || cedula.isEmpty()) {
                Notification.show("⚠️ Completa todos los campos", 3000, Notification.Position.TOP_CENTER);
                return;
            }

            Conexiones db = new Conexiones();
            boolean actualizado = db.updateUserProfile(idUsuario, nombre, apellido, cedula, sueldo, con);

            if (actualizado) {
                Notification.show("✅ Perfil actualizado exitosamente", 3000, Notification.Position.TOP_CENTER);
                lblNombreCompleto.setText(nombre + " " + apellido);
                lblCedula.setText("Cedula: " + cedula);
                lblSueldo.setText("Sueldo: $" + sueldo);
                toggleModoEdicion(false);
            } else {
                Notification.show("❌ Error al guardar cambios", 3000, Notification.Position.TOP_CENTER);
            }
        } catch (NumberFormatException e) {
            Notification.show("❌ El sueldo debe ser un número válido", 3000, Notification.Position.TOP_CENTER);
        } catch (Exception e) {
            Notification.show("❌ Error: " + e.getMessage(), 3000, Notification.Position.TOP_CENTER);
            e.printStackTrace();
        }
    }
}

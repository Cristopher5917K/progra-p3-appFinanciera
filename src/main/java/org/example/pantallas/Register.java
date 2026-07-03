package org.example.pantallas;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.example.backend.Conexiones;

import java.sql.Connection;
import java.sql.SQLException;

@Route("register")
public class Register extends VerticalLayout{
    public void mostrarAdvertencia(String message){
        Notification.show(message, 3000, Notification.Position.TOP_CENTER)
                .addThemeVariants(NotificationVariant.LUMO_WARNING);
    }

    public Register(){
        this.setSpacing(true);
        this.setSizeFull();
        this.setAlignItems(Alignment.CENTER);

        VerticalLayout seccionSuperior = new VerticalLayout();
        seccionSuperior.setWidthFull();
        seccionSuperior.setHeight("45vh");
        seccionSuperior.setPadding(true);
        seccionSuperior.setAlignItems(Alignment.CENTER);
        seccionSuperior.setJustifyContentMode(JustifyContentMode.CENTER);

        seccionSuperior.getStyle().set("background", "linear-gradient(90deg, #2ecc71 0%, #2ec4b6 100%)");
        seccionSuperior.getStyle().set("border-radius", "0 0 50px 50px");
        seccionSuperior.getStyle().set("box-shadow", "0px 4px 15px rgba(0, 0, 0, 0.1)");


        H1 tittle = new H1("Registro");

        H4 subtittle = new H4("Registrate para poder empezar a usar Smart Savings");
        subtittle.setWidthFull();
        subtittle.getStyle().set("text-align", "center");
        subtittle.getStyle().set("margin-bottom", "20px");

        Span id = new Span("Cedula");
        Span name = new Span("Nombre");
        Span lastname= new Span("Apellido");
        Span mail = new Span("Correo electronico");
        Span password = new Span("Contraseña");

        TextField txtfid = new TextField();
        txtfid.setPlaceholder("Cedula");

        TextField txtfname = new TextField();
        txtfname.setPlaceholder("Nombre");

        TextField txtflastname = new TextField();
        txtflastname.setPlaceholder("Apellido");

        TextField txtfmail = new TextField();
        txtfmail.setPlaceholder("Correo");

        TextField txtfpassword = new TextField();
        txtfpassword.setPlaceholder("Contraseña");

        Button btnregister = new Button("Registrarse");
        btnregister.addThemeName("primary");

        Button btnbacklogin = new Button("Volver");
        btnbacklogin.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        seccionSuperior.add(tittle, subtittle);

        add(seccionSuperior, id, txtfid, name, txtfname, lastname, txtflastname, mail, txtfmail, password, txtfpassword, btnregister,btnbacklogin);

        btnbacklogin.addClickListener(event -> {
            getUI().ifPresent(ui -> ui.navigate(""));
        });

        btnregister.addClickListener(event -> {
            Conexiones database = new Conexiones();
            Connection conn = null;
            String cedula = txtfid.getValue();
            String nombre = txtfname.getValue();
            String apellido = txtflastname.getValue();
            String correo = txtfmail.getValue();
            String contrasena = txtfpassword.getValue();

            if (cedula.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || contrasena.isEmpty()){
                mostrarAdvertencia("LLENE TODOS LOS CAMPOS");
                return;
            }

            if (!cedula.matches("\\d{10}")){
                mostrarAdvertencia("CEDULA NO VALIDA, DEBE CONTENER 10 DIGITOS");
                return;
            }

            if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\\\s]+")){
                mostrarAdvertencia("NOMBRE NO PUEDE CONTENER NUMEROS");
                return;
            }

            if (!apellido.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\\\s]+")){
                mostrarAdvertencia("APELLIDO NO PUEDE CONTENER NUMEROS");
                return;
            }

            if (!correo.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]{2,6}$")){
                mostrarAdvertencia("EL FORMATO DE CORREO NO ES VALIDO");
                return;
            }

            if (contrasena.length() < 8){
                mostrarAdvertencia("LA CONTRASEÑA DEBE SER DE MINIMO 8 CARACTERES");
                return;
            }

            try {
                conn = database.getConnection();
                if (database.noRepetir(cedula, conn)){
                    mostrarAdvertencia("CEDULA YA INGRESADA");
                    return;
                }
                database.registerUser(conn, nombre, apellido, correo, contrasena,0.0 , cedula);
                database.registerClient(conn, nombre, apellido, cedula, 0.0, contrasena);
                Notification.show("¡Registro exitoso!", 3000, Notification.Position.TOP_CENTER);
                getUI().ifPresent(ui -> ui.navigate(""));
            } catch (SQLException e){
                e.printStackTrace();
                Notification.show("Error al registrar el usuario.", 3000, Notification.Position.TOP_CENTER);
            }
        });

    }
}
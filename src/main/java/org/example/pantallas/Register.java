package org.example.pantallas;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.example.backend.Conexiones;

import java.sql.Connection;
import java.sql.SQLException;

@Route("register")
public class Register extends VerticalLayout{
    public Register(){
        this.setSpacing(true);
        this.setSizeFull();
        this.setAlignItems(Alignment.CENTER);


        VerticalLayout seccionSuperior = new VerticalLayout();
        seccionSuperior.setWidthFull(); // Ocupa todo el ancho
        seccionSuperior.setHeight("45vh"); // Ocupa el 45% de la altura de la pantalla
        seccionSuperior.setPadding(true);
        seccionSuperior.setAlignItems(Alignment.CENTER);
        seccionSuperior.setJustifyContentMode(JustifyContentMode.CENTER); // Centra logo y textos verticalmente

        // 🎨 ESTILOS CSS PARA LA SECCIÓN SUPERIOR: Degradado azul y forma curva
        seccionSuperior.getStyle().set("background", "linear-gradient(90deg, #2ecc71 0%, #2ec4b6 100%)"); // Degradado de azul oscuro a más claro
        seccionSuperior.getStyle().set("border-radius", "0 0 50px 50px"); // Redondea las esquinas inferiores (la "curva")
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
            getUI().ifPresent(ui -> ui.navigate("login"));
        });

        btnregister.addClickListener(event -> {
            Conexiones database = new Conexiones();
            Connection conn = null;
            String cedula = txtfid.getValue();
            String nombre = txtfname.getValue();
            String apellido = txtflastname.getValue();
            String correo = txtfmail.getValue();
            String contrasena = txtfpassword.getValue();

            //pongang el metodo para registrar usuarios (gays)
            try {
                conn = database.getConnection();
                database.registerUser(conn, nombre, apellido, correo, contrasena,0.0 , cedula);
                database.registerClient(conn, nombre, apellido, cedula, 0.0, contrasena);
                UI.getCurrent().navigate("login");
            } catch (SQLException e){
                e.printStackTrace();
                System.out.println("ERROR AL REGISTRAR");
            }
        });

    }
}

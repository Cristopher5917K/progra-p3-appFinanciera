package org.example.pantallas;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import javax.swing.*;

@Route("login")
public class Login extends VerticalLayout {

    private VerticalLayout areaContenido;

    public Login(){
        this.setSizeFull(); // Ocupa toda la pantalla
        this.setPadding(false); // Sin márgenes externos
        this.setSpacing(true); // Sin espacio entre elementos
        this.setAlignItems(Alignment.CENTER); // Centra todo el contenido
        this.getStyle().set("background-color", "#ffffff"); // Fondo blanco para la parte inferior

        // --- 2. CONTENEDOR SUPERIOR (La sección azul curva) ---
        VerticalLayout seccionSuperior = new VerticalLayout();
        seccionSuperior.setWidthFull(); // Ocupa todo el ancho
        seccionSuperior.setHeight("45vh"); // Ocupa el 45% de la altura de la pantalla
        seccionSuperior.setPadding(true);
        seccionSuperior.setAlignItems(Alignment.CENTER);
        seccionSuperior.setJustifyContentMode(JustifyContentMode.CENTER); // Centra logo y textos verticalmente

        // 🎨 ESTILOS CSS PARA LA SECCIÓN SUPERIOR: Degradado azul y forma curva
        seccionSuperior.getStyle().set("background", "linear-gradient(180deg, #1d3557 0%, #457b9d 100%)"); // Degradado de azul oscuro a más claro
        seccionSuperior.getStyle().set("border-radius", "0 0 50px 50px"); // Redondea las esquinas inferiores (la "curva")
        seccionSuperior.getStyle().set("box-shadow", "0px 4px 15px rgba(0, 0, 0, 0.1)"); // Sombra suave

        // --- 3. LOGO (El cerebro verde) ---
        // Asumiendo que tienes la imagen en 'src/main/resources/META-INF/resources/images/logo.png'
        Image logo = new Image("logo.png", "Logo");
        logo.setWidth("220px"); // Tamaño del logo
        logo.setHeight("150px");
        logo.getStyle().set("margin-bottom", "20px"); // Espacio abajo del logo


        // --- 4. TÍTULOS (En color blanco) ---
        H1 titulo = new H1("Smart Savings");
        titulo.getStyle().set("color", "#ffffff"); // Color blanco
        titulo.getStyle().set("margin", "0"); // Quita márgenes por defecto
        titulo.getStyle().set("font-size", "24px");
        titulo.getStyle().set("font-weight", "bold");

        Span subtitulo = new Span("Gestiona tu dinero con inteligencia");
        subtitulo.getStyle().set("color", "#a8dadc"); // Un azul muy claro para que sea legible
        subtitulo.getStyle().set("font-size", "14px");
        subtitulo.getStyle().set("margin-bottom", "20px");


        // --- 6. AÑADIMOS TODO A LA SECCIÓN SUPERIOR ---
        seccionSuperior.add(logo, titulo, subtitulo);

        // --- 7. AÑADIMOS LA SECCIÓN SUPERIOR AL LOGIN ---

        H3 lbllogin = new H3("Iniciar Sesion");
        H4 lblunderlogin = new H4("Accede a tu cuenta de Smart Savings");
        Span lblmail = new Span("Correo electronico");

        Span lblpassword = new Span("Contraseña");

        Span lbltambien = new Span("---------------O tambien---------------");

        TextField txtflogin = new TextField();
        txtflogin.setPlaceholder("Correo");

        TextField txtfpassword = new TextField();
        txtfpassword.setPlaceholder("contraseña");

        Button btlogin = new Button("Iniciar Sesion");
        btlogin.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button btsigin = new Button("No tienes Cuenta? Registrate");
        btsigin.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        Button btadmin_panel = new Button("Acceso al panel de Administrador");
        btadmin_panel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btadmin_panel.getStyle().set("border", "1.5px solid #d8e2ef");   // Borde delgado gris/azul claro
        btadmin_panel.getStyle().set("border-radius", "20px");


        this.add(seccionSuperior, lbllogin, lblunderlogin, lblmail, txtflogin, lblpassword, txtfpassword, btlogin, btsigin, lbltambien, btadmin_panel);

        btlogin.addClickListener(event -> {
            String correo = txtflogin.getValue();
            String contrasena = txtfpassword.getValue();

            //Ingresen el metodo de la base de datos y manden los vallores

            if(txtflogin.isEmpty() || txtfpassword.isEmpty()){
                getUI().ifPresent(ui -> ui.navigate("noUser"));
            }

        });

        btsigin.addClickListener(event -> {
            getUI().ifPresent(ui -> ui.navigate("register"));
        });

        btadmin_panel.addClickListener(event -> {
            getUI().ifPresent(ui -> ui.navigate(""));
        });
    }

}

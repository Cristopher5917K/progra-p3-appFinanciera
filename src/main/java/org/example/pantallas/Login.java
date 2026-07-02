package org.example.pantallas;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.VaadinSession;
import org.example.backend.Conexiones;
import org.example.info.Cliente;

import java.sql.Connection;
import java.sql.SQLException;

@Route("")
@RouteAlias("login")
public class Login extends VerticalLayout implements BeforeEnterObserver {

    private VerticalLayout areaContenido;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (VaadinSession.getCurrent().getAttribute("usuarioId") != null) {
            event.forwardTo("perfil");
        }
    }

    public Login(){
        this.setSizeFull();
        this.setPadding(false);
        this.setSpacing(true);
        this.setAlignItems(Alignment.CENTER);
        this.getStyle().set("background-color", "#ffffff");

        VerticalLayout seccionSuperior = new VerticalLayout();
        seccionSuperior.setWidthFull();
        seccionSuperior.setHeight("45vh");
        seccionSuperior.setPadding(true);
        seccionSuperior.setAlignItems(Alignment.CENTER);
        seccionSuperior.setJustifyContentMode(JustifyContentMode.CENTER);

        seccionSuperior.getStyle().set("background", "linear-gradient(180deg, #1d3557 0%, #457b9d 100%)");
        seccionSuperior.getStyle().set("border-radius", "0 0 50px 50px");
        seccionSuperior.getStyle().set("box-shadow", "0px 4px 15px rgba(0, 0, 0, 0.1)");

        Image logo = new Image("logo.png", "Logo");
        logo.setWidth("220px");
        logo.setHeight("120px");
        logo.getStyle().set("border-radius", "12px");
        logo.getStyle().set("margin-bottom", "20px");

        H1 titulo = new H1("Smart Savings");
        titulo.getStyle().set("color", "#ffffff");
        titulo.getStyle().set("margin", "0");
        titulo.getStyle().set("font-size", "24px");
        titulo.getStyle().set("font-weight", "bold");

        Span subtitulo = new Span("Gestiona tu dinero con inteligencia");
        subtitulo.getStyle().set("color", "#a8dadc");
        subtitulo.getStyle().set("font-size", "14px");
        subtitulo.getStyle().set("margin-bottom", "20px");

        seccionSuperior.add(logo, titulo, subtitulo);

        H3 lbllogin = new H3("Iniciar Sesion");
        H4 lblunderlogin = new H4("Accede a tu cuenta de Smart Savings");
        Span lblmail = new Span("Correo electronico");

        Span lblpassword = new Span("Contraseña");

        Span lbltambien = new Span("---------------O tambien---------------");

        TextField txtflogin = new TextField();
        txtflogin.setPlaceholder("Correo");

        TextField txtfpassword = new TextField();
        txtfpassword.setPlaceholder("Contraseña");

        Button btlogin = new Button("Iniciar Sesion");
        btlogin.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button btsigin = new Button("No tienes Cuenta? Registrate");
        btsigin.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        Button btadmin_panel = new Button("Acceso al panel de Administrador");
        btadmin_panel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btadmin_panel.getStyle().set("border", "1.5px solid #d8e2ef");
        btadmin_panel.getStyle().set("border-radius", "20px");


        this.add(seccionSuperior, lbllogin, lblunderlogin, lblmail, txtflogin, lblpassword, txtfpassword, btlogin, btsigin, lbltambien, btadmin_panel);

        btlogin.addClickListener(event -> {
            Conexiones database = new Conexiones();
            Connection conn = null;
            Cliente user = null;
            String correo = txtflogin.getValue();
            String contrasena = txtfpassword.getValue();

            try {
                conn = database.getConnection();
                user =  database.userLogin(correo, contrasena, conn);
                if (user != null){
                    VaadinSession.getCurrent().setAttribute("usuarioId", user.getIdCliente());
                    getUI().ifPresent(ui -> ui.navigate("perfil"));
                } else {
                    getUI().ifPresent(ui -> ui.navigate("noUser"));
                }
            } catch (SQLException e){
                e.printStackTrace();
                System.out.println("NO SE LOGRO CONECTAR A LA BASE DE DATOS");
            }

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
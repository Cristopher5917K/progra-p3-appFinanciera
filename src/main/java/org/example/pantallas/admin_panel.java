package org.example.pantallas;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.icon.Icon;

import javax.swing.*;

@Route("")

public class admin_panel extends VerticalLayout {

    private VerticalLayout areaContenido;

    public admin_panel() {
        // Un título web principal (Equivalente a un <h1> en HTML)
        H1 titulo = new H1("Panel de Control");
        titulo.setWidthFull();
        titulo.getStyle().set("text-align", "center");


        H2 subtitulo = new H2("Administrador");
        subtitulo.setWidthFull();
        subtitulo.getStyle().set("text-align", "center");


        //botones nav
        Button btn_dashboard = new Button("Dashboard");
        btn_dashboard.addThemeName("primary");

        Button btn_plantilla = new Button("Plantilla");
        btn_plantilla.addThemeName("primary");

        Button btn_usuarios = new Button("Usuarios");
        btn_usuarios.addThemeName("primary");

        Button btn_alertas = new Button("Alertas");
        btn_alertas.addThemeName("primary");


        //Alinear todo los elementos
        HorizontalLayout menu_admin = new HorizontalLayout();
        menu_admin.setWidthFull();

// 2. LA CLAVE: Distribuye los botones de forma equitativa a lo largo de todo ese ancho
        menu_admin.setJustifyContentMode(JustifyContentMode.BETWEEN);



// Si quieres que no queden pegados al borde de la pantalla de la Mac, activa el padding
        menu_admin.setPadding(true);
        menu_admin.add(btn_dashboard, btn_plantilla, btn_usuarios, btn_alertas);
        menu_admin.setFlexGrow(1, btn_dashboard);
        menu_admin.setFlexGrow(1, btn_plantilla);
        menu_admin.setFlexGrow(1, btn_usuarios);
        menu_admin.setFlexGrow(1, btn_alertas);

        areaContenido = new VerticalLayout();
        areaContenido.setWidthFull();
        areaContenido.setAlignItems(Alignment.CENTER);

        dashboard();

        // Añadimos los componentes a la vista vertical
        add(titulo, subtitulo, menu_admin, areaContenido);


        btn_dashboard.addClickListener(e -> dashboard());
        btn_plantilla.addClickListener(event -> plantillas());
        btn_usuarios.addClickListener(event -> usuarios());
        btn_alertas.addClickListener(event -> alertas());
    }

    private void dashboard(){
        areaContenido.removeAll();

        VerticalLayout tarjeta = new VerticalLayout();
        tarjeta.setWidth("700px"); // Le asignas un ancho fijo adecuado
        tarjeta.setHeight("220px");
        tarjeta.setPadding(true);
        tarjeta.setSpacing(true);

// 🎨 LE DAMOS EL ESTILO DE LA IMAGEN (Esquinas redondeadas, fondo y sombra)
        tarjeta.getStyle().set("background-color", "#ffffff"); // Fondo blanco
        tarjeta.getStyle().set("border-radius", "24px");       // Bordes ultra redondeados
        tarjeta.getStyle().set("box-shadow", "0px 4px 12px rgba(0, 0, 0, 0.05)"); // Sombra suave

// 2. El Icono (Usamos los iconos nativos de Vaadin)
// Para el círculo azul claro que envuelve al icono:
        VerticalLayout circuloIcono = new VerticalLayout();
        circuloIcono.setWidth("50px");
        circuloIcono.setHeight("50px");
        circuloIcono.setPadding(false);
        circuloIcono.setJustifyContentMode(JustifyContentMode.CENTER);
        circuloIcono.setAlignItems(Alignment.CENTER);
        circuloIcono.getStyle().set("background-color", "#e8f0fe"); // Azul claro de fondo
        circuloIcono.getStyle().set("border-radius", "50%");         // Lo hace un círculo perfecto

        Icon iconoUsuario = VaadinIcon.USERS.create();
        iconoUsuario.setColor("#1a73e8"); // Color azul del icono de la imagen
        circuloIcono.add(iconoUsuario);

// 3. Los Textos (Usando Span y H2)
        Span txtTitulo = new Span("Usuarios activos");
        txtTitulo.getStyle().set("color", "#80868b"); // Gris suave
        txtTitulo.getStyle().set("font-size", "16px");

        H2 txtNumero = new H2("1,248");
        txtNumero.getStyle().set("margin", "0"); // Quita márgenes por defecto para que no se mueva
        txtNumero.getStyle().set("color", "#0a2540"); // Azul oscuro/negro premium
        txtNumero.getStyle().set("font-size", "36px");
        txtNumero.getStyle().set("font-weight", "bold");

        Span txtPorcentaje = new Span("+8.3% este mes");
        txtPorcentaje.getStyle().set("color", "#137333"); // Verde de éxito
        txtPorcentaje.getStyle().set("font-weight", "bold");
        txtPorcentaje.getStyle().set("font-size", "14px");

// 4. Juntamos todo dentro de la tarjeta
        tarjeta.add(circuloIcono, txtTitulo, txtNumero, txtPorcentaje);

        VerticalLayout tarjeta2 = new VerticalLayout();
        tarjeta2.setWidth("700px"); // Le asignas un ancho fijo adecuado
        tarjeta2.setHeight("220px");
        tarjeta2.setPadding(true);
        tarjeta2.setSpacing(true);

// 🎨 LE DAMOS EL ESTILO DE LA IMAGEN (Esquinas redondeadas, fondo y sombra)
        tarjeta2.getStyle().set("background-color", "#ffffff"); // Fondo blanco
        tarjeta2.getStyle().set("border-radius", "24px");       // Bordes ultra redondeados
        tarjeta2.getStyle().set("box-shadow", "0px 4px 12px rgba(0, 0, 0, 0.05)"); // Sombra suave

// 2. El Icono (Usamos los iconos nativos de Vaadin)
// Para el círculo azul claro que envuelve al icono:
        VerticalLayout circuloIcono2 = new VerticalLayout();
        circuloIcono.setWidth("50px");
        circuloIcono.setHeight("50px");
        circuloIcono.setPadding(false);
        circuloIcono.setJustifyContentMode(JustifyContentMode.CENTER);
        circuloIcono.setAlignItems(Alignment.CENTER);
        circuloIcono.getStyle().set("background-color", "#e8f0fe"); // Azul claro de fondo
        circuloIcono.getStyle().set("border-radius", "50%");         // Lo hace un círculo perfecto

        Icon iconoUsuario2 = VaadinIcon.BELL.create();
        iconoUsuario2.setColor("#f59e0b");
        circuloIcono2.add(iconoUsuario2);

// 3. Los Textos (Usando Span y H2)
        Span txtTitulo2 = new Span("Alertas Enviadas");
        txtTitulo.getStyle().set("color", "#80868b"); // Gris suave
        txtTitulo.getStyle().set("font-size", "16px");

        H2 txtNumero2 = new H2("386");
        txtNumero.getStyle().set("margin", "0"); // Quita márgenes por defecto para que no se mueva
        txtNumero.getStyle().set("color", "#0a2540"); // Azul oscuro/negro premium
        txtNumero.getStyle().set("font-size", "36px");
        txtNumero.getStyle().set("font-weight", "bold");

        Span txtPorcentaje2 = new Span("+12% este mes");
        txtPorcentaje.getStyle().set("color", "#137333"); // Verde de éxito
        txtPorcentaje.getStyle().set("font-weight", "bold");
        txtPorcentaje.getStyle().set("font-size", "14px");

// 4. Juntamos todo dentro de la tarjeta
        tarjeta2.add(circuloIcono2, txtTitulo2, txtNumero2, txtPorcentaje2);

        VerticalLayout tarjeta3 = new VerticalLayout();
        tarjeta3.setWidth("700px"); // Le asignas un ancho fijo adecuado
        tarjeta3.setHeight("220px");
        tarjeta3.setPadding(true);
        tarjeta3.setSpacing(true);

// 🎨 LE DAMOS EL ESTILO DE LA IMAGEN (Esquinas redondeadas, fondo y sombra)
        tarjeta3.getStyle().set("background-color", "#ffffff"); // Fondo blanco
        tarjeta3.getStyle().set("border-radius", "24px");       // Bordes ultra redondeados
        tarjeta3.getStyle().set("box-shadow", "0px 4px 12px rgba(0, 0, 0, 0.05)"); // Sombra suave

// 2. El Icono (Usamos los iconos nativos de Vaadin)
// Para el círculo azul claro que envuelve al icono:
        VerticalLayout circuloIcono3 = new VerticalLayout();
        circuloIcono3.setWidth("50px");
        circuloIcono3.setHeight("50px");
        circuloIcono3.setPadding(false);
        circuloIcono3.setJustifyContentMode(JustifyContentMode.CENTER);
        circuloIcono3.setAlignItems(Alignment.CENTER);
        circuloIcono3.getStyle().set("background-color", "#e8f0fe"); // Azul claro de fondo
        circuloIcono3.getStyle().set("border-radius", "50%");         // Lo hace un círculo perfecto

        Icon iconoUsuario3 = VaadinIcon.TAG.create();
        iconoUsuario3.setColor("#875aef"); // Color azul del icono de la imagen
        circuloIcono3.add(iconoUsuario3);

// 3. Los Textos (Usando Span y H2)
        Span txtTitulo3 = new Span("Usuarios activos");
        txtTitulo3.getStyle().set("color", "#80868b"); // Gris suave
        txtTitulo3.getStyle().set("font-size", "16px");

        H2 txtNumero3 = new H2("1,248");
        txtNumero3.getStyle().set("margin", "0"); // Quita márgenes por defecto para que no se mueva
        txtNumero3.getStyle().set("color", "#0a2540"); // Azul oscuro/negro premium
        txtNumero3.getStyle().set("font-size", "36px");
        txtNumero3.getStyle().set("font-weight", "bold");

        Span txtPorcentaje3 = new Span("+8.3% este mes");
        txtPorcentaje3.getStyle().set("color", "#137333"); // Verde de éxito
        txtPorcentaje3.getStyle().set("font-weight", "bold");
        txtPorcentaje3.getStyle().set("font-size", "14px");

// 4. Juntamos todo dentro de la tarjeta
        tarjeta3.add(circuloIcono3, txtTitulo3, txtNumero3, txtPorcentaje3);

        VerticalLayout tarjeta4 = new VerticalLayout();
        tarjeta4.setWidth("700px"); // Le asignas un ancho fijo adecuado
        tarjeta4.setHeight("220px");
        tarjeta4.setPadding(true);
        tarjeta4.setSpacing(true);

// 🎨 LE DAMOS EL ESTILO DE LA IMAGEN (Esquinas redondeadas, fondo y sombra)
        tarjeta4.getStyle().set("background-color", "#ffffff"); // Fondo blanco
        tarjeta4.getStyle().set("border-radius", "24px");       // Bordes ultra redondeados
        tarjeta4.getStyle().set("box-shadow", "0px 4px 12px rgba(0, 0, 0, 0.05)"); // Sombra suave

// 2. El Icono (Usamos los iconos nativos de Vaadin)
// Para el círculo azul claro que envuelve al icono:
        VerticalLayout circuloIcono4 = new VerticalLayout();
        circuloIcono4.setWidth("50px");
        circuloIcono4.setHeight("50px");
        circuloIcono4.setPadding(false);
        circuloIcono4.setJustifyContentMode(JustifyContentMode.CENTER);
        circuloIcono4.setAlignItems(Alignment.CENTER);
        circuloIcono4.getStyle().set("background-color", "#e8f0fe"); // Azul claro de fondo
        circuloIcono4.getStyle().set("border-radius", "50%");         // Lo hace un círculo perfecto

        Icon iconoUsuario4 = VaadinIcon.TRENDING_UP.create();
        iconoUsuario4.setColor("#27ae60"); // Color azul del icono de la imagen
        circuloIcono4.add(iconoUsuario4);

// 3. Los Textos (Usando Span y H2)
        Span txtTitulo4 = new Span("Usuarios activos");
        txtTitulo4.getStyle().set("color", "#80868b"); // Gris suave
        txtTitulo4.getStyle().set("font-size", "16px");

        H2 txtNumero4 = new H2("1,248");
        txtNumero4.getStyle().set("margin", "0"); // Quita márgenes por defecto para que no se mueva
        txtNumero4.getStyle().set("color", "#0a2540"); // Azul oscuro/negro premium
        txtNumero4.getStyle().set("font-size", "36px");
        txtNumero4.getStyle().set("font-weight", "bold");

        Span txtPorcentaje4 = new Span("+8.3% este mes");
        txtPorcentaje4.getStyle().set("color", "#137333"); // Verde de éxito
        txtPorcentaje4.getStyle().set("font-weight", "bold");
        txtPorcentaje4.getStyle().set("font-size", "14px");

// 4. Juntamos todo dentro de la tarjeta
        tarjeta4.add(circuloIcono4, txtTitulo4, txtNumero4, txtPorcentaje4);

        HorizontalLayout filaTarjetas = new HorizontalLayout();
        filaTarjetas.setWidthFull();
        filaTarjetas.setPadding(true);
        filaTarjetas.setSpacing(true);

        HorizontalLayout filaTarjetas2 = new HorizontalLayout();
        filaTarjetas2.setWidthFull();
        filaTarjetas2.setPadding(true);
        filaTarjetas2.setSpacing(true);

        filaTarjetas.add(tarjeta, tarjeta2);
        filaTarjetas2.add(tarjeta3, tarjeta4);

        areaContenido.add( filaTarjetas, filaTarjetas2);
    }

    private void plantillas(){
        areaContenido.removeAll();
    }

    private void usuarios(){
        areaContenido.removeAll();
    }

    private void alertas(){
        areaContenido.removeAll();
    }
}
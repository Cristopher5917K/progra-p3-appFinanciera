package org.example.pantallas;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.icon.Icon;

@Route("admin")
public class AdminPanel extends VerticalLayout {

    private final VerticalLayout areaContenido;

    public AdminPanel() {
        this.setPadding(true);
        this.setSpacing(true);
        this.setAlignItems(Alignment.CENTER);

        H1 titulo = new H1("Panel de Control");
        titulo.setWidthFull();
        titulo.getStyle().set("text-align", "center");
        titulo.getStyle().set("margin-bottom", "0");

        H2 subtitulo = new H2("Administrador");
        subtitulo.setWidthFull();
        subtitulo.getStyle().set("text-align", "center");
        subtitulo.getStyle().set("margin-top", "5px");

        // 🔘 BOTONES DE NAVEGACIÓN (Solo quedan los 3 solicitados)
        Button btn_dashboard = new Button("Dashboard");
        btn_dashboard.addThemeName("primary");

        Button btn_usuarios = new Button("Usuarios");
        btn_usuarios.addThemeName("primary");

        Button btn_alertas = new Button("Alertas");
        btn_alertas.addThemeName("primary");

        HorizontalLayout menu_admin = new HorizontalLayout();
        menu_admin.setWidth("100%");
        menu_admin.setMaxWidth("600px"); // Evita que se estire de forma exagerada en Mac
        menu_admin.setJustifyContentMode(JustifyContentMode.CENTER);
        menu_admin.getStyle().set("gap", "10px");

        menu_admin.getStyle().set("flex-wrap", "wrap");

        menu_admin.add(btn_dashboard, btn_usuarios, btn_alertas);


        menu_admin.setFlexGrow(1, btn_dashboard);
        menu_admin.setFlexGrow(1, btn_usuarios);
        menu_admin.setFlexGrow(1, btn_alertas);

        areaContenido = new VerticalLayout();
        areaContenido.setWidthFull();
        areaContenido.setPadding(false);
        areaContenido.setAlignItems(Alignment.CENTER);

        dashboard();

        add(titulo, subtitulo, menu_admin, areaContenido);


        btn_dashboard.addClickListener(e -> dashboard());
        btn_usuarios.addClickListener(event -> usuarios());
        btn_alertas.addClickListener(event -> alertas());
    }

    private void dashboard() {
        areaContenido.removeAll();

        VerticalLayout tarjeta = configurarEstiloTarjeta();
        tarjeta.add(crearBloqueIcono(VaadinIcon.USERS, "#1a73e8"), new Span("Usuarios activos"), new H2("1,248"), new Span("+8.3% este mes"));

        VerticalLayout tarjeta2 = configurarEstiloTarjeta();
        tarjeta2.add(crearBloqueIcono(VaadinIcon.BELL, "#f59e0b"), new Span("Alertas Enviadas"), new H2("386"), new Span("+12% este mes"));

        VerticalLayout tarjeta3 = configurarEstiloTarjeta();
        tarjeta3.add(crearBloqueIcono(VaadinIcon.TAG, "#875aef"), new Span("Usuarios activos"), new H2("1,248"), new Span("+8.3% este mes"));

        VerticalLayout tarjeta4 = configurarEstiloTarjeta();
        tarjeta4.add(crearBloqueIcono(VaadinIcon.TRENDING_UP, "#27ae60"), new Span("Usuarios activos"), new H2("1,248"), new Span("+8.3% este mes"));


        HorizontalLayout filaTarjetas = new HorizontalLayout(tarjeta, tarjeta2);
        filaTarjetas.setWidthFull();
        filaTarjetas.getStyle().set("flex-wrap", "wrap");
        filaTarjetas.setJustifyContentMode(JustifyContentMode.CENTER);

        HorizontalLayout filaTarjetas2 = new HorizontalLayout(tarjeta3, tarjeta4);
        filaTarjetas2.setWidthFull();
        filaTarjetas2.getStyle().set("flex-wrap", "wrap");
        filaTarjetas2.setJustifyContentMode(JustifyContentMode.CENTER);

        areaContenido.add(filaTarjetas, filaTarjetas2);
    }

    private void usuarios() {
        areaContenido.removeAll();

        VerticalLayout seccionUsuarios = new VerticalLayout();
        seccionUsuarios.setPadding(true);
        seccionUsuarios.setSpacing(true);
        seccionUsuarios.getStyle().set("gap", "15px");
        seccionUsuarios.setWidth("100%");
        seccionUsuarios.setMaxWidth("650px");

        HorizontalLayout encabezado = new HorizontalLayout();
        encabezado.setWidthFull();
        encabezado.setJustifyContentMode(JustifyContentMode.BETWEEN);
        encabezado.setAlignItems(Alignment.CENTER);

        H3 tituloSeccion = new H3("Usuarios registrados");
        tituloSeccion.getStyle().set("color", "#0a2540");
        tituloSeccion.getStyle().set("margin", "0");
        tituloSeccion.getStyle().set("font-size", "18px");

        // invoca la base de datos
        int totalBD = 0;

        Span txtTotal = new Span(totalBD + " total");
        txtTotal.getStyle().set("background-color", "#e8f0fe");
        txtTotal.getStyle().set("color", "#1a73e8");
        txtTotal.getStyle().set("font-weight", "bold");
        txtTotal.getStyle().set("padding", "6px 14px");
        txtTotal.getStyle().set("border-radius", "15px");

        encabezado.add(tituloSeccion, txtTotal);
        seccionUsuarios.add(encabezado);

        // 🔄 El bucle esperando tu clase de MySQL (Arreglado el cierre de llaves)
        // aqui emte el metodo de la base de datos
        for (Object usuarioObjeto : new Object[]{} ) {

            String nombre = "Nombre de la BD";//cambio estos con el rs.getstring o coom hayas hecho
            String apellido = "Apellido de la BD";
            String correo = "correo@ejemplo.com";
            double saldo = 25000.00;
            boolean estaActivo = true;

            String iniciales = "US";
            if (nombre != null && !nombre.isEmpty()) {
                iniciales = nombre.substring(0, Math.min(nombre.length(), 2)).toUpperCase();
            }

            String nombreCompleto = nombre + " " + apellido;
            String saldoFormateado = "$" + String.format("%.2f", saldo);
            String estadoTexto = estaActivo ? "Activo" : "Inactivo";
            String colorEstado = estaActivo ? "#2ecc71" : "#94a3b8";

            seccionUsuarios.add(crearFilaUsuario(iniciales, nombreCompleto, correo, saldoFormateado, estadoTexto, colorEstado));
        }
        areaContenido.add(seccionUsuarios);
    }

    private void alertas() {
        areaContenido.removeAll();

        VerticalLayout seccionAlertas = new VerticalLayout();
        seccionAlertas.setPadding(true);
        seccionAlertas.setSpacing(true);
        seccionAlertas.getStyle().set("gap", "15px");
        seccionAlertas.setWidth("100%");
        seccionAlertas.setMaxWidth("650px");

        HorizontalLayout encabezado = new HorizontalLayout();
        encabezado.setWidthFull();
        encabezado.setJustifyContentMode(JustifyContentMode.BETWEEN);
        encabezado.setAlignItems(Alignment.CENTER);

        H3 tituloSeccion = new H3("Alertas del sistema");
        tituloSeccion.getStyle().set("color", "#0a2540");
        tituloSeccion.getStyle().set("margin", "0");
        tituloSeccion.getStyle().set("font-size", "18px");

        // aqui mete el el metodo para traer la base de datos
        int alertasActivasBD = 7;

        Span txtActivas = new Span(alertasActivasBD + " activas");
        txtActivas.getStyle().set("background-color", "#fde8e8");
        txtActivas.getStyle().set("color", "#e74c3c");
        txtActivas.getStyle().set("font-weight", "bold");
        txtActivas.getStyle().set("padding", "6px 14px");
        txtActivas.getStyle().set("border-radius", "15px");
        txtActivas.getStyle().set("font-size", "13px");

        encabezado.add(tituloSeccion, txtActivas);
        seccionAlertas.add(encabezado);

        // 🔄 Bucle listo para mapear las alertas de tu base de datos clásica
        //aqui va igual las alertas, no creo que hay alertas por eso le deje puestas unas con print por si las dudas
        //esta hehoc con ia bro, no sabia como hacer esto dinamico xd

        for (Object alertaObjeto : new Object[]{} ) {
            String mensaje = "Alerta de la BD";
            String usuarioAsociado = "Nombre Usuario";
            String tiempo = "Hace un momento";
            String tipoAlerta = "peligro"; // peligro, advertencia o info

            String colorFondoIcono = "#fde8e8";
            String colorIcono = "#e74c3c";

            if (tipoAlerta.equals("advertencia")) {
                colorFondoIcono = "#fef3c7";
                colorIcono = "#f59e0b";
            } else if (tipoAlerta.equals("info")) {
                colorFondoIcono = "#e8f0fe";
                colorIcono = "#1a73e8";
            }

            String subtituloAlerta = usuarioAsociado + " • " + tiempo;
            seccionAlertas.add(crearFilaAlerta(mensaje, subtituloAlerta, colorFondoIcono, colorIcono));
        }

        seccionAlertas.add(crearFilaAlerta("Usuario sobre presupuesto en Transporte", "Ana García • Hace 5 min", "#fde8e8", "#e74c3c"));
        seccionAlertas.add(crearFilaAlerta("3 metas sin aporte en +30 días", "Roberto Torres • Hace 1 hora", "#fef3c7", "#f59e0b"));
        seccionAlertas.add(crearFilaAlerta("Nueva meta creada: Fondo Educación", "Sofía Ramírez • Hace 2 horas", "#e8f0fe", "#1a73e8"));
        seccionAlertas.add(crearFilaAlerta("Gasto en Entretenimiento +80% mensual", "Carlos López • Hace 3 horas", "#fde8e8", "#e74c3c"));

        areaContenido.add(seccionAlertas);
    }

    private HorizontalLayout crearFilaUsuario(String iniciales, String nombre, String correo, String monto, String estado, String colorEstado) {
        HorizontalLayout fila = new HorizontalLayout();
        fila.setWidthFull();
        fila.setAlignItems(Alignment.CENTER);
        fila.setJustifyContentMode(JustifyContentMode.BETWEEN);

        fila.getStyle().set("background-color", "#ffffff");
        fila.getStyle().set("border-radius", "16px");
        fila.getStyle().set("padding", "12px 15px");
        fila.getStyle().set("box-shadow", "0px 4px 12px rgba(0, 0, 0, 0.02)");

        HorizontalLayout bloqueIzquierdo = new HorizontalLayout();
        bloqueIzquierdo.setAlignItems(Alignment.CENTER);
        bloqueIzquierdo.getStyle().set("gap", "10px");

        VerticalLayout avatar = new VerticalLayout();
        avatar.setWidth("40px");
        avatar.setHeight("40px");
        avatar.setPadding(false);
        avatar.setJustifyContentMode(JustifyContentMode.CENTER);
        avatar.setAlignItems(Alignment.CENTER);
        avatar.getStyle().set("background-color", "#1d3557");
        avatar.getStyle().set("border-radius", "50%");

        Span txtIniciales = new Span(iniciales);
        txtIniciales.getStyle().set("color", "#ffffff");
        txtIniciales.getStyle().set("font-weight", "bold");
        avatar.add(txtIniciales);

        VerticalLayout infoTexto = new VerticalLayout();
        infoTexto.setPadding(false);
        infoTexto.setSpacing(false);

        Span txtNombre = new Span(nombre);
        txtNombre.getStyle().set("color", "#0a2540");
        txtNombre.getStyle().set("font-weight", "bold");
        txtNombre.getStyle().set("font-size", "14px");

        Span txtCorreo = new Span(correo);
        txtCorreo.getStyle().set("color", "#94a3b8");
        txtCorreo.getStyle().set("font-size", "11px");

        infoTexto.add(txtNombre, txtCorreo);
        bloqueIzquierdo.add(avatar, infoTexto);

        VerticalLayout infoFinanzas = new VerticalLayout();
        infoFinanzas.setPadding(false);
        infoFinanzas.setSpacing(false);
        infoFinanzas.setAlignItems(FlexComponent.Alignment.END);

        Span txtMonto = new Span(monto);
        txtMonto.getStyle().set("color", "#2ecc71");
        txtMonto.getStyle().set("font-weight", "bold");
        txtMonto.getStyle().set("font-size", "14px");

        Span txtEstado = new Span(estado);
        txtEstado.getStyle().set("color", colorEstado);
        txtEstado.getStyle().set("font-size", "11px");

        infoFinanzas.add(txtMonto, txtEstado);
        fila.add(bloqueIzquierdo, infoFinanzas);

        return fila;
    }

    private HorizontalLayout crearFilaAlerta(String mensaje, String detalle, String colorFondoIcono, String colorIcono) {
        HorizontalLayout fila = new HorizontalLayout();
        fila.setWidthFull();
        fila.setAlignItems(Alignment.CENTER);
        fila.getStyle().set("background-color", "#ffffff");
        fila.getStyle().set("border-radius", "16px");
        fila.getStyle().set("padding", "15px 20px");
        fila.getStyle().set("box-shadow", "0px 4px 12px rgba(0, 0, 0, 0.02)");
        fila.getStyle().set("gap", "15px");

        VerticalLayout circulo = new VerticalLayout();
        circulo.setWidth("40px");
        circulo.setHeight("40px");
        circulo.setPadding(false);
        circulo.setJustifyContentMode(JustifyContentMode.CENTER);
        circulo.setAlignItems(Alignment.CENTER);
        circulo.getStyle().set("background-color", colorFondoIcono);
        circulo.getStyle().set("border-radius", "50%");

        Icon icono = VaadinIcon.EXCLAMATION_CIRCLE_O.create();
        icono.setColor(colorIcono);
        icono.setSize("18px");
        circulo.add(icono);

        VerticalLayout textos = new VerticalLayout();
        textos.setPadding(false);
        textos.setSpacing(false);

        Span txtMensaje = new Span(mensaje);
        txtMensaje.getStyle().set("color", "#0a2540");
        txtMensaje.getStyle().set("font-weight", "bold");
        txtMensaje.getStyle().set("font-size", "14px");

        Span txtDetalle = new Span(detalle);
        txtDetalle.getStyle().set("color", "#94a3b8");
        txtDetalle.getStyle().set("font-size", "12px");

        textos.add(txtMensaje, txtDetalle);
        fila.add(circulo, textos);

        return fila;
    }

    private VerticalLayout configurarEstiloTarjeta() {
        VerticalLayout t = new VerticalLayout();
        t.setWidth("90%");
        t.setMaxWidth("330px");
        t.setPadding(true);
        t.getStyle().set("background-color", "#ffffff");
        t.getStyle().set("border-radius", "24px");
        t.getStyle().set("box-shadow", "0px 4px 12px rgba(0, 0, 0, 0.05)");
        return t;
    }

    private VerticalLayout crearBloqueIcono(VaadinIcon vIcon, String colorHex) {
        VerticalLayout c = new VerticalLayout();
        c.setWidth("45px");
        c.setHeight("45px");
        c.setPadding(false);
        c.setJustifyContentMode(JustifyContentMode.CENTER);
        c.setAlignItems(Alignment.CENTER);
        c.getStyle().set("background-color", "#e8f0fe");
        c.getStyle().set("border-radius", "50%");
        Icon ico = vIcon.create();
        ico.setColor(colorHex);
        c.add(ico);
        return c;
    }
}

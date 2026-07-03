package org.example.pantallas;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.component.dialog.Dialog;
import org.example.backend.Conexiones;
import org.example.backend.DataStructures;
import org.example.info.Cliente;
import org.example.info.Movimientos;
import org.example.info.Reporte;
import org.example.info.Meta;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeSet;


@Route("dashboard")
public class Dashboard extends VerticalLayout implements BeforeEnterObserver {

    private Integer idUsuario;
    private Cliente user;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        idUsuario = (Integer) VaadinSession.getCurrent().getAttribute("usuarioId");
        if (idUsuario == null) {
            event.rerouteTo("");
        } else {
            try (Connection conn = Conexiones.getConnection()) {
                user = new Conexiones().userInfoById(idUsuario, conn);
                if (user == null) {
                    event.rerouteTo("");
                } else {
                    createUI();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                add(new Span("Error al conectar con la base de datos."));
            }
        }
    }

    private void createUI(){
        removeAll();
        setSizeUndefined();
        setWidthFull();
        getStyle().set("background-color", "#F8F9FA");
        getStyle().set("margin", "0 auto");
        getStyle().set("padding-bottom", "60px");
        setPadding(false);
        setSpacing(false);

        if (user.getInitialSalary() <= 0){
            actualizarSueldo(user);
            return;
        }

        VerticalLayout mainContainer = new VerticalLayout();
        mainContainer.setPadding(true);
        mainContainer.setSpacing(true);

        // Fetch movements and totals for the user
        List<Movimientos> movements = new ArrayList<>();
        double totalGastos = 0.0;
        double totalMetas = 0.0;
        try (Connection conn = Conexiones.getConnection()) {
            Conexiones database = new Conexiones();
            List<Movimientos> ingresos = database.movements("INGRESO", user.getIdCliente(), conn);
            List<Movimientos> gastos = database.movements("GASTO", user.getIdCliente(), conn);
            if (ingresos != null) movements.addAll(ingresos);
            if (gastos != null) movements.addAll(gastos);
            totalGastos = database.sumarGastosDelMes(user.getIdCliente(), conn);
            totalMetas = database.sumarMetas(user.getIdCliente(), conn);
            if (totalMetas <= 0){
                actualizarMeta(user);
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mainContainer.add(
                generateScroll(user, movements),
                generatePercentage(user.getInitialSalary(), totalMetas),
                recommendationsCards(user.getInitialSalary(), totalGastos),
                crearTarjetasMovimientos()
        );


        add(
                headerUser(user),
                mainContainer,
                navigationBar()
        );

    }

    private Component headerUser(Cliente user){
        LocalDate time = LocalDate.now();
        DateTimeFormatter date = DateTimeFormatter.ofPattern("dd-MMMM-yyyy");
        String formato = time.format(date).toUpperCase();

        VerticalLayout headerInfo = new VerticalLayout();
        headerInfo.setWidthFull();
        headerInfo.getStyle().set("border-radius", "0 0  12px 12px");
        headerInfo.getStyle().set("background", "linear-gradient(160deg, #0D2B55 0%, #1a4a8a 100%)");

        Span dateText = new Span(formato);
        dateText.getStyle().set("color", "#FFFFFF");
        dateText.getStyle().set("font-size", "0.85rem");

        Span header = new Span("HOLA, " + user.getNameCliente().toUpperCase() + " " + user.getApellidoCliente().toUpperCase());
        header.getStyle().set("color", "#FFFFFF");
        header.getStyle().set("font-weight", "bold");
        header.getStyle().set("font-size", "1.2rem");

        headerInfo.add(dateText, header);
        return headerInfo;
    }

    private Component generateScroll(Cliente user, List<Movimientos> movements){
        VerticalLayout savings = dynamicCard("MONTO AHORRO", String.valueOf(user.getInitialSalary()),"#4ade80" ,VaadinIcon.WALLET);
        VerticalLayout graphic = dynamicCard("GRAFICO AHORRO", "VER MOVIMIENTOS", "#60a5fa",VaadinIcon.PIE_CHART);

        savings.addClickListener(e -> abrirVentanaReporte(user, movements));
        graphic.addClickListener(e -> graphicMovements(user, movements));

        HorizontalLayout container = new HorizontalLayout(savings, graphic);
        container.setPadding(false);
        container.setSpacing(true);

        Scroller carousel = new Scroller(container);
        carousel.setScrollDirection(Scroller.ScrollDirection.HORIZONTAL);
        carousel.setWidthFull();

        carousel.setHeight("160px");

        return carousel;
    }

    // Open a simple dialog listing movements (both ingresos y gastos)
    private void graphicMovements(Cliente user, List<Movimientos> movimientosList){
        Dialog graphic = new Dialog();
        graphic.setHeaderTitle("GRAFICO DE AHORRO");
        graphic.setWidth("350px");

        DataStructures data = new DataStructures();

        Map<String, Double> grafico = data.calculateDistribution(user, movimientosList);
        TreeSet<Movimientos> filter = data.ordenarMovimientos(movimientosList);

        double ahorro = grafico.get("Ahorros Disponibles");
        double gastos = grafico.get("Gastos");
        double total = ahorro + gastos;

        System.out.println(ahorro);
        System.out.println(gastos);

        int percentage;

        if (total > 0){
            percentage = (int) Math.round((gastos / total) * 100);
        } else {
            percentage = 0;
        }

        VerticalLayout containerGraphic = new VerticalLayout();
        containerGraphic.setAlignItems(Alignment.CENTER);
        containerGraphic.setPadding(false);

        Span donut = new Span();
        donut.setWidth("150px");
        donut.setHeight("150px");
        donut.getStyle().set("border-radius", "50%");
        // La magia visual: un gradiente cónico que crea la dona
        donut.getStyle().set("background", "conic-gradient(#ED2100 " + percentage + "%, #28a745 " + percentage + "% 100%)");
        // El agujero del centro
        donut.getStyle().set("mask-image", "radial-gradient(circle, transparent 55%, black 56%)");
        donut.getStyle().set("-webkit-mask-image", "radial-gradient(circle, transparent 55%, black 56%)");

        HorizontalLayout leyendas = new HorizontalLayout();
        leyendas.setWidthFull();
        leyendas.setJustifyContentMode(JustifyContentMode.CENTER);

        Span leyendaAhorro = new Span("Ahorro: $" + String.format("%.0f", ahorro));
        leyendaAhorro.getStyle().set("color", "#28a745").set("font-size", "0.8rem").set("font-weight", "bold");

        Span leyendaGastos = new Span("Gastos: $" + String.format("%.0f", gastos));
        leyendaGastos.getStyle().set("color", "#ED2100").set("font-size", "0.8rem").set("font-weight", "bold");

        leyendas.add(leyendaAhorro, leyendaGastos);
        containerGraphic.add(donut, leyendas);

        // 3. LA LISTA DE MOVIMIENTOS (De tu TreeSet)
        VerticalLayout listaMovimientos = new VerticalLayout();
        listaMovimientos.setPadding(false);
        listaMovimientos.getStyle().set("margin-top", "15px");

        Span tituloHistorial = new Span("Historial Reciente");
        tituloHistorial.getStyle().set("font-weight", "bold").set("font-size", "0.9rem");
        listaMovimientos.add(tituloHistorial);

        if (filter.isEmpty()) {
            listaMovimientos.add(new Span("No hay movimientos registrados."));
        } else {
            for (Movimientos mov : filter) {
                HorizontalLayout fila = new HorizontalLayout();
                fila.setWidthFull();
                fila.setJustifyContentMode(JustifyContentMode.BETWEEN);

                boolean esIngreso = "INGRESO".equalsIgnoreCase(mov.getTipoMovimiento());

                Span desc = new Span(mov.getCategoria() + " (" + mov.getFecha().toString() + ")");
                desc.getStyle().set("font-size", "0.8rem");

                Span valor = new Span((esIngreso ? "+$" : "-$") + mov.getMonto());
                valor.getStyle().set("color", esIngreso ? "#28a745" : "#dc3545");
                valor.getStyle().set("font-weight", "bold");

                fila.add(desc, valor);
                listaMovimientos.add(fila);
            }
        }

        Scroller scroller = new Scroller(listaMovimientos);
        scroller.setMaxHeight("200px");
        scroller.setWidthFull();

        graphic.add(containerGraphic, scroller);
        graphic.getFooter().add(new Button("Cerrar", e -> graphic.close()));

        graphic.open();

    }

    private void abrirVentanaReporte(Cliente user, List<Movimientos> movimientosList) {
        DataStructures dataStructures = new DataStructures();
        Reporte datos = dataStructures.savingsUser(user, movimientosList);
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Resumen Financiero");
        dialog.setWidth("350px");

        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);

        // Totales
        Span lblSueldo = new Span("Sueldo Inicial: $" + String.format("%.2f", datos.getSueldoInicial()));

        Span lblIngresos = new Span("Otros Ingresos: +$" + String.format("%.2f", datos.getTotalIngresos()));
        lblIngresos.getStyle().set("color", "#28a745");

        Span lblGastos = new Span("Gastos Totales: -$" + String.format("%.2f", datos.getTotalGastos()));
        lblGastos.getStyle().set("color", "#ED2100");

        Span lblTotal = new Span("DISPONIBLE: $" + String.format("%.2f", datos.getAhorroDisponible()));
        lblTotal.getStyle().set("font-weight", "bold").set("font-size", "1.2rem").set("margin-top", "10px");

        layout.add(lblSueldo, lblIngresos, lblGastos, lblTotal);

        // Iteramos los arreglos paralelos para generar el desglose visual
        if (datos.categorias.length > 0) {
            H4 subtitulo = new H4("Desglose de Gastos (A-Z)");
            subtitulo.getStyle().set("color", "#64748B");
            layout.add(subtitulo);

            for (int i = 0; i < datos.categorias.length; i++) {
                HorizontalLayout fila = new HorizontalLayout();
                fila.setWidthFull();
                fila.setJustifyContentMode(JustifyContentMode.BETWEEN);

                Span nombre = new Span(datos.categorias[i]);
                Span monto = new Span("$" + String.format("%.2f", datos.montos[i]));
                monto.getStyle().set("font-weight", "bold");

                fila.add(nombre, monto);
                layout.add(fila);
            }
        }

        dialog.add(layout);

        // --- NUEVO BOTÓN PARA ACTUALIZAR EL SUELDO ---
        Button btnActualizar = new Button("Convertir en Sueldo", event -> {
            double nuevoSueldo = datos.getAhorroDisponible();

            Conexiones db = new Conexiones();
            try (java.sql.Connection conn = db.getConnection()) {

                // 1. Actualizar en MySQL usando los métodos que ya tienes
                db.updateSalary(user.getIdCliente(), nuevoSueldo, conn);
                db.updateSalaryUSer(user.getIdCliente(), nuevoSueldo, conn);

                // 2. Actualizar la mochila (sesión actual)
                user.setInitialSalary(nuevoSueldo);

                com.vaadin.flow.component.notification.Notification.show(
                        "¡Sueldo actualizado a $" + String.format("%.2f", nuevoSueldo) + "!",
                        300,
                        com.vaadin.flow.component.notification.Notification.Position.TOP_CENTER
                );

                dialog.close();

                // 3. Recargar la página para que la tarjeta de capacidad de ahorro y los gráficos se re-dibujen
                com.vaadin.flow.component.UI.getCurrent().getPage().reload();

            } catch (java.sql.SQLException ex) {
                ex.printStackTrace();
                com.vaadin.flow.component.notification.Notification.show("Error de base de datos.");
            }
        });

        // Estilo del botón (Verde para indicar acción positiva)
        btnActualizar.getStyle().set("background-color", "#28a745");
        btnActualizar.getStyle().set("color", "white");

        Button btnCerrar = new Button("Cerrar", e -> dialog.close());

        // Añadimos ambos botones al pie de la ventana
        dialog.getFooter().add(btnCerrar, btnActualizar);
        dialog.open();
    }

    private VerticalLayout cardTemplate(){
        VerticalLayout template = new VerticalLayout();
        template.setWidthFull();
        template.setPadding(true);
        template.setSpacing(true);

        template.getStyle().set("background-color", "var(--lumo-base-color)");
        template.getStyle().set("color", "var(--lumo-body-text-color)");
        template.getStyle().set("border-radius", "14px");
        template.getStyle().set("border", "1px solid #D1D5DB");
        template.getStyle().set("box-shadow", "0 4px 6px rgba(0, 0, 0, 0.1)");
        template.getStyle().set("box-sizing", "border-box");
        return template;
    }

    private VerticalLayout dynamicCard(String message, String amount, String colorCode, VaadinIcon icono){
        VerticalLayout card = cardTemplate();
        card.setWidth("200px");
        card.setHeight("130px");
        card.setJustifyContentMode(JustifyContentMode.CENTER);
        card.getStyle().set("cursor", "pointer");

        Icon icon = icono.create();
        icon.setColor(colorCode);
        icon.setSize("30px");
        icon.getElement().getStyle().set("width", "30px");
        icon.getElement().getStyle().set("height", "30px");
        icon.getElement().getStyle().set("flex-shrink", "0");

        Span text = new Span(message);
        text.getStyle().set("font-size", "1.0rem");
        text.getStyle().set("font-weight", "bold");

        Span value = new Span(amount);
        value.getStyle().set("font-size", "0.8rem");

        card.add(icon, text, value);
        return card;
    }

    private Component generatePercentage(double savings, double goal){
        VerticalLayout infoAhorro = cardTemplate();
        infoAhorro.setWidthFull();

        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);
        header.setAlignItems(Alignment.BASELINE);

        H4 info = new H4("CAPACIDAD DE AHORRO");
        info.getStyle().set("font-size", "0.9rem");

        int percentage = (int)((savings / goal) * 100);
        if (percentage > 100){
            percentage = 100;
        }
        Span infoPercentage = new Span(percentage + "%");
        infoPercentage.getStyle().set("color", "#28a745");
        infoPercentage.getStyle().set("font-weight", "bold");
        infoPercentage.getStyle().set("font-size", "1.2rem");

        header.add(info, infoPercentage);

        Span timeText = new Span("Este mes");
        timeText.getStyle().set("font-size", "0.85rem");
        timeText.getStyle().set("color", "#898989");

        HorizontalLayout progressBar = new HorizontalLayout();
        progressBar.setWidthFull();
        progressBar.setHeight("12px");
        progressBar.setPadding(false);
        infoAhorro.setSpacing(true);
        progressBar.getStyle().set("background-color", "#D3D3D3");
        progressBar.getStyle().set("border-radius", "10px");
        progressBar.addClassNames(LumoUtility.BorderRadius.MEDIUM);

        Span percentageBar = new Span();
        percentageBar.setHeightFull();
        if (percentage > 60){
            percentageBar.setWidth(percentage + "%");
            percentageBar.getStyle().set("background-color", "#28a745");
        } else if (percentage > 40 && percentage < 60){
            percentageBar.setWidth(percentage + "%");
            percentageBar.getStyle().set("background-color", "#FDBE02");
        } else {
            percentageBar.setWidth(percentage + "%");
            percentageBar.getStyle().set("background-color", "#ED2100");
        }

        percentageBar.getStyle().set("border-radius", "10px");

        progressBar.add(percentageBar);

        HorizontalLayout amount = new HorizontalLayout();
        amount.setWidthFull();
        amount.setJustifyContentMode(JustifyContentMode.BETWEEN);

        Span savingsText = new Span("$" +String.format("%.0f", savings) + " ahorrado");
        savingsText.getStyle().set("font-size", "0.7rem");
        savingsText.getStyle().set("color", "#898989");

        Span goalText = new Span("$" + String.format("%.0f", goal) + " meta");
        goalText.getStyle().set("font-size", "0.7rem");
        goalText.getStyle().set("color", "#898989");

        amount.add(savingsText, goalText);

        infoAhorro.add(header, timeText, progressBar, amount);

        return infoAhorro;
    }

    private Component recommendationsCards(double income, double expense){
        VerticalLayout recommendations = new VerticalLayout();
        recommendations.setWidthFull();
        recommendations.setSpacing(true);
        recommendations.setPadding(true);
        recommendations.getStyle().set("background-color", "#FFFFFF");
        recommendations.addClassName(LumoUtility.BoxShadow.SMALL);
        recommendations.getStyle().set("border-radius", "12px");
        recommendations.getStyle().set("border", "1px solid #D1D5DB");
        recommendations.getStyle().set("box-shadow", "0 4px 6px rgba(0, 0, 0, 0.1)");
        recommendations.getStyle().set("box-sizing", "border-box");

        Span title = new Span("Recomendaciones Inteligentes");
        title.getStyle().set("font-weight", "bold");
        title.getStyle().set("font-size", "0.9rem");

        double utility = income - expense;
        double valid = income * 0.10;
        if (utility > valid){
            Span text = new Span("Mantener este ahorro te ayudará a cubrir el costo de tu examen Cisco CCST muy pronto");
            text.getStyle().set("font-size", "0.8rem");
            text.getStyle().set("color", "#898989");
            Span text2 = new Span("Mantener tus hábitos te permitirá comprar una nueva laptop");
            text2.getStyle().set("font-size", "0.8rem");
            text2.getStyle().set("color", "#898989");
            Span text3  = new Span("Mantener tus hábitos te van a permitir viajar al mundial");
            text3.getStyle().set("font-size", "0.8rem");
            text3.getStyle().set("color", "#898989");

            recommendations.add(title, text, text2, text3);
        } else if (utility == valid){
            Span text = new Span("Diversificar ahorros para construir un historial financiero sólido.");
            text.getStyle().set("font-size", "0.8rem");
            text.getStyle().set("color", "#898989");

            Span text2 = new Span("Intentar evitar comer afuera");
            text2.getStyle().set("font-size", "0.8rem");
            text2.getStyle().set("color", "#898989");

            Span text3  = new Span("Usar medios como bicicletas para transportarse");
            text3.getStyle().set("font-size", "0.8rem");
            text3.getStyle().set("color", "#898989");

            recommendations.add(title, text, text2, text3);
        } else {
            Span text = new Span("Tus gastos diarios han superado el límite recomendado esta semana. ¡Ajusta tu presupuesto!");
            text.getStyle().set("font-size", "0.8rem");
            text.getStyle().set("color", "#898989");

            Span text2 = new Span("Guardar un 40% de sus ingresos para salir de la situación actual");
            text2.getStyle().set("font-size", "0.8rem");
            text2.getStyle().set("color", "#898989");

            Span text3  = new Span("Conseguir otro trabajo para cubrir los gastos");
            text3.getStyle().set("font-size", "0.8rem");
            text3.getStyle().set("color", "#898989");

            recommendations.add(title, text, text2, text3);
        }

        return recommendations;
    }

    private Component navigationBar(){
        HorizontalLayout icons = new HorizontalLayout();
        icons.setWidthFull();
        icons.setHeight("60px");
        icons.getStyle().set("position", "fixed");
        icons.getStyle().set("bottom", "0");
        icons.getStyle().set("left", "50%");
        icons.getStyle().set("transform", "translateX(-50%)");
        icons.getStyle().set("width", "100%");
        icons.getStyle().set("max-width", "390px");

        icons.getStyle().set("background-color", "var(--lumo-base-color)");
        icons.getStyle().set("border-top", "1px solid var(--lumo-contrast-10pct)");
        icons.getStyle().set("z-index", "100");

        icons.setJustifyContentMode(JustifyContentMode.EVENLY);
        icons.setVerticalComponentAlignment(Alignment.CENTER);

        Icon home = VaadinIcon.HOME_O.create();

        Icon expenses = VaadinIcon.WALLET.create();

        Icon incomes = VaadinIcon.MONEY.create();

        Icon goals = VaadinIcon.BULLSEYE.create();

        Icon user = VaadinIcon.USER.create();


        Icon[] icon = {home, expenses, incomes, goals, user};
        for(Icon image : icon){
            image.setColor("#A0AEC0");
            image.setSize("24px");
            image.getStyle().set("cursor", "pointer");
        }

        home.setColor("#28a745");
        goals.addClickListener(e -> UI.getCurrent().navigate("metas"));
        incomes.addClickListener(e -> UI.getCurrent().navigate("ingreso"));
        expenses.addClickListener(e -> UI.getCurrent().navigate("gasto"));
        user.addClickListener(e -> UI.getCurrent().navigate("perfil"));

        icons.add(home, expenses, incomes, goals, user);
        return icons;
    }

    private Component crearTarjetasMovimientos() {
        HorizontalLayout contenedor = new HorizontalLayout();
        contenedor.setWidthFull();
        contenedor.setSpacing(true);
        contenedor.setPadding(false);
        contenedor.getStyle().set("margin-bottom", "20px");

        double totalIngresos = 0;
        double totalGastos = 0;
        try (Connection conn = Conexiones.getConnection()) {
            totalIngresos = new Conexiones().sumarIngresosDelMes(user.getIdCliente(), conn);
            totalGastos = new Conexiones().sumarGastosDelMes(user.getIdCliente(), conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        VerticalLayout cardIngresos = crearTarjetaMini("INGRESOS", String.format("$%,.2f", totalIngresos), VaadinIcon.ARROW_UP, true);
        VerticalLayout cardGastos = crearTarjetaMini("GASTOS", String.format("$%,.2f", totalGastos), VaadinIcon.ARROW_DOWN, false);

        contenedor.add(cardIngresos, cardGastos);
        return contenedor;
    }

    private VerticalLayout crearTarjetaMini(String titulo, String monto, VaadinIcon icono, boolean esIngreso) {
        VerticalLayout card = cardTemplate();
        card.setWidth("50%");
        card.setHeight("100px");
        card.setJustifyContentMode(JustifyContentMode.CENTER);
        card.setAlignItems(Alignment.CENTER);
        card.setSpacing(false);
        card.getStyle().set("gap", "4px");
        card.getStyle().set("cursor", "pointer");

        HorizontalLayout container = new HorizontalLayout();
        container.setWidth("28px");
        container.setHeight("28px");
        container.setPadding(false);
        container.getStyle().set("border-radius", "50px");
        container.setSpacing(false);
        container.setJustifyContentMode(JustifyContentMode.CENTER);
        container.setAlignItems(Alignment.CENTER);

        Icon icon = icono.create();

        if (esIngreso){
            container.getStyle().set("background-color", "#88E788");
            card.getStyle().set("background-color", "#80EF80");
            icon.setColor("#28a745");
        } else {
            container.getStyle().set("background-color", "#FF746C");
            card.getStyle().set("background-color", "#FFC5D3");
            icon.setColor("#ED2100");
        }
        icon.setSize("20px");
        icon.getElement().getStyle().set("width", "20px");
        icon.getElement().getStyle().set("height", "20px");
        icon.getElement().getStyle().set("flex-shrink", "0");

        container.add(icon);

        Span text = new Span(titulo);
        text.getStyle().set("font-weight", "bold");
        text.getStyle().set("font-size", "0.8rem");
        text.getStyle().set("color", "#898989");

        Span value = new Span(monto);
        value.getStyle().set("font-weight", "bold");
        value.getStyle().set("font-size", "1.0rem");

        card.add(container, text, value);

        card.addClickListener(e -> abrirVentanaMovimientos(titulo, esIngreso));

        return card;
    }

    private void abrirVentanaMovimientos(String titulo, boolean esIngreso) {
        Dialog ventana = new Dialog();
        ventana.setHeaderTitle("Detalle de " + titulo);

        VerticalLayout listaMovimientos = new VerticalLayout();
        listaMovimientos.setPadding(false);

        try (Connection conn = Conexiones.getConnection()) {
            List<Movimientos> movimientos = new Conexiones().movements(esIngreso ? "INGRESO" : "GASTO", idUsuario, conn);
            for (Movimientos movimiento : movimientos) {
                HorizontalLayout fila = new HorizontalLayout();
                fila.setWidthFull();
                fila.setJustifyContentMode(JustifyContentMode.BETWEEN);

                Span desc = new Span(movimiento.getCategoria());
                desc.getStyle().set("font-size", "0.9rem");

                Span valor = new Span((esIngreso ? "+$" : "-$") + String.format("%,.2f", movimiento.getMonto()));
                valor.getStyle().set("color", esIngreso ? "#28a745" : "#dc3545");
                valor.getStyle().set("font-weight", "bold");

                fila.add(desc, valor);
                listaMovimientos.add(fila);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Scroller scroller = new Scroller(listaMovimientos);
        scroller.setMaxHeight("300px");
        scroller.setWidthFull();

        ventana.add(scroller);

        Button btnCerrar = new Button("Cerrar", e -> ventana.close());
        ventana.getFooter().add(btnCerrar);

        ventana.open();
    }

    private void actualizarSueldo(Cliente user) {
        Dialog ventanaSueldo = new Dialog();
        ventanaSueldo.setHeaderTitle("¡Bienvenido! Configura tu cuenta");
        ventanaSueldo.setCloseOnEsc(false);
        ventanaSueldo.setCloseOnOutsideClick(false);

        VerticalLayout layout = new VerticalLayout();
        Span mensaje = new Span("Para darte mejores recomendaciones, ingresa tu sueldo mensual actual:");
        mensaje.getStyle().set("font-size", "0.9rem");

        NumberField sueldoField = new NumberField("Sueldo Inicial");
        sueldoField.setPlaceholder("0.00");
        sueldoField.setWidthFull();

        Button btnGuardar = new Button("Guardar y Empezar", event -> {
            if (sueldoField.getValue() != null && sueldoField.getValue() > 0) {

                Conexiones db = new Conexiones();
                try (Connection conn = db.getConnection()) {
                    db.updateSalary(user.getIdCliente(), sueldoField.getValue(), conn);
                    db.updateSalaryUSer(user.getIdCliente(), sueldoField.getValue(), conn);
                } catch (SQLException e){
                    e.printStackTrace();
                }


                user.setInitialSalary(sueldoField.getValue());
                ventanaSueldo.close();

            } else {
                sueldoField.setErrorMessage("Ingresa un valor válido");
                sueldoField.setInvalid(true);
            }
        });

        ventanaSueldo.addOpenedChangeListener(e -> {
            if (!e.isOpened()) { // Si el estado cambió a "Cerrado"
                createUI();
            }
        });

        btnGuardar.getStyle().set("background-color", "#28a745");
        btnGuardar.getStyle().set("color", "white");
        btnGuardar.setWidthFull();

        layout.add(mensaje, sueldoField, btnGuardar);
        ventanaSueldo.add(layout);
        ventanaSueldo.open();
    }

    private void actualizarMeta(Cliente user) {
        Dialog ventanaMeta = new Dialog(); // Cambié el nombre de la variable para que tenga sentido
        ventanaMeta.setHeaderTitle("¡Bienvenido! Configura tu primera meta");
        ventanaMeta.setCloseOnEsc(false);
        ventanaMeta.setCloseOnOutsideClick(false);

        VerticalLayout layout = new VerticalLayout();
        Span mensaje = new Span("Para darte mejores recomendaciones, ingresa tu meta financiera a alcanzar:");
        mensaje.getStyle().set("font-size", "0.9rem");

        NumberField metaField = new NumberField("Monto de la Meta");
        metaField.setPlaceholder("0.00");
        metaField.setWidthFull();

        Button btnGuardar = new Button("Guardar y Empezar", event -> {
            // 1. Validar usando metaField (ya no sueldoField)
            if (metaField.getValue() != null && metaField.getValue() > 0) {

                Conexiones db = new Conexiones();
                try (Connection conn = db.getConnection()) {

                    // 2. Crear una meta por defecto con el monto ingresado
                    Meta nuevaMeta = new Meta(
                            0, // ID autoincremental
                            "Mi Primera Meta", // Nombre por defecto
                            metaField.getValue(), // El monto objetivo que puso el usuario
                            0.0, // Monto ahorrado inicial (0)
                            LocalDate.now().plusYears(1), // Le damos 1 año de plazo por defecto
                            "#3B82F6", // Color azul
                            "General", // Categoría
                            LocalDate.now() // Fecha de creación
                    );

                    // 3. Guardar la meta en la base de datos usando el método que ya tienes
                    db.addMeta(nuevaMeta, user.getIdCliente(), conn);
                    ventanaMeta.close();

                } catch (SQLException e){
                    e.printStackTrace();
                }

                UI.getCurrent().getPage().reload();
                ventanaMeta.close();

            } else {
                // 4. Mostrar error en el campo correcto
                metaField.setErrorMessage("Ingresa un valor válido");
                metaField.setInvalid(true);
            }
        });

        ventanaMeta.addOpenedChangeListener(e -> {
            if (!e.isOpened()) { // Si el estado cambió a "Cerrado"
                createUI();
            }
        });

        btnGuardar.getStyle().set("background-color", "#28a745");
        btnGuardar.getStyle().set("color", "white");
        btnGuardar.setWidthFull();

        layout.add(mensaje, metaField, btnGuardar);
        ventanaMeta.add(layout);
        ventanaMeta.open();
    }

}

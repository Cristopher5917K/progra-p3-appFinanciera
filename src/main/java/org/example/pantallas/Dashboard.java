package org.example.pantallas;

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
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.component.dialog.Dialog;
import org.example.backend.Conexiones;
import org.example.info.Cliente;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Route("dashboard")
public class Dashboard extends VerticalLayout {
    public Dashboard(){
        Cliente user = VaadinSession.getCurrent().getAttribute(Cliente.class);

        if (user == null) {
            UI.getCurrent().navigate("login");
            return;
        }

        this.setWidthFull();
        this.setAlignItems(Alignment.CENTER);
        this.setPadding(false);
        this.setSpacing(false);
        this.getStyle().set("background-color", "#F8F9FA");
        getStyle().set("margin", "0 auto");
        getStyle().set("padding-bottom", "60px");
        //setHeight("100vh");
        setPadding(false);
        setSpacing(false);

        if (user.getInitialSalary() <= 0){
            actualizarSueldo(user);

        }

        VerticalLayout mainContainer = new VerticalLayout();
        mainContainer.setPadding(true);
        mainContainer.setSpacing(true);
        mainContainer.add(
                generateScroll(),
                generatePercentage(user.getInitialSalary(), 1800),
                recommendationsCards(user.getInitialSalary(),900),
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

    private Component generateScroll(){
        VerticalLayout savings = dynamicCard("MONTO AHORRO", "$24850","#4ade80" ,VaadinIcon.WALLET);
        VerticalLayout graphic = dynamicCard("GRAFICO AHORRO", "VER MOVIMIENTOS", "#60a5fa",VaadinIcon.PIE_CHART);

        HorizontalLayout container = new HorizontalLayout(savings, graphic);
        container.setPadding(false);
        container.setSpacing(true);

        Scroller carousel = new Scroller(container);
        carousel.setScrollDirection(Scroller.ScrollDirection.HORIZONTAL);
        carousel.setWidthFull();

        carousel.setHeight("160px");

        return carousel;
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

        VerticalLayout cardIngresos = crearTarjetaMini("INGRESOS", "$3,200", VaadinIcon.ARROW_UP, true);
        VerticalLayout cardGastos = crearTarjetaMini("GASTOS", "$1,450", VaadinIcon.ARROW_DOWN, false);

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

        // SIMULACIÓN DINÁMICA: Aquí es donde después usarás un 'for' con tu lista de SQLWorkbench
        for (int i = 1; i <= 6; i++) {
            HorizontalLayout fila = new HorizontalLayout();
            fila.setWidthFull();
            fila.setJustifyContentMode(JustifyContentMode.BETWEEN);

            Span desc = new Span(esIngreso ? "Depósito de nómina" : "Gasto en Supermaxi");
            desc.getStyle().set("font-size", "0.9rem");

            Span valor = new Span(esIngreso ? "+$500.00" : "-$45.00");
            valor.getStyle().set("color", esIngreso ? "#28a745" : "#dc3545");
            valor.getStyle().set("font-weight", "bold");

            fila.add(desc, valor);
            listaMovimientos.add(fila);
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
        ventanaSueldo.setCloseOnOutsideClick(false); // Obliga a que ingresen un dato

        VerticalLayout layout = new VerticalLayout();
        Span mensaje = new Span("Para darte mejores recomendaciones, ingresa tu sueldo mensual actual:");
        mensaje.getStyle().set("font-size", "0.9rem");

        NumberField sueldoField = new NumberField("Sueldo Inicial");
        sueldoField.setPlaceholder("0.00");
        sueldoField.setWidthFull();

        Button btnGuardar = new Button("Guardar y Empezar", event -> {
            if (sueldoField.getValue() != null && sueldoField.getValue() > 0) {

                // 1. Actualizar el sueldo en la base de datos usando un método UPDATE
                Conexiones db = new Conexiones();
                Connection conn = null;
                try {
                    conn = db.getConnection();
                    db.updateSalary(user.getIdCliente(), sueldoField.getValue(), conn);
                    db.updateSalaryUSer(user.getIdCliente(), sueldoField.getValue(), conn);
                } catch (SQLException e){
                    e.printStackTrace();
                }


                user.setInitialSalary(sueldoField.getValue());

                UI.getCurrent().getPage().reload();

                ventanaSueldo.close();
            } else {
                sueldoField.setErrorMessage("Ingresa un valor válido");
                sueldoField.setInvalid(true);
            }
        });

        btnGuardar.getStyle().set("background-color", "#28a745");
        btnGuardar.getStyle().set("color", "white");
        btnGuardar.setWidthFull();

        layout.add(mensaje, sueldoField, btnGuardar);
        ventanaSueldo.add(layout);
        ventanaSueldo.open();
    }

}

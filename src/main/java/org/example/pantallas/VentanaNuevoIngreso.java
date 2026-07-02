package org.example.pantallas;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.example.backend.Conexiones;
import org.example.info.Cliente;

import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Route("ingreso")
public class VentanaNuevoIngreso extends VerticalLayout {
    private final List<VerticalLayout> category = new ArrayList<>();
    private final List<HorizontalLayout> timeSpam = new ArrayList<>();
    private NumberField amount;
    private DatePicker date;
    private String categoria = "SUELDO";
    private String frecuencia = "MENSUAL";

    public VentanaNuevoIngreso(){
        Cliente user = VaadinSession.getCurrent().getAttribute(Cliente.class);
        this.setSizeFull();
        this.setAlignItems(Alignment.CENTER);
        this.setPadding(false);
        this.setSpacing(false);
        this.getStyle().set("background-color", "#F8F9FA");
        getStyle().set("margin", "0 auto");
        getStyle().set("padding-bottom", "68px");
        setSpacing(false);
        setPadding(false);

        VerticalLayout mainContainer = new VerticalLayout();
        mainContainer.setSpacing(true);
        mainContainer.setPadding(true);
        mainContainer.add(
                incomeEntry(),
                categoryCards(),
                frequencyCard(),
                crearSeccionFecha(),
                save(user.getIdCliente())
        );

        add(
                headerIncome(),
                mainContainer
        );
    }

    private Component headerIncome(){
        VerticalLayout mainHeader = new VerticalLayout();
        mainHeader.setWidthFull();
        mainHeader.getStyle().set("border-radius", "0 0 24px 24px");
        mainHeader.getStyle().set("background-color", "#FFFFFF");

        HorizontalLayout titleContainer = new HorizontalLayout();
        titleContainer.setWidthFull();
        titleContainer.setSpacing(false);
        titleContainer.setPadding(false);
        titleContainer.setAlignItems(Alignment.CENTER);
        titleContainer.setJustifyContentMode(JustifyContentMode.BETWEEN);

        HorizontalLayout iconContainer = new HorizontalLayout();
        iconContainer.setWidth("40px");
        iconContainer.setHeight("40px");
        iconContainer.getStyle().set("border", "1px solid #D1D5DB");
        iconContainer.getStyle().set("border-radius", "30%");
        iconContainer.setAlignItems(Alignment.CENTER);
        iconContainer.setJustifyContentMode(JustifyContentMode.CENTER);

        Icon backButton = VaadinIcon.ANGLE_LEFT.create();
        backButton.setColor("#000000");
        backButton.getStyle().set("cursor", "pointer");
        iconContainer.addClickListener(e -> UI.getCurrent().navigate("dashboard"));

        iconContainer.add(backButton);

        Icon money = VaadinIcon.MONEY_DEPOSIT.create();
        money.setColor("#28a745");

        Span title = new Span("NUEVO INGRESO");
        title.getStyle().set("font-size", "1.1rem");
        title.getStyle().set("font-weight", "bold");
        title.getStyle().set("color", "#000000");

        titleContainer.add(iconContainer, title, money);

        Span subtitle = new Span("Registra nuevo ingreso");
        subtitle.getStyle().set("font-size", "0.7rem");
        subtitle.getStyle().set("color", "#898989");

        mainHeader.add(titleContainer, subtitle);
        return mainHeader;
    }

    private Component incomeEntry(){
        VerticalLayout cardInput = new VerticalLayout();
        cardInput.setPadding(false);

        Span title = new Span("MONTO DE INGRESO");
        title.getStyle().set("color", "#898989");
        title.getStyle().set("font-size", "0.75rem");
        title.getStyle().set("font-weight", "bold");

        HorizontalLayout input = new HorizontalLayout();
        input.setWidthFull();
        input.getStyle().set("background-color", "white");
        input.getStyle().set("border-radius", "16px");
        input.setPadding(true);
        input.setAlignItems(FlexComponent.Alignment.CENTER);

        VerticalLayout iconContainer = new VerticalLayout();
        iconContainer.setWidth("40px");
        iconContainer.setHeight("40px");
        iconContainer.getStyle().set("background-color", "#88E788");
        iconContainer.getStyle().set("border-radius", "30%");
        iconContainer.setAlignItems(Alignment.CENTER);
        iconContainer.setJustifyContentMode(JustifyContentMode.CENTER);

        Icon money = VaadinIcon.DOLLAR.create();
        money.setColor("#28a745");
        money.setSize("22px");
        money.getElement().getStyle().set("width", "22px");
        money.getElement().getStyle().set("height", "22px");
        money.getElement().getStyle().set("flex-shrink", "0");

        iconContainer.add(money);

        amount = new NumberField();
        amount.setPlaceholder("0.00");
        amount.setWidthFull();

        amount.getStyle().set("--lumo-text-field-size", "var(--lumo-size-xl)");
        amount.getStyle().set("font-size", "1.5rem");
        amount.getStyle().set("font-weight", "bold");
        amount.getStyle().set("--lumo-contrast-10pct", "transparent");

        input.add(iconContainer, amount);

        cardInput.add(title,input);

        return cardInput;
    }

    private Component categoryCards(){
        VerticalLayout categoryCard = new VerticalLayout();
        categoryCard.setPadding(false);

        Span titleCategory = new Span("SELECCIONE UNA CATEGORIA");
        titleCategory.getStyle().set("color", "#898989");
        titleCategory.getStyle().set("font-size", "0.75rem");
        titleCategory.getStyle().set("font-weight", "bold");

        HorizontalLayout cardSelection = new HorizontalLayout();
        cardSelection.setWidthFull();
        cardSelection.setSpacing(true);
        cardSelection.getStyle().set("background-color", "#FFFFFF");
        cardSelection.getStyle().set("border-radius", "14px");

        VerticalLayout option1 = crearTarjetaCategoria(VaadinIcon.BRIEFCASE, "SUELDO", "EMPLEADO FORMAL");
        VerticalLayout option2 = crearTarjetaCategoria(VaadinIcon.LAPTOP, "FREELANCE", "EMPLEO INDEPENDIENTE");
        VerticalLayout option3 = crearTarjetaCategoria(VaadinIcon.ELLIPSIS_DOTS_H, "OTROS", "INVERSIONES, REGALIS");

        category.addAll(List.of(option1, option2, option3));

        /*for (VerticalLayout card : category){
            card.addClickListener(e -> seleccionarTarjetaCategoria(card));
        }*/

        option1.addClickListener(e -> {seleccionarTarjetaCategoria(option1); categoria = "SUELDO";});
        option2.addClickListener(e -> {seleccionarTarjetaCategoria(option2); categoria = "FREELANCE";});
        option3.addClickListener(e -> {seleccionarTarjetaCategoria(option3); categoria = "OTROS";});


        seleccionarTarjetaCategoria(option1);

        cardSelection.add(option1, option2, option3);
        categoryCard.add(titleCategory, cardSelection);
        return categoryCard;
    }

    private Component frequencyCard(){
        VerticalLayout frequencyContainer = new VerticalLayout();
        frequencyContainer.setPadding(false);

        Span titleFrequency = new Span("SELECCIONE LA FRECUENCIA DEL INGRESO");
        titleFrequency.getStyle().set("color", "#898989");
        titleFrequency.getStyle().set("font-size", "0.75rem");
        titleFrequency.getStyle().set("font-weight", "bold");

        VerticalLayout cardSelection = new VerticalLayout();
        cardSelection.setWidthFull();
        cardSelection.setSpacing(true);

        HorizontalLayout option1 = crearTarjetaFrecuencia("MENSUAL", "CADA MES");
        HorizontalLayout option2 = crearTarjetaFrecuencia("QUINCENAL", "CADA 15 DIAS");
        HorizontalLayout option3 = crearTarjetaFrecuencia("SEMANAL", "CADA SEMANA");

        timeSpam.addAll(List.of(option1, option2, option3));

        /*for (HorizontalLayout card1 : timeSpam){
            card1.addClickListener(e -> seleccionarTarjetaFrecuencia(card1));
        }*/

        option1.addClickListener(e -> {seleccionarTarjetaFrecuencia(option1); frecuencia = "MENSUAL";});
        option2.addClickListener(e -> {seleccionarTarjetaFrecuencia(option2); frecuencia = "QUINCENAL";});
        option3.addClickListener(e -> {seleccionarTarjetaFrecuencia(option3); frecuencia = "SEMANAL";});


        seleccionarTarjetaFrecuencia(option1);

        cardSelection.add(option1, option2, option3);
        frequencyContainer.add(titleFrequency, cardSelection);
        return frequencyContainer;
    }

    private Component save(int id){
        VerticalLayout container = new VerticalLayout();
        container.setWidthFull();
        container.setSpacing(true);
        container.setAlignItems(Alignment.CENTER);
        container.setJustifyContentMode(JustifyContentMode.CENTER);
        container.getStyle().set("background-color", "#28a745");
        container.getStyle().set("border-radius", "16px");

        Icon check = VaadinIcon.CHECK.create();
        check.setSize("22px");
        check.setColor("#FFFFFF");
        check.setSize("16px");

        Span message = new Span("GUARDAR INGRESO");
        message.getStyle().set("color", "#FFFFFF");
        message.getStyle().set("font-size", "0.9rem");
        message.getStyle().set("font-weight", "bold");

        container.addClickListener(e ->{
            if (amount.getValue() != null && amount.getValue() > 0){
                Conexiones database = new Conexiones();
                try {
                    Connection conn = database.getConnection();
                    if (conn != null){
                        Date dateIncome = Date.valueOf(date.getValue());
                        database.insertarMovimiento(conn, id, "INGRESO", categoria, frecuencia, amount.getValue(), dateIncome);
                        UI.getCurrent().navigate("dashboard");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        });
        container.add(check, message);
        return container;
    }

    private HorizontalLayout crearTarjetaFrecuencia(String titulo, String sub) {
        HorizontalLayout card = new HorizontalLayout();
        card.setWidthFull();
        card.getStyle().set("background-color", "white");
        card.getStyle().set("border-radius", "16px");
        card.getStyle().set("border", "1px solid #E2E8F0");
        card.setAlignItems(FlexComponent.Alignment.CENTER);
        card.setPadding(true);
        card.getStyle().set("cursor", "pointer");

        Icon radioIcon = VaadinIcon.CIRCLE_THIN.create();
        radioIcon.setColor("gray");

        VerticalLayout textos = new VerticalLayout();
        textos.setPadding(false);
        textos.setSpacing(false);
        Span txtTitulo = new Span(titulo);
        txtTitulo.getStyle().set("font-weight", "bold");
        Span txtSub = new Span(sub);
        txtSub.getStyle().set("color", "gray");
        txtSub.getStyle().set("font-size", "0.75rem");
        textos.add(txtTitulo, txtSub);

        card.add(radioIcon, textos);
        return card;
    }

    private void seleccionarTarjetaFrecuencia(HorizontalLayout seleccionada) {
        for (HorizontalLayout f : timeSpam) {
            f.getStyle().set("border", "1px solid #E2E8F0");
            f.getStyle().set("background-color", "white");
            Icon icon = (Icon) f.getComponentAt(0);
            icon.getElement().setAttribute("icon", "vaadin:circle-thin");
            icon.setColor("gray");
        }

        seleccionada.getStyle().set("border", "2px solid #28a745");
        seleccionada.getStyle().set("background-color", "#F0FFF4");
        Icon iconActivo = (Icon) seleccionada.getComponentAt(0);
        iconActivo.getElement().setAttribute("icon", "vaadin:dot-circle");
        iconActivo.setColor("#28a745");
    }

    private Component crearSeccionFecha() {
        VerticalLayout contenedor = new VerticalLayout();
        contenedor.setPadding(false);

        Span label = new Span("FECHA DE RECEPCIÓN");
        label.getStyle().set("color", "gray");
        label.getStyle().set("font-size", "0.75rem");
        label.getStyle().set("font-weight", "bold");

        date = new DatePicker();
        date.setWidthFull();
        date.setValue(LocalDate.now());

        date.getStyle().set("--lumo-base-color", "white");
        date.getStyle().set("--lumo-contrast-10pct", "white");
        date.getStyle().set("border-radius", "16px");
        date.getStyle().set("border", "1px solid #D1D5DB");
        date.getStyle().set("box-shadow", "0 4px 6px rgba(0, 0, 0, 0.05)");

        contenedor.add(label, date);
        return contenedor;
    }

    private VerticalLayout crearTarjetaCategoria(VaadinIcon icono, String titulo, String sub) {
        VerticalLayout card = new VerticalLayout();
        card.getStyle().set("background-color", "white");
        card.getStyle().set("border-radius", "16px");
        card.getStyle().set("border", "1px solid #E2E8F0");
        card.setAlignItems(FlexComponent.Alignment.CENTER);
        card.setPadding(true);
        card.setWidth("33%");
        card.getStyle().set("cursor", "pointer");

        Icon icon = icono.create();
        icon.setColor("gray");
        icon.setId("icono");

        Span txtTitulo = new Span(titulo);
        txtTitulo.getStyle().set("font-weight", "bold");
        txtTitulo.getStyle().set("font-size", "0.85rem");

        Span txtSub = new Span(sub);
        txtSub.getStyle().set("color", "gray");
        txtSub.getStyle().set("font-size", "0.65rem");
        txtSub.getStyle().set("text-align", "center");

        card.add(icon, txtTitulo, txtSub);
        return card;
    }

    private void seleccionarTarjetaCategoria(VerticalLayout seleccionada) {
        for (VerticalLayout t : category) {
            t.getStyle().set("border", "1px solid #E2E8F0");
            t.getStyle().set("background-color", "white");
            t.getChildren().filter(c -> c instanceof Icon).forEach(c -> ((Icon) c).setColor("gray"));
        }

        seleccionada.getStyle().set("border", "2px solid #28a745");
        seleccionada.getStyle().set("background-color", "#F0FFF4");
        seleccionada.getChildren().filter(c -> c instanceof Icon).forEach(c -> ((Icon) c).setColor("#28a745"));
    }
}

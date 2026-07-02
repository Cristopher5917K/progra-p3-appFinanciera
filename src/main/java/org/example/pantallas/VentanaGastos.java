package org.example.pantallas;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.example.backend.Conexiones;
import org.example.info.Cliente;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

@Route("gasto")
public class VentanaGastos extends VerticalLayout implements BeforeEnterObserver {
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

    private void createUI() {
        removeAll();
        setAlignItems(Alignment.CENTER);
        setSizeUndefined();
        setWidthFull();
        setWidth("390px");
        getStyle().set("background-color", "#F8F9FA");
        getStyle().set("margin", "0 auto");
        getStyle().set("padding-bottom", "60px");

        VerticalLayout mainContainer = new VerticalLayout();
        mainContainer.setMaxWidth("390px");
        // MEJORA UI/UX: Cambiamos setPadding a true para que los campos no choquen con los bordes de la pantalla
        mainContainer.setPadding(true);
        mainContainer.setSpacing(true);

        HorizontalLayout header = crearHeader();
        
        double salary = 0;
        double expenses = 0;
        try (Connection conn = Conexiones.getConnection()) {
            salary = user.getInitialSalary();
            expenses = new Conexiones().sumarGastosDelMes(user.getIdCliente(), conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Div budgetCard = crearTarjetaPresupuesto(salary, expenses);

        NumberField amountField = new NumberField("MONTO DEL GASTO");
        amountField.setPrefixComponent(new Icon(VaadinIcon.DOLLAR));
        amountField.setPlaceholder("0.00");
        amountField.setWidthFull();
        aplicarEstiloTarjeta(amountField);

        ComboBox<String> categoryBox = new ComboBox<>("CATEGORÍA");
        categoryBox.setPlaceholder("Selecciona una categoría");
        categoryBox.setItems("Alimentación", "Transporte", "Servicios", "Gasolina");
        categoryBox.setWidthFull();
        aplicarEstiloTarjeta(categoryBox);

        DatePicker datePicker = new DatePicker("FECHA");
        datePicker.setValue(LocalDate.now());
        datePicker.setWidthFull();
        aplicarEstiloTarjeta(datePicker);

        TextArea descriptionArea = new TextArea("DESCRIPCIÓN (OPCIONAL)");
        descriptionArea.setPlaceholder("Ej: Gasolina semanal, almuerzo con clientes...");
        descriptionArea.setWidthFull();
        aplicarEstiloTarjeta(descriptionArea);

        Button saveButton = new Button("Guardar gasto", new Icon(VaadinIcon.CHECK));
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        saveButton.setWidthFull();
        saveButton.getStyle().set("border-radius", "15px");
        saveButton.getStyle().set("padding", "25px");
        saveButton.getStyle().set("margin-top", "10px");
        saveButton.addClickListener(e -> {
            if (amountField.getValue() != null && amountField.getValue() > 0
                    && categoryBox.getValue() != null
                    && datePicker.getValue() != null) {

                try (Connection conn = Conexiones.getConnection()) {
                    Date sqlDate = Date.valueOf(datePicker.getValue());
                    String categoria = categoryBox.getValue();
                    double monto = amountField.getValue();

                    new Conexiones().insertarMovimiento(conn, user.getIdCliente(),"GASTO", categoria.toUpperCase(), "UNICO", monto, sqlDate);
                    UI.getCurrent().navigate("dashboard");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Notification.show("Error de conexión a la base de datos.", 3000, Notification.Position.MIDDLE)
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            } else {
                Notification.show("Por favor, completa el monto, la categoría y la fecha.", 3000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_WARNING);
            }
        });

        mainContainer.add(header, budgetCard, amountField, categoryBox, datePicker, descriptionArea, saveButton);
        add(mainContainer, navigationBar());
    }

    private HorizontalLayout crearHeader() {
        Button backButton = new Button(new Icon(VaadinIcon.ANGLE_LEFT));
        backButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        backButton.getStyle().set("background-color", "white").set("border-radius", "50%");
        backButton.addClickListener(e -> UI.getCurrent().navigate("dashboard"));

        H3 title = new H3("Nuevo Gasto");
        title.getStyle().set("margin", "0").set("color", "#1A2035");

        Span subtitle = new Span("Registra un gasto reciente");
        subtitle.getStyle().set("font-size", "12px").set("color", "#8C98A9");

        VerticalLayout titleLayout = new VerticalLayout(title, subtitle);
        titleLayout.setSpacing(false);
        titleLayout.setPadding(false);

        Icon moneyIcon = VaadinIcon.MONEY_EXCHANGE.create();
        moneyIcon.getStyle().set("color", "#28A745");
        Div iconContainer = new Div(moneyIcon);
        iconContainer.getStyle().set("background-color", "#E8F5E9").set("padding", "8px").set("border-radius", "50%");

        HorizontalLayout header = new HorizontalLayout(backButton, titleLayout, iconContainer);
        header.setWidthFull();
        header.setAlignItems(Alignment.CENTER);
        header.expand(titleLayout);
        return header;
    }

    private Div crearTarjetaPresupuesto(double presupuestoMaximo, double gastado) {
        Div card = new Div();
        aplicarEstiloTarjeta(card);
        card.getStyle().set("background-color", "#FFF8F2");
        card.getStyle().set("border", "1px solid #FFE4CD");

        Icon alertIcon = new Icon(VaadinIcon.WARNING);
        alertIcon.getStyle().set("color", "#F5A623");

        Span alertTitle = new Span("Presupuesto del mes");
        alertTitle.getStyle().set("color", "#F5A623").set("font-weight", "bold").set("margin-left", "10px");

        HorizontalLayout titleLayout = new HorizontalLayout(alertIcon, alertTitle);
        titleLayout.setAlignItems(Alignment.CENTER);

        double restante = presupuestoMaximo - gastado;
        if (restante < 0) restante = 0;

        Span alertText = new Span("Te quedan $" + String.format("%.2f", restante) + " de tu presupuesto de $" + String.format("%.2f", presupuestoMaximo));
        alertText.getStyle().set("font-size", "14px").set("color", "#555").set("display", "block").set("margin-top", "5px");

        double topeBarra = gastado > presupuestoMaximo ? presupuestoMaximo : gastado;
        ProgressBar progressBar;
        // Protect against invalid ProgressBar range when presupuestoMaximo is zero or negative
        if (presupuestoMaximo <= 0) {
            // Use a safe range [0,1] with value 0 (no progress)
            progressBar = new ProgressBar(0.0, 1.0, 0.0);
            progressBar.getStyle().set("margin-top", "10px");
        } else {
            progressBar = new ProgressBar(0.0, presupuestoMaximo, topeBarra);
            progressBar.getStyle().set("margin-top", "10px");

            if (gastado >= presupuestoMaximo) {
                progressBar.getStyle().set("--lumo-primary-color", "#ED2100");
            } else if (gastado >= presupuestoMaximo * 0.8) {
                progressBar.getStyle().set("--lumo-primary-color", "#F5A623");
            }
        }

        card.add(titleLayout, alertText, progressBar);
        return card;
    }

    private void aplicarEstiloTarjeta(com.vaadin.flow.component.Component component) {
        component.getStyle().set("background-color", "white");
        component.getStyle().set("border-radius", "15px");
        component.getStyle().set("padding", "15px");
        component.getStyle().set("box-shadow", "0 2px 8px rgba(0,0,0,0.04)");
    }

    private Component navigationBar(){
        HorizontalLayout icons = new HorizontalLayout();
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

        expenses.setColor("#28a745");
        home.addClickListener(e -> UI.getCurrent().navigate("dashboard"));
        goals.addClickListener(e -> UI.getCurrent().navigate("metas"));
        incomes.addClickListener(e -> UI.getCurrent().navigate("ingreso"));
        user.addClickListener(e -> UI.getCurrent().navigate("perfil"));

        icons.add(home, expenses, incomes, goals, user);
        return icons;
    }
}
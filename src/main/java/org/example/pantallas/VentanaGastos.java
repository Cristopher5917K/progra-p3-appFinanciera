package org.example.pantallas;

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
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.example.backend.Conexiones;
import org.example.info.Cliente;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

@Route("gasto")
public class VentanaGastos extends VerticalLayout {
    Connection conn = null;
    public VentanaGastos() {
        Cliente user = VaadinSession.getCurrent().getAttribute(Cliente.class);
        Conexiones database = new Conexiones();

        try {
            conn = database.getConnection();
        } catch (SQLException e){
            e.printStackTrace();
        }


        if (user == null){
            UI.getCurrent().navigate("login");
            return;
        }
        // 1. Configuración del fondo general de la aplicación
        this.setWidthFull();
        this.setAlignItems(Alignment.CENTER);
        this.setPadding(false);
        this.setSpacing(false);
        this.getStyle().set("background-color", "#F8F9FA");
        getStyle().set("margin", "0 auto");
        getStyle().set("padding-bottom", "60px"); // Fondo azul muy claro similar al Figma

        // 2. Contenedor principal (Simulando el ancho de un celular)
        VerticalLayout mainContainer = new VerticalLayout();
        mainContainer.setMaxWidth("390px");
        mainContainer.setPadding(false);
        mainContainer.setSpacing(true);

        // --- CONSTRUCCIÓN DE LAS SECCIONES ---

        HorizontalLayout header = crearHeader();
        double salary = user.getInitialSalary();
        double expenses = database.sumarGastosDelMes(user.getIdCliente(), conn);
        Div budgetCard = crearTarjetaPresupuesto(salary, expenses);

        // Campo: Monto del Gasto
        NumberField amountField = new NumberField("MONTO DEL GASTO");
        amountField.setPrefixComponent(new Icon(VaadinIcon.DOLLAR));
        amountField.setPlaceholder("0.00");
        amountField.setWidthFull();
        aplicarEstiloTarjeta(amountField);

        // Campo: Categoría
        ComboBox<String> categoryBox = new ComboBox<>("CATEGORÍA");
        categoryBox.setPlaceholder("Selecciona una categoría");
        // Aquí puedes cargar las categorías desde tu base de datos
        categoryBox.setItems("Alimentación", "Transporte", "Servicios", "Gasolina");
        categoryBox.setWidthFull();
        aplicarEstiloTarjeta(categoryBox);

        // Campo: Fecha
        DatePicker datePicker = new DatePicker("FECHA");
        datePicker.setValue(LocalDate.of(2025, 5, 3)); // Fecha por defecto del mockup
        datePicker.setWidthFull();
        aplicarEstiloTarjeta(datePicker);

        // Campo: Descripción
        TextArea descriptionArea = new TextArea("DESCRIPCIÓN (OPCIONAL)");
        descriptionArea.setPlaceholder("Ej: Gasolina semanal, almuerzo con clientes...");
        descriptionArea.setWidthFull();
        aplicarEstiloTarjeta(descriptionArea);

        // Botón: Guardar
        Button saveButton = new Button("Guardar gasto", new Icon(VaadinIcon.CHECK));
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR); // Color rojizo/coral
        saveButton.setWidthFull();
        saveButton.getStyle().set("border-radius", "15px");
        saveButton.getStyle().set("padding", "25px");
        saveButton.getStyle().set("margin-top", "10px");
        saveButton.addClickListener(e -> {
            // 1. Validar que los campos obligatorios no estén vacíos
            if (amountField.getValue() != null && amountField.getValue() > 0 && categoryBox.getValue() != null && datePicker.getValue() != null) {
                try {
                    // Convertir la fecha de Vaadin a SQL
                    Date sqlDate = Date.valueOf(datePicker.getValue());
                    String categoria = categoryBox.getValue();
                    double monto = amountField.getValue();

                    database.insertarMovimiento(conn, user.getIdCliente(),"GASTO", categoria.toUpperCase(), "UNICO", monto, sqlDate);
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

        // 3. Agregar todo al contenedor principal y luego a la vista
        mainContainer.add(header, budgetCard, amountField, categoryBox, datePicker, descriptionArea, saveButton);
        add(mainContainer);
    }

    // --- MÉTODOS AUXILIARES PARA MANTENER EL CÓDIGO LIMPIO ---

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
        header.expand(titleLayout); // Empuja el icono de dinero a la derecha
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

        // Cálculos matemáticos reales
        double restante = presupuestoMaximo - gastado;
        if (restante < 0) restante = 0; // Evitar que muestre números negativos si se pasó

        Span alertText = new Span("Te quedan $" + String.format("%.2f", restante) + " de tu presupuesto de $" + String.format("%.2f", presupuestoMaximo));
        alertText.getStyle().set("font-size", "14px").set("color", "#555").set("display", "block").set("margin-top", "5px");

        // Configuración de la barra de progreso
        double topeBarra = gastado > presupuestoMaximo ? presupuestoMaximo : gastado;
        ProgressBar progressBar = new ProgressBar(0, presupuestoMaximo, topeBarra);
        progressBar.getStyle().set("margin-top", "10px");

        // Cambiar color si ya se pasó del límite
        if (gastado >= presupuestoMaximo) {
            progressBar.getStyle().set("--lumo-primary-color", "#ED2100"); // Rojo
        } else if (gastado >= presupuestoMaximo * 0.8) {
            progressBar.getStyle().set("--lumo-primary-color", "#F5A623"); // Naranja
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
}
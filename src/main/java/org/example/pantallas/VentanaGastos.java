package org.example.pantallas;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;

@Route("gasto")
public class VentanaGastos extends VerticalLayout {

    public VentanaGastos() {
        // 1. Configuración del fondo general de la aplicación
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        getStyle().set("background-color", "#F4F6FB"); // Fondo azul muy claro similar al Figma

        // 2. Contenedor principal (Simulando el ancho de un celular)
        VerticalLayout mainContainer = new VerticalLayout();
        mainContainer.setMaxWidth("450px");
        mainContainer.setPadding(false);
        mainContainer.setSpacing(true);

        // --- CONSTRUCCIÓN DE LAS SECCIONES ---

        HorizontalLayout header = crearHeader();
        Div budgetCard = crearTarjetaPresupuesto();

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

        // 3. Agregar todo al contenedor principal y luego a la vista
        mainContainer.add(header, budgetCard, amountField, categoryBox, datePicker, descriptionArea, saveButton);
        add(mainContainer);
    }

    // --- MÉTODOS AUXILIARES PARA MANTENER EL CÓDIGO LIMPIO ---

    private HorizontalLayout crearHeader() {
        Button backButton = new Button(new Icon(VaadinIcon.ANGLE_LEFT));
        backButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        backButton.getStyle().set("background-color", "white").set("border-radius", "50%");

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

    private Div crearTarjetaPresupuesto() {
        Div card = new Div();
        aplicarEstiloTarjeta(card);
        card.getStyle().set("background-color", "#FFF8F2"); // Fondo naranja pastel
        card.getStyle().set("border", "1px solid #FFE4CD");

        Icon alertIcon = new Icon(VaadinIcon.WARNING);
        alertIcon.getStyle().set("color", "#F5A623");

        Span alertTitle = new Span("Presupuesto del mes");
        alertTitle.getStyle().set("color", "#F5A623").set("font-weight", "bold").set("margin-left", "10px");

        HorizontalLayout titleLayout = new HorizontalLayout(alertIcon, alertTitle);
        titleLayout.setAlignItems(Alignment.CENTER);

        Span alertText = new Span("Te quedan $1,352.00 de tu presupuesto de $3,500.00");
        alertText.getStyle().set("font-size", "14px").set("color", "#555").set("display", "block").set("margin-top", "5px");

        // Barra de progreso (Max: 3500, Valor actual: Lo ya gastado 2148)
        ProgressBar progressBar = new ProgressBar(0, 3500, 2148);
        progressBar.getStyle().set("margin-top", "10px");

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
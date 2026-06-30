package org.example.pantallas;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.example.backend.Conexiones;
import org.example.info.Meta;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Route("metas")
public class MetasView extends VerticalLayout {

    private List<Meta> goals = new ArrayList<>();
    private VerticalLayout goalsContainer = new VerticalLayout();
    private Integer idUsuario;
    private VerticalLayout header;

    public MetasView() {
        try {
            idUsuario = (Integer) VaadinSession.getCurrent().getAttribute("usuarioId");
            if (idUsuario == null) {
                idUsuario = 1; // Fallback to user 1 if not in session
            }
            createUI();
        } catch (Exception e) {
            add(new Span("Error al cargar la vista de metas: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    private void loadAndRefresh() {
        loadGoalsFromDatabase();
        sortGoals( "Más Recientes (FIFO)");
        updateSummary();
    }

    private void loadGoalsFromDatabase() {
        try (Connection conn = Conexiones.getConnection()) {
            goals = new Conexiones().getMetasByUsuario(idUsuario, conn);
        } catch (SQLException e) {
            e.printStackTrace();
            Notification.show("Error al cargar las metas desde la base de datos.");
        }
    }

    private void createUI() {
        getStyle().set("background", "#F0F4FF");
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        // Header
        header = new VerticalLayout();
        header.getStyle().set("background", "linear-gradient(160deg, #0D2B55 0%, #1a4a8a 100%)");
        header.getStyle().set("padding-bottom", "16px");
        header.setWidthFull();
        header.setPadding(false);
        header.setSpacing(false);

        H1 title = new H1("Mis Metas");
        title.getStyle().set("color", "white");
        title.getStyle().set("font-size", "24px");
        title.getStyle().set("font-weight", "800");
        title.getStyle().set("margin-left", "20px");

        Paragraph subtitle = new Paragraph("Sigue el progreso de tus objetivos");
        subtitle.getStyle().set("color", "#A9C1E8");
        subtitle.getStyle().set("font-size", "12px");
        subtitle.getStyle().set("margin-left", "20px");

        header.add(title, subtitle);

        // Filtering
        ComboBox<String> filterComboBox = new ComboBox<>("Ordenar por");
        filterComboBox.setItems("Más Recientes (FIFO)", "Menos Recientes (LIFO)", "Nombre (A-Z)", "Progreso (Mayor a Menor)");
        filterComboBox.addValueChangeListener(event -> sortGoals(event.getValue()));
        filterComboBox.setValue("Más Recientes (FIFO)");

        HorizontalLayout filterLayout = new HorizontalLayout(filterComboBox);
        filterLayout.setWidthFull();
        filterLayout.setPadding(true);

        goalsContainer.setPadding(true);
        goalsContainer.setSpacing(true);

        // FAB
        Button fab = new Button(new Icon(VaadinIcon.PLUS));
        fab.getStyle().set("position", "fixed");
        fab.getStyle().set("bottom", "20px");
        fab.getStyle().set("right", "20px");
        fab.getStyle().set("width", "56px");
        fab.getStyle().set("height", "56px");
        fab.getStyle().set("border-radius", "50%");
        fab.getStyle().set("background-color", "#27AE60");
        fab.getStyle().set("color", "white");
        fab.getStyle().set("box-shadow", "0 8px 24px rgba(39,174,96,0.45)");
        fab.addClickListener(e -> openAddGoalDialog());

        add(header, filterLayout, goalsContainer, fab);
        loadAndRefresh();
    }

    private void updateSummary() {
        header.getChildren().filter(c -> c.getId().orElse("").equals("summary-layout")).findFirst()
              .ifPresent(header::remove);

        double totalSaved = goals.stream().mapToDouble(Meta::getSavedAmount).sum();
        double totalTarget = goals.stream().mapToDouble(Meta::getTargetAmount).sum();
        int progress = (totalTarget > 0) ? (int) ((totalSaved / totalTarget) * 100) : 0;

        HorizontalLayout summary = new HorizontalLayout();
        summary.setId("summary-layout");
        summary.getStyle().set("background", "rgba(255,255,255,0.08)");
        summary.getStyle().set("border", "1px solid rgba(255,255,255,0.1)");
        summary.getStyle().set("border-radius", "16px");
        summary.setWidth("calc(100% - 40px)");
        summary.getStyle().set("margin", "20px");
        summary.setPadding(true);
        summary.setJustifyContentMode(JustifyContentMode.BETWEEN);

        summary.add(createSummaryBlock("Total ahorrado", String.format("$%,.2f", totalSaved)),
                    createSummaryBlock("Meta total", String.format("$%,.2f", totalTarget)),
                    createSummaryBlock("Progreso", progress + "%"));

        header.add(summary);
    }

    private VerticalLayout createSummaryBlock(String label, String value) {
        VerticalLayout block = new VerticalLayout();
        block.setSpacing(false);
        block.setPadding(false);
        block.setAlignItems(Alignment.CENTER);

        Span labelSpan = new Span(label);
        labelSpan.getStyle().set("color", "#A9C1E8");
        labelSpan.getStyle().set("font-size", "10px");
        labelSpan.getStyle().set("text-transform", "uppercase");

        Span valueSpan = new Span(value);
        valueSpan.getStyle().set("color", "white");
        valueSpan.getStyle().set("font-size", "18px");
        valueSpan.getStyle().set("font-weight", "700");

        block.add(labelSpan, valueSpan);
        return block;
    }

    private void sortGoals(String criteria) {
        if (criteria == null) return;
        switch (criteria) {
            case "Más Recientes (FIFO)":
                goals.sort(Comparator.comparing(Meta::getCreationDate));
                break;
            case "Menos Recientes (LIFO)":
                goals.sort(Comparator.comparing(Meta::getCreationDate).reversed());
                break;
            case "Nombre (A-Z)":
                goals.sort(Comparator.comparing(Meta::getName));
                break;
            case "Progreso (Mayor a Menor)":
                goals.sort(Comparator.comparingDouble((Meta g) -> (g.getTargetAmount() > 0 ? (g.getSavedAmount() / g.getTargetAmount()) : 0)).reversed());
                break;
        }
        refreshGoalsList();
    }

    private void refreshGoalsList() {
        goalsContainer.removeAll();
        if (goals.isEmpty()) {
            goalsContainer.add(new Span("¡Aún no tienes metas! Añade una para empezar."));
        } else {
            for (Meta goal : goals) {
                goalsContainer.add(createGoalCard(goal));
            }
        }
    }

    private VerticalLayout createGoalCard(Meta goal) {
        VerticalLayout card = new VerticalLayout();
        card.getStyle().set("background", "white");
        card.getStyle().set("border-radius", "24px");
        card.getStyle().set("box-shadow", "0 4px 16px rgba(0,0,0,0.06)");
        card.getStyle().set("padding", "16px");
        card.setWidthFull();

        H3 name = new H3(goal.getName());
        name.getStyle().set("color", "#0D2B55");
        name.getStyle().set("font-size", "16px");
        name.getStyle().set("font-weight", "700");
        name.getStyle().set("margin", "0");

        Span category = new Span(goal.getCategory());
        category.getStyle().set("color", "#64748B");
        category.getStyle().set("font-size", "12px");

        double percentage = (goal.getTargetAmount() > 0) ? (goal.getSavedAmount() / goal.getTargetAmount()) * 100 : 0;
        Div progressBarOuter = new Div();
        progressBarOuter.getStyle().set("height", "8px");
        progressBarOuter.getStyle().set("width", "100%");
        progressBarOuter.getStyle().set("background", "#F1F5F9");
        progressBarOuter.getStyle().set("border-radius", "4px");
        progressBarOuter.getStyle().set("margin-top", "12px");

        Div progressBarInner = new Div();
        progressBarInner.getStyle().set("height", "100%");
        progressBarInner.getStyle().set("width", percentage + "%");
        progressBarInner.getStyle().set("background", "linear-gradient(90deg, " + goal.getColor() + ", " + goal.getColor() + "bb)");
        progressBarInner.getStyle().set("border-radius", "4px");
        progressBarOuter.add(progressBarInner);

        Span savedAmount = new Span(String.format("$%,.2f ahorrados", goal.getSavedAmount()));
        savedAmount.getStyle().set("color", goal.getColor());
        savedAmount.getStyle().set("font-size", "12px");
        savedAmount.getStyle().set("font-weight", "700");

        Span targetAmount = new Span(String.format("Meta: $%,.2f", goal.getTargetAmount()));
        targetAmount.getStyle().set("color", "#64748B");
        targetAmount.getStyle().set("font-size", "12px");

        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), goal.getDeadline());
        Span deadline = new Span(String.format("%d días restantes", daysLeft));
        deadline.getStyle().set("color", daysLeft < 30 ? "#E74C3C" : "#64748B");
        deadline.getStyle().set("font-size", "12px");

        Button contributeButton = new Button("Aportar", new Icon(VaadinIcon.PLUS));
        contributeButton.addClickListener(e -> openContributeDialog(goal));

        Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
        deleteButton.addClickListener(e -> openDeleteDialog(goal));

        card.add(name, category, progressBarOuter, new HorizontalLayout(savedAmount, targetAmount), new HorizontalLayout(deadline, contributeButton, deleteButton));
        return card;
    }

    private void openAddGoalDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Nueva Meta");

        TextField nameField = new TextField("Nombre");
        NumberField targetAmountField = new NumberField("Monto Objetivo");
        DatePicker deadlinePicker = new DatePicker("Fecha Límite");
        TextField categoryField = new TextField("Categoría");
        TextField colorField = new TextField("Color (Hex)");

        Button saveButton = new Button("Guardar", e -> {
            try (Connection conn = Conexiones.getConnection()) {
                Meta newMeta = new Meta(0, nameField.getValue(), targetAmountField.getValue(), 0, deadlinePicker.getValue(), colorField.getValue(), categoryField.getValue(), LocalDate.now());
                if (new Conexiones().addMeta(newMeta, idUsuario, conn)) {
                    Notification.show("Meta añadida con éxito.");
                    loadAndRefresh();
                    dialog.close();
                } else {
                    Notification.show("Error al añadir la meta.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                Notification.show("Error de base de datos.");
            }
        });
        Button cancelButton = new Button("Cancelar", e -> dialog.close());

        dialog.add(nameField, targetAmountField, deadlinePicker, categoryField, colorField);
        dialog.getFooter().add(cancelButton, saveButton);
        dialog.open();
    }

    private void openContributeDialog(Meta goal) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Aportar a " + goal.getName());

        NumberField amountField = new NumberField("Monto a aportar");
        amountField.setPrefixComponent(new Span("$"));

        Button saveButton = new Button("Aportar", e -> {
            double newSavedAmount = goal.getSavedAmount() + amountField.getValue();
            goal.setSavedAmount(newSavedAmount);
            try (Connection conn = Conexiones.getConnection()) {
                if (new Conexiones().updateMeta(goal, conn)) {
                    Notification.show("Aporte realizado con éxito.");
                    loadAndRefresh();
                    dialog.close();
                } else {
                    Notification.show("Error al realizar el aporte.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                Notification.show("Error de base de datos.");
            }
        });
        Button cancelButton = new Button("Cancelar", e -> dialog.close());

        dialog.add(amountField);
        dialog.getFooter().add(cancelButton, saveButton);
        dialog.open();
    }

    private void openDeleteDialog(Meta goal) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Confirmar Eliminación");
        dialog.add(new Span("¿Estás seguro de que quieres eliminar la meta '" + goal.getName() + "'?"));

        Button deleteButton = new Button("Eliminar", e -> {
            try (Connection conn = Conexiones.getConnection()) {
                if (new Conexiones().deleteMeta(goal.getId(), conn)) {
                    Notification.show("Meta eliminada con éxito.");
                    loadAndRefresh();
                    dialog.close();
                } else {
                    Notification.show("Error al eliminar la meta.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                Notification.show("Error de base de datos.");
            }
        });
        deleteButton.getStyle().set("color", "red");
        Button cancelButton = new Button("Cancelar", e -> dialog.close());

        dialog.getFooter().add(cancelButton, deleteButton);
        dialog.open();
    }
}
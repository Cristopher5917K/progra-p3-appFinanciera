package org.example.pantallas;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.Route;

@Route("noUser")
public class ErrorScreen extends  VerticalLayout{

    public ErrorScreen(){
        this.setSizeFull();
        this.setAlignItems(Alignment.CENTER);
        this.setJustifyContentMode(JustifyContentMode.CENTER);


        H1 errorMessage = new H1("Error: No se a encontrado el usuario");
        errorMessage.setWidthFull();
        errorMessage.getStyle().set("text-align", "center");
        errorMessage.getStyle().set("margin-bottom", "20px");

        Button back_login = new Button("Volver");
        back_login.addThemeName("primary");

        add(errorMessage, back_login);


        back_login.addClickListener(event -> {
            getUI().ifPresent(ui -> ui.navigate("login"));
        });
    }
}
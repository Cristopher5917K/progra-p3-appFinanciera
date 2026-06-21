package org.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("perfil")
public class PerfilUsuarioView extends VerticalLayout {

    public PerfilUsuarioView() {
        // 1. Creamos un título llamativo para tu ventana
        H2 titulo = new H2("Perfil de Usuario");

        // 2. Creamos los campos donde se mostrará la información financiera/personal
        TextField txtNombre = new TextField();
        txtNombre.setLabel("Nombre Completo");
        txtNombre.setPlaceholder("Ej. Juan Pérez"); // Texto de guía gris de fondo

        TextField txtEmail = new TextField();
        txtEmail.setLabel("Correo Electrónico");

        // 3. Creamos el botón de acción
        Button btnGuardar = new Button("Guardar Cambios");

        // 4. ¡EL PASO CLAVE! Agregamos los componentes al contenedor vertical
        // El orden en que los pongas aquí adentro es el orden en que aparecerán hacia abajo en la web
        add(titulo, txtNombre, txtEmail, btnGuardar);
    }
}
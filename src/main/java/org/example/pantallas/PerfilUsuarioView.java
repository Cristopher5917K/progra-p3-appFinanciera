package org.example.pantallas;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.server.VaadinSession;
import org.example.backend.Conexiones;
import org.example.info.Cliente;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.server.VaadinSession;

import java.sql.Connection;
import java.sql.SQLException;

@Route("perfil")
public class PerfilUsuarioView extends VerticalLayout implements BeforeEnterObserver {

    private Integer idUsuario;
    private Connection con;

    private Span lblNombreCompleto;
    private Span lblCorreo;
    private Span lblCedula;
    private Span lblSueldo;

    private TextField txtNombre;
    private TextField txtApellido;
    private TextField txtCorreo;
    private TextField txtCedula;
    private TextField txtSueldo;

    private Button btnEditar;
    private Button btnGuardar;
    private Button btnCancelar;
    private HorizontalLayout botonesAccion;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        idUsuario = (Integer) VaadinSession.getCurrent().getAttribute("usuarioId");
        if (idUsuario == null) {
            event.rerouteTo("");
        } else {
            try {
                createUI();
            } catch (Exception e) {
                add(new Span("Error al cargar la vista de perfil: " + e.getMessage()));
                e.printStackTrace();
            }
        }
    }

    private void createUI() {
        removeAll();
        setSizeUndefined();
        setWidthFull();
        setWidth("390px");
        getStyle().set("background-color", "#F8F9FA");
        getStyle().set("margin", "0 auto");
        getStyle().set("padding-bottom", "60px");
        setPadding(false);
        setSpacing(false);

        VerticalLayout container = new VerticalLayout();
        container.setWidth("100%");
        container.setPadding(false);

        VerticalLayout seccionPerfil = new VerticalLayout();
        seccionPerfil.getStyle().set("background-color", "#203a60");
        seccionPerfil.setWidth("100%");
        seccionPerfil.getStyle().set("border-radius", "15px");
        seccionPerfil.getStyle().set("border-top-left-radius","0px");
        seccionPerfil.getStyle().set("border-top-right-radius","0px");

        VerticalLayout mainTitle = new VerticalLayout();
        mainTitle.setWidth("100%");
        mainTitle.setPadding(false);

        H2 titulo = new H2("Mi Perfil");
        titulo.getStyle().set("color", "white");
        titulo.getStyle().set("margin", "0");
        mainTitle.add(titulo);

        HorizontalLayout userCard = new HorizontalLayout();
        userCard.getStyle().set("background-color", "rgba(255, 255, 255, 0.05)");
        userCard.getStyle().set("backdrop-filter", "blur(10px)");
        userCard.setWidth("100%");
        userCard.getStyle().set("border-radius", "10px");
        userCard.getStyle().set("padding", "15px");
        userCard.getStyle().set("flex-wrap", "wrap");
        userCard.setAlignItems(Alignment.CENTER);

        Conexiones database = new Conexiones();
        String nombre="";
        String apellido="";
        String cedula="";
        String correo="";
        double sueldo=0.0;

        try{
            con = Conexiones.getConnection();
            // Load cliente data by idUsuario (id_cliente stored in session)
            Cliente cliente = new Conexiones().userInfoById(idUsuario, con);

            if (cliente == null){
                UI.getCurrent().navigate("login");
                return;
            }

            nombre = cliente.getNameCliente();
            apellido = cliente.getApellidoCliente();
            cedula = cliente.getCedula();
            correo = database.correoUser(cliente.getIdCliente(), con); // correo lives in 'usuarios' table; optional to fetch it later
            sueldo = cliente.getInitialSalary();
        

            String inicialNombre = (nombre != null && !nombre.isEmpty()) ? nombre.substring(0,1).toUpperCase() : "";
            String inicialApellido = (apellido != null && !apellido.isEmpty()) ? apellido.substring(0,1).toUpperCase() : "";
            String iniciales = inicialNombre + inicialApellido;

            VerticalLayout avatar = new VerticalLayout();
            avatar.setWidth("75px");
            avatar.setHeight("75px");
            avatar.getStyle().set("background-color", "#2ecc71");
            avatar.getStyle().set("border-radius", "12px");
            avatar.setJustifyContentMode(JustifyContentMode.CENTER);
            avatar.setAlignItems(Alignment.CENTER);
            avatar.setPadding(false);

            Span txtIniciales = new Span(iniciales);
            txtIniciales.getStyle().set("color", "white");
            txtIniciales.getStyle().set("font-weight", "bold");
            txtIniciales.getStyle().set("font-size", "22px");
            avatar.add(txtIniciales);

            lblNombreCompleto = new Span(nombre + " " + apellido);
            lblNombreCompleto.getStyle().set("color", "white");
            lblNombreCompleto.getStyle().set("font-weight", "bold");
            lblNombreCompleto.getStyle().set("font-size", "20px");

            lblCorreo = new Span("Correo: " + correo);
            lblCorreo.getStyle().set("color", "#8fa3bf");

            lblCedula = new Span("Cedula: " + cedula);
            lblCedula.getStyle().set("color", "#8fa3bf");

            lblSueldo = new Span("Sueldo: $" + sueldo);
            lblSueldo.getStyle().set("color", "#8fa3bf");

            txtNombre = new TextField("Nombre");
            txtNombre.setValue(nombre);
            txtNombre.setVisible(false);
            txtNombre.setWidthFull();

            txtApellido = new TextField("Apellido");
            txtApellido.setValue(apellido);
            txtApellido.setVisible(false);
            txtApellido.setWidthFull();

            txtCorreo = new TextField("Correo");
            txtCorreo.setValue(correo);
            txtCorreo.setVisible(false);
            txtCorreo.setWidthFull();

            txtCedula = new TextField("Cédula");
            txtCedula.setValue(cedula);
            txtCedula.setVisible(false);
            txtCedula.setWidthFull();

            txtSueldo = new TextField("Sueldo");
            txtSueldo.setValue(String.valueOf(sueldo));
            txtSueldo.setVisible(false);
            txtSueldo.setWidthFull();

            btnEditar = new Button(VaadinIcon.EDIT.create());
            btnEditar.getStyle().set("border-radius", "50%");
            btnEditar.getStyle().set("width", "40px");
            btnEditar.getStyle().set("height", "40px");
            btnEditar.getStyle().set("min-width", "40px");
            btnEditar.getStyle().set("background-color", "#28a745");
            btnEditar.getStyle().set("color", "white");
            btnEditar.getStyle().set("cursor", "pointer");

            btnGuardar = new Button("✓ Guardar");
            btnGuardar.getStyle().set("background-color", "#28a745");
            btnGuardar.getStyle().set("color", "white");
            btnGuardar.getStyle().set("font-weight", "bold");
            btnGuardar.setWidthFull();

            btnCancelar = new Button("✕ Cancelar");
            btnCancelar.getStyle().set("background-color", "#dc3545");
            btnCancelar.getStyle().set("color", "white");
            btnCancelar.getStyle().set("font-weight", "bold");
            btnCancelar.setWidthFull();

            btnEditar.addClickListener(event -> toggleModoEdicion(true));
            btnGuardar.addClickListener(event -> guardarCambios());
            btnCancelar.addClickListener(event -> toggleModoEdicion(false));

            VerticalLayout infoUsuario = new VerticalLayout();
            infoUsuario.setPadding(false);
            infoUsuario.setSpacing(true);
            infoUsuario.setWidth("100%");

            infoUsuario.add(lblNombreCompleto, lblCorreo, lblCedula, lblSueldo);
            infoUsuario.add(txtNombre, txtApellido, txtCorreo, txtCedula, txtSueldo);
            
            botonesAccion = new HorizontalLayout(btnGuardar, btnCancelar);
            botonesAccion.setSpacing(true);
            botonesAccion.setWidthFull();
            botonesAccion.setVisible(false);
            
            infoUsuario.add(botonesAccion);
            
            userCard.add(avatar, infoUsuario);
            seccionPerfil.add(mainTitle, userCard, btnEditar);
            container.add(seccionPerfil);

            VerticalLayout containerActividad = new VerticalLayout();
            containerActividad.setWidth("100%");
            containerActividad.setPadding(false);
            containerActividad.setSpacing(false);
            containerActividad.getStyle().set("margin-top", "20px");

            H4 tituloActividad = new H4("Actividad");
            tituloActividad.getStyle().set("color", "#94A3B8");
            tituloActividad.getStyle().set("padding","15px");
            containerActividad.add(tituloActividad);

            /*VerticalLayout containerMetas = new VerticalLayout();
            containerMetas.setWidth("100%");
            containerMetas.getStyle().set("background-color", "white");
            containerMetas.getStyle().set("border-radius", "15px");
            containerMetas.getStyle().set("cursor", "pointer");
            containerMetas.addClickListener(e -> UI.getCurrent().navigate("metas"));

            Icon iconMetas = VaadinIcon.BULLSEYE.create();
            iconMetas.getStyle().set("color", "black");
            iconMetas.getStyle().set("font-size", "24px");

            H3 tituloMetas = new H3("METAS");
            tituloMetas.getStyle().set("color", "black");
            tituloMetas.getStyle().set("margin", "0");

            HorizontalLayout contentMetas = new HorizontalLayout(iconMetas, tituloMetas);
            contentMetas.setAlignItems(Alignment.CENTER);
            contentMetas.setSpacing(true);
            contentMetas.getStyle().set("margin", "auto");

            containerMetas.add(contentMetas);
            containerActividad.add(containerMetas);
            container.add(containerActividad);

            VerticalLayout containerDashboard = new VerticalLayout();
            containerDashboard.setWidth("100%");
            containerDashboard.getStyle().set("background-color", "white");
            containerDashboard.getStyle().set("border-radius", "15px");
            containerDashboard.getStyle().set("cursor", "pointer");
            containerDashboard.addClickListener(e -> UI.getCurrent().navigate("dashboard"));

            Icon iconDashboard = VaadinIcon.HOME_O.create();
            iconDashboard.getStyle().set("color", "black");
            iconDashboard.getStyle().set("font-size", "24px");

            H3 titleDashboard = new H3("DASHBOARD");
            titleDashboard.getStyle().set("color", "black");
            titleDashboard.getStyle().set("margin", "0");

            HorizontalLayout contentDashboard = new HorizontalLayout(iconDashboard, titleDashboard);
            contentDashboard.setAlignItems(Alignment.CENTER);
            contentDashboard.setSpacing(true);
            contentDashboard.getStyle().set("margin", "auto");

            containerDashboard.add(contentDashboard);
            containerActividad.add(containerDashboard);
            container.add(containerDashboard);

            VerticalLayout containerExpenses = new VerticalLayout();
            containerExpenses.setWidth("100%");
            containerExpenses.getStyle().set("background-color", "white");
            containerExpenses.getStyle().set("border-radius", "15px");
            containerExpenses.getStyle().set("cursor", "pointer");
            containerExpenses.addClickListener(e -> UI.getCurrent().navigate("gasto"));

            Icon iconExpenses = VaadinIcon.WALLET.create();
            iconExpenses.getStyle().set("color", "black");
            iconExpenses.getStyle().set("font-size", "24px");

            H3 titleExpense = new H3("GASTOS");
            titleExpense.getStyle().set("color", "black");
            titleExpense.getStyle().set("margin", "0");

            HorizontalLayout contentExpenses = new HorizontalLayout(iconExpenses, titleExpense);
            contentExpenses.setAlignItems(Alignment.CENTER);
            contentExpenses.setSpacing(true);
            contentExpenses.getStyle().set("margin", "auto");

            containerExpenses.add(contentExpenses);
            containerActividad.add(containerExpenses);
            container.add(containerExpenses);

            VerticalLayout containerIncome = new VerticalLayout();
            containerIncome.setWidth("100%");
            containerIncome.getStyle().set("background-color", "white");
            containerIncome.getStyle().set("border-radius", "15px");
            containerIncome.getStyle().set("cursor", "pointer");
            containerIncome.addClickListener(e -> UI.getCurrent().navigate("ingreso"));

            Icon iconIncome = VaadinIcon.MONEY.create();
            iconIncome.getStyle().set("color", "black");
            iconIncome.getStyle().set("font-size", "24px");

            H3 titleIncome = new H3("INGRESOS");
            titleIncome.getStyle().set("color", "black");
            titleIncome.getStyle().set("margin", "0");

            HorizontalLayout contentIncomes = new HorizontalLayout(iconIncome, titleIncome);
            contentIncomes.setAlignItems(Alignment.CENTER);
            contentIncomes.setSpacing(true);
            contentIncomes.getStyle().set("margin", "auto");

            containerIncome.add(contentIncomes);
            containerActividad.add(containerIncome);
            container.add(containerIncome);*/

            Button logoutButton = new Button("Cerrar Sesión");
            logoutButton.setWidthFull();
            logoutButton.getStyle().set("background-color", "#FFF0F0");
            logoutButton.getStyle().set("color", "#DC3545"); // Texto rojo
            logoutButton.getStyle().set("border", "1px solid #DC3545");
            logoutButton.getStyle().set("border-radius", "12px");
            logoutButton.getStyle().set("margin-top", "30px"); // Separa el botón de las opciones de arriba
            logoutButton.getStyle().set("margin-bottom", "80px");
            logoutButton.addClickListener(e -> {
                VaadinSession.getCurrent().getSession().invalidate();
                UI.getCurrent().navigate("");
            });

            Button btnEliminarCuenta = new Button("Eliminar Cuenta", new Icon(VaadinIcon.TRASH));
            btnEliminarCuenta.setWidthFull();
            btnEliminarCuenta.getStyle().set("background-color", "#FFF0F0");
            btnEliminarCuenta.getStyle().set("color", "#DC3545"); // Rojo de alerta
            btnEliminarCuenta.getStyle().set("border", "1px solid #DC3545");
            btnEliminarCuenta.getStyle().set("border-radius", "12px");
            btnEliminarCuenta.getStyle().set("margin-top", "30px");
            btnEliminarCuenta.getStyle().set("margin-bottom", "80px"); // Respiro para la barra inferior

            // Al hacer clic, NO borramos de inmediato, abrimos la validación
            btnEliminarCuenta.addClickListener(e -> abrirDialogoConfirmacionEliminar());
            container.add(
                    containerActividad,
                    crearBotonActividad("DASHBOARD", VaadinIcon.HOME_O, "metas"),
                    crearBotonActividad("GASTOS", VaadinIcon.WALLET, "gastos"),
                    crearBotonActividad("INGRESOS", VaadinIcon.MONEY, "ingresos"),
                    crearBotonActividad("METAS", VaadinIcon.BULLSEYE, "metas"),
                    logoutButton,
                    btnEliminarCuenta
            );

            this.add(container, navigationBar());

        } catch (Exception e) {
            add(new Span("Error al conectar a la base de datos: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    private VerticalLayout crearBotonActividad(String titulo, VaadinIcon iconType, String ruta) {
        VerticalLayout boton = new VerticalLayout();
        boton.setWidth("100%");
        boton.getStyle().set("background-color", "white");
        boton.getStyle().set("border-radius", "15px");
        boton.getStyle().set("cursor", "pointer");
        boton.getStyle().set("transition", "background-color 0.3s");
        boton.setPadding(true);

        // Efectos de Hover (sombra al pasar el mouse/dedo)
        boton.getElement().addEventListener("mouseover", e -> boton.getStyle().set("background-color", "#F0F0F0"));
        boton.getElement().addEventListener("mouseout", e -> boton.getStyle().set("background-color", "white"));

        // Navegación
        boton.addClickListener(e -> UI.getCurrent().navigate(ruta));

        // --- EL TRUCO DE LA ALINEACIÓN ---

        Icon icono = iconType.create();
        icono.getStyle().set("color", "black");
        icono.setSize("24px");
        // 1. Obligamos al ícono a ocupar exactamente 45px de ancho, sin encogerse
        icono.getElement().getStyle().set("width", "45px");
        icono.getElement().getStyle().set("flex-shrink", "0");

        H3 texto = new H3(titulo);
        texto.getStyle().set("color", "black");
        texto.getStyle().set("margin", "0");
        texto.getStyle().set("font-size", "1.1rem");

        HorizontalLayout contenido = new HorizontalLayout(icono, texto);
        contenido.setWidthFull();
        contenido.setAlignItems(Alignment.CENTER);
        // 2. Empujamos todo hacia la izquierda
        contenido.setJustifyContentMode(JustifyContentMode.START);
        // 3. Le damos un margen interno para que no quede pegado al borde izquierdo del celular
        contenido.getStyle().set("padding-left", "25px");

        boton.add(contenido);
        return boton;
    }

    private void toggleModoEdicion(boolean editar) {
        lblNombreCompleto.setVisible(!editar);
        lblCorreo.setVisible(!editar);
        lblCedula.setVisible(!editar);
        lblSueldo.setVisible(!editar);
        btnEditar.setVisible(!editar);

        txtNombre.setVisible(editar);
        txtApellido.setVisible(editar);
        txtCorreo.setVisible(editar);
        txtCedula.setVisible(editar);
        txtSueldo.setVisible(editar);
        botonesAccion.setVisible(editar);
    }

    private void guardarCambios() {
        if (txtNombre.isEmpty() || txtApellido.isEmpty() || txtCedula.isEmpty() || txtSueldo.isEmpty()) {
            Notification.show("Por favor, no dejes campos en blanco.", 3000, Notification.Position.MIDDLE);
            return;
        }

        double sueldo = 0.0;
        try {
            sueldo = Double.parseDouble(txtSueldo.getValue());
        } catch (NumberFormatException e) {
            Notification.show("El sueldo debe ser un número válido.", 3000, Notification.Position.MIDDLE);
            return;
        }

        String nombre = txtNombre.getValue();
        String apellido = txtApellido.getValue();
        String cedula = txtCedula.getValue();

        Conexiones db = new Conexiones();
        boolean actualizado = db.updateUserProfile(idUsuario, nombre, apellido, cedula, sueldo, con);

        if (actualizado) {
            // Actualizar la sesión para que el Dashboard vea los datos nuevos
            Cliente userSession = VaadinSession.getCurrent().getAttribute(Cliente.class);
            if (userSession != null) {
                userSession.setNameCliente(nombre);
                userSession.setApellidoCliente(apellido);
                userSession.setInitialSalary(sueldo);
            }

            Notification.show("Perfil actualizado con éxito", 3000, Notification.Position.TOP_CENTER);
            toggleModoEdicion(false);

            // Recargar para que el Avatar genere las iniciales correctas
            UI.getCurrent().getPage().reload();
        } else {
            Notification.show("Error al actualizar el perfil", 3000, Notification.Position.TOP_CENTER);
        }
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

        user.setColor("#28a745");
        home.addClickListener(e -> UI.getCurrent().navigate("dashboard"));
        goals.addClickListener(e -> UI.getCurrent().navigate("metas"));
        incomes.addClickListener(e -> UI.getCurrent().navigate("ingreso"));
        expenses.addClickListener(e -> UI.getCurrent().navigate("gasto"));

        icons.add(home, expenses, incomes, goals, user);
        return icons;
    }

    private void abrirDialogoConfirmacionEliminar() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Eliminar Cuenta Definitivamente");
        dialog.setWidth("350px");

        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);

        Span textoAdvertencia = new Span("¿Estás completamente seguro? Esta acción eliminará tu perfil, historial de movimientos y metas. Esta acción NO se puede deshacer.");
        textoAdvertencia.getStyle().set("color", "#64748B");
        textoAdvertencia.getStyle().set("font-size", "0.9rem");
        textoAdvertencia.getStyle().set("text-align", "justify");

        layout.add(textoAdvertencia);
        dialog.add(layout);

        // Botón seguro (Cancelar)
        Button btnCancelar = new Button("Cancelar", e -> dialog.close());

        // Botón destructivo (Eliminar)
        Button btnConfirmarEliminar = new Button("Sí, eliminar mi cuenta", e -> {

            Conexiones db = new Conexiones();
            // Aseguramos el try-with-resources para no dejar conexiones fantasma
            try (Connection conn = db.getConnection()) {

                boolean eliminado = db.eliminarUsuarioCompleto(idUsuario, conn);

                if (eliminado) {
                    VaadinSession.getCurrent().getSession().invalidate();
                    dialog.close();
                    UI.getCurrent().navigate("");
                } else {
                    Notification.show("Hubo un problema al intentar eliminar tu cuenta.", 3000, Notification.Position.TOP_CENTER);
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                Notification.show("Error de servidor al intentar eliminar.", 3000, Notification.Position.TOP_CENTER);
            }
        });

        btnConfirmarEliminar.getStyle().set("background-color", "#DC3545");
        btnConfirmarEliminar.getStyle().set("color", "white");

        // Añadimos los botones al footer del popup
        dialog.getFooter().add(btnCancelar, btnConfirmarEliminar);

        dialog.open();
    }
}
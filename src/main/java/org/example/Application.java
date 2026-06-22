package org.example;

import com.vaadin.flow.component.page.Viewport;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;

@SpringBootApplication
@StyleSheet("styles.css") // Your custom styles
@Push
@Viewport("width=device-width, initial-scale=1.0")
public class Application implements AppShellConfigurator {

    //Eres maricon cris

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}

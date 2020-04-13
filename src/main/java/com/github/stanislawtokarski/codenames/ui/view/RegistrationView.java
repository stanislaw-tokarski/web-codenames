package com.github.stanislawtokarski.codenames.ui.view;

import com.github.stanislawtokarski.codenames.ui.component.CodenamesTabs;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class RegistrationView extends VerticalLayout {

    private final TextField nameField = new TextField("Your name:");
    private final Button registerButton = new Button("Let me in!");
    private final TextField godTokenField = new TextField("Secret God token:");
    private final Button registerAsGodButton = new Button("Let the God in!");

    public RegistrationView() {
        var registerHeader = new H5("Complete your registration below so you can join the game");
        var registerAsGodHeader = new H5("...or try joining as God!");
        add(new CodenamesTabs("registration"), registerHeader, nameField, registerButton,
                registerAsGodHeader, godTokenField, registerAsGodButton);
    }

    public void initListeners(
            ComponentEventListener<ClickEvent<Button>> registerButtonClickedListener,
            ComponentEventListener<ClickEvent<Button>> registerAsGodButtonClickedListener) {
        registerButton.addClickListener(registerButtonClickedListener);
        registerAsGodButton.addClickListener(registerAsGodButtonClickedListener);
    }

    public String getName() {
        var name = nameField.getValue();
        nameField.clear();
        return name;
    }

    public char[] getToken() {
        var token = godTokenField.getValue().toCharArray();
        godTokenField.clear();
        return token;
    }

    public void showNotification(String message) {
        Notification.show(message, 2000, Notification.Position.MIDDLE);
    }

    public void setRolesRestrictions(boolean alreadyRegistered) {
        registerButton.setEnabled(!alreadyRegistered);
        registerAsGodButton.setEnabled(!alreadyRegistered);
    }
}

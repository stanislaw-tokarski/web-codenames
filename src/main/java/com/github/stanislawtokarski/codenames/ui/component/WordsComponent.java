package com.github.stanislawtokarski.codenames.ui.component;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;

import java.util.stream.Stream;

import static com.vaadin.flow.component.button.ButtonVariant.LUMO_ERROR;
import static com.vaadin.flow.component.button.ButtonVariant.LUMO_SUCCESS;

public class WordsComponent extends VerticalLayout {

    private final TextArea textArea = new TextArea("Paste words below");
    private final Button submitWordsButton = new Button("Submit");
    private final Button clearWordsButton = new Button("Clear");
    private final Button defaultWordsButton = new Button("Default");
    private final Details wordsLeftDetails = new Details();

    public WordsComponent() {
        wordsLeftDetails.setSummaryText("Words left:");
        wordsLeftDetails.setOpened(true);
        textArea.setPlaceholder("And don't forget to submit them");
        textArea.setHeight("500px");
        textArea.setWidth("250px");
        submitWordsButton.addThemeVariants(LUMO_SUCCESS);
        clearWordsButton.addThemeVariants(LUMO_ERROR);
        var buttonsLayout = new HorizontalLayout(submitWordsButton, clearWordsButton, defaultWordsButton);
        add(textArea, wordsLeftDetails, buttonsLayout);
    }

    public void initListeners(
            ComponentEventListener<ClickEvent<Button>> submitButtonClickedListener,
            ComponentEventListener<ClickEvent<Button>> clearButtonClickedListener,
            ComponentEventListener<ClickEvent<Button>> defaultButtonClickedListener) {
        submitWordsButton.addClickListener(submitButtonClickedListener);
        clearWordsButton.addClickListener(clearButtonClickedListener);
        defaultWordsButton.addClickListener(defaultButtonClickedListener);
    }

    public String getWords() {
        return textArea.getValue();
    }

    public void setWords(String words) {
        textArea.setValue(words);
    }

    public void clearWords() {
        textArea.clear();
    }

    public void setWordsLeft(int wordsLeft) {
        wordsLeftDetails.setContent(new Text(String.valueOf(wordsLeft)));
    }

    public void setRolesRestrictions(boolean isRegistered, boolean isGod) {
        Stream.of(textArea, submitWordsButton, clearWordsButton, defaultWordsButton)
                .forEach(component -> component.setEnabled(isRegistered && isGod));
    }
}

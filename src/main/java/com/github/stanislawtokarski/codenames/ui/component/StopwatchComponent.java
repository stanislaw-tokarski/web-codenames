package com.github.stanislawtokarski.codenames.ui.component;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;

import java.util.stream.IntStream;

import static com.vaadin.flow.component.button.ButtonVariant.LUMO_ERROR;
import static com.vaadin.flow.component.button.ButtonVariant.LUMO_SUCCESS;
import static java.util.stream.Collectors.toUnmodifiableList;

public class StopwatchComponent extends VerticalLayout {

    private final ComboBox<Integer> comboBox = new ComboBox<>("Final countdown [s]:");
    private final Button startButton = new Button("Start");
    private final Button resetButton = new Button("Reset");
    private final ProgressBar progressBar = new ProgressBar();

    public StopwatchComponent() {
        var values = IntStream.iterate(30, i -> i < 301, i -> i + 30)
                .boxed()
                .filter(i -> i % 30 == 0)
                .collect(toUnmodifiableList());
        comboBox.setItems(values);
        comboBox.setValue(60);
        comboBox.setAllowCustomValue(false);
        startButton.addThemeVariants(LUMO_SUCCESS);
        resetButton.addThemeVariants(LUMO_ERROR);
        var configLayout = new HorizontalLayout(comboBox, startButton, resetButton);
        configLayout.setVerticalComponentAlignment(Alignment.END, startButton);
        configLayout.setVerticalComponentAlignment(Alignment.END, resetButton);
        add(configLayout, progressBar);
    }

    public void initListeners(
            ComponentEventListener<ClickEvent<Button>> startButtonClickedListener,
            ComponentEventListener<ClickEvent<Button>> resetButtonClickedListener) {
        startButton.addClickListener(startButtonClickedListener);
        resetButton.addClickListener(resetButtonClickedListener);
    }

    public void configureButtons(boolean inProgress) {
        startButton.setEnabled(!inProgress);
        resetButton.setEnabled(inProgress);
    }

    public int getConfiguredTime() {
        return comboBox.getValue();
    }

    public void setCurrentProgress(double min, double max, double current) {
        progressBar.setMin(min);
        progressBar.setMax(max);
        progressBar.setValue(current);
    }

    public void setCurrentPeriod(int period) {
        comboBox.setValue(period);
    }

    public void setRolesRestrictions(boolean isRegistered) {
        var startEnabled = startButton.isEnabled();
        var resetEnabled = resetButton.isEnabled();
        comboBox.setEnabled(isRegistered);
        startButton.setEnabled(isRegistered && startEnabled);
        resetButton.setEnabled(isRegistered && resetEnabled);
    }
}

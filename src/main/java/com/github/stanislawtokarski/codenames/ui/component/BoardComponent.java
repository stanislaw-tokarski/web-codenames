package com.github.stanislawtokarski.codenames.ui.component;

import com.github.stanislawtokarski.codenames.model.Board;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.List;

import static com.github.stanislawtokarski.codenames.model.CardType.INNOCENT;
import static com.github.stanislawtokarski.codenames.util.ImageLoader.getFor;
import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;

public class BoardComponent extends HorizontalLayout {

    private final boolean readOnly;
    private final boolean tiny;
    private final List<Button> fields;

    public BoardComponent(boolean readOnly, boolean tiny) {
        this.readOnly = readOnly;
        this.tiny = tiny;
        setPadding(false);
        setSpacing(false);
        fields = buildBoard();
    }

    public void initListeners(ComponentEventListener<ClickEvent<Button>> boardButtonClickedListener) {
        fields.forEach(button -> button.addClickListener(boardButtonClickedListener));
    }

    public void updateBoard(List<Board.Field> updatedFields) {
        fields.forEach(button -> {
            var combinedId = parseInt(button.getId().get());
            var field = updatedFields.get(combinedId);
            if (readOnly) {
                forReadOnlyBoard(button, field);
            } else {
                forClickableBoard(button, field);
            }
        });
    }

    public void disableButton(int id) {
        fields.stream()
                .filter(b -> valueOf(id).equals(b.getId().get()))
                .findFirst()
                .get()
                .setEnabled(false);
    }

    public void setRolesRestrictions(boolean isRegistered) {
        fields.forEach(button -> button.setEnabled(isRegistered));
    }

    private List<Button> buildBoard() {
        var buttons = new ArrayList<Button>(25);
        var currentIndex = 0;
        for (int i = 0; i < 5; i++) {
            var rowLayout = new VerticalLayout();
            rowLayout.setPadding(true);
            rowLayout.setSpacing(false);
            for (int j = 0; j < 5; j++) {
                var button = new Button();
                if (tiny) {
                    button.setIcon(getFor(INNOCENT, true));
                    button.setWidth("30px");
                    button.setHeight("30px");
                } else {
                    button.setText(currentIndex % 2 == 0 ? "CODENAMES" : "CODE4IT");
                    button.setWidth("170px");
                    button.setHeight("115px");
                }
                button.setEnabled(!readOnly);
                button.setId(valueOf(currentIndex));
                currentIndex++;
                rowLayout.add(button);
                buttons.add(button);
            }
            add(rowLayout);
        }
        return buttons;
    }

    private void forClickableBoard(Button button, Board.Field field) {
        if (field.isClicked()) {
            button.setEnabled(false);
            button.setIcon(getFor(field.getType(), tiny));
        } else {
            button.setText(field.getWord());
        }
    }

    private void forReadOnlyBoard(Button button, Board.Field field) {
        button.setIcon(getFor(field.getType(), tiny));
    }
}

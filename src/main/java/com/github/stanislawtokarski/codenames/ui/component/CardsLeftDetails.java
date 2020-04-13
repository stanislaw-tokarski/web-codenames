package com.github.stanislawtokarski.codenames.ui.component;

import com.github.stanislawtokarski.codenames.model.CardType;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.Map;
import java.util.stream.Stream;

import static com.github.stanislawtokarski.codenames.util.ImageLoader.getFor;
import static java.lang.String.valueOf;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class CardsLeftDetails extends HorizontalLayout {

    private final Map<CardType, CardDetails> stats;

    public CardsLeftDetails() {
        stats = Stream.of(CardType.values())
                .map(CardDetails::new)
                .collect(toMap(CardDetails::getCardType, identity()));
        add(stats.values().toArray(CardDetails[]::new));
    }

    public void setCardsLeft(Map<CardType, Integer> cardsLeft) {
        cardsLeft.forEach((cardType, count) -> stats.get(cardType).setDetails(new Text(valueOf(count))));
    }

    private static final class CardDetails extends VerticalLayout {

        private final Details details = new Details();
        private final CardType cardType;

        public CardDetails(CardType cardType) {
            this.cardType = cardType;
            details.setSummaryText("Cards left:");
            details.setContent(new Text("N/A"));
            details.setOpened(true);
            var image = getFor(cardType, false);
            image.setWidth("118px");
            image.setHeight("79px");
            add(details, image);
        }

        public CardType getCardType() {
            return cardType;
        }

        public void setDetails(Component details) {
            this.details.setContent(null);
            this.details.addContent(details);
        }
    }
}

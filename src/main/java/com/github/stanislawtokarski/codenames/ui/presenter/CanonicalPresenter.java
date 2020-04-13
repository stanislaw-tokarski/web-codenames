package com.github.stanislawtokarski.codenames.ui.presenter;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.spring.annotation.UIScope;
import org.slf4j.Logger;

import java.lang.reflect.ParameterizedType;

import static org.slf4j.LoggerFactory.getLogger;

@UIScope
public class CanonicalPresenter<V extends Component> extends Main {

    private static final Logger log = getLogger(CanonicalPresenter.class);
    protected V view;

    protected CanonicalPresenter() {
        try {
            this.view = ((Class<V>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0])
                    .getDeclaredConstructor()
                    .newInstance();
            add(this.view);
            UI.getCurrent().addBeforeEnterListener(e -> refresh());
        } catch (Exception ex) {
            log.error("Cannot initialize view", ex);
        }
    }

    protected void refresh() {
    }

    protected String getSessionId() {
        return UI.getCurrent().getSession().getSession().getId();
    }
}
package com.github.stanislawtokarski.codenames.ui.presenter;

import com.github.stanislawtokarski.codenames.service.PlayersHolder;
import com.github.stanislawtokarski.codenames.ui.view.RegistrationView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Route("registration")
@PageTitle("Codenames - Registration")
@Theme(value = Lumo.class, variant = Lumo.DARK)
public class RegistrationPresenter extends CanonicalPresenter<RegistrationView> {

    private final PlayersHolder playersHolder;
    private final String godToken;

    public RegistrationPresenter(@Value("${god.secret.token}") String godToken, PlayersHolder playersHolder) {
        this.godToken = godToken;
        this.playersHolder = playersHolder;
    }

    @Override
    protected void refresh() {
        var alreadyRegistered = playersHolder.existsForSession(getSessionId());
        view.setRolesRestrictions(alreadyRegistered);
    }

    @PostConstruct
    private void initViewListeners() {
        view.initListeners(
                e -> onRegisterButtonClicked(),
                e -> onRegisterAsGodButtonClicked());
        refresh();
    }

    private void onRegisterButtonClicked() {
        var name = view.getName();
        if (isNotBlank(name)) {
            playersHolder.addPlayer(null, name, false, false, null, null, getSessionId());
            view.showNotification("Here you go!");
            UI.getCurrent().navigate("");
        } else {
            view.showNotification("Please be so kind and provide us with a proper name");
        }
        refresh();
    }

    private void onRegisterAsGodButtonClicked() {
        var name = view.getName();
        if (isNotBlank(name) && godToken.equals(new String(view.getToken()))) {
            playersHolder.addPlayer(null, name, true, false, null, null, getSessionId());
            view.showNotification("God is alive!");
            UI.getCurrent().navigate("god");
        } else {
            view.showNotification("You shall not pass!");
        }
        refresh();
    }
}

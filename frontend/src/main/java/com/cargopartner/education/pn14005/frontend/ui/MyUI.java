package com.cargopartner.education.pn14005.frontend.ui;

import javax.inject.Inject;

import com.vaadin.annotations.Theme;
import com.vaadin.cdi.CDIUI;
import com.vaadin.cdi.CDIViewProvider;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

@CDIUI("")
@Theme("mytheme")
public class MyUI  extends UI{	

    @Inject
    private CDIViewProvider viewProvider;

    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle("Training Application");
        final Navigator navigator = new Navigator(this, this);
        navigator.addProvider(viewProvider);
        navigator.navigateTo(MainView.VIEW_NAME);
    }   
}

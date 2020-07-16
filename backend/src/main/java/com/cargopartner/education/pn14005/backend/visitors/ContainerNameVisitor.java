package com.cargopartner.education.pn14005.backend.visitors;

import com.cargopartner.education.pn14005.backend.entity.RefrigeratorContainer;
import com.cargopartner.education.pn14005.backend.entity.StandardContainer;

public class ContainerNameVisitor implements ContainerVisitor {

    @Override
    public String visit(StandardContainer standardContainer) {
        return standardContainer.getName() + " (Standard)";
    }

    @Override
    public String visit(RefrigeratorContainer refrigeratorContainer) {
        return refrigeratorContainer.getName() + " (Refrigerator)";
    }
}
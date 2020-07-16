package com.cargopartner.education.pn14005.backend.visitors;

import com.cargopartner.education.pn14005.backend.entity.RefrigeratorContainer;
import com.cargopartner.education.pn14005.backend.entity.StandardContainer;
import com.cargopartner.education.pn14005.core.dto.ContainerType;

public class ContainerTypeVisitor implements ContainerVisitor {

    @Override
    public String visit(StandardContainer standardContainer) {
        return ContainerType.STANDARD.toString();
    }

    @Override
    public String visit(RefrigeratorContainer refrigeratorContainer) {
        return ContainerType.REFRIGERATOR.toString();
    }
}
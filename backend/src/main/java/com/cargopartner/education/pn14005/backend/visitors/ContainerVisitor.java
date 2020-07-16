package com.cargopartner.education.pn14005.backend.visitors;

import com.cargopartner.education.pn14005.backend.entity.RefrigeratorContainer;
import com.cargopartner.education.pn14005.backend.entity.StandardContainer;

public interface ContainerVisitor {
	 public String visit(StandardContainer standardContainer);
	 public String visit(RefrigeratorContainer refrigeratorContainer);
}

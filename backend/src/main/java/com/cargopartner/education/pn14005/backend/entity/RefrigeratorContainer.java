package com.cargopartner.education.pn14005.backend.entity;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.cargopartner.education.pn14005.backend.visitors.ContainerVisitor;
import com.cargopartner.education.pn14005.backend.visitors.Visitable;

@Entity
@DiscriminatorValue(value = "R")
public class RefrigeratorContainer extends Container{

	@Override
	public String accept(ContainerVisitor visitor) {
		return visitor.visit(this);		
	}
    
}
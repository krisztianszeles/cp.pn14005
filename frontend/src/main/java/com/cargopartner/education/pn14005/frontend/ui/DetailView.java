package com.cargopartner.education.pn14005.frontend.ui;

import java.util.Set;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.cargopartner.education.pn14005.core.dto.ContainerDTO;
import com.cargopartner.education.pn14005.core.dto.ContainerType;
import com.cargopartner.education.pn14005.core.dto.ShipDTO;
import com.cargopartner.education.pn14005.core.dto.ShipDimensionsDTO;
import com.cargopartner.education.pn14005.frontend.restclient.ShipRestClient;
import com.vaadin.cdi.CDIView;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.data.validator.DoubleRangeValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.themes.ValoTheme;

@CDIView(value = DetailView.VIEW_NAME, supportsParameters = true)
public class DetailView extends VerticalLayout implements View {
	
	public static final String VIEW_NAME = "detail";
	private final static Logger LOGGER = Logger.getLogger(DetailView.class);
	
	@Inject
    private ShipRestClient service;
   
	private ShipDTO shipDTO = new ShipDTO();
    private ContainerDTO containerDTO = new ContainerDTO();    
    private BeanFieldGroup<ShipDTO> shipFieldGroup;
    private BeanFieldGroup<ContainerDTO> containerFieldGroup;
    private Grid grid;
	private Label totalWeight;
    private Label totalVolume;    
    private NativeSelect containerType;
    private TextField containerName;
    private TextField origin;
    private TextField destination;
    private TextField weight;
    private TextField volume;  
    private TextField shipName;
    private Integer page;
    
    private StringLengthValidator nameValidator = new StringLengthValidator("should be at least 3 letters long and not longer than 255", 3, 255, false);
    private DoubleRangeValidator notNegativeValidator = new DoubleRangeValidator("cannot be negative", 0.0, null);
        
	@Override
	public void enter(ViewChangeEvent event) {
		Label titleLabel = new Label("Create / Edit Ship");
        titleLabel.addStyleName(ValoTheme.LABEL_H2);
        if (event.getParameters() != null && !event.getParameters().isEmpty()) {
        	String[] parameters = event.getParameters().split("/");
            shipDTO = service.getShip(Long.parseLong(parameters[0]));
            page = Integer.parseInt(parameters[1]);
        } else {
            shipDTO = new ShipDTO();      
            page = null;
        }
        addComponents(titleLabel, buildCreateEditForm());
        
        setSpacing(true);
        setMargin(true);
	}
    
	
	private Component buildCreateEditForm() {
		FormLayout form = new FormLayout();
		shipName = createTextField("Ship Name");
		totalWeight = new Label("0");
        totalVolume = new Label("0");
        
        if (shipDTO != null && shipDTO.getId() != null) {
            ShipDimensionsDTO dimensionsDTO = service.getShipDimensions(shipDTO.getId());
            totalWeight.setValue(String.valueOf(dimensionsDTO.getWeight()));
            totalVolume.setValue(String.valueOf(dimensionsDTO.getVolume()));
            shipName.setValue(dimensionsDTO.getName());
        }
        
        HorizontalLayout weightLayout = new HorizontalLayout(new Label("Total Weight:"), totalWeight);
        weightLayout.setSpacing(true);
        HorizontalLayout volumeLayout = new HorizontalLayout(new Label("Total Volume:"), totalVolume);
        volumeLayout.setSpacing(true);
        
        shipFieldGroup = new BeanFieldGroup<>(ShipDTO.class);
        shipFieldGroup.bind(shipName, "name");
        BeanItem<ShipDTO> item = new BeanItem<ShipDTO>(shipDTO);
        shipFieldGroup.setItemDataSource(item);
       
        
        form.addComponents(shipName, weightLayout, volumeLayout, buildContainersGrid(), buildAddContainerForm(), createButtons()); 
        form.setMargin(true);
		return form;
	}
	
    private Component buildContainersGrid() {
        grid = new Grid();
        updateContainerGridData();        
        grid.getColumn("type").setHeaderCaption("Container Type");
        grid.getColumn("name").setHeaderCaption("Container Name");
        grid.getColumn("origin").setHeaderCaption("Origin");
        grid.getColumn("destination").setHeaderCaption("Destination");
        grid.getColumn("weight").setHeaderCaption("Weight (kg)");
        grid.getColumn("volume").setHeaderCaption("Volume (m3)");
        grid.getColumn("delete").setHeaderCaption("Remove");
        grid.getColumn("delete")
                .setRenderer(new ButtonRenderer(e ->  {
                	shipDTO.getContainers().remove((ContainerDTO)e.getItemId());
                	grid.getContainerDataSource().removeItem((ContainerDTO)e.getItemId());
                    grid.getSelectionModel().reset();
                    updateShipDimensionsData();
                    updateContainerGridData();
                }));        
        grid.setColumns("type", "name", "origin", "destination", "weight", "volume", "delete");        
        grid.setSizeFull();
        grid.setHeightMode(HeightMode.ROW);
        grid.setEditorEnabled(true);
        grid.getEditorFieldGroup().addCommitHandler(new CommitHandler() {			
			@Override
			public void preCommit(CommitEvent commitEvent) throws CommitException {				
			}			
			@Override
			public void postCommit(CommitEvent commitEvent) throws CommitException {
				updateShipDimensionsData();
				Notification.show("Container was edited.", Type.TRAY_NOTIFICATION);
			}
		});        
        
        addValidatorToGridColumn("name", nameValidator);
        addValidatorToGridColumn("volume", notNegativeValidator);
        addValidatorToGridColumn("weight", notNegativeValidator);

        return grid;
	}
	private Component buildAddContainerForm() {
        FormLayout form = new FormLayout();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        containerType = createNativeSelect();
        containerName = createTextField("Container Name");
        origin = createTextField("Origin");
        destination = createTextField("Destination");
        weight = createTextField("Weight (kg)");
        weight.setConverter(new StringToDoubleConverter());
        volume = createTextField("Volume (m3)");
        volume.setConverter(new StringToDoubleConverter());
        horizontalLayout.setSpacing(true);
        horizontalLayout.addComponents(containerType, containerName, origin, destination, weight, volume, createAddContainerButton());
        
        containerFieldGroup = new BeanFieldGroup<>(ContainerDTO.class);
        containerFieldGroup.bind(containerType, "type");
        containerFieldGroup.bind(containerName, "name");
        containerFieldGroup.bind(origin, "origin");
        containerFieldGroup.bind(destination, "destination");
        containerFieldGroup.bind(weight, "weight");
        containerFieldGroup.bind(volume, "volume");

        BeanItem<ContainerDTO> item = new BeanItem<ContainerDTO>(containerDTO);
        containerFieldGroup.setItemDataSource(item);        
       
        form.addComponents(horizontalLayout);
        return form;
    }
	
	private Component createButtons() {	
		Button saveButton = new Button("Save", clickEvent -> saveShipChanges());
        saveButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
        saveButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        Button closeButton = new Button("Close", clickEvent -> navigateToMainView());
        HorizontalLayout horizontalLayout = new HorizontalLayout(saveButton, closeButton);
        horizontalLayout.setSpacing(true);
        return horizontalLayout;
	}
	
    private Button createAddContainerButton() {
        return new Button("Add Container", clickEvent -> addContainer());
    }
    
    private void addValidatorToGridColumn(String column, AbstractValidator validator) {
        TextField editor = new TextField();
        editor.addValidator(validator);
        grid.getColumn(column).setEditorField(editor);
    }
	
	private void addContainer() {
		try {
			containerFieldGroup.commit();
			ContainerDTO newContainer = createNewContainerDTO(containerDTO);
			shipDTO.getContainers().add(newContainer);
			updateShipDimensionsData();
	        updateContainerGridData();
	        clearContainerForm();	        
		} catch (CommitException e) {
			setTextFieldValidationVisible(true);
			LOGGER.error("Error adding new container", e);
		}
	}
	
	   private ContainerDTO createNewContainerDTO(final ContainerDTO containerDTO) {
	        ContainerDTO newContainer = new ContainerDTO();	        
	        newContainer.setType(containerDTO.getType());
	        newContainer.setName(containerDTO.getName());	 
	        newContainer.setOrigin(containerDTO.getOrigin());
	        newContainer.setDestination(containerDTO.getDestination());	      	             
	        newContainer.setWeight(containerDTO.getWeight());
	        newContainer.setVolume(containerDTO.getVolume());
	        return newContainer;
	    }
	
    public void updateShipDimensionsData() {
		double weight = shipDTO.getContainers().stream()
    		   .map(container ->{
			    	   if(container.getWeight() == null) {
			    		   container.setWeight(0d);
			    	   }
			    	   return container.getWeight();})
    		   .mapToDouble(Double::doubleValue)
    		   .sum();
		double volume = shipDTO.getContainers().stream()
      		   .map(container ->{
  			    	   if(container.getVolume() == null) {
  			    		   container.setVolume(0d);
  			    	   }
  			    	   return container.getVolume();})
      		   .mapToDouble(Double::doubleValue)
      		   .sum();
		
		totalWeight.setValue(String.valueOf(weight));
        totalVolume.setValue(String.valueOf(volume));
	}
	private void updateContainerGridData() {
        Set<ContainerDTO> containers = shipDTO.getContainers();
        BeanItemContainer<ContainerDTO> container = new BeanItemContainer<>(ContainerDTO.class, containers);
        GeneratedPropertyContainer generatedPropertyContainer = new GeneratedPropertyContainer(container);
        generatedPropertyContainer.addGeneratedProperty("delete",
                new PropertyValueGenerator<String>() {

                    @Override
                    public String getValue(Item item, Object itemId,
                                           Object propertyId) {
                        return "X";
                    }

                    @Override
                    public Class<String> getType() {
                        return String.class;
                    }
                });
        grid.setContainerDataSource(generatedPropertyContainer);
    }
	
    private void clearContainerForm() {
    	containerName.clear();
        origin.clear();
        destination.clear();
        weight.clear();
        volume.clear();
    }
    
    private void setTextFieldValidationVisible(boolean isVisible) {
    	containerName.setValidationVisible(isVisible);
        origin.setValidationVisible(isVisible);
        destination.setValidationVisible(isVisible);
        weight.setValidationVisible(isVisible);
        volume.setValidationVisible(isVisible);
    }
	
	private TextField createTextField(String inputPrompt) {
        TextField textField = new TextField();
        textField.setInputPrompt(inputPrompt);
        textField.setNullRepresentation("");
        textField.setValidationVisible(false);
        return textField;
    }
	
    private void saveShipChanges() {
        try {
            shipFieldGroup.commit();
            if (shipDTO.getId() == null) {
                service.createShip(shipDTO);
            } else {
                service.updateShip(shipDTO);
            }
            navigateToMainView();
        } catch (FieldGroup.CommitException e) {
        	shipName.setValidationVisible(true);
            LOGGER.error("Error saving ship updates", e);
        }
    }
    
    private NativeSelect createNativeSelect() {
    	NativeSelect nativeSelect = new NativeSelect();
    	nativeSelect.addItem(ContainerType.STANDARD);
    	nativeSelect.addItem(ContainerType.REFRIGERATOR);
    	nativeSelect.setEnabled(true); 	
    	nativeSelect.setRequired(true);
    	nativeSelect.setImmediate(true);
    	nativeSelect.setValue(0L);
    	nativeSelect.setNewItemsAllowed(false);
    	return nativeSelect;
    }
    
    private void navigateToMainView() {
    	if(page != null) {
    		getUI().getNavigator().navigateTo(MainView.VIEW_NAME  + "/" + page);
    	} else {
    		getUI().getNavigator().navigateTo(MainView.VIEW_NAME);
    	}
        
    }

}

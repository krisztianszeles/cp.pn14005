package com.cargopartner.education.pn14005.frontend.ui;

import java.util.Set;

import javax.inject.Inject;

import com.cargopartner.education.pn14005.core.dto.ShipDTO;
import com.cargopartner.education.pn14005.core.dto.ShipIndexDTO;
import com.cargopartner.education.pn14005.frontend.restclient.ShipRestClient;
import com.vaadin.cdi.CDIView;
import com.vaadin.data.Item;
import com.vaadin.data.sort.Sort;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.themes.ValoTheme;

@CDIView(value =MainView.VIEW_NAME, supportsParameters = true)
public class MainView extends VerticalLayout implements View{
	
	public static final String VIEW_NAME = "main";
	private static final Integer PAGE_SIZE = 10;
	
	@Inject
	private ShipRestClient service;
	
	private Integer page;
	private DetailView detailView;
	private Grid grid = new Grid();
	private Set<ShipIndexDTO> ships;
	private Button prevButton;
    private Button nextButton;
    private long shipsCount;
	
	@Override
	public void enter(ViewChangeEvent event) {		
		if (event.getParameters() != null && !event.getParameters().isEmpty()) {
        	page = Integer.parseInt(event.getParameters());
        } else {
        	page = 0;
        }		
		shipsCount = service.countAllShips();
		Label titleLabel = new Label("Ships");		
		addComponents(titleLabel, buildGrid(), createButtons());
		setSpacing(true);
        setMargin(true);
	}
	private Component buildGrid() {
		grid = new Grid();
		updateTableData();
		grid.setColumns("name", "containerCount", "delete");
		grid.getColumn("name").setHeaderCaption("Ship Name");
		grid.getColumn("containerCount").setHeaderCaption("No. of Containers");
		grid.getColumn("delete").setHeaderCaption("Remove ship");
		grid.getColumn("delete")
		.setRenderer(new ButtonRenderer(e -> {
			service.deleteShip(((ShipIndexDTO) e.getItemId()).getId());
			shipsCount = service.countAllShips();
			grid.getContainerDataSource().removeItem((ShipIndexDTO)e.getItemId());
			grid.getSelectionModel().reset();
		}));

		grid.setColumns("name", "containerCount", "delete");
		grid.setSizeFull();
		grid.setHeightMode(HeightMode.ROW);
		grid.setSortOrder(Sort.by("name").build());
		grid.addItemClickListener(clickEvent -> {
			if (clickEvent.isDoubleClick()) {
				getUI().getNavigator().navigateTo(DetailView.VIEW_NAME + "/" + ((ShipDTO) clickEvent.getItemId()).getId()  + "/" + page);
			}
		});
		return grid;
	}


	private void updateTableData() {
		loadTableData();
		BeanItemContainer<ShipIndexDTO> container = new BeanItemContainer<>(ShipIndexDTO.class, ships);
		GeneratedPropertyContainer generatedPropertyContainer = new GeneratedPropertyContainer(container);				
		generatedPropertyContainer.addGeneratedProperty("delete",
				new PropertyValueGenerator<String>() {
			@Override
			public String getValue(Item item, Object itemId, Object propertyId) {
				return "Delete"; 
			}
			@Override
			public Class<String> getType() {
				return String.class;
			}
		});
		grid.setContainerDataSource(generatedPropertyContainer);		
	}
	
    private Component createButtons() {
    	Button createShipButton = new Button("Create Ship", e -> getUI().getNavigator().navigateTo(DetailView.VIEW_NAME));
		createShipButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
        HorizontalLayout horizontalLayout = new HorizontalLayout(createShipButton, buildPrevButton(), buildNextButton());
        horizontalLayout.setSpacing(true);
        updateNavigationButtons();
        return horizontalLayout;
    }
	
    private Component buildPrevButton() {
        prevButton = new Button("Prev", FontAwesome.ARROW_LEFT);
        prevButton.addClickListener(clickEvent -> {
            page--;
            updateTableData();
            updateNavigationButtons();
        });
        prevButton.setClickShortcut(ShortcutAction.KeyCode.ARROW_LEFT);
        return prevButton;
    }

    private Component buildNextButton() {
        nextButton = new Button("Next", FontAwesome.ARROW_RIGHT);
        nextButton.addClickListener(clickEvent -> {
            page++;
            updateTableData();
            updateNavigationButtons();
        });
        nextButton.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_RIGHT);
        nextButton.setClickShortcut(ShortcutAction.KeyCode.ARROW_RIGHT);
        return nextButton;
    }
    
    private void updateNavigationButtons() {
        prevButton.setEnabled(page > 0);
        nextButton.setEnabled(shipsCount > (page + 1) * PAGE_SIZE);
    }
	
	private void loadTableData() {
		int offset = page * PAGE_SIZE;
        ships = service.getAllShipsFromDB(offset, PAGE_SIZE);
	}	
	
}

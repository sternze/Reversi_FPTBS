package uni_klu.se2.reversi.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.effect.Light;
import javafx.scene.effect.LightingBuilder;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.RegionBuilder;
import uni_klu.se2.reversi.data.FieldStatus;
import uni_klu.se2.reversi.data.Move;

public class ReversiSquare extends Region {

	private static ReversiModel model;
	
	private ReversiSquare me;
	
	private final String LEGALSQUARE = "reversi-square-legal";
	
	private Region highlight = RegionBuilder.create().opacity(0)
			.style("-fx-border-width: 3; -fx-border-color: dodgerblue").build();

	public ReversiSquare(final int x, final int y, ReversiModel model) {
		ReversiSquare.model = model;
		this.me = this;
		
		getStyleClass().add("reversi-square");
		
		model.getBoard().getFields()[x][y].getStatus().addListener(new ChangeListener<FieldStatus>() {

			public void changed(ObservableValue<? extends FieldStatus> observable, FieldStatus oldValue, FieldStatus newValue) {
				if (newValue == FieldStatus.LEGAL) {
					me.getStyleClass().add(LEGALSQUARE);
				} else if (newValue == FieldStatus.EMPTY) {
					if (me.getStyleClass().contains(LEGALSQUARE))
						me.getStyleClass().remove(LEGALSQUARE);
				}
			}
		});
		
		Light.Distant light = new Light.Distant();
		light.setAzimuth(-135);
		light.setElevation(30);
		setEffect(LightingBuilder.create().light(light).build());
		setPrefSize(200, 200);
		
		addEventHandler(MouseEvent.MOUSE_CLICKED,
				new EventHandler<MouseEvent>() {
					public void handle(MouseEvent t) {
						ReversiSquare.model.play(new Move(x,y));
					}
				});
	}

	@Override
	protected void layoutChildren() {
		layoutInArea(highlight, 0, 0, getWidth(), getHeight(), getBaselineOffset(), HPos.CENTER, VPos.CENTER);
	}
}

package uni_klu.se2.reversi.gui;

import javafx.beans.binding.Bindings;
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
	
	private Region highlight = RegionBuilder.create().opacity(0)
			.style("-fx-border-width: 3; -fx-border-color: dodgerblue").build();

	public ReversiSquare(final int x, final int y, ReversiModel model) {
		ReversiSquare.model = model;
		
		styleProperty().bind(Bindings.when(ReversiSquare.model.getBoard().getFields()[x][y].getStatus().isEqualTo(FieldStatus.LEGAL))
									 .then("-fx-background-color: derive(dodgerblue, -60%)")
									 .otherwise("-fx-background-color: #177B0C"));

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

package uni_klu.se2.reversi.gui;

import javafx.animation.FadeTransition;
import javafx.animation.FadeTransitionBuilder;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.effect.Light;
import javafx.scene.effect.LightingBuilder;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.RegionBuilder;
import javafx.util.Duration;

public class ReversiSquare extends Region {

	private static ReversiModel model = ReversiModel.getInstance();
	
	private Region highlight = RegionBuilder.create()
			  .opacity(0)
			  .style("-fx-border-width: 3; -fx-border-color: dodgerblue")
			  .build();
	
	private FadeTransition highlightTransition = FadeTransitionBuilder.create()
			  .node(highlight)
			  .duration(Duration.millis(200))
			  .fromValue(0)
			  .toValue(1)
			  .build();
	
	public ReversiSquare(final int x, final int y) {
		styleProperty().bind(Bindings.when(model.legalMove(x, y))
	    .then("-fx-background-color: derive(dodgerblue, -60%)")
	    .otherwise("-fx-background-color: #177B0C"));
	    Light.Distant light = new Light.Distant();
	    light.setAzimuth(-135);
	    light.setElevation(30);
	    setEffect(LightingBuilder.create().light(light).build());
	    setPrefSize(200, 200);
	    //getChildren().add(highlight); 
	    addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
    	  public void handle(MouseEvent t) {
    	    model.play(x, y);
    	    highlightTransition.setRate(1000);
    	    highlightTransition.play();
    	    
    	  }
    	});
	  }
	
	@Override
	protected void layoutChildren() {
	  layoutInArea(highlight, 0, 0, getWidth(), getHeight(), getBaselineOffset(), HPos.CENTER, VPos.CENTER);
	} 
}
package uni_klu.se2.reversi.gui;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.TimelineBuilder;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.Region;
import javafx.util.Duration;
import uni_klu.se2.reversi.gui.ReversiModel.Owner;

public class ReversiPiece extends Region {

	private ObjectProperty<Owner> ownerProperty = new SimpleObjectProperty<Owner>(this, "owner", Owner.NONE);
	
	private DoubleProperty translateZ = new SimpleDoubleProperty();
    private DoubleProperty angle = new SimpleDoubleProperty();
	private final Region me;
    
	public ReversiPiece() {
		  this.getStyleClass().add("revercie-piece");
		  
		  styleProperty().bind(Bindings.when(ownerProperty.isEqualTo(Owner.NONE))
		    .then("radius 0")
		    .otherwise(Bindings.when(ownerProperty.isEqualTo(Owner.WHITE))
		      .then("-fx-background-color: radial-gradient(radius 100%, white .4, gray .9, darkgray 1)")
		      .otherwise("-fx-background-color: radial-gradient(radius 100%, white 0, black .6)"))
		    .concat("; -fx-background-radius: 1000em; -fx-background-insets: 5;"));
		  Reflection reflection = new Reflection();
		  reflection.setFraction(1);
		  reflection.topOffsetProperty().bind(heightProperty().multiply(-.75));
		  setEffect(reflection);
		  setPrefSize(180, 180);
		  setMouseTransparent(true);
		  this.translateZProperty().bind(translateZ);
	      me = this;
	      /*
	        ownerProperty.addListener(new ChangeListener() {
	        	@Override 
	        	public void changed(ObservableValue o, Object oldVal, Object newVal){
	                System.out.println("owner changed");

	                RotateTransition rt = new RotateTransition(Duration.millis(500), me);
	                rt.setCycleCount(1);
	                rt.setAutoReverse(false);
	                rt.setByAngle(180);
	                rt.setAxis(Rotate.Z_AXIS);
	                rt.play();
	           }
	        });*/ 
		}
	
	public void animate() {
        TimelineBuilder.create()
            .cycleCount(1)
            .keyFrames(
                new KeyFrame(
                    Duration.seconds(0.5),
                    new KeyValue(translateZ, 100),
                    new KeyValue(angle, 180)
                )

            )
            .build().play();
    }
	
	public ReversiPiece(Owner owner) {
		  this();
		  ownerProperty.setValue(owner);
		}
	
	public ObjectProperty<Owner> ownerProperty() {
		return ownerProperty;
	}
	
	public Owner getOwner() {
		return ownerProperty.get();
	} 
	
	public void setOwner(Owner owner) {
	    ownerProperty.set(owner);
	  }
}

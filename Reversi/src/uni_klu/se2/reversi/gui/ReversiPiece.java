package uni_klu.se2.reversi.gui;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.TimelineBuilder;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.Region;
import javafx.util.Duration;
import uni_klu.se2.reversi.data.FieldStatus;

public class ReversiPiece extends Region {

	private ObjectProperty<FieldStatus> ownerProperty = new SimpleObjectProperty<FieldStatus>(
			this, "owner", FieldStatus.EMPTY);

	private DoubleProperty translateZ = new SimpleDoubleProperty();
	private DoubleProperty angle = new SimpleDoubleProperty();

	private ReversiPiece me;
	
	private final String WHITEPIECE = "white-piece";
	private final String BLACKPIECE = "black-piece";

	public ReversiPiece() {
		this.getStyleClass().add("reversi-piece");
		me = this;

		ownerProperty.addListener(new ChangeListener<FieldStatus>() {

			public void changed(ObservableValue<? extends FieldStatus> observable, FieldStatus oldValue, FieldStatus newValue) {
				if (newValue == FieldStatus.BLACK) {
					me.getStyleClass().remove(WHITEPIECE);
					
					if (!me.getStyleClass().contains(BLACKPIECE))
						me.getStyleClass().add(BLACKPIECE);
					
				} else if (newValue == FieldStatus.WHITE) {
					me.getStyleClass().remove(BLACKPIECE);
					
					if (!me.getStyleClass().contains(WHITEPIECE))
						me.getStyleClass().add(WHITEPIECE);

				}
			}

		});
		
		Reflection reflection = new Reflection();
		reflection.setFraction(1);
		reflection.topOffsetProperty().bind(heightProperty().multiply(-.75));
		setEffect(reflection);
		setPrefSize(180, 180);
		setMouseTransparent(true);
		this.translateZProperty().bind(translateZ);
		/*
		 * ownerProperty.addListener(new ChangeListener() {
		 * 
		 * @Override public void changed(ObservableValue o, Object oldVal,
		 * Object newVal){ System.out.println("owner changed");
		 * 
		 * RotateTransition rt = new RotateTransition(Duration.millis(500), me);
		 * rt.setCycleCount(1); rt.setAutoReverse(false); rt.setByAngle(180);
		 * rt.setAxis(Rotate.Z_AXIS); rt.play(); } });
		 */
	}

	public void animate() {
		TimelineBuilder
				.create()
				.cycleCount(1)
				.keyFrames(
						new KeyFrame(Duration.seconds(0.5), new KeyValue(
								translateZ, 100), new KeyValue(angle, 180))

				).build().play();
	}

	public ObjectProperty<FieldStatus> ownerProperty() {
		return ownerProperty;
	}

	public FieldStatus getOwner() {
		return ownerProperty.get();
	}

	public void setOwner(FieldStatus owner) {
		ownerProperty.set(owner);
	}
}


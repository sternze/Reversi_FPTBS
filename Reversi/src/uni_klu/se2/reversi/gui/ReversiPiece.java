package uni_klu.se2.reversi.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.effect.Light;
import javafx.scene.effect.LightingBuilder;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.Region;
import uni_klu.se2.reversi.data.FieldStatus;

public class ReversiPiece extends Region {

	private ObjectProperty<FieldStatus> ownerProperty = new SimpleObjectProperty<FieldStatus>(
			this, "owner", FieldStatus.EMPTY);

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
		
		Light.Distant light = new Light.Distant();
		light.setAzimuth(-135);
		light.setElevation(30);
		setEffect(LightingBuilder.create().light(light).build());
		setPrefSize(180, 180);
		setMouseTransparent(true);
		
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


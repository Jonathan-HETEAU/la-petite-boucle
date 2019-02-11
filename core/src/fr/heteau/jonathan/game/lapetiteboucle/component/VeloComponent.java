package fr.heteau.jonathan.game.lapetiteboucle.component;

import com.artemis.Component;

public class VeloComponent extends Component {
	
	public int vitesse;
	public float vitesseRotation;

	public float angle;
	public float frein;
	
	
	public void init() {
		this.vitesse = 01;
		this.vitesseRotation = 0;
		this.angle = 0;
		this.frein =0;
	}
}

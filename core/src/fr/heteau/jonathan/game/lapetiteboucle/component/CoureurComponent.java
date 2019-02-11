package fr.heteau.jonathan.game.lapetiteboucle.component;

import com.artemis.Component;

public class CoureurComponent extends Component {
	public String name;
	public float vitesse;
	public float vitesseMax;
	public float vitesseIdeal;
	public float temps;
	public float distance;
	public float tourSeconde;
	public float angleVirage;
	public float angleMonte;
	
	@Override
	public String toString() {
		return "CoureurComponent [name=" + name + ", vitesse=" + vitesse + ", vitesseMax=" + vitesseMax + ", temps="
				+ temps + ", distance=" + distance + ", tourSeconde=" + tourSeconde + ", angle=" + angleVirage
				+ ", angleMonte=" + angleMonte + "]";
	}
	
	
}

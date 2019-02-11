package fr.heteau.jonathan.game.lapetiteboucle.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

import fr.heteau.jonathan.game.lapetiteboucle.system.CalculeSystem;

public class PlayerEtapeController implements GameControllerListener {

	@SuppressWarnings("unused")
	private static final String TAG = "PlayerEtapeController";
	private static final int SIZE = 4;

	private int sizeTour = SIZE;
	private boolean tour[] = new boolean[SIZE];
	private int nbrTour = 0;

	
	private Controller controller;
	private int vitesse;

	
	

	public PlayerEtapeController(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void connected(Controller controller) {
		// TODO Auto-generated method stub
	}

	@Override
	public void disconnected(Controller controller) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		switch(buttonCode){
		case 4:
			vitesse --;
			if(vitesse<1){
				vitesse = 1;
			}
			break;
		case 5:
			vitesse++;
			if(vitesse > CalculeSystem.nombreVitesse){
				vitesse = CalculeSystem.nombreVitesse;
			}
			break;
		}
		return false;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		return false;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		switch (axisCode) {
		case 2:
			if (0.5 <= Math.abs(value)) {
				if(value > 0){
					if(!tour[0]){
						tour[0]=true;
						sizeTour--;
					}
				}else{
					if(!tour[1]){
						tour[1]=true;
						sizeTour--;
					}
				}
			}
			break;
		case 3:
			if (0.5 <= Math.abs(value)) {
				if(value > 0){
					if(!tour[2]){
						tour[2]=true;
						sizeTour--;
					}
				}else{
					if(!tour[3]){
						tour[3]=true;
						sizeTour--;
					}
				}
			}
			break;
		default:
			break;
		}
		if(sizeTour<= 0){
			nbrTour++;
			sizeTour = SIZE;
			tour = new boolean[SIZE];
		}
		return false;
	}

	@Override
	public boolean povMoved(Controller controller, int povCode, PovDirection value) {
		return false;
	}

	@Override
	public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
		return false;
	}

	@Override
	public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
		return false;
	}

	@Override
	public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
		return false;
	}

	public int getNbrTour() {
		return nbrTour;
	}

	public void setNbrTour(int nbrTour) {
		this.nbrTour = nbrTour;
	}

	public int getVitesse() {
		return vitesse;
	}

	public void setVitesse(int vitesse) {
		this.vitesse = vitesse;
	}

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}
	
}

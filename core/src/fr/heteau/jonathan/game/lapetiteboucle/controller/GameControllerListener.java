package fr.heteau.jonathan.game.lapetiteboucle.controller;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;

public interface GameControllerListener extends ControllerListener {
	public void setController(Controller controller);
	public Controller getController();
}

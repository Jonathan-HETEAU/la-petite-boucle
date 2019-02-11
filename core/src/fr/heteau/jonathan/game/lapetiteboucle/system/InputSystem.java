package fr.heteau.jonathan.game.lapetiteboucle.system;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;

import fr.heteau.jonathan.game.lapetiteboucle.component.HumanComponent;
import fr.heteau.jonathan.game.lapetiteboucle.component.VeloComponent;
import fr.heteau.jonathan.game.lapetiteboucle.controller.PlayerEtapeController;

public class InputSystem extends BaseEntitySystem {

	@SuppressWarnings("unused")
	private static final String TAG = "InputSystem";

	private ComponentMapper<VeloComponent> cmVelo;
	private PlayerEtapeController playerEtapeController;

	private float deltaReff;
	private float deltaCurrent = 0f;

	private boolean updateVitesseRotation;

	@SuppressWarnings("unchecked")
	public InputSystem(float delta, PlayerEtapeController playerEtapeController) {
		super(Aspect.all(HumanComponent.class, VeloComponent.class));
		this.deltaReff = delta;
		this.playerEtapeController = playerEtapeController;
	}

	@Override
	protected void begin() {
		deltaCurrent += world.getDelta();
		if (deltaReff <= deltaCurrent) {
			deltaCurrent = 0;
			updateVitesseRotation = true;
		}
	}

	@Override
	protected void processSystem() {
		IntBag actives = subscription.getEntities();
		int[] ids = actives.getData();
		for (int i = 0, s = actives.size(); s > i; i++) {
			VeloComponent velo = cmVelo.get(ids[i]);
			velo.angle = playerEtapeController.getController().getAxis(1)*90;
			velo.frein = (2+playerEtapeController.getController().getAxis(4)+playerEtapeController.getController().getAxis(5))/4;
			velo.vitesse = playerEtapeController.getVitesse();
			if (updateVitesseRotation) {
				float facteur = (60f/deltaReff);
				velo.vitesseRotation = (facteur *playerEtapeController.getNbrTour())/60f;
				playerEtapeController.setNbrTour(0);
			}
		}
	}

	@Override
	protected void end() {
		updateVitesseRotation = false;
	}

	public PlayerEtapeController getPlayerEtapeController() {
		return playerEtapeController;
	}

	public void setPlayerEtapeController(PlayerEtapeController playerEtapeController) {
		this.playerEtapeController = playerEtapeController;
	}
	
	

}

package fr.heteau.jonathan.game.lapetiteboucle.system;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;

import fr.heteau.jonathan.game.lapetiteboucle.Etape;
import fr.heteau.jonathan.game.lapetiteboucle.GameActionListener;
import fr.heteau.jonathan.game.lapetiteboucle.component.CoureurComponent;
import fr.heteau.jonathan.game.lapetiteboucle.component.VeloComponent;
import fr.heteau.jonathan.game.lapetiteboucle.enumeration.WorldEnum;

public class CalculeSystem  extends BaseEntitySystem {

	public static int nombreVitesse=9;
	
	@SuppressWarnings("unused")
	private static final String TAG="CalculeSystem";
	
	private Etape etape;
	
	private ComponentMapper<VeloComponent> cmVelo;
	private ComponentMapper<CoureurComponent> cmPlayer;

	private GameActionListener gameActionListener;
	
	
	@SuppressWarnings("unchecked")
	public CalculeSystem(Etape etape,GameActionListener gal) {
		super(Aspect.all(VeloComponent.class));
		this.etape=etape;
		this.gameActionListener = gal;
	}

	@Override
	protected void processSystem() {
		IntBag actives = subscription.getEntities();
		int[] ids = actives.getData();
		for (int i = 0, s = actives.size(); s > i; i++) {
			calcule(cmPlayer.get(ids[i]),cmVelo.get(ids[i]),world.getDelta());
		}
	}
	
	private void calcule(CoureurComponent player , VeloComponent velo,float delta){
		player.angleMonte = etape.getAngle(player.distance);
		player.vitesseIdeal =velo.vitesse + (player.angleMonte/10f);
		float vit = 1 -  Math.abs(player.vitesseIdeal  - velo.vitesseRotation)/20f;
		float vitesseTmp = ((player.vitesseMax-((nombreVitesse-velo.vitesse)*5)) * velo.vitesseRotation/10f)*vit;
		
		player.tourSeconde = velo.vitesseRotation;
		if(player.vitesse < vitesseTmp){
			player.vitesse +=  (vitesseTmp-player.vitesse)/2;
		}
		player.vitesse -= (5+player.angleMonte) *delta;
		player.vitesse *= 1-(velo.frein*0.5);
		
		if(player.vitesse < 0){
			player.vitesse = 0;
		}
		if(player.vitesse > 105){
			player.vitesse = 105;
		}
		player.angleVirage = velo.angle;
		player.distance += (player.vitesse/3600*delta); 
		player.temps +=delta;
		if(player.distance >= etape.getDistanceEtape()){
			float time = (player.temps/player.distance)*etape.getDistanceEtape();
			gameActionListener.etapeTerminer(etape, time);
		}
		
	}
}

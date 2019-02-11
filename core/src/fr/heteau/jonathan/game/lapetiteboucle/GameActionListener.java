package fr.heteau.jonathan.game.lapetiteboucle;

import com.artemis.World;

import fr.heteau.jonathan.game.lapetiteboucle.controller.GameControllerListener;
import fr.heteau.jonathan.game.lapetiteboucle.enumeration.WorldEnum;

public interface GameActionListener {

	public void changeWorld(WorldEnum world);
	public void addWorld(WorldEnum name, World world);
	public void removeWorld(WorldEnum nam);
	public void addControllerListener(WorldEnum name,GameControllerListener listener);
	public void etapeTerminer(Etape etape, float time);
	public void quitter();
	
}

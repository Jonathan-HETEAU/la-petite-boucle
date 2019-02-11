package fr.heteau.jonathan.game.lapetiteboucle;

import java.util.HashMap;
import java.util.Map;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import fr.heteau.jonathan.game.lapetiteboucle.controller.GameControllerListener;
import fr.heteau.jonathan.game.lapetiteboucle.enumeration.WorldEnum;
import fr.heteau.jonathan.game.lapetiteboucle.system.painter.ArriverPainter;
import fr.heteau.jonathan.game.lapetiteboucle.system.painter.ControllerPainter;
import fr.heteau.jonathan.game.lapetiteboucle.system.painter.MenuSystem;

public class LaPetiteBoucle extends ApplicationAdapter implements ControllerListener, GameActionListener {

	public static final int HEIGHT = 800;
	public static final int WIDTH = 600;

	private WorldEnum currentWorld;
	private WorldEnum pauseWorld;

	private Controller controller;
	private Map<WorldEnum, World> worlds;
	private Map<WorldEnum, GameControllerListener> controllerListeners;

	private Viewport viewport;
	private OrthographicCamera camera;
	private GameVisualConfiguration configuration;

	@Override
	public void create() {
		this.camera = new OrthographicCamera();
		this.camera.update();
		this.camera.zoom = (float) 1;
		this.camera.position.set(WIDTH / 2, HEIGHT / 2, 0);
		this.viewport = new FitViewport(WIDTH, HEIGHT, camera);
		this.configuration = new GameVisualConfiguration();
		this.worlds = new HashMap<WorldEnum, World>();
		this.controllerListeners = new HashMap<WorldEnum, GameControllerListener>();

		Controllers.addListener(this);
		MenuSystem menu = new MenuSystem(camera, configuration, this, null);
		this.controllerListeners.put(WorldEnum.Menu, menu);
		worlds.put(WorldEnum.Menu, new World(new WorldConfigurationBuilder().with(menu).build()));

		worlds.put(WorldEnum.GamePad,
				new World(new WorldConfigurationBuilder().with(new ControllerPainter(camera, configuration)).build()));

		currentWorld = WorldEnum.GamePad;
		if (Controllers.getControllers().size > 0) {
			currentWorld = WorldEnum.Menu;
			this.controller = Controllers.getControllers().first();
			for (GameControllerListener gcl : controllerListeners.values()) {
				gcl.setController(this.controller);
			}
		} else {
			pauseWorld = WorldEnum.Menu;
		}

	}

	@Override
	public void render() {
		World world = worlds.get(currentWorld);
		world.setDelta(Gdx.graphics.getDeltaTime());
		world.process();
	}

	public void resize(int width, int height) {
		viewport.update(width, height, false);
		camera.update();
	}

	@Override
	public void dispose() {
		for (World world : worlds.values()) {
			world.dispose();
		}
		configuration.dispose();
	}

	@Override
	public void connected(Controller controller) {
		if (currentWorld == WorldEnum.GamePad) {
			if (this.controller == null) {
				this.controller = controller;
				for (GameControllerListener gcl : controllerListeners.values()) {
					gcl.setController(this.controller);
				}
				if (pauseWorld != null) {
					currentWorld = pauseWorld;
					pauseWorld = null;
				} else {
					currentWorld = WorldEnum.Menu;
				}
			}
		}
	}

	@Override
	public void disconnected(Controller controller) {
		if (controller.equals(this.controller)) {
			if (Controllers.getControllers().size > 0) {
				this.controller = Controllers.getControllers().first();
				for (GameControllerListener gcl : controllerListeners.values()) {
					gcl.setController(this.controller);
				}
			} else {
				if (currentWorld != WorldEnum.GamePad) {
					pauseWorld = currentWorld;
					currentWorld = WorldEnum.GamePad;
					this.controller = null;
				}
			}
		}
	}

	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		if (controller.equals(this.controller) && controllerListeners.containsKey(currentWorld)) {
			controllerListeners.get(currentWorld).buttonDown(controller, buttonCode);
		}
		return false;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		if (controller.equals(this.controller) && controllerListeners.containsKey(currentWorld)) {
			if (buttonCode == 7) {
				if (currentWorld == WorldEnum.Etape) {
					pauseWorld = WorldEnum.Etape;
					currentWorld = WorldEnum.Menu;
				} else {
					if (WorldEnum.Etape.equals(pauseWorld) && WorldEnum.Menu.equals(currentWorld)) {
						currentWorld = WorldEnum.Etape;
						pauseWorld = null;
					}
				}
			} else {
				controllerListeners.get(currentWorld).buttonUp(controller, buttonCode);
			}
		} else {
			if (WorldEnum.Victoir.equals(currentWorld)) {
				currentWorld = WorldEnum.Menu;
				pauseWorld = null;
				worlds.remove(WorldEnum.Victoir);
				controllerListeners.remove(WorldEnum.Victoir);
				controllerListeners.remove(WorldEnum.Etape);
				World world = worlds.remove(WorldEnum.Etape);
				world.dispose();
			}
		}
		return false;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		if (controller.equals(this.controller) && controllerListeners.containsKey(currentWorld)) {
			controllerListeners.get(currentWorld).axisMoved(controller, axisCode, value);
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

	@Override
	public void changeWorld(WorldEnum world) {
		this.currentWorld = world;
	}

	@Override
	public void addWorld(WorldEnum name, World world) {
		this.worlds.put(name, world);
	}

	@Override
	public void removeWorld(WorldEnum nam) {
		World world = this.worlds.remove(nam);
		if (world != null) {
			world.dispose();
		}
	}

	@Override
	public void addControllerListener(WorldEnum name, GameControllerListener listener) {
		controllerListeners.put(name, listener);
	}

	@Override
	public void quitter() {
		Gdx.app.exit();
	}

	@Override
	public void etapeTerminer(Etape etape, float time) {
		worlds.put(WorldEnum.Victoir, new World(
				new WorldConfigurationBuilder().with(new ArriverPainter(camera, configuration, etape, time)).build()));
		currentWorld = WorldEnum.Victoir;
	}
}

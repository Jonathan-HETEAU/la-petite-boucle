package fr.heteau.jonathan.game.lapetiteboucle.system.painter;

import java.util.ArrayList;
import java.util.List;

import com.artemis.BaseSystem;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

import fr.heteau.jonathan.game.lapetiteboucle.Etape;
import fr.heteau.jonathan.game.lapetiteboucle.GameActionListener;
import fr.heteau.jonathan.game.lapetiteboucle.GameVisualConfiguration;
import fr.heteau.jonathan.game.lapetiteboucle.component.CoureurComponent;
import fr.heteau.jonathan.game.lapetiteboucle.component.HumanComponent;
import fr.heteau.jonathan.game.lapetiteboucle.component.VeloComponent;
import fr.heteau.jonathan.game.lapetiteboucle.controller.GameControllerListener;
import fr.heteau.jonathan.game.lapetiteboucle.controller.PlayerEtapeController;
import fr.heteau.jonathan.game.lapetiteboucle.enumeration.WorldEnum;
import fr.heteau.jonathan.game.lapetiteboucle.system.CalculeSystem;
import fr.heteau.jonathan.game.lapetiteboucle.system.InputSystem;

public class MenuSystem extends BaseSystem implements GameControllerListener {

	private GameVisualConfiguration configuration;

	private Camera camera;
	private SpriteBatch spriteBatch;
	private ShapeRenderer shapeRenderer;

	private boolean update;

	private float deltaCurrent;
	private float deltaReff = 0.04f;
	private GameActionListener gameActionListener;
	private Controller controller;
	private List<Etape> etapes;
	private int etape = 0;
	private boolean isCredits = false;

	public MenuSystem(Camera camera, GameVisualConfiguration configuration, GameActionListener gal,
			Controller controller) {
		super();
		this.configuration = configuration;
		this.camera = camera;
		this.spriteBatch = new SpriteBatch();
		this.shapeRenderer = new ShapeRenderer();
		this.gameActionListener = gal;
		this.etapes = new ArrayList<Etape>();
		Etape etape = new Etape("ETAPE 0", new float[] { 0, 0,0.5f, 0 });
		etapes.add(etape);
		etape = new Etape("ETAPE 1", new float[] { 0, 0, 0.2f, 0, 0.5f,0.2f, 0.8f, 0,1,0 });
		etapes.add(etape);
		etape = new Etape("ETAPE 2", new float[] { 0, 0,0.4f, 0.1f, 0.6f, 0.2f, 0.7f, 0.2f,1,0.4f,1.5f,0.4f });
		etapes.add(etape);
		etape = new Etape("ETAPE 3", new float[] { 0, 0,0.4f, 0.2f, 0.6f, 0, 0.7f, 0.1f,1,0.4f,1.5f,0.0f,2,0 });
		etapes.add(etape);

	}

	private void initWorldsEtape(Etape etape, int playerId) {
		PlayerEtapeController playerEtapeController = new PlayerEtapeController(controller);
		gameActionListener.addControllerListener(WorldEnum.Etape, playerEtapeController);
		World world = new World(new WorldConfigurationBuilder().with(new InputSystem(1.25f, playerEtapeController),
				new CalculeSystem(etape, gameActionListener), new EtapePainter(camera, configuration, etape)).build());
		gameActionListener.addWorld(WorldEnum.Etape, world);
		createPlayer(world, 0);
	}

	private void createPlayer(World world, int playerID) {
		int entityId = world.create();
		VeloComponent vc = world.getMapper(VeloComponent.class).create(entityId);
		vc.init();
		HumanComponent cc = world.getMapper(HumanComponent.class).create(entityId);
		cc.controllerId = playerID;
		CoureurComponent pc = world.getMapper(CoureurComponent.class).create(entityId);
		pc.vitesseMax = 70;
		pc.name = "jonathan";

	}

	@Override
	protected void begin() {
		deltaCurrent += world.getDelta();
		if (deltaReff <= deltaCurrent) {
			deltaCurrent = 0;
			update = true;
		}
	}

	@Override
	protected void processSystem() {
		if (update) {
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			shapeRenderer.setProjectionMatrix(camera.combined);
			spriteBatch.setProjectionMatrix(camera.combined);
			if (isCredits)
				drawCredit();
			else {
				drawFond();
				drawMenu();
			}
		}
	}

	private void drawCredit() {
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(configuration.COLOR_FOND);
		shapeRenderer.rect(0, 0, 600, 800);
		shapeRenderer.setColor(Color.GOLD);
		shapeRenderer.rect(0, 600, 600, 200);
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.rect(0, 650, 600, 125);
		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.rect(0, 400, 600, 200);
		shapeRenderer.rect(0, 250, 600, 50);
		shapeRenderer.rect(0, 150, 600, 50);
		shapeRenderer.rect(0, 50, 600, 50);
		shapeRenderer.end();
		spriteBatch.begin();
		configuration.fontConteurFond.draw(spriteBatch, "     CREDITS    ", 20, 750);
		configuration.fontConteurFond.draw(spriteBatch, "8888888888888888", 20, 550);
		configuration.fontConteurFond.draw(spriteBatch, "8888888888888888", 20, 475);
		configuration.fontConteurVitesse.draw(spriteBatch, "    JONATHAN    ", 20, 550);
		configuration.fontConteurVitesse.draw(spriteBatch, "     HETEAU     ", 20, 475);
		configuration.fontConteurTime.draw(spriteBatch, "      DMITRY SUKHANKIN", 0, 290);
		spriteBatch.draw(configuration.padController, 275, 210, 35, 35);
		configuration.fontConteurTime.draw(spriteBatch, "       NATHAN LOVATO", 0, 190);
		spriteBatch.draw(configuration.A, 0, 110, 40, 40);
		spriteBatch.draw(configuration.B, 60, 110, 40, 40);
		spriteBatch.draw(configuration.X, 120, 110, 40, 40);
		spriteBatch.draw(configuration.Y, 180, 110, 40, 40);
		spriteBatch.draw(configuration.BL, 275, 110, 40, 40);
		spriteBatch.draw(configuration.BR, 340, 110, 40, 40);
		spriteBatch.draw(configuration.GL, 470, 110, 40, 40);
		spriteBatch.draw(configuration.GR, 540, 110, 40, 40);

		configuration.fontConteurTime.draw(spriteBatch, "     DAVID J PATTERSON", 0, 90);
		configuration.fontConteurVitesse.draw(spriteBatch, "ABCDEFGHIJKLMNOPQR", 0, 45);
		spriteBatch.end();
	}

	private void drawMenu() {
		spriteBatch.begin();
		configuration.fontConteurVitesse.draw(spriteBatch, "8888888888888888", 20, 750);
		configuration.fontConteurFond.draw(spriteBatch, "LA PETITE BOUCLE", 20, 750);
		spriteBatch.draw(configuration.BL, 10, 560, 40, 40);
		spriteBatch.draw(configuration.BR, 550, 560, 40, 40);
		configuration.fontConteurVitesse.draw(spriteBatch,
				String.format("%" + ((int) (16 - etapes.get(etape).getName().length() / 2)) + "s",
						etapes.get(etape).getName()),
				0, 600);
		spriteBatch.draw(configuration.A, 270, 460, 70, 70);
		configuration.fontConteurVitesse.draw(spriteBatch,
				String.format("%" + ((int) (16 - "CREDITS".length() / 2)) + "s", "CREDITS"), 0, 400);
		spriteBatch.draw(configuration.Y, 270, 260, 70, 70);
		configuration.fontConteurVitesse.draw(spriteBatch,
				String.format("%" + ((int) (16 - "QUITTER".length() / 2)) + "s", "QUITTER"), 0, 200);
		spriteBatch.draw(configuration.B, 270, 60, 70, 70);
		spriteBatch.end();
	}

	private void drawFond() {
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(configuration.COLOR_FOND);
		shapeRenderer.rect(0, 0, 600, 800);
		shapeRenderer.setColor(Color.GOLD);
		shapeRenderer.rect(0, 600, 600, 200);
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.rect(0, 650, 600, 125);
		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.rect(0, 550, 600, 70);
		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.rect(0, 350, 600, 70);
		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.rect(0, 150, 600, 70);
		shapeRenderer.end();
	}

	@Override
	protected void end() {
		update = false;
	}

	@Override
	protected void dispose() {
		super.dispose();
	}

	@Override
	public void connected(Controller controller) {
	}

	@Override
	public void disconnected(Controller controller) {
	}

	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		switch (buttonCode) {
		case 0:
			initWorldsEtape(etapes.get(etape), 0);
			gameActionListener.changeWorld(WorldEnum.Etape);
			break;
		case 1:
			gameActionListener.quitter();
			break;
		case 2:
			break;
		case 3:
			isCredits = !isCredits;
			break;
		case 4:
			etape--;
			if(etape < 0){
				etape = 0;
			}
			break;
		case 5:
			etape++;
			if(etape >= etapes.size()){
				etapes.add(new Etape(etapes.size()));
				etape = etapes.size()-1;
			}
			break;
		default:
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
	public void setController(Controller controller) {
		this.controller = controller;
	}

	@Override
	public Controller getController() {
		return controller;
	}
}

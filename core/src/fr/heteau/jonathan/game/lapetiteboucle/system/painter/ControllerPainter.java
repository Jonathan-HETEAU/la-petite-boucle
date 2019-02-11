package fr.heteau.jonathan.game.lapetiteboucle.system.painter;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import fr.heteau.jonathan.game.lapetiteboucle.GameVisualConfiguration;

public class ControllerPainter extends BaseSystem {

	private GameVisualConfiguration configuration;

	private Camera camera;
	private SpriteBatch spriteBatch;
	private ShapeRenderer shapeRenderer;

	private boolean update;

	private float deltaCurrent;
	private float deltaReff = 0.04f;



	public ControllerPainter(Camera camera,GameVisualConfiguration configuration) {
		super();
		this.configuration = configuration;
		this.camera = camera;
		this.spriteBatch = new SpriteBatch();
		this.shapeRenderer = new ShapeRenderer();
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
			drawFond();
		}
	}

	

	private void drawFond() {
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(configuration.COLOR_FOND);
		shapeRenderer.rect(0, 0, 600, 800);
		shapeRenderer.end();
		spriteBatch.begin();
		spriteBatch.setColor(Color.GOLD);
		spriteBatch.draw(configuration.padController,30,300,540,320);
		spriteBatch.end();
	}

	@Override
	protected void end() {
		update = false;
	}

	@Override
	protected void dispose() {
		super.dispose();
	}
}

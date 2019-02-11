package fr.heteau.jonathan.game.lapetiteboucle.system.painter;

import java.util.concurrent.TimeUnit;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import fr.heteau.jonathan.game.lapetiteboucle.Etape;
import fr.heteau.jonathan.game.lapetiteboucle.GameVisualConfiguration;

public class ArriverPainter extends BaseSystem {

	private GameVisualConfiguration configuration;

	private Camera camera;
	private SpriteBatch spriteBatch;
	private ShapeRenderer shapeRenderer;

	private boolean update;

	private float deltaCurrent;
	private float deltaReff = 0.04f;
	
	private String resultat;


	public ArriverPainter(Camera camera,GameVisualConfiguration configuration,Etape etape, float resultat) {
		super();
		this.configuration = configuration;
		this.camera = camera;
		this.spriteBatch = new SpriteBatch();
		this.shapeRenderer = new ShapeRenderer();
		long millis = (long) (resultat*1000);
		this.resultat = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
	            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
	            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
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
		shapeRenderer.setColor(Color.GOLD);
		shapeRenderer.rect(0, 600,600,200);
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.rect(0, 650,600,125);
		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.rect(0, 500, 600, 75);
		shapeRenderer.end();
		spriteBatch.begin();
		configuration.fontConteurFond.draw(spriteBatch, "    VICTOIRE    ",20,750);
		configuration.fontConteurFond.draw(spriteBatch, 	"8888"+"88:88:88"+"8888",20,550);
		configuration.fontConteurVitesse.draw(spriteBatch, 	"    "+resultat+"    ",20,550);
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

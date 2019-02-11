package fr.heteau.jonathan.game.lapetiteboucle.system.painter;

import java.util.concurrent.TimeUnit;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.EarClippingTriangulator;

import fr.heteau.jonathan.game.lapetiteboucle.Etape;
import fr.heteau.jonathan.game.lapetiteboucle.GameVisualConfiguration;
import fr.heteau.jonathan.game.lapetiteboucle.LaPetiteBoucle;
import fr.heteau.jonathan.game.lapetiteboucle.component.CoureurComponent;
import fr.heteau.jonathan.game.lapetiteboucle.component.HumanComponent;

public class EtapePainter extends BaseEntitySystem {

	private GameVisualConfiguration configuration;

	private PolygonSprite poly;
	private PolygonSpriteBatch polyBatch;
	private Texture textureSolid;

	private Camera camera;
	private SpriteBatch spriteBatch;
	private ShapeRenderer shapeRenderer;

	private Etape etape;
	private float[] traçage;

	private boolean update;

	private float deltaCurrent;
	private float deltaReff = 0.04f;

	private ComponentMapper<CoureurComponent> cmCoureur;

	private float facteurX;
	private float facteurY;



	@SuppressWarnings("unchecked")
	public EtapePainter(Camera camera,GameVisualConfiguration configuration, Etape etape) {
		super(Aspect.all(CoureurComponent.class, HumanComponent.class));
		this.configuration = configuration;
		this.etape = etape;
		initEtapeTraçage();
		this.camera = camera;
		this.spriteBatch = new SpriteBatch();
		this.shapeRenderer = new ShapeRenderer();
		this.polyBatch = new PolygonSpriteBatch();

		

	}

	private void initEtapeTraçage() {
		float[] parcoure = etape.getParcourAltitude();
		float xMax = parcoure[parcoure.length - 2];
		float xMin = parcoure[0];
		float yMin = parcoure[1];
		float yMax = parcoure[1];
		for (int i = 1; i < parcoure.length; i = i + 2) {
			if (parcoure[i] > yMax) {
				yMax = parcoure[i];
			}
			if (parcoure[i] < yMin) {
				yMin = parcoure[i];
			}
		}

		facteurX = (LaPetiteBoucle.WIDTH-20)/ (xMax - xMin);
		facteurY = (190) / (yMax+0.1f);

		float[] vertix = new float[parcoure.length + 2];
		for (int i = 0; i < parcoure.length; i = i + 2) {
			vertix[i] = (parcoure[i] - xMin) * facteurX;
			vertix[i + 1] = (parcoure[i + 1]) * facteurY;
		}
		vertix[parcoure.length] = vertix[parcoure.length - 2];
		vertix[parcoure.length + 1] = 0;
		this.traçage = vertix;
		Pixmap pixmap = new Pixmap((LaPetiteBoucle.WIDTH-20), 190, Format.RGBA8888);
		pixmap.setColor(configuration.COLOR_ETAPE);
		pixmap.fillRectangle(0, 0,(LaPetiteBoucle.WIDTH-20), 190);
		textureSolid = new Texture(pixmap);

		PolygonRegion polyReg = new PolygonRegion(new TextureRegion(textureSolid), this.traçage,
				new EarClippingTriangulator().computeTriangles(vertix).toArray());
		poly = new PolygonSprite(polyReg);
		poly.setOrigin(0, 0);

		Gdx.gl.glLineWidth(4);
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
			polyBatch.setProjectionMatrix(camera.combined);
			spriteBatch.setProjectionMatrix(camera.combined);
			drawFond();
			drawEtapeAltitude();
			IntBag actives = subscription.getEntities();
			int[] ids = actives.getData();
			for (int i = 0, s = actives.size(); s > i; i++) {
				CoureurComponent coureurComponent = cmCoureur.get(ids[i]);
				if(coureurComponent.distance<etape.getDistanceEtape()){
				drawInformation(coureurComponent);
				drawJoueur(coureurComponent);
				drawDashBoard(coureurComponent);
				}
			}
		}
	}

	private void drawJoueur(CoureurComponent coureurComponent) {
		float[] parcour = etape.getParcourAltitude();
		int j = etape.getNumIndex(coureurComponent.distance);
		float distTmp = coureurComponent.distance;
		if (j > 0) {
			distTmp -= etape.getDistancePPcumul()[j-1];
		}
		distTmp = distTmp / etape.getDistancePP()[j];
		
		float position = (parcour[j * 2] + ((parcour[(j + 1) * 2] - parcour[j * 2]) * (distTmp))) * facteurX;
		
		shapeRenderer.translate(10, 600, 0);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(configuration.COLOR_POSITION_PLAYER);
		shapeRenderer.rect(position - 1, -20, 2, 220);
		shapeRenderer.end();
		shapeRenderer.translate(-10, -600, 0);
		spriteBatch.begin();
		spriteBatch.draw(configuration.veloSprite,position-10,545,60,40);
		spriteBatch.end();
	}


	private void drawEtapeAltitude() {
		shapeRenderer.translate(10, 605, 0);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(configuration.COLOR_INFO_BORD);
		shapeRenderer.rect(-4,-4, 588, 198);
		shapeRenderer.setColor(configuration.COLOR_ETAPE_FOND);
		shapeRenderer.rect(0,0, 580, 190);
		shapeRenderer.end();

		polyBatch.begin();
		poly.translate(10, 605);
		poly.draw(polyBatch);
		poly.translate(-10, -605);
		polyBatch.end();

		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(configuration.COLOR_ETAPE_BORD);
		shapeRenderer.polyline(traçage, 0, traçage.length - 2);
		
		shapeRenderer.end();

		shapeRenderer.translate(-10, -605, 0);
	}

	private void drawFond() {
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(configuration.COLOR_FOND);

		shapeRenderer.rect(0, 0, 600, 800);
		shapeRenderer.end();
	}

	private void drawInformation(CoureurComponent coureurComponent) {

		shapeRenderer.translate(0, 400, 0);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(configuration.COLOR_INFO_BORD);
		shapeRenderer.rect(0, 0, 600, 100);
		shapeRenderer.setColor(configuration.COLOR_INFO_FOND);
		for(int i = 0 ; i < 3 ; i++){
			shapeRenderer.rect((i*200)+5, 5, 190, 90);
		}
		shapeRenderer.end();
		shapeRenderer.translate(0, -400, 0);
		
		
		long millis = (long) (coureurComponent.temps *1000);
		
		spriteBatch.begin();
		configuration.fontConteurFond.draw(spriteBatch,"888", 250, 475);
		configuration.fontConteurVitesse.draw(spriteBatch,String.format("%3d",(int)coureurComponent.vitesse), 250, 475);
		configuration.fontConteurTimeFond.draw(spriteBatch,"88:88:88", 20, 470);
		String time = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
	            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
	            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
		configuration.fontConteurTime.draw(spriteBatch,time, 20, 470);
		configuration.fontConteurTime.draw(spriteBatch,String.format(" %3.3f ",etape.getDistanceEtape()-coureurComponent.distance), 420, 470);
		configuration.fontConteurTimeFond.draw(spriteBatch,"DISTANCE", 410, 425);
		configuration.fontConteurTimeFond.draw(spriteBatch," TEMPS", 20, 425);
		configuration.fontConteurTimeFond.draw(spriteBatch,"  Km/H ", 210, 425);
		spriteBatch.end();
		
		
//		float angle = etape.getAngle(coureurComponent.distance);
		
	}

	private void drawDashBoard(CoureurComponent coureurComponent) {
		float position = 20*coureurComponent.tourSeconde;
		float positionIdeal =coureurComponent.vitesseIdeal*20;
		if(position>190){
			position = 190;
		}
		
		spriteBatch.begin();
		spriteBatch.setColor(Color.GOLD);
		spriteBatch.draw(configuration.BL,150,350,40,40);
		spriteBatch.draw(configuration.BR,410,350,40,40);
		spriteBatch.draw(configuration.R,280,300,40,40);
		spriteBatch.draw(configuration.GL,140,250,40,40);
		spriteBatch.draw(configuration.GR,420,250,40,40);
		spriteBatch.draw(configuration.guidonSprite,20,150,560,200);
		spriteBatch.end();
		
		 
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(configuration.COLOR_VITESSE_TOUR);
		shapeRenderer.rect(200,350,200,40);
		if(positionIdeal>-10 && positionIdeal < 200){
			shapeRenderer.setColor(configuration.COLOR_POSITION_IDEAL);
			shapeRenderer.rect(200+positionIdeal,350,10,40);
		}
		shapeRenderer.setColor(configuration.COLOR_POSITION_PLAYER);
		shapeRenderer.rect(200+position,350,10,40);
		shapeRenderer.end();
		
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(configuration.COLOR_VITESSE_TOUR_BORD);
		shapeRenderer.rect(200,350,200,40);
		shapeRenderer.translate(300,320, 0);
		shapeRenderer.circle(0,0, 20);
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.circle(20,0,2);
		shapeRenderer.circle(-20,0,2);
		shapeRenderer.translate(-300,-320,0);
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
}

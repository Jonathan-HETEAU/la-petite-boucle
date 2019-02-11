package fr.heteau.jonathan.game.lapetiteboucle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class GameVisualConfiguration {
	
	public Texture veloSprite = new Texture(Gdx.files.internal("sprites/velo.png"));
	public Texture guidonSprite = new Texture(Gdx.files.internal("sprites/guidon.png"));
	public Texture padController = new Texture(Gdx.files.internal("sprites/Xbox360.png"));
	public Texture A= new Texture(Gdx.files.internal("XBOX360/ButtonA.png"));
	public Texture B= new Texture(Gdx.files.internal("XBOX360/ButtonB.png"));
	public Texture X= new Texture(Gdx.files.internal("XBOX360/ButtonX.png"));
	public Texture Y= new Texture(Gdx.files.internal("XBOX360/ButtonY.png"));
	public Texture L= new Texture(Gdx.files.internal("XBOX360/StickLeft.png"));
	public Texture R= new Texture(Gdx.files.internal("XBOX360/StickRight.png"));
	public Texture GR= new Texture(Gdx.files.internal("XBOX360/TriggerRight.png"));
	public Texture GL= new Texture(Gdx.files.internal("XBOX360/TriggerLeft.png"));
	public Texture BL= new Texture(Gdx.files.internal("XBOX360/BumperLeft.png"));
	public Texture BR= new Texture(Gdx.files.internal("XBOX360/BumperRight.png"));
	
	public Color COLOR_FOND = Color.YELLOW;
	public Color COLOR_ETAPE = Color.GOLDENROD;
	public Color COLOR_ETAPE_BORD = Color.BLACK;
	public Color COLOR_ETAPE_FOND = Color.WHITE;
	public Color COLOR_INFO_FOND =  Color.WHITE;
	public Color COLOR_INFO_BORD = Color.GOLD;
	public Color COLOR_DASHBOARD_FOND = Color.CYAN;
	public Color COLOR_POSITION_PLAYER = Color.RED;
	public Color COLOR_TEXT = Color.BLACK;
	public Color COLOR_VITESSE_TOUR=Color.BLACK;
	public Color COLOR_VITESSE_TOUR_BORD=Color.GOLD;
	public Color COLOR_POSITION_IDEAL = Color.GREEN;
	
	
	public BitmapFont fontConteurVitesse;
	public BitmapFont fontConteurTime;
	public BitmapFont fontConteurFond;
	public BitmapFont fontConteurTimeFond;
	
	private FreeTypeFontGenerator generator= new FreeTypeFontGenerator(
			Gdx.files.internal("fonts/alarmClock.ttf"));;
	
	public GameVisualConfiguration() {
		fontConteurVitesse = generateBitmapFont(generator, Color.BLACK,60); 
		fontConteurFond = generateBitmapFont(generator, Color.GOLD,60); 
		fontConteurTime = generateBitmapFont(generator, Color.BLACK,40); 
		fontConteurTimeFond= generateBitmapFont(generator, Color.GOLD,40); 
	}
	
	public static BitmapFont generateBitmapFont(FreeTypeFontGenerator generator,Color color,int size){
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = size;
		parameter.color = color;
		return generator.generateFont(parameter);
	}
	
	public void dispose() {
		veloSprite.dispose();
		guidonSprite.dispose();
		A.dispose();
		B.dispose();
		X.dispose();
		Y.dispose();
		L.dispose();
		R.dispose();
		GR.dispose();
		GL.dispose();
		BL.dispose();
		BR.dispose();
		generator.dispose();
		fontConteurVitesse.dispose();
		fontConteurFond.dispose();
		fontConteurTime.dispose();
		fontConteurTimeFond.dispose();
		}
	
	
	
}

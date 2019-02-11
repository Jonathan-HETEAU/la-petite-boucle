package fr.heteau.jonathan.game.lapetiteboucle;

import java.util.Random;

public class Etape {
	
	private String name;
	private float[] parcourAltitude;
	
	private float[] distancePP;
	private float[] distancePPcumul;
	private float[] pentePP;
	private float distanceEtape;
	
	public Etape(int dif){
		Random rand = new Random();
		this.name = "ETAPE "+dif;
		this.parcourAltitude = new float[dif*2];
		this.parcourAltitude[0]=0;
		this.parcourAltitude[1]=0;
		for(int i =1; i < dif ; i++){
			int meter = rand.nextInt(99)+1;
			int angle = rand.nextInt(meter)-(meter/2);
			this.parcourAltitude[i*2]=(0.001f*(meter))+this.parcourAltitude[(i-1)*2];
			this.parcourAltitude[(i*2)+1]=this.parcourAltitude[((i-1)*2)+1]+(0.001f*(angle));
			if(this.parcourAltitude[(i*2)+1]<0){
				this.parcourAltitude[(i*2)+1]=0;
			}
			
		}
		
		this.distancePP = new float[(int) (parcourAltitude.length/2-1)];
		this.pentePP = new float[distancePP.length];
		this.distancePPcumul = new float[distancePP.length];
		float cumul = 0;
		for(int i = 0 ; i < distancePP.length ; i++){
			int j1 = (i*2);
			int j2 = (i+1)*2;
			this.distancePP[i]= (float) Math.sqrt(Math.pow(parcourAltitude[j2]-parcourAltitude[j1],2)+Math.pow(parcourAltitude[j2+1]-parcourAltitude[j1+1],2));
			this.pentePP[i]=(float) (90-Math.toDegrees( Math.atan((parcourAltitude[j2]-parcourAltitude[j1])/(parcourAltitude[j2+1]-parcourAltitude[j1+1]))));
			if(this.pentePP[i]>90){
				this.pentePP[i] -=180;
			}
			cumul += this.distancePP[i];
			distancePPcumul[i]=cumul;
		}
		this.distanceEtape = cumul;
	}
	
	public Etape(String name, float[] parcourAltitude) {
		super();
		this.name = name;
		this.parcourAltitude = parcourAltitude;
		this.distancePP = new float[(int) (parcourAltitude.length/2-1)];
		this.pentePP = new float[distancePP.length];
		this.distancePPcumul = new float[distancePP.length];
		float cumul = 0;
		for(int i = 0 ; i < distancePP.length ; i++){
			int j1 = (i*2);
			int j2 = (i+1)*2;
			this.distancePP[i]= (float) Math.sqrt(Math.pow(parcourAltitude[j2]-parcourAltitude[j1],2)+Math.pow(parcourAltitude[j2+1]-parcourAltitude[j1+1],2));
			this.pentePP[i]=(float) (90-Math.toDegrees( Math.atan((parcourAltitude[j2]-parcourAltitude[j1])/(parcourAltitude[j2+1]-parcourAltitude[j1+1]))));
			if(this.pentePP[i]>90){
				this.pentePP[i] -=180;
			}
			cumul += this.distancePP[i];
			distancePPcumul[i]=cumul;
		}
		this.distanceEtape = cumul;
		
	}
	
	public float getAngle(float distance){
		float angle = 0;
		for(int i = 0; i < distancePPcumul.length ; i++ ){
			if(distance < distancePPcumul[i]){
				angle = pentePP[i];
				break;
			}
		}
		return angle;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public int getNumIndex(float distance){
		for(int i = 0; i < distancePPcumul.length ; i++ ){
			if(distance < distancePPcumul[i]){
				return i;
			}
		}
		return distancePPcumul.length;
	}
	public float getDistancePP(float distance) {
		int i = getNumIndex(distance);
		if(i < distancePP.length){
			return distancePP[i];
		}
		return 0;
	} 
	public float getDistancePPcumul(float distance) {
		int i = getNumIndex(distance);
		if(i < distancePPcumul.length){
			return distancePPcumul[i];
		}
		return 0;
	}
	
	public float[] getDistancePP() {
		return distancePP;
	}

	public void setDistancePP(float[] distancePP) {
		this.distancePP = distancePP;
	}

	public float[] getPentePP() {
		return pentePP;
	}

	public void setPentePP(float[] pentePP) {
		this.pentePP = pentePP;
	}

	public float getDistanceEtape() {
		return distanceEtape;
	}

	public void setDistanceEtape(float distanceEtape) {
		this.distanceEtape = distanceEtape;
	}

	public float[] getParcourAltitude() {
		return parcourAltitude;
	}

	public void setParcourAltitude(float[] parcourAltitude) {
		this.parcourAltitude = parcourAltitude;
	}

	public float[] getDistancePPcumul() {
		return distancePPcumul;
	}

	public void setDistancePPcumul(float[] distancePPcumul) {
		this.distancePPcumul = distancePPcumul;
	}
	
	

}

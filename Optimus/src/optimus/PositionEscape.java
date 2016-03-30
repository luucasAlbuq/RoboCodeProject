package optimus;

public class PositionEscape {
	
	public int positionShot;
	public int positionEscape;
	public int angleEscape;
	public Double timeOfResistence;
	
	
	public Double getTimeOfResistence() {
		return timeOfResistence;
	}
	public void setTimeOfResistence(Double timeOfResistence) {
		this.timeOfResistence = timeOfResistence;
	}
	public int getPositionShot() {
		return positionShot;
	}
	public void setPositionShot(int positionShot) {
		this.positionShot = positionShot;
	}
	public int getPositionEscape() {
		return positionEscape;
	}
	public void setPositionEscape(int positionEscape) {
		this.positionEscape = positionEscape;
	}
	public int getAngleEscape() {
		return angleEscape;
	}
	public void setAngleEscape(int angleEscape) {
		this.angleEscape = angleEscape;
	}
	
	public String toString(){
		
		String retorno = positionShot+";"+positionEscape+";"+angleEscape+";"+timeOfResistence+"|";
		
		return retorno;
	}
	
	
	public PositionEscape clone(){
		PositionEscape posicaoFugaClone = new PositionEscape();
		posicaoFugaClone.setAngleEscape(this.getAngleEscape());
		posicaoFugaClone.setPositionEscape(this.getPositionEscape());
		posicaoFugaClone.setPositionShot(this.getPositionShot());
		posicaoFugaClone.setTimeOfResistence(this.getTimeOfResistence());
		
		return posicaoFugaClone;
	}
	
	

}

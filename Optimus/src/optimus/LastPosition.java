package optimus;

public class LastPosition extends PositionEscape {
	
	Long timeShot = new Long(0);
	Long timeToNextShot = new Long(0);
	
	public LastPosition(PositionEscape positionEscape){
		this.angleEscape = positionEscape.getAngleEscape();
		this.positionEscape = positionEscape.getPositionEscape();
		this.positionShot = positionEscape.getPositionShot();
		this.timeOfResistence = positionEscape.getTimeOfResistence();
	}
	
	
	public Long getTimeShot() {
		return timeShot;
	}
	public void setTimeShot(Long timeShot) {
		this.timeShot = timeShot;
	}
	public Long getTimeToNextShot() {
		return timeToNextShot;
	}
	public void setTimeToNextShot(Long timeToNextShot) {
		this.timeToNextShot = timeToNextShot;
	}
	
	
	@Override
	public String toString(){

		String retorno = positionShot+";"+positionEscape+";"+angleEscape+";"+timeOfResistence+";"+timeShot+";"+timeToNextShot+"|";
			
		return retorno;
		
	}
	
	

}

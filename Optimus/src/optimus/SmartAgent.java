package optimus;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.omg.CORBA.ULongSeqHolder;

import robocode.HitByBulletEvent;

public class SmartAgent {
	double enemyX;
	double enemyY;
	boolean podeMovimentar = true;
	double enemyMovimentVelocityX = 0;
	double enemyMovimentVelocityY = 0;
	
	private final int ANGLE_MOVEMENT = 10;
	private final int NUM_ULTIMAS_ACOES = 10;
	public static List<PositionEscape> positionEscapeList = new ArrayList<PositionEscape>();
	public static LastPosition lastPosition;

	public MovementRobot continuousMovement() {
		MovementRobot continuousMovement = new MovementRobot();
		int fatorX = 1;
		int fatorY = 1;

		if (enemyX > 500) {
			fatorX = -1;
		}
		if (enemyY > 300) {
			fatorY = -1;
		}

		if (enemyMovimentVelocityX < 5 && enemyMovimentVelocityX >= 0) {
			continuousMovement.rigth = 0;
		} else if (enemyMovimentVelocityX > 10) {
			continuousMovement.rigth = fatorX * 30;
		} else if (enemyMovimentVelocityX > 30) {
			continuousMovement.rigth = fatorX * 50;
		}else{
			continuousMovement.front = fatorX * 70;
		}

		if (enemyMovimentVelocityY < 5 && enemyMovimentVelocityY >= 0) {
			continuousMovement.front = 0;
		} else if (enemyMovimentVelocityY > 10) {
			continuousMovement.front = fatorY * 30;
		} else if (enemyMovimentVelocityY > 30) {
			continuousMovement.front = fatorY * 50;
		}else{
			continuousMovement.front = fatorY * 70;
		}
		
		return continuousMovement;
	}

	public void updatePosEnemy(double posX, double posY) {
		
		enemyMovimentVelocityX = enemyX - posX;
		enemyMovimentVelocityY = enemyY - posY;

		enemyX = posX;
		enemyY = posY;

	}

	/**
	 * Prepares a nice shot based on distance of the target, bullet's power and enemy's velocity.
	 * 
	 * @param distance
	 * @param power
	 * @return shoot
	 */
	public Shot niceShot(double distance, double energy) {
		if (energy < 20) {
			return new Shot(1, 1);
		}

		if (distance < 180) {
			return new Shot(3, 3);
		} else if (distance < 250) {
			return new Shot(2, 3);
		} else if (distance < 300) {
			return new Shot(1, 2);
		} else if (distance < 550) {
			if (enemyMovimentVelocityX < 5) {
				return new Shot(2, 3);
			}
			return new Shot(2, 1);
		} else if (distance < 700) {
			if (enemyMovimentVelocityX < 5) {
				return new Shot(2, 3);
			}
			return new Shot(1, 1);
		} else if (distance < 900) {
			if (enemyMovimentVelocityX < 5) {
				return new Shot(2, 3);
			}
			return new Shot(1, 1);
		}
		return new Shot(1, 1);
	}

	public double normalRelativeAngle(double angle) {
		if (angle > -180 && angle <= 180) {
			return angle;
		}

		double fixedAngle = angle;
		while (fixedAngle <= -180) {
			fixedAngle += 360;
		}

		while (fixedAngle > 180) {
			fixedAngle -= 360;
		}

		return fixedAngle;
	}

	public double target(double Adv, double dirTanque, double dirMetralhadora) {
		double angleShot = dirTanque + Adv - dirMetralhadora;
		if (!(angleShot > -180 && angleShot <= 180)) {
			while (angleShot <= -180) {
				angleShot += 360;
			}
			while (angleShot > 180) {
				angleShot -= 360;
			}
		}
		return angleShot;
	}

	
	public MovementRobot hit(double anguloBala) throws Exception {

		int angleHit = 0;

		if (anguloBala >= 90 && anguloBala < 180) {
			angleHit = 0;
		} else if (anguloBala >= 0 && anguloBala < 90) {
			angleHit = 1;
		} else if (anguloBala < 0 && anguloBala >= -90) {
			angleHit = 2;
		} else if (anguloBala < -90 && anguloBala >= -180) {
			angleHit = 3;
		}
		
		if (positionEscapeList.isEmpty()) {
			try {
				positionEscapeList = FileController.readFile();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		if (positionEscapeList.isEmpty()) {
			
			for (int angleShot = 0; angleShot < 4; angleShot++) {
				for(int ladoFuga = 0; ladoFuga < 4; ladoFuga++){
					for (int h = 0; h < 19; h++) {
						int angleMovement = h * ANGLE_MOVEMENT;
						PositionEscape positionEscape = new PositionEscape();
						
						positionEscape.setAngleEscape(angleMovement);
						positionEscape.setTimeOfResistence(new Double(0));
						positionEscape.setPositionShot(angleShot);
						positionEscape.setPositionEscape(ladoFuga);
						positionEscapeList.add(positionEscape);
						
					}
				}
			}
			FileController.reacordFile(positionEscapeList);
		}
		
		if (lastPosition != null) {
			lastPosition.setTimeToNextShot(Calendar.getInstance().getTimeInMillis());
			
			for (PositionEscape positionEscape : positionEscapeList) {
				if (positionEscape.getAngleEscape() == lastPosition.getAngleEscape()
						&& positionEscape.getPositionEscape() == lastPosition.getPositionEscape()
						&& positionEscape.getPositionShot() == lastPosition.getPositionShot()) {
					Double tempoResistencia = ((((double) (lastPosition.getTimeToNextShot() - lastPosition.getTimeShot()) / 1000)) + positionEscape.getTimeOfResistence()) / 2;
					
					positionEscape.setTimeOfResistence(tempoResistencia);
					break;
					
				}
			}
		}
		return chooseBestAngle(angleHit);
	}
	
	/**
	 * Make a choose! 
	 * 
	 * @param posicaoTiro
	 * @return
	 */
	public MovementRobot chooseBestAngle(int posicaoTiro) {


		PositionEscape bestPositionEscape= new PositionEscape();
		boolean haPosicoesNaoTestadas = false;
		
		if (!positionEscapeList.isEmpty()) {
			for (PositionEscape postionEscape : positionEscapeList) {
				if (postionEscape.getPositionShot() == posicaoTiro) {
					if (postionEscape.getTimeOfResistence().doubleValue() == 0) {
						bestPositionEscape = postionEscape.clone();	
						haPosicoesNaoTestadas = true;	
						lastPosition = new LastPosition(postionEscape);
						lastPosition.setTimeShot(Calendar.getInstance().getTimeInMillis());
						break;
					}
				}
			}
		}
		
		if (!haPosicoesNaoTestadas) {
			bestPositionEscape = getHighResistance(posicaoTiro);
			lastPosition = new LastPosition(bestPositionEscape);
			lastPosition.setTimeShot(Calendar.getInstance().getTimeInMillis());
		}
		

		switch (bestPositionEscape.getPositionEscape()) {
		case 0:
			return new MovementRobot(100, 0, 0, bestPositionEscape.getAngleEscape());
		case 1:
			return new MovementRobot(100, 0, bestPositionEscape.getAngleEscape(), 0);
		case 2:
			return new MovementRobot(0, 100, 0, bestPositionEscape.getAngleEscape());
		case 3:
			return new MovementRobot(0, 100, bestPositionEscape.getAngleEscape(), 0);
		default:
			return new MovementRobot(100, 0, 0, 0);
		}

	}
	
	public PositionEscape getHighResistance(int posicaoTiro){
		Random random = new Random();
		PositionEscape bestPosition = null;
		for (PositionEscape postionEscape : positionEscapeList) {
			if (postionEscape.getPositionShot() == posicaoTiro) {
				if (bestPosition == null) {
					bestPosition = postionEscape.clone();
				}
				if (postionEscape.getTimeOfResistence().doubleValue() > bestPosition.getTimeOfResistence().doubleValue()) {
					bestPosition = postionEscape.clone();
					
				}else if (postionEscape.getTimeOfResistence().doubleValue() ==  bestPosition.getTimeOfResistence().doubleValue()) {					
					int value = random.nextInt(2);
					if (value == 0) {
						bestPosition = postionEscape.clone();
					}
				}
			}
		}
		return bestPosition;
	}

}
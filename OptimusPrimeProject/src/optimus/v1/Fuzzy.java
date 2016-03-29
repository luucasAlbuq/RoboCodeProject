package optimus.v1;

import robocode.HitRobotEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;
import java.awt.*;
import static robocode.util.Utils.normalRelativeAngleDegrees;

public class Fuzzy extends Robot {

	double bearingFromGun;
	double lastBearingFronGun;
	double energy;
	double energyEnemy = 0;
	double distPertoPert = 0.0;
	double distLongePert = 0.0;
	double energBaixaPert = 0.0;
	double energAltaPert = 0.0;
	double agressBaixaPert = 0.0;
	double agressMediaPert = 0.0;
	double agressAltaPert = 0.0;
	double precision = 0.01;

	public void run() {
		//Colorindo o robo
		setBodyColor(Color.red);
		setGunColor(Color.black);
		setRadarColor(Color.red);
		setBulletColor(Color.cyan);
		setScanColor(Color.cyan);
		
		// dados iniciais----------------------
		energy = this.getEnergy();
		
		while (true) {
			energy = this.getEnergy();
			if (lastBearingFronGun <= 0) {
				turnLeft(10);
			} else {
				turnRight(10);
			}
		}
	}
	
	public void onScannedRobot(ScannedRobotEvent e) {
		energy = this.getEnergy();
		energyEnemy = e.getEnergy();
		double absoluteBearing = getHeading() + e.getBearing();
		lastBearingFronGun = bearingFromGun;
		bearingFromGun = normalRelativeAngleDegrees(absoluteBearing	- getGunHeading());

		if (Math.abs(bearingFromGun) <= 3) {
			turnRight(bearingFromGun);
			if (getGunHeat() == 0) {
				controleNebuloso(e);
			}
		} else {
			turnRight(bearingFromGun);
		}
		
		if (bearingFromGun == 0) {
			scan();
		}
	}

	public void onHitRobot(HitRobotEvent e) {
		back(20);
	}
	
	public void controleNebuloso(ScannedRobotEvent e) {
		fuzificarDist(e.getDistance());
		fuzificarEnergia(this.energy);
		resetAgressividade();
		gerarAgressividade();
		
		double forcaTiro = defuzificar();
		fire(forcaTiro);
		System.out.println(forcaTiro);
		
		
//		if (Math.min(3 - Math.abs(bearingFromGun), getEnergy() - .1) > 2.85) {
//			fire(3);			
//		} else {
		
		ahead(10);
	}
	
	private void resetAgressividade() {
		agressBaixaPert = 0.0;
		agressMediaPert = 0.0;
		agressAltaPert = 0.0;
	}
	
	private double defuzificar() {
		double nivelDeSaida = 0.0;
		double ctr = 0.0;
		
		for (double i = 0.0; i <= 3.0; i+=precision) {
			
			//Agressividade Baixa
			if (i >= 0.0 && i < 1.0) {
				if (agressBaixaPert != 0) {
					nivelDeSaida += agressBaixaPert*i;
					ctr++;
				}
			}
			
			//Agressividade Baixa-M�dia
			else if (i >= 1.0 && i <= 1.5) {
				if (agressBaixaPert > agressMediaPert) {
					if (agressBaixaPert > 0) {
						nivelDeSaida += agressBaixaPert*i;
						ctr++;
					}
				} else {
					if (agressMediaPert > 0) {
						nivelDeSaida += agressMediaPert*i;
						ctr++;
					}
				}
			}
			
			//Agressividade M�dia
			else if (i > 1.5 && i < 2.0) {
				if (agressMediaPert > 0) {
					nivelDeSaida += agressMediaPert*i;
					ctr++;
				}
			}
			
			//Agressividade M�dia-Alta
			else if (i >= 2.0 && i <= 2.5) {
				if (agressMediaPert > agressAltaPert) {
					if (agressMediaPert > 0) {
						nivelDeSaida += agressMediaPert*i;
						ctr++;
					}
				} else {
					if (agressAltaPert > 0) {
						nivelDeSaida += agressAltaPert*i;
						ctr++;
					}
				}
			}
			
			//Agressividade Alta
			else if (i > 2.5) {
				if (agressAltaPert > 0) {
					nivelDeSaida += agressAltaPert*i;
					ctr++;
				}
			}		
		}
		
		nivelDeSaida /= ctr;
		
		return nivelDeSaida;
	}
	
	private void gerarAgressividade() {
		//Energia = Alta
		if (energAltaPert > 0.0) {
			//Dist�ncia = Perto
			if (distPertoPert > 0.0) {
				agressAltaPert += energAltaPert * distPertoPert;
			} 
			//Dist�ncia = Longe
			if (distLongePert > 0.0) {
				agressMediaPert += energAltaPert * distLongePert;
			}	
		}
		//Energia = Baixa
		if (energBaixaPert > 0.0) {
			//Dist�ncia = Perto
			if (distPertoPert > 0.0) {
				agressMediaPert += energBaixaPert * distPertoPert;
			} 
			//Dist�ncia = Longe
			if (distLongePert > 0.0) {
				agressBaixaPert += energBaixaPert * distLongePert;
			}
		}
	}
	
	private void fuzificarDist(double dist) {
		//Perto(x)
		if (dist <= 200) {
			distPertoPert = 1.0;
		} else if (dist > 200 && dist < 300) {
			distPertoPert = (300.0 - dist)/100.0;
		} else {
			distPertoPert = 0.0;
		}
		
		//Longe(x)
		if (dist <= 200) {
			distLongePert = 0.0;
		} else if (dist > 200 && dist < 300) {
			distLongePert = (dist - 200.0)/100.0;
		} else {
			distLongePert = 1.0;
		}
	}
	
	private void fuzificarEnergia(double energia) {
		//Baixa(x)
		if (energia <= 30.0) {
			energBaixaPert = 1.0;
		} else if (energia > 30.0 && energia < 50.0) {
			energBaixaPert = (50.0 - energia)/20.0;
		} else {
			energBaixaPert = 0.0;
		}
		
		//Alta(x)
		if (energia <= 30.0) {
			energAltaPert = 0.0;
		} else if (energia > 30.0 && energia < 50.0) {
			energAltaPert = (energia - 30.0)/20.0;
		} else {
			energAltaPert = 1.0;
		}
	}
	
//	public static void main(String[] args) {
//		
//		double dist = 200.0, energyVal = 1.0;
//		
//		fuzificarDist(dist);
//		fuzificarEnergia(energyVal);
//		gerarAgressividade();
//		
//		double forcaTiro = defuzificar();
//		System.out.println(forcaTiro);
//	}

}
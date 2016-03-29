package util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import robocode.RobocodeFileOutputStream;

public class FileController {
	
	public FileController(){super();};
	
	public static List<PositionEscape> readFile() throws Exception{
		File file = new File("C:/Users/Lucas/workspace/RoboCodeProject/OptimusPrimeProject/bin/optimus/v1/positionsEscape.txt");
		List<PositionEscape> positionsEscape = new ArrayList<PositionEscape>();
		if (file.exists()) {
			Scanner s = new Scanner(file);
			Pattern pattern = Pattern.compile("\\d+;\\d+;\\d+;\\d+\\.\\d+\\|");
			while(s.hasNext()){
				String position = s.next(pattern);
				String[] positions = position.split(";");
				PositionEscape positionEscape = new PositionEscape();
				
				positionEscape.setPositionShot(Integer.parseInt(positions[0]));
				positionEscape.setPositionEscape(Integer.parseInt(positions[1]));
				positionEscape.setAnguleEscape(Integer.parseInt(positions[2]));
				positionEscape.setTimeOfResistance(Double.parseDouble(positions[3].substring(0, positions[3].length()-1)));
				positionsEscape.add(positionEscape);
			}
			s.close();
		}

		return positionsEscape;
	}
	
	public static void record(List<PositionEscape> positionsEscape) throws IOException{
		File file = new File("C:/Users/Lucas/workspace/RoboCodeProject/OptimusPrimeProject/bin/optimus/v1/positionsEscape.txt");
		RobocodeFileOutputStream fos = null;
		try {
			fos = new RobocodeFileOutputStream(file);
			for (PositionEscape posicaoFuga : positionsEscape) {
				fos.write(posicaoFuga.toString().getBytes());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		fos.close();
	}
}

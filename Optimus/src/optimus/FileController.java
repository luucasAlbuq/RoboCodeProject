package optimus;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import robocode.RobocodeFileOutputStream;


public class FileController {
	
	
	public FileController(){}
	
	/**
	 * Read a file
	 * @return
	 * @throws Exception
	 */
	public static List<PositionEscape> readFile() throws Exception{
		File file = new File("C:/robocode/robots/optimus/Optimus.data/positionsEscape.txt");
		List<PositionEscape> posicoesFuga = new ArrayList<PositionEscape>();
		if (file.exists()) {
			Scanner s = new Scanner(file);
			Pattern pattern = Pattern.compile("\\d+;\\d+;\\d+;\\d+\\.\\d+\\|");
			while(s.hasNext()){
				String posicao = s.next(pattern);
				String[] posicoes = posicao.split(";");
				PositionEscape posicaoFuga = new PositionEscape();
				posicaoFuga.setPositionShot(Integer.parseInt(posicoes[0]));
				posicaoFuga.setPositionEscape(Integer.parseInt(posicoes[1]));
				posicaoFuga.setAngleEscape(Integer.parseInt(posicoes[2]));
				posicaoFuga.setTimeOfResistence(Double.parseDouble(posicoes[3].substring(0, posicoes[3].length()-1)));
				posicoesFuga.add(posicaoFuga);
			}
			s.close();
		}
		return posicoesFuga;
	}
	
	
	/**
	 * Feed the file with information about de robot`s positions.
	 * 
	 * @param positionsEscape
	 * @throws IOException
	 */
	public static void reacordFile(List<PositionEscape> positionsEscape) throws IOException{
		
		File file = new File("C:/robocode/robots/optimus/Optimus.data/positionsEscape.txt");
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

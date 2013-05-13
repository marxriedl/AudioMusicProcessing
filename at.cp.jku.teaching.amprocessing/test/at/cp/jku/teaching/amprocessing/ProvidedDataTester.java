package at.cp.jku.teaching.amprocessing;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

public class ProvidedDataTester {
	
	private static final int NUMBER = 7;

	public static void main(String[] args) throws Exception {
	
		File wavFile = getResourceFile("train" + NUMBER + ".wav");
		File beatFile = getResourceFile("train" + NUMBER + ".beats");
		File tempoFile = getResourceFile("train" + NUMBER + ".bpms");
		File onsetFile = getResourceFile("train" + NUMBER + ".onsets");
		File outputFolder = new File(wavFile.getParent() + "/output");
		if(!outputFolder.exists()) {
			outputFolder.mkdir();
		}
		
//	     * -i WAVFILENAME (the file you want to analyze)
//	     * -d DIR (the directory in which the 2 resultfiles (WAVFILENAME.onsets and WAVEFILENAME.tempo) are written to
//	     * -o ONSETGROUNDTRUTHFILE (the file including the onset groundtruth, optional!)
//	     * -t TEMPOGROUNDTRUTHFILE (the file including the tempo groundtruth, optional!)
//	     * -b BEATGROUNDTRUTHFILE (the file including the beat groundtruth, optional!)
		String[] args2 = new String[10]; 
		args2[0] = "-i";
		args2[1] = wavFile.getCanonicalPath();
		args2[2] = "-d";
		args2[3] = outputFolder.getCanonicalPath();
		args2[4] = "-o";
		args2[5] = onsetFile.getCanonicalPath();
		args2[6] = "-t";
		args2[7] = tempoFile.getCanonicalPath();
		args2[8] = "-b";
		args2[9] = beatFile.getCanonicalPath();
		
		Runner.main(args2);
		

	}

	private static File getResourceFile(String name) throws UnsupportedEncodingException {
		URL url = ClassLoader.getSystemResource(name);
		File file = convertToFile(url);
		return file;
	}

	private static File convertToFile(URL url) throws UnsupportedEncodingException {
		File file = new File(URLDecoder.decode(url.getFile(), "UTF-8"));
		return file;
	}
	

}

package p2pRes.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils { //Pretty simple for now, do a filemodel withe a stream visitor to handle io
	public static byte[] readFile(String filePath) throws IOException {
		return Files.readAllBytes(Paths.get(filePath));
	}
	
	public static void writeFile(byte[] toWrite, String filePath) throws IOException {
		Files.write(Paths.get(filePath), toWrite);
	}
}

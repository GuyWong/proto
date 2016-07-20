package p2pRes.tools;

import p2pRes.io.FileReader;
import p2pRes.io.ReaderException;
import p2pRes.log.Logger;
import p2pRes.model.BlocksDescriptor;
import p2pRes.utils.HashBuilder;

public class FileComparator {
	private final String filePath1;
	private final String filePath2;
	private final int blockSize;
	
	public FileComparator(String filePath1, String filePath2, int blockSize) {
		this.filePath1 = filePath1;
		this.filePath2 = filePath2;
		this.blockSize = blockSize;
	}
	
	public void compare() {
		try {
			String[] hashResultsFile1 =  computeFileHash(filePath1, blockSize);
			String[] hashResultsFile2 =  computeFileHash(filePath2, blockSize);
			
			int maxIteration = hashResultsFile1.length>hashResultsFile2.length?
								hashResultsFile1.length:hashResultsFile2.length;
			
			for (int i=0; i<maxIteration; i++) {
				String hash1 = hashResultsFile1.length>i?hashResultsFile1[i]:"EMPTY";
				String hash2 = hashResultsFile2.length>i?hashResultsFile2[i]:"EMPTY";
				if (!hash1.equals(hash2)) {
					Logger.info("Blk" + i + " KO " + hash1 + " " + hash2);
				}
			}
		} catch (ReaderException e) {
			Logger.error(e.getMessage());
		}
	}
	
	private String[] computeFileHash(String path, int blockSize) throws ReaderException {
		FileReader fileReader = new FileReader(path);
		BlocksDescriptor descriptor = new BlocksDescriptor(fileReader.getFileSize(), blockSize);
		
		String[] result = new String[descriptor.getBlockNumbers()];
		for (int i = 0; i<descriptor.getBlockNumbers(); i++) {
			byte[] bloc = fileReader.read(descriptor.getPosition(i), descriptor.getBlockSize(i));
			result[i] = (new HashBuilder(bloc)).build();
		}
		
		return result;
	}
}

package p2pRes.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import p2pRes.io.FileReader;
import p2pRes.io.ReaderException;
import p2pRes.model.BlocksDescriptor;

public class FileHashBuilder {
	private final static int BLOCKSIZE = 1024*1024*1; //1MO blockSize

	private final BlocksDescriptor descriptor;
	
	private FileReader fileReader;

	public FileHashBuilder(String filePath) throws HashBuilderException {
		try {
			this.fileReader = new FileReader(filePath);
			this.descriptor = new BlocksDescriptor(this.fileReader.getFileSize(), BLOCKSIZE);
		} catch (ReaderException e) {
			throw new HashBuilderException("Error opening file " + filePath, e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public String build() throws HashBuilderException {
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		FutureTask<String>[] tasks = new FutureTask[this.descriptor.getBlockNumbers()];;
		
		for (int i = 0; i<this.descriptor.getBlockNumbers(); i++) {
			final int blockNumber = i;
			tasks[blockNumber] = new FutureTask<String>(new Callable<String>() {
				public String call() throws Exception {
					return (new HashBuilder(readBlock(blockNumber))).buildSkein512();
				}
			});
			executorService.execute(tasks[blockNumber]);
		}
		
		executorService.shutdown();
		try {
			executorService.awaitTermination(60*30, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			throw new HashBuilderException("Error awaiting file hashing tasks termination", e);
		}
		
		return (new HashBuilder(aggegateTaskResults(tasks).getBytes())).buildSkein512();
	}
	
	private String aggegateTaskResults(FutureTask<String>[] tasks) throws HashBuilderException {
		StringBuffer strBuff = new StringBuffer();
		
		for (int i=0; i<tasks.length; i++) {
			try {
				strBuff.append(tasks[i].get());
			} catch (InterruptedException e) {
				throw new HashBuilderException("Error getting hash task result block " + i, e);
			} catch (ExecutionException e) {
				throw new HashBuilderException("Error getting hash task result block " + i, e);
			}
		}
		
		return strBuff.toString();
	}
	
	private byte[] readBlock(int blockNumber) throws HashBuilderException {
		try {
			return this.fileReader.read(this.descriptor.getPosition(blockNumber), 
												this.descriptor.getBlockSize(blockNumber));
		} catch (ReaderException e) {
			throw new HashBuilderException("Error reading block " + blockNumber, e);
		}
	}
}

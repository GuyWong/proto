package p2pRes.utils;

import org.junit.Test;
import p2pRes.stats.StatInfo;
import p2pRes.stats.Stats;
import p2pRes.tools.FileComparator;
import org.junit.Assert;

@SuppressWarnings("unused")
public class HashBuilderTest {
	private final String hash_SHA256 = "05e8c7a34e61aee63b3ea2a27ade55c925e04b7f6dc723784290cca01b4dc749";
	private final String hash_SKEIN512 = "d7994e957db6230dfa12ef6a664c0c92780c4fddaee4578911c0d0ec9a32f6ffba7a24cca41237c1fdbc0d9c7336f60125eb84e136064ba43ee17ce4617f5a52";
	
	private final byte[] testBlock = {7,-99,-8,51,-62,3,-33,-78,58,79,19,-125,15,-20,46,24,5,97,44,-117,-59,-115,125,-100,59,-30,-72,102,76,-94,17,-99,102,58,-6,-92,6,-54,-128,-120,-57,120,
			24,-19,-64,120,124,-1,60,-78,27,71,47,-70,46,21,-97,-94,-70,-8,35,29,-2,-95,124,86,21,-101,0,4,125,-26,109,-46,-47,123,115,-68,61,-126,-33,-120,-84,-10,-115,-119,-16,58,-79,-68,
			69,6,124,-96,-17,87,5,-31,-6,11,-40,-29,-19,17,14,88,-81,-65,-30,127,99,55,-118,-55,28,17,-72,-7,62,-128,-45,-114,-8,79,-57,12,27,101,-72,16,-62,114,101,-25,112,-94,19,-111,-10,
			20,-85,40,111,-111,106,-39,45,-41,-27,-89,-19,85,-25,-44,-83,-74,-5,-111,-3,59,49,16,115,51,124,107,94,-53,-87,5,-4,-33,-24,-69,-19,-113,-128,50,28,114,127,-1,67,-32,1,-16,-83,
			121,-74,-96,-67,-105,-14,14,-52,-41,-2,14,-36,67,-21,99,85,-21,83,65,118,87,35,-51,-23,-24,-122,-1,56,-128,8,-1,-35,57,-33,20,-31,5,-56,-78,0,-3,-67,35,43,-35,32,-108,71,48,
			-119,-42,-2,34,-97,-108,93,124,-78,121,-120,95,-82,-86,-19,119,-64,-22,-58,-15,20,25,-14,-125,-67,92,23,-121,-24,47,99,-113,-76,68,57,98,-66,-1,-119,-3,-116,-34,43,36,112,70,
			-29,-28,-6,3,78,59,-31,63,28,48,109,-106,-32,67,9,-55,-105,-99,-62,-120,78,71,-40,82,-84,-95,-66,69,-85,100,-73,95,-106,-97,-75,87,-97,82,-74,-37,-18,71,-12,-20,-60,65,-52,-51,
			-15,-83,123,46,-92,23,-13,127,-94,-17,-74,62,0,-56,113,-55,-1,-3,15,-128,7,-62,-75,-26,-38,-126,-10,95,-56,59,51,95,-8,59,113,15,-83,-115,87,-83,77,5,-39,92,-113,55,-89,-94,27,
			-4,-30,0,35,-1,116,-25,124,83,-124,23,34,-56,3,-10,-12,-116,-81,116,-126,81,28,-62,39,91,-8,-118,126,81,117,-14,-55,-26,33,126,-70,-85,-75,-33,-1,-16,0,0,127,-2,-128,1,-4,60,125,
			-62,119,0,13,59,-128,9,-17,123,-34,-9,-67,-17,0,0,-1,-10,-37,109,-74,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,64,0,0,0,32,16,0,0,0,16,0,0,0,0,0,0,32,0,0,0,34,74,102,38,98,36,
			-94,74,53,106,74,80,100,33,38,57,-48,-125,-104,-26,57,-50,-124,29,9,57,-54,102,45,74,74,76,115,-100,-25,58,16,115,-95,8,57,-50,-108,-91,7,49,-52,-59,-87,73,73,-114,115,-100,-25,
			66,14,116,33,7,57,-46,-108,-96,-25,57,-102,-75,37,40,50,16,-109,28,-24,65,-52,115,28,-25,66,14,-124,-100,-27,51,86,-92,-91,6,66,18,99,-99,8,57,-114,99,-100,-24,65,-48,-109,-100,
			-90,68,0,1,0,0,0,0,0,68,4,1,0,0,0,0,1,16,16,4,0,0,0,0,17,0,0,64,0,0,0,0,68,0,1,0,0,0,0,0,-63,-126,-91,-116,21,46,96,-103,-125,6,74,20,44,84,-55,-126,-28,-54,-108,46,92,-71,-109,
			5,-53,-103,46,84,-127,66,-123,-118,-105,40,92,-55,83,36,-54,22,42,84,-79,114,-123,-52,24,42,84,-63,-125,5,-54,-105,50,100,-55,-126,-91,75,19,40,88,-87,114,-123,-52,-107,50,76,
			-95,98,-91,75,23,40,92,-63,-126,-91,76,24,48,92,-87,115,38,76,-104,42,84,-79,50,-122,12,21,44,96,-87,115,4,-52,24,50,80,-95,98,-90,76,23,38,84,-95,114,-27,-52,-104,46,92,-55,
			114,-92,10,20,48,96,-87,99,5,75,-104,38,96,-63,-110,-123,11,21,50,96,-71,50,-91,11,-105,46,100,-63,114,-26,75,-107,32,80,-89,2,114,-100,-68,15,2,16,30,54,80,86,60,13,-34,27,
			-72,-124,-30,41,61,-5,2,94,-56,-69,-61,-26,47,-12,99,-3,-49,-72,44,94,-35,-62,15,124,-14,65,-39,-7,-119,63,-5,-87,-4,105,-32,125,115,-2,87,-92,-31,-54,-16,6,62,-28,-36,100,-97,
			-71,-80,-21,-60,8,109,-47,-104,95,-2,-107,-39,-39,-98,-77,-42,-12,76,-3,-111,97,-81,-92,-40,68,17,-75,-19,-73,-94,-33,-128,-124,32,-3,0,-117,24,115,-45,-51,10,68,-111,112,-36,
			114,-12,-41,-21,42,-106,17,109,104,117,95,27,-89,51,-20,116,88,101,108,-32,73,-46,-49,-128,98,-25,113,-32,3,-98,-80,58,125,-61,94,86,106,-25,-35,16,-66,64,6,38,101,122,-111,
			61,124,-126,-45,-110,34,-103,-40,108,80,-72,5,58,87,74,-98,-51,-37,116,66,13,-1,-40,41,-21,25,-11,-56,-35,95,35,-125,-38,-63,-100,35,7,-4,102,-37,-17,-23,62,32,-14,1,-25,-16,
			-41,32,-35,41,-31,30,-123,-127,0,2,111,-63,-18,88,107,117,5,-103,-86,-10,90,70,-12,102,39,-7,-39,19,39,-27,88,-41,-68,-111,-123,21} /*1ko block*/

;
	
	
	@Test
	public void testEqualBlocks() {
		Assert.assertEquals("Error, equals hash expected", hash_SKEIN512, (new HashBuilder(testBlock)).build()); ;
	}
	
	@Test
	public void testDifferentBlocks() {
		byte[] differentBlock = testBlock.clone();
		differentBlock[differentBlock.length/2] = (byte) (differentBlock[differentBlock.length/2]+1);
		Assert.assertFalse("Error, different hash expected", hash_SKEIN512.equals((new HashBuilder(differentBlock)).build()));
	}
	
	/*@Test
	public void testFiles() throws HashBuilderException {
		String hash1 = (new FileHashBuilder("D://Dev//Workspace//test//test.mkv")).build();
		String hash2 = (new FileHashBuilder("D://Dev//Workspace//test//test.mkv")).build();
		System.out.println("testFiles");
		System.out.println(hash1);
		System.out.println(hash2);
		Assert.assertEquals(hash1, hash2);
	}
	
	@Test
	public void testFilesErr() throws HashBuilderException {
		String hash1 = (new FileHashBuilder("D://Dev//Workspace//test//test.mkv")).build();
		String hash2 = (new FileHashBuilder("D://Dev//Workspace//test//test.mkv")).buildErr();
		System.out.println("testFilesErr");
		System.out.println(hash1);
		System.out.println(hash2);
		Assert.assertFalse(hash1.equals(hash2));
	}
	*/
	
	/*@Test
	public void testFiles2() throws HashBuilderException {
		String hash1 = (new FileHashBuilder("D://Dev//Workspace//test//test.mkv")).build();
		String hash2 = (new FileHashBuilder("D://Dev//Workspace//test//out//test.mkv")).build();
		System.out.println("testFiles2");
		System.out.println(hash1);
		System.out.println(hash2);
		Assert.assertEquals(hash1, hash2);
	}*/
	
	/*@Test
	public void testHashPerfs() throws HashBuilderException {
		Stats stats=new Stats();
		StatInfo statsFileHashBuilder = stats.getNewCounter("FileHashBuilder");
		
		System.out.println("MonoThreaded hash computing...");
		statsFileHashBuilder.start();
		System.out.println((new FileHashBuilder("D://Dev//Workspace//test//test.mkv")).buildSingleThread());
		statsFileHashBuilder.end();
		System.out.println("MonoThreaded computation time " + statsFileHashBuilder.getStatTime()/1000L);
		
		System.out.println("MultiThreaded hash computing...");
		statsFileHashBuilder.start();
		System.out.println((new FileHashBuilder("D://Dev//Workspace//test//test.mkv")).build());
		statsFileHashBuilder.end();
		System.out.println("MultiThreaded computation time " + statsFileHashBuilder.getStatTime()/1000L);

	}*/
	
	/*@Test
	public void compareFiles() throws HashBuilderException {
		(new FileComparator("D://Dev//Workspace//test//test.mkv",
							"D://Dev//Workspace//test//out//test.mkv",
							1024*100)).compare();
	}*/
}

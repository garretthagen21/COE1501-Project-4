import java.util.*;


public class Substitute implements SymCipher{
	private byte[] key;
	private byte[] decode;
	
	public Substitute() {
	
		ArrayList<Byte> randOrder = new ArrayList<Byte>();
		
		// Java byte range goes from -128 to 127 
		for(int i = -128; i < 128; i++) {
			//System.out.println(i);
			randOrder.add((byte)i);
		}
		Collections.shuffle(randOrder);		//Shuffle the order
		
		//Place the bytes into the instance array in shuffled order
		key = new byte[256];
		decode = new byte[256];
		

		for(int i = 0; i < key.length; i++) {
			byte item = (byte)randOrder.get(i);	
			key[i] = item;
			//System.out.println(key[i]);
			decode[0xFF & item] = (byte)i;
		}
		
		
	
	}
	public Substitute(byte [] array) {
		key = array;
	}

	@Override
	public byte[] getKey() {
		return key;
	}

	/**@category Encode String message by switching the byte value of the key index with the index in the message
	 * 
	 * 
	 * @param The string message: String
	 * @return The encrypted byte array: byte []
	 */
	@Override
	public byte[] encode(String S) {
		byte [] encryptMsg = S.getBytes();		//Initialize the encyrpted message with the string converted to byte array
	
		for(int i = 0; i < encryptMsg.length; i++) {
			encryptMsg[i] = key[0xFF & encryptMsg[i]];
		}
		
		return encryptMsg;
	}
	
	
	
	/**@category Decode the byte array by subtracting the the index of the key array from the corresponding byte array
	 * 
	 * 
	 * @param The encrypted message: byte []
	 * @return The decoded message: String
	 */
	@Override
	public String decode(byte[] bytes) {
		for(int i = 0; i < bytes.length; i++) {
			bytes[i] = decode[0xFF & bytes[i]];
		}
		return new String(bytes);
	}
	
	
}
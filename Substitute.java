import java.util.*;


public class Substitute implements SymCipher{
	private byte[] key;
	private byte[] decode;
	
	public Substitute() {
	
		ArrayList<Byte> randOrder = new ArrayList<Byte>();
		key = new byte[256];
		decode = new byte[256];
		
		// Java byte range goes from -128 to 127 
		for(int i = -128; i < 128; i++) {
			randOrder.add((byte)i);
		}
		Collections.shuffle(randOrder);		//Shuffle the order
		
		//Move the shuffled ArrayList to the key array and make the decode array
		for(int i = 0; i < key.length; i++) {
			byte item = (byte)randOrder.get(i);	
			key[i] = item;
			decode[0xFF & item] = (byte)i;
		}
		
		
	
	}
	public Substitute(byte [] array) {
		key = array;
		decode = new byte[256];
		for(int i = 0; i < key.length;i++) {
			decode[0xFF & key[i]] = (byte)i;
		}
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
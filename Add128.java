import java.util.*;

public class Add128 implements SymCipher{
	private byte [] key;
	
	public Add128() {
		Random randGen = new Random();
		key = new byte[128];
		randGen.nextBytes(key);	
	}
	public Add128(byte [] array) {
		key = array;
	}
	
	
	
	@Override
	public byte[] getKey() {
		return key;
	}
	
	
	/**@category Encode String message by adding the the index of the key array to the corresponding byte array index
	 * 
	 * 
	 * @param The string message: String
	 * @return The encrypted byte array: byte []
	 */
	@Override
	public byte[] encode(String S) {
		
		byte [] encryptMsg = S.getBytes();	//Initialize the encyrpted message with the string converted to byte array
	
		for(int i = 0,j = 0; i < encryptMsg.length; i++,j++) {
			
			if(j == key.length-1)
				j = 0;	
			
			encryptMsg[i] = (byte)(encryptMsg[i] + key[j]);
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
		
		for(int i = 0,j = 0; i < bytes.length; i++,j++) {
			
			if(j == key.length-1)
				j = 0;	
			
			bytes[i] = (byte)(bytes[i] - key[j]);
		
		}
		
		return new String(bytes);
	}
	
	
	
	
}
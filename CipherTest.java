public class CipherTest{
	
		public static void main(String args []) {
			
			SymCipher subCipher = new Substitute();
			SymCipher addCipher = new Add128();
			
			String encodeString  = "The quick brown fox jumped over the lazy dog.";
			
			byte [] addEncode = addCipher.encode(encodeString);
			byte [] subEncode = subCipher.encode(encodeString);
			
			System.out.println("Original String: "+encodeString);
			System.out.println();
			System.out.println("Add128 Encoded Byte Array: "+addEncode.toString());
			System.out.println("Substitute Encoded Byte Array: "+subEncode.toString());
			System.out.println();
			System.out.println("Add128 Decoded String: "+addCipher.decode(addEncode));
			System.out.println("Substitute Decoded String: "+subCipher.decode(subEncode));
			
			
		}
	
	
	
	
	
	
	
	
	
	
	
	
}
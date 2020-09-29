import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class saont {
	
	public static int getRandomInt(int min, int max){

	    int x = (int)(Math.random()*((max-min)+1))+min;
	    return x;

	}
	
	public List<byte[]> splitArray(byte[] items) {
		  List<byte[]> result = new ArrayList<byte[]>();
		  if (items ==null || items.length == 0) {
		      return result;
		  }

		  int from = 0;
		  int to = 0;
		  int slicedItems = 0;
		  int maxSubArraySize = (items.length)/2;
		  while (slicedItems < items.length){
			  int size = getRandomInt(1,maxSubArraySize);
			  to = from + size;
			  byte[] slice = Arrays.copyOfRange(items, from, to);
			  result.add(slice);
			  slicedItems += slice.length;
			  from = to;
			  if (items.length -slicedItems <= maxSubArraySize)
		    	  maxSubArraySize = items.length - slicedItems ;
		  }
		  return result;
		}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		saont saont = new saont();
		
		//Encryption with AES
		String originalString = "Hello";
		final String secretKey = "ssshhhhhhhhhhh!!!!";
	    String encryptedString = aes.encrypt(originalString, secretKey) ;    
	    //String decryptedString = aes.decrypt(encryptedString, secretKey) ;
		byte[] inputArray = encryptedString.getBytes();
		System.out.println(Arrays.toString(inputArray));
		
		
		//Fragmentation of ciphertext into fragments of different sizes
		List<byte[]> fragment = saont.splitArray(inputArray);
		for(int i = 0; i < fragment.size(); i++) {
            System.out.println(Arrays.toString(fragment.get(i)));
        }
		
		//Divide fragments into share_0_pub and share_1
		List<byte[]> share_0_pub = new ArrayList<byte[]>();
		List<byte[]> share_1 = new ArrayList<byte[]>();
		
		for (int i = 0; i< fragment.size(); i++){	
			if(i % 2 == 0)
				share_0_pub.add(fragment.get(i));
			else
				share_1.add(fragment.get(i));
		}
		
		int share_0_pub_subBlocks = 0;
		System.out.println("share_0_pub: ");
		for(int i = 0; i < share_0_pub.size(); i++) {
            System.out.println(Arrays.toString(share_0_pub.get(i)));
            share_0_pub_subBlocks += share_0_pub.get(i).length;
        }
		
		System.out.println("share_1: ");
		int share_1_subBlocks = 0;
		for(int i = 0; i < share_1.size(); i++) {
            System.out.println(Arrays.toString(share_1.get(i)));
            share_1_subBlocks +=share_1.get(i).length;
        }
		
		
		//Fragment share_1 into share_10_priv and share_11
		
		int size = share_1_subBlocks/2;
		int share_10_priv_subBlocks;
		int share_11_subBlocks; // has to be even
		if (size % 2 != 0) { //if size is odd
				share_11_subBlocks = size - 1;
				share_10_priv_subBlocks = share_1_subBlocks - share_11_subBlocks;
		}
		else {
			share_11_subBlocks = size;
			share_10_priv_subBlocks = share_1_subBlocks - share_11_subBlocks;
		}
		
		byte[] share_10_priv = new byte[share_10_priv_subBlocks];
		byte[] share_11 = new byte[share_11_subBlocks];
		byte[] share_1_bytes = new byte [share_1_subBlocks];
		int pos = 0;
		for (int i = 0; i<share_1.size(); i++) {
			System.arraycopy(share_1.get(i), 0, share_1_bytes, pos, share_1.get(i).length);
			pos += share_1.get(i).length;
		}
		
		System.out.println("share_1_bytes: ");
		System.out.println(Arrays.toString(share_1_bytes));
		
		byte[] share_0_pub_bytes = new byte [share_0_pub_subBlocks];
		int pos1 = 0;
		for (int i = 0; i<share_0_pub.size(); i++) {
			System.arraycopy(share_0_pub.get(i), 0, share_0_pub_bytes, pos1, share_0_pub.get(i).length);
			pos1 += share_0_pub.get(i).length;
		}
		
		System.out.println("share_0_pub_bytes:");
		System.out.println(Arrays.toString(share_0_pub_bytes));
		
		System.arraycopy(share_1_bytes, 0, share_10_priv, 0, share_10_priv_subBlocks);
		System.arraycopy(share_1_bytes, share_10_priv_subBlocks, share_11, 0, share_11_subBlocks);
		
		System.out.println("share_10_priv: ");
		System.out.println(Arrays.toString(share_10_priv));
		System.out.println("share_11: ");
		System.out.println(Arrays.toString(share_11));

		//Apply linear AONT to share_11
		int t = 0;
		for (int i=0; i<share_11_subBlocks; i++) {
			t=t^share_11[i];
		}
		int[] share_11_AONT_int = new int[share_11_subBlocks];
		for (int i=0; i<share_11_subBlocks; i++) {
			share_11_AONT_int[i] = t^share_11[i];
		}
		byte[] share_11_AONT = new byte [share_11_subBlocks];
		for (int i=0; i<share_11_subBlocks; i++) {
			share_11_AONT[i] = (byte)share_11_AONT_int[i];
		}
		System.out.println("share_11_AONT: ");
		System.out.println(Arrays.toString(share_11_AONT));
		
		//Fragment share_11_AONT into share_110_priv and share_111_pub
		int size_11 = share_11_subBlocks/2;
		int share_110_priv_subBlocks = size_11;
		int share_111_pub_subBlocks = share_11_subBlocks - share_110_priv_subBlocks;
		
		byte[] share_110_priv = new byte[share_110_priv_subBlocks];
		byte[] share_111_pub = new byte[share_111_pub_subBlocks];
		
		System.arraycopy(share_11_AONT, 0, share_110_priv, 0, share_110_priv_subBlocks);
		System.arraycopy(share_11_AONT, share_110_priv_subBlocks, share_111_pub, 0, share_111_pub_subBlocks);
		
		System.out.println("share_110_priv: ");
		System.out.println(Arrays.toString(share_110_priv));
		System.out.println("share_111_pub: ");
		System.out.println(Arrays.toString(share_111_pub));
		
		//Forming the final public and private fragments
		byte [] fpriv = new byte[share_10_priv_subBlocks + share_110_priv_subBlocks];
		byte [] fpub = new byte [share_0_pub_subBlocks + share_111_pub_subBlocks];
		System.arraycopy(share_10_priv, 0, fpriv, 0, share_10_priv_subBlocks);
		System.arraycopy(share_110_priv, 0, fpriv, share_10_priv_subBlocks, share_110_priv_subBlocks);
		
		System.arraycopy(share_0_pub_bytes, 0, fpub, 0, share_0_pub_subBlocks);
		System.arraycopy(share_111_pub, 0, fpub, share_0_pub_subBlocks, share_111_pub_subBlocks);
		
		System.out.println("fpriv: ");
		System.out.println(Arrays.toString(fpriv));
		System.out.println("fpub: ");
		System.out.println(Arrays.toString(fpub));
		
		
		
		
		
		
		
		
	}

}

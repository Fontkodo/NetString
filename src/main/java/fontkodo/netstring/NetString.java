package fontkodo.netstring;

import java.io.*;

/**
 * 
 * The NetString class provides encoding and decoding of Netstrings,
 * as described in http://cr.yp.to/proto/netstrings.txt.
 * 
 * <P>
 * For example, "Alex Griffin" is encoded as "12:Alex Griffin,".
 * 
 * @author alexgriffin
 *
 */

public class NetString {
	
	NetString() {
		
	}
	
	/**
	 * toNetStringBytes takes a string and formats it into a Netstring.
	 * 
	 * @param string
	 * @return Netstring encoding of string input
	 * @throws IOException
	 */
	public static byte[] toNetStringBytes(String string) throws IOException {
		byte[] tempBytes = string.getBytes();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		ps.printf("%d:", tempBytes.length);
		ps.write(tempBytes);
		ps.printf(",");
		return baos.toByteArray();
	}
	
	/**
	 * readString takes a (valid) Netstring and returns the string it represents.
	 * 
	 * @param is
	 * @return String represented by input Netstring
	 * @throws IOException
	 */
	public static String readString(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder();
		int c = is.read();
		while(c != -1 && Character.isDigit(c)) {
			sb.append((char) c);
			c = is.read();
		}
		if(c != ':') {
			throw new RuntimeException("Bad netString, no colon");
		}
		int size = Integer.parseInt(sb.toString());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while(size > 0 && (c = is.read()) != -1) {
			baos.write(c);
			size--;
		}
		if(is.read() != ',') {
			throw new RuntimeException("Bad netString, no comma");
		}
		return new String(baos.toByteArray());
	}
	
	public static String readString(byte[] bs) throws IOException {
		return readString(new ByteArrayInputStream(bs));
	}
}

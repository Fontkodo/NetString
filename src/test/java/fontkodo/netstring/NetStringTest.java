package fontkodo.netstring;

import static org.junit.Assert.*;

import java.io.*;

import org.junit.Test;

public class NetStringTest {

	static class EncodingPair {
		final String plainText;
		final String encodedText;
		final boolean good;

		EncodingPair(String plainText, String encodedText, boolean good) {
			this.plainText = plainText;
			this.encodedText = encodedText;
			this.good = good;
		}

		EncodingPair(String plainText, String encodedText) {
			this(plainText, encodedText, true);
		}
	}

	static EncodingPair[] myTable = { new EncodingPair("", "0:,"),
			new EncodingPair("ralph", "5:ralph,"),
			new EncodingPair("Sierpiński", "11:Sierpiński,"),
			new EncodingPair("汉字", "6:汉字,"),
			new EncodingPair(":,", "2::,,"),
			new EncodingPair("汉字", "2:汉字,", false),
			new EncodingPair("ralph", "5:ralph<", false),
			new EncodingPair("bye", "100:bye,", false),
			new EncodingPair("hi", "2hi,", false),
			new EncodingPair("gee", "23", false),
			};

	@Test
	public void toNetString() throws IOException {
		new NetString(); //this tricks coverage into working
		for (EncodingPair pair : myTable) {
			String temp = new String(NetString.toNetStringBytes(pair.plainText));
			if (pair.good) {
				assertEquals(temp, pair.encodedText);
			} else {
				assertNotEquals(temp, pair.encodedText);
			}
		}
	}

	@Test
	public void fromNetString() throws IOException {
		for (EncodingPair pair : myTable) {
			if (pair.good) {
				String result = NetString.readString(pair.encodedText.getBytes());
				assertEquals(pair.plainText, result);
			}
		}
	}
	
	static private boolean GoodNetString(String ns) {
		ByteArrayInputStream temp = new ByteArrayInputStream(ns.getBytes());
		try {
			NetString.readString(temp);
			return true;
		} catch(Exception e) {
			return false;
		}
	}

	@Test
	public void fromBadNetString() throws IOException {
		for (EncodingPair pair : myTable) {
//			ByteArrayInputStream temp = new ByteArrayInputStream(pair.encodedText.getBytes());
//			if (pair.good) {
//				NetString.readString(temp);
//				assertTrue(true);
//			} else {
//				assertThrows(Exception.class, () -> {
//					NetString.readString(temp);
//				});
//			}
			assertEquals(pair.good, GoodNetString(pair.encodedText));
		}
	}
}
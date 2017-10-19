package dlt.study.nio.charset;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class CharsetDemo {

	public static String byteToBit(byte b) {
		return "" + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
				+ (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
				+ (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
				+ (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
	}

	public static void println(byte[] bs) {
		for (byte b : bs) {
			String tmp = byteToBit(b);
			System.out.print(tmp);
			System.out.print(" ");
		}
		System.out.println("");
	}

	@Test
	public void getDefault() {
		Charset charset = Charset.defaultCharset();
		System.out.println("charset name is " + charset.name());
	}

	@Test
	public void getCodePoint() {
		String str = "你";
		char aChar = str.charAt(0);
		System.out.println(aChar);
		int codePoint = Character.codePointAt(str, 0);
		System.out.println(Character.isBmpCodePoint(codePoint));
		System.out.println(codePoint);
		byte[] bs = str.getBytes(StandardCharsets.UTF_8);
		System.out.println(bs.length);
		println(bs);
		bs = str.getBytes(StandardCharsets.UTF_16);
		System.out.println(bs.length);
		println(bs);

		System.out.println(new String(bs, StandardCharsets.UTF_16));

		codePoint = 65535 + 30;
		System.out.println(Character.isBmpCodePoint(codePoint));
	}

	@Test
	public void coder() throws CharacterCodingException {
		String src="你好";
		Charset charset = StandardCharsets.UTF_8;
		CharsetEncoder encoder = charset.newEncoder();
		CharsetDecoder decoder = charset.newDecoder();
		CharBuffer charBuffer = CharBuffer.wrap(src.toCharArray());

		ByteBuffer bb = encoder.encode(charBuffer);  // charset.encode(charBuffer);

		println(bb.array());
		byte[] bs = src.getBytes(StandardCharsets.UTF_8);

		println(bs);
		charBuffer = decoder.decode(bb);
		System.out.println(charBuffer.charAt(0));
		System.out.println(new String(bb.array(), StandardCharsets.UTF_8));
	}
	
	@Test
	public void coder2() throws CharacterCodingException {
		Charset charset = StandardCharsets.UTF_16;
		CharsetEncoder encoder = charset.newEncoder();
		CharsetDecoder decoder = charset.newDecoder();
		CharBuffer charBuffer = CharBuffer.wrap(new char[] { '你' });
		ByteBuffer bb = encoder.encode(charBuffer);
		println(bb.array());
		charBuffer = decoder.decode(bb);
		System.out.println(charBuffer.charAt(0));
		
		System.out.println(new String(bb.array(), StandardCharsets.UTF_16));
	}
}

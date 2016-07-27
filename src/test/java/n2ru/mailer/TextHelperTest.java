package n2ru.mailer;

import static org.junit.Assert.*;

import org.junit.Test;

public class TextHelperTest {

	@Test
	public void testPruneSubject() {
		TextHelper underTest = new TextHelper();
		
		assertEquals("Abc", underTest.pruneSubject("Abc."));
		assertEquals("Abc", underTest.pruneSubject("Abc.!:..._-;"));
		
		assertEquals("Abc", underTest.pruneSubject(" Abc . "));
		
		assertEquals("Hallo! Das ist ein Test", underTest.pruneSubject(" Hallo! Das ist ein Test. "));
	}

}

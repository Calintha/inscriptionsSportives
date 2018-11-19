package inscriptions.test;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import inscriptions.Inscriptions;


public class TestInscriptions
{
	@Test
	public void TestReinitialiser()
	{
		Inscriptions inscriptions = Inscriptions.reinitialiser();
		assertTrue(inscriptions.getCompetitions().isEmpty());
	}
}

package inscriptions.test;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import inscriptions.Inscriptions;
import inscriptions.Personne;

public class TestCandidat
{
	@Test
	public void TestGetNom()
	{
		Inscriptions inscriptions = Inscriptions.getInscriptions();
		Personne tony = inscriptions.createPersonne("Tony", "Dent de plomb", "azerty");
		assertEquals("Tony", tony.getNom());
	}
}

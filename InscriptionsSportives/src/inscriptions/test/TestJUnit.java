package inscriptions.test;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import inscriptions.Inscriptions;
import inscriptions.Personne;

public class TestJUnit
{
	@Test
	public void TestGetNom()
	{
		Inscriptions inscriptions = Inscriptions.getInscriptions();
		Personne tony = inscriptions.createPersonne("Tony", "Dent de plomb", "azerty");
		assertEquals("Tony", tony.getNom());
	}
	
	public void TestSetNom()
	{
		Inscriptions inscriptions = Inscriptions.getInscriptions();
		Personne tony = inscriptions.createPersonne("Tony", "Dent de plomb", "azerty");
		assertEquals("Tony", tony.setNom(nom));
	}
}

package inscriptions.test;
import org.junit.Test;
import static org.junit.Assert.assertFalse;

import inscriptions.Inscriptions;
import inscriptions.Competition;
import inscriptions.Candidat;


public class TestCompetition
{
	@Test
	public void TestDelete()
	{
		Competition competition = competition.delete();
		TestCompetition.delete();
		assertFalse(inscriptions.getCompetition().contains(TestCompetition));
		
	}
}

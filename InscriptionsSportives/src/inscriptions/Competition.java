package inscriptions;

import java.io.Serializable;
import java.util.Collections;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Set;
import java.util.TreeSet;

/**
 * Représente une compétition, c'est-à-dire un ensemble de candidats 
 * inscrits à un événement, les inscriptions sont closes à la date dateCloture.
 *
 */

public class Competition implements Comparable<Competition>, Serializable
{
	private static final long serialVersionUID = -2882150118573759729L;
	private Inscriptions inscriptions;
	private String nom;
	private Set<Candidat> candidats;
	private LocalDate dateCloture;
	private boolean enEquipe = false;

	Competition(Inscriptions inscriptions, String nom, LocalDate dateCloture, boolean enEquipe)
	{
		this.enEquipe = enEquipe;
		this.inscriptions = inscriptions;
		this.nom = nom;
		this.dateCloture = dateCloture;
		candidats = new TreeSet<>();
	}
	
	/**
	 * Retourne le nom de la compétition.
	 * @return
	 */
	
	public String getNom()
	{
		return nom;
	}
	
	/**
	 * Modifie le nom de la compétition.
	 */
	
	public void setNom(String nom)
	{
		this.nom = nom ;
	}
	
	/**
	 * Retourne vrai si les inscriptions sont encore ouvertes, 
	 * faux si les inscriptions sont closes.
	 * @return
	 */
	
	public boolean inscriptionsOuvertes()
	{
		// TODO retourner vrai si et seulement si la date systeme est anterieure a la date de cloture.
		if(dateCloture != null) {
			Calendar cal = Calendar.getInstance();
		                int jour = cal.get(Calendar.DAY_OF_MONTH);
		                int mois = cal.get(Calendar.MONTH);
		                int annee = cal.get(Calendar.YEAR);
		                if(annee > dateCloture.getYear()) {
		                    return false;
		                }
		                if(annee == dateCloture.getYear() && mois > dateCloture.getMonthValue()) {
		                    return false;
		                }
		                if(annee == dateCloture.getYear() && mois > dateCloture.getMonthValue() && jour > dateCloture.getDayOfMonth()) {
		                    return false;
		                }
                }
            return true;
	}
	
	/**
	 * Retourne la date de cloture des inscriptions.
	 * @return
	 */
	
	public LocalDate getDateCloture()
	{
		return dateCloture;
	}
	
	/**
	 * Est vrai si et seulement si les inscriptions sont réservées aux équipes.
	 * @return
	 */
	
	public boolean estEnEquipe()
	{
		return enEquipe;
	}
	
	/**
	 * Modifie la date de cloture des inscriptions. Il est possible de la reculer 
	 * mais pas de l'avancer.
	 * @param dateCloture
	 */
	
	public void setDateCloture(LocalDate dateCloture)
	{
		// TODO vérifier que l'on avance pas la date.
		if (this.dateCloture.isBefore(dateCloture))
			System.out.println("erreur: on ne doit pas avancer la date de cloture");
		else
			this.dateCloture = dateCloture;
	}
	
	/**
	 * Retourne l'ensemble des candidats inscrits.
	 * @return
	 */
	
	public Set<Candidat> getCandidats()
	{
		return Collections.unmodifiableSet(candidats);
	}
	
	/**
	 * Inscrit un candidat de type Personne à la compétition. Provoque une
	 * exception si la compétition est réservée aux équipes ou que les 
	 * inscriptions sont closes.
	 * @param personne
	 * @return
	 */
	
	public boolean add(Personne personne)
	{
		// TODO vérifier que la date de clôture n'est pas passée
            if(!inscriptionsOuvertes())
            {
    			throw new RuntimeException();
            }
		if (enEquipe)
			throw new RuntimeException();
		personne.add(this);
		return candidats.add(personne);
	}

	/**
	 * Inscrit un candidat de type Equipe à la compétition. Provoque une
	 * exception si la compétition est réservée aux personnes ou que 
	 * les inscriptions sont closes.
	 * @param personne
	 * @return
	 */

	public boolean add(Equipe equipe)
	{
		// TODO vérifier que la date de clôture n'est pas passée
        if(!inscriptionsOuvertes())
        {
            return false;
        }
		if (!enEquipe)
			throw new RuntimeException();
		equipe.add(this);
		return candidats.add(equipe);
	}
	
	/**
	 * Retourne les Candidats que l'on peut inscrire à cette competition.
	 * @return les candidats que l'on peut inscrire à cette compétition.
	 */
	
	public Set<Candidat> getCandidatsAInscrire()
	{
		// TODO les candidats que l'on peut inscrire à cette compétition.
		{
            Set<Candidat> candidatsAInscrire = new TreeSet<>();
            for(Candidat c : inscriptions.getCandidats()) {
                if(!(c instanceof Equipe) && !candidats.contains(c)) {
                    candidatsAInscrire.add(c);
                }
            }
            return candidatsAInscrire;
	}
		
	}

	/**
	 * Désinscrit un candidat.
	 * @param candidat
	 * @return
	 */
	
	public boolean remove(Candidat candidat)
	{
		candidat.remove(this);
		return candidats.remove(candidat);
	}
	
	/**
	 * Supprime la compétition de l'application.
	 */
	
	public void delete()
	{
		for (Candidat candidat : candidats)
			remove(candidat);
		inscriptions.delete(this);
	}
	
	@Override
	public int compareTo(Competition o)
	{
		return getNom().compareTo(o.getNom());
	}
	
	@Override
	public String toString()
	{
		return getNom();
	}
}


package inscriptions;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.time.LocalDate;
import java.util.SortedSet;
import java.util.TreeSet;

import commandLineMenus.Menu;
import commandLineMenus.Option;
import commandLineMenus.rendering.examples.util.InOut;
import commandLineMenus.Action;
import commandLineMenus.List;
import commandLineMenus.ListAction;
import commandLineMenus.ListData;
/**
 * Point d'entree dans l'application, un seul objet de type Inscription
 * permet de gerer les competitions, candidats (de type equipe ou personne)
 * ainsi que d'inscrire des candidats a des competitions.
 */

public class Inscriptions implements Serializable
{
	private static final long serialVersionUID = -3095339436048473524L;
	private static final String FILE_NAME = "Inscriptions.srz";
	private static Inscriptions inscriptions;
	
	private SortedSet<Competition> competitions = new TreeSet<>();
	private SortedSet<Candidat> candidats = new TreeSet<>();

	private Inscriptions()
	{
	}
	
	/**
	 * Retourne les competitions.
	 * @return
	 */
	
	public SortedSet<Competition> getCompetitions()
	{
		return Collections.unmodifiableSortedSet(competitions);
	}
	
	/**
	 * Retourne tous les candidats (personnes et equipes confondues).
	 * @return
	 */
	
	public SortedSet<Candidat> getCandidats()
	{
		return Collections.unmodifiableSortedSet(candidats);
	}

	/**
	 * Retourne toutes les personnes.
	 * @return
	 */
	
	public SortedSet<Personne> getPersonnes()
	{
		SortedSet<Personne> personnes = new TreeSet<>();
		for (Candidat c : getCandidats())
			if (c instanceof Personne)
				personnes.add((Personne)c);
		return Collections.unmodifiableSortedSet(personnes);
	}

	/**
	 * Retourne toutes les equipes.
	 * @return
	 */
	
	public SortedSet<Equipe> getEquipes()
	{
		SortedSet<Equipe> equipes = new TreeSet<>();
		for (Candidat c : getCandidats())
			if (c instanceof Equipe)
				equipes.add((Equipe)c);
		return Collections.unmodifiableSortedSet(equipes);
	}

	/**
	 * Cree une competition. Ceci est le seul moyen, il n'y a pas
	 * de constructeur public dans {@link Competition}.
	 * @param nom
	 * @param dateCloture
	 * @param enEquipe
	 * @return
	 */
	
	public Competition createCompetition(String nom, 
			LocalDate dateCloture, boolean enEquipe)
	{
		Competition competition = new Competition(this, nom, dateCloture, enEquipe);
		competitions.add(competition);
		return competition;
	}

	/**
	 * Cree une Candidat de type Personne. Ceci est le seul moyen, il n'y a pas
	 * de constructeur public dans {@link Personne}.

	 * @param nom
	 * @param prenom
	 * @param mail
	 * @return
	 */
	
	public Personne createPersonne(String nom, String prenom, String mail)
	{
		Personne personne = new Personne(this, nom, prenom, mail);
		candidats.add(personne);
		return personne;
	}
	
	/**
	 * Cree une Candidat de type equipe. Ceci est le seul moyen, il n'y a pas
	 * de constructeur public dans {@link Equipe}.
	 * @param nom
	 * @param prenom
	 * @param mail
	 * @return
	 */
	
	public Equipe createEquipe(String nom)
	{
		Equipe equipe = new Equipe(this, nom);
		candidats.add(equipe);
		return equipe;
	}
	
	void delete(Competition competition)
	{
		competitions.remove(competition);
	}
	
	void delete(Personne personne) {
		for(Competition c : competitions) {
			if(c.getCandidats().contains(personne)) {
				c.remove(personne);
			}
		}
		for(Equipe e : getEquipes()) {
			if(e.getMembres().contains(personne)) {
				e.remove(personne);
			}
		}
		candidats.remove(personne);
	}

	
	void delete(Candidat candidat)
	{
		candidats.remove(candidat);
	}
	
	/**
	 * Retourne l'unique instance de cette classe.
	 * Cree cet objet s'il n'existe deja .
	 * @return l'unique objet de type {@link Inscriptions}.
	 */
	
	public static Inscriptions getInscriptions()
	{
		
		if (inscriptions == null)
		{
			inscriptions = readObject();
			if (inscriptions == null)
				inscriptions = new Inscriptions();
		}
		return inscriptions;
	}

	/**
	 * Retourne un object inscriptions vide. Ne modifie pas les competitions
	 * et candidats deja  existants.
	 */
	
	public static Inscriptions reinitialiser()
	{
		inscriptions = new Inscriptions();
		return getInscriptions();
	}

	/**
	 * Efface toutes les modifications sur Inscriptions depuis la derniere sauvegarde.
	 * Ne modifie pas les competitions et candidats deja  existants.
	 */
	
	public Inscriptions recharger()
	{
		inscriptions = null;
		return getInscriptions();
	}
	
	private static Inscriptions readObject()
	{
		ObjectInputStream ois = null;
		try
		{
			FileInputStream fis = new FileInputStream(FILE_NAME);
			ois = new ObjectInputStream(fis);
			return (Inscriptions)(ois.readObject());
		}
		catch (IOException | ClassNotFoundException e)
		{
			return null;
		}
		finally
		{
				try
				{
					if (ois != null)
						ois.close();
				} 
				catch (IOException e){}
		}	
	}
	
	/**
	 * Sauvegarde le gestionnaire pour qu'il soit ouvert automatiquement 
	 * lors d'une execution ulterieure du programme.
	 * @throws IOException 
	 */
	
	public void sauvegarder() throws IOException
	{
		ObjectOutputStream oos = null;
		try
		{
			FileOutputStream fis = new FileOutputStream(FILE_NAME);
			oos = new ObjectOutputStream(fis);
			oos.writeObject(this);
		}
		catch (IOException e)
		{
			throw e;
		}
		finally
		{
			try
			{
				if (oos != null)
					oos.close();
			} 
			catch (IOException e){}
		}
	}
	
	@Override
	public String toString()
	{
		return "Candidats : " + getCandidats().toString()
			+ "\nCompetitions  " + getCompetitions().toString();
	}

	public static void main(String[] args)
	{
		try
		{
			inscriptions.sauvegarder();
		}
		catch (IOException e)
		{
			System.out.println("Sauvegarde impossible." + e);
		}
		inscriptions.afficherMenu(inscriptions);
	}
	
	private ArrayList<String> compL = new ArrayList<String>();
	
	public void afficherMenu(Inscriptions inscriptions) {
		compL = new ArrayList<String>();
		compL.add("Gérer les compétitions");
		compL.add("Accéder à la liste des joueurs inscrits");
		for(Competition c : inscriptions.getCompetitions()) {
			compL.add(c.getNom());
		}
		// On créé le menu
		List<String> menu = new List<String>("Liste des compétitions",
			new ListData<String>()		
			{
			// La liste des compétitions
				public java.util.List<String> getList()
				{
					return compL;
				}
			},
			new ListOption<String>()
			{
				public Option getOption(String arg0)
				{
					if(arg0 != "Gérer les compétitions" && arg0 != "Accéder à la liste des joueurs inscrits")
					{
						return optionsCompetition(arg0, inscriptions);
					}
					else if(arg0.startsWith("G")) { // Donc gérer les compétitions
						return gererCompetition(inscriptions);
					}
					else {
						return listeInscrits(inscriptions);
					}
					//return optionsCompetition(arg0, inscriptions);
				}
			});
		menu.setAutoBack(false);
		menu.addQuit("q");
		menu.start();
	}
	
	public Menu gererCompetition(Inscriptions inscriptions) {
		Menu compMenu = new Menu("Gérer les compétitions");
		compMenu.add(new Option("Créer une compétition", "c", new Action() {
			public void optionSelected() {
				String nom = InOut.getString("Choississez le nom de la compétition : ");
				String date = InOut.getString("Voulez-vous mettre une date de cloture (o/n) : ");
				if(date.equals("o")) {
					int annee = InOut.getInt("Année de cloture : ");
					int mois = InOut.getInt("Mois de cloture : ");
					int jour = InOut.getInt("Jour de cloture : ");
					String eq = InOut.getString("Le tournoi est-il en équipe (o/n) ? ");
					if(eq.equals("o")) {
						Competition c = inscriptions.createCompetition(nom, LocalDate.of(annee, mois, jour), true);
						compL.add(c.getNom());
					}
					else if(eq.equals("n")) {
						Competition c = inscriptions.createCompetition(nom, LocalDate.of(annee, mois, jour), false);
						compL.add(c.getNom());
					}
				}
				else if(date.equals("n")) {
					String eq = InOut.getString("Le tournoi est-il en équipe (o/n) ? ");
					if(eq.equals("o")) {
						Competition c = inscriptions.createCompetition(nom, null, true);
						compL.add(c.getNom());
					}
					else if(eq.equals("n")) {
						Competition c = inscriptions.createCompetition(nom, null, false);
						compL.add(c.getNom());
					}
				}
			}
		}));
		compMenu.add(new Option("Supprimer une compétition", "s", new Action() {
			public void optionSelected() {
				int nbr = 0;
				for(Competition c : inscriptions.getCompetitions()) {
					System.out.println(nbr+" : "+c.getNom());
					nbr++;
				}
				System.out.println(nbr+" : Annuler");
				int choix = InOut.getInt("Taper le numéro de la compétition à supprimer : ");
				nbr = 0;
				for(Competition c : inscriptions.getCompetitions()) {
					if(choix == nbr) {
						inscriptions.delete(c);
						compL.remove(c.getNom());
						break;
					}
					nbr++;
				}
			}
		}));
		compMenu.addBack("b");
		return compMenu;
	}
	
	public Menu listeInscrits(Inscriptions inscriptions) {
		Menu inscrits = new Menu("Liste des inscrits");
		inscrits.add(new Option("Ajouter une personne", "a", new Action() {
			public void optionSelected() {
				String prenom = InOut.getString("Choississez le prénom : ");
				String nom = InOut.getString("Choississez le nom : ");
				String email = InOut.getString("Choississez un email : ");
				if(prenom.trim().equals("") || nom.trim().equals("") || email.trim().equals("")) {
					System.out.println("Le prénom, nom ou email ne sont pas valides");
				}
				else {
					inscriptions.createPersonne(nom, prenom, email);
				}
			}
		}));
		inscrits.add(new Option("Ajouter une équipe", "e", new Action() {
			public void optionSelected() {
				String nom = InOut.getString("Choississez le nom de l'équipe : ");
				if(nom.trim().equals("")) {
					System.out.println("Le prénom, nom ou email ne sont pas valides");
				}
				else {
					inscriptions.createEquipe(nom);
				}
			}
		}));
		inscrits.add(new Option("Ajouter un membre à une équipe", "t", new Action() {
			public void optionSelected() {
				int nbr = 0;
				for(Equipe c : inscriptions.getEquipes()) {
					System.out.println(nbr+" : "+c.getNom());
					nbr++;
				}
				System.out.println(nbr+" : Annuler");
				int choix = InOut.getInt("Quelle équipe : ");
				nbr = 0;
				for(Equipe e : inscriptions.getEquipes()) {
					if(choix == nbr) {
						nbr = 0;
						for(Candidat c : inscriptions.getPersonnes()) {
							System.out.println(nbr+" : "+c.getNom());
							nbr++;
						}
						System.out.println(nbr+" : Annuler");
						choix = InOut.getInt("Taper le numéro de la personne à ajouter à l'équipe : ");
						nbr = 0;
						for(Personne p : inscriptions.getPersonnes()) {
							if(choix == nbr) {
								afficherAjout(e.add(p));
								break;
							}
							nbr++;
						}
						break;
					}
					nbr++;
				}
			}
		}));
		inscrits.add(new Option("Supprimer une personne", "s", new Action() {
			public void optionSelected() {
				int nbr = 0;
				for(Candidat c : inscriptions.getPersonnes()) {
					System.out.println(nbr+" : "+c.getNom());
					nbr++;
				}
				System.out.println(nbr+" : Annuler");
				int choix = InOut.getInt("Taper le numéro de la personne à supprimer : ");
				nbr = 0;
				for(Personne c : inscriptions.getPersonnes()) {
					if(choix == nbr) {
						inscriptions.delete(c);
						break;
					}
					nbr++;
				}
			}
		}));
		inscrits.add(new Option("Supprimer une équipe", "r", new Action() {
			public void optionSelected() {
				int nbr = 0;
				for(Candidat c : inscriptions.getEquipes()) {
					System.out.println(nbr+" : "+c.getNom());
					nbr++;
				}
				System.out.println(nbr+" : Annuler");
				int choix = InOut.getInt("Taper le numéro de l'équipe à supprimer : ");
				nbr = 0;
				for(Equipe e : inscriptions.getEquipes()) {
					if(choix == nbr) {
						inscriptions.delete(e);
						break;
					}
					nbr++;
				}
			}
		}));
		inscrits.add(new Option("Supprimer un membre d'une équipe", "u", new Action() {
			public void optionSelected() {
				int nbr = 0;
				for(Equipe c : inscriptions.getEquipes()) {
					System.out.println(nbr+" : "+c.getNom());
					nbr++;
				}
				System.out.println(nbr+" : Annuler");
				int choix = InOut.getInt("Quelle équipe : ");
				nbr = 0;
				for(Equipe e : inscriptions.getEquipes()) {
					if(choix == nbr) {
						nbr = 0;
						for(Candidat c : inscriptions.getPersonnes()) {
							System.out.println(nbr+" : "+c.getNom());
							nbr++;
						}
						System.out.println(nbr+" : Annuler");
						choix = InOut.getInt("Taper le numéro de la personne à ajouter à l'équipe : ");
						nbr = 0;
						for(Personne p : e.getMembres()) {
							if(choix == nbr) {
								afficherSuppression(e.remove(p));
								break;
							}
							nbr++;
						}
						break;
					}
					nbr++;
				}
			}
		}));
		inscrits.add(new Option("Afficher la liste des personnes", "l", new Action() {
			public void optionSelected() {
				int nbr = 0;
				for(Personne p : inscriptions.getPersonnes()) {
					System.out.println(nbr+" : "+p.getNom());
					nbr++;
				}
			}
		}));
		inscrits.add(new Option("Afficher la liste des équipes", "q", new Action() {
			public void optionSelected() {
				int nbr = 0;
				for(Equipe e : inscriptions.getEquipes()) {
					System.out.println(nbr+" : "+e.getNom());
					System.out.println("Constituée de "+e.getMembres());
					nbr++;
				}
			}
		}));
		inscrits.addBack("b");
		return inscrits;
	}
	
	private Competition compTmp;
	
	public Option optionsCompetition(String nom, Inscriptions inscriptions) {
		for(Competition c : inscriptions.getCompetitions()) {
			if(c.getNom() == nom) {
				compTmp = c;
			}
		}
		ArrayList<String> options = new ArrayList<String>();
		options.add("Ajouter une "+(compTmp.estEnEquipe() ? "une équipe" : "une personne")+" parmis celles inscrites");
		options.add("Retirer une personne "+(compTmp.estEnEquipe() ? "une équipe" : "une personne")+" de la compétitions");
		options.add("Afficher les participants à la compétitions");
		List<String> menu = new List<String>(nom+" "+(compTmp.estEnEquipe() ? "(en équipe)" : "(pas en équipe)"), 
				new ListData<String>()		
				{
					public java.util.List<String> getList()
					{
						return options;
					}	
				},
				new ListAction<String>()
				{
					public void itemSelected(int index, String someone)
					{
						switch(index) {
							case 0:
								if(!compTmp.inscriptionsOuvertes()) {
									System.out.println("Inscriptions fermées.");
								}
								else {
									if(!compTmp.estEnEquipe()) {
										int nbr = 0;
										for(Personne c : inscriptions.getPersonnes()) {
											System.out.println(nbr+" : "+c.getNom());
											nbr++;
										}
										System.out.println(nbr+" : Annuler");
										int choix = InOut.getInt("Taper le numéro de la personne à ajouter : ");
										nbr = 0;
										for(Personne c : inscriptions.getPersonnes()) {
											if(choix == nbr) {
												afficherAjout(compTmp.add(c));
												break;
											}
											nbr++;
										}
									}
									else {
										int nbr = 0;
										for(Equipe e : inscriptions.getEquipes()) {
											System.out.println(nbr+" : "+e.getNom());
											nbr++;
										}
										System.out.println(nbr+" : Annuler");
										int choix = InOut.getInt("Taper le numéro de l'équipe à ajouter : ");
										nbr = 0;
										for(Equipe e : inscriptions.getEquipes()) {
											if(choix == nbr) {
												afficherAjout(compTmp.add(e));
												break;
											}
											nbr++;
										}
									}
								}
								break;
							case 1:
								int nbr = 0;
								for(Candidat c : compTmp.getCandidats()) {
									System.out.println(nbr+" : "+c.getNom());
									nbr++;
								}
								System.out.println(nbr+" : Annuler");
								int choix = InOut.getInt("Taper le numéro de "+(compTmp.estEnEquipe() ? "l'équipe" : "la personne")+" à supprimer : ");
								nbr = 0;
								for(Candidat c : compTmp.getCandidats()) {
									if(choix == nbr) {
										afficherSuppression(compTmp.remove(c));
										break;
									}
									nbr++;
								}
								break;
							case 2:
								System.out.println("Liste des "+(compTmp.estEnEquipe() ? "équipes" : "participants")+" :");
								for(Candidat e : compTmp.getCandidats()) {
									System.out.println(e.getNom());
								}
								break;
						}
					}
				});
		menu.setAutoBack(false);
		menu.addBack("b");
		return menu;
	}
	
	public void afficherAjout(boolean a) {
		if(a) {
			System.out.println("Ajout effectué");
		}
		else {
			System.out.println("Echec de l'ajout");
		}
	}
	
	public void afficherSuppression(boolean s) {
		if(s) {
			System.out.println("Ajout effectué");
		}
		else {
			System.out.println("Echec de l'ajout");
		}
	}
}

	}
}
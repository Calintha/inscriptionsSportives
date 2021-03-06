package inscriptions;

import java.time.LocalDate;
import java.util.ArrayList;

import commandLineMenus.Action;
import commandLineMenus.List;
import commandLineMenus.ListAction;
import commandLineMenus.ListData;
import commandLineMenus.ListOption;
import commandLineMenus.Menu;
import commandLineMenus.Option;
import commandLineMenus.rendering.examples.util.InOut;

public class Affichage {
	
	public Affichage() {}
	
	private ArrayList<String> compL = new ArrayList<String>();
	private Competition compTmp;

	public void afficherMenu(Inscriptions inscriptions) {
		compL = new ArrayList<String>();
		compL.add("G�rer les comp�titions");
		compL.add("Acc�der � la liste des joueurs inscrits");
		for(Competition c : inscriptions.getCompetitions()) {
			compL.add(c.getNom());
		}
		// On cr�� le menu
		List<String> menu = new List<String>("Liste des comp�titions", 
			new ListData<String>()		
			{
			// La liste des comp�titions
				public java.util.List<String> getList()
				{
					return compL;
				}
			},
			new ListOption<String>() {
				public Option getOption(String arg0) {
					if(arg0 != "G�rer les comp�titions" && arg0 != "Acc�der � la liste des joueurs inscrits") {
						return optionsCompetition(arg0, inscriptions);
					}
					else if(arg0.startsWith("G")) { // Donc g�rer les comp�titions
						return gererCompetition(inscriptions);
					}
					else {
						return listeInscrits(inscriptions);
					}
				}
			});
		menu.setAutoBack(false);
		menu.addQuit("q");
		menu.start();
	}
	
	public Menu gererCompetition(Inscriptions inscriptions) {
		Menu compMenu = new Menu("G�rer les comp�titions");
		compMenu.add(new Option("Cr�er une comp�tition", "c", new Action() {
			public void optionSelected() {
				String nom = InOut.getString("Choississez le nom de la comp�tition : ");
				String date = InOut.getString("Voulez-vous mettre une date de cloture (o/n) : ");
				if(date.equals("o")) {
					int annee = InOut.getInt("Ann�e de cloture : ");
					int mois = InOut.getInt("Mois de cloture : ");
					int jour = InOut.getInt("Jour de cloture : ");
					String eq = InOut.getString("Le tournoi est-il en �quipe (o/n) ? ");
					if(eq.equals("o")) {
						Competition c = inscriptions.createCompetition(nom, LocalDate.of(annee, mois - 1, jour), true);
						compL.add(c.getNom());
					}
					else if(eq.equals("n")) {
						Competition c = inscriptions.createCompetition(nom, LocalDate.of(annee, mois - 1, jour), false);
						compL.add(c.getNom());
					}
				}
				else if(date.equals("n")) {
					String eq = InOut.getString("Le tournoi est-il en �quipe (o/n) ? ");
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
		compMenu.add(new Option("Supprimer une comp�tition", "s", new Action() {
			public void optionSelected() {
				int nbr = 0;
				for(Competition c : inscriptions.getCompetitions()) {
					System.out.println(nbr+" : "+c.getNom());
					nbr++;
				}
				System.out.println(nbr+" : Annuler");
				int choix = InOut.getInt("Taper le num�ro de la comp�tition � supprimer : ");
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
				String prenom = InOut.getString("Choississez le pr�nom : ");
				String nom = InOut.getString("Choississez le nom : ");
				String email = InOut.getString("Choississez un email : ");
				if(prenom.trim().equals("") || nom.trim().equals("") || email.trim().equals("")) {
					System.out.println("Le pr�nom, nom ou email ne sont pas valides");
				}
				else {
					inscriptions.createPersonne(nom, prenom, email);
				}
			}
		}));
		inscrits.add(new Option("Ajouter une �quipe", "e", new Action() {
			public void optionSelected() {
				String nom = InOut.getString("Choississez le nom de l'�quipe : ");
				if(nom.trim().equals("")) {
					System.out.println("Le pr�nom, nom ou email ne sont pas valides");
				}
				else {
					inscriptions.createEquipe(nom);
				}
			}
		}));
		inscrits.add(new Option("Ajouter un membre � une �quipe", "t", new Action() {
			public void optionSelected() {
				int nbr = 0;
				for(Equipe c : inscriptions.getEquipes()) {
					System.out.println(nbr+" : "+c.getNom());
					nbr++;
				}
				System.out.println(nbr+" : Annuler");
				int choix = InOut.getInt("Quelle �quipe : ");
				nbr = 0;
				for(Equipe e : inscriptions.getEquipes()) {
					if(choix == nbr) {
						nbr = 0;
						for(Candidat c : inscriptions.getPersonnes()) {
							System.out.println(nbr+" : "+c.getNom());
							nbr++;
						}
						System.out.println(nbr+" : Annuler");
						choix = InOut.getInt("Taper le num�ro de la personne � ajouter � l'�quipe : ");
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
				int choix = InOut.getInt("Taper le num�ro de la personne � supprimer : ");
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
		inscrits.add(new Option("Supprimer une �quipe", "r", new Action() {
			public void optionSelected() {
				int nbr = 0;
				for(Candidat c : inscriptions.getEquipes()) {
					System.out.println(nbr+" : "+c.getNom());
					nbr++;
				}
				System.out.println(nbr+" : Annuler");
				int choix = InOut.getInt("Taper le num�ro de l'�quipe � supprimer : ");
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
		inscrits.add(new Option("Supprimer un membre d'une �quipe", "u", new Action() {
			public void optionSelected() {
				int nbr = 0;
				for(Equipe c : inscriptions.getEquipes()) {
					System.out.println(nbr+" : "+c.getNom());
					nbr++;
				}
				System.out.println(nbr+" : Annuler");
				int choix = InOut.getInt("Quelle �quipe : ");
				nbr = 0;
				for(Equipe e : inscriptions.getEquipes()) {
					if(choix == nbr) {
						nbr = 0;
						for(Personne p : e.getMembres()) {
							System.out.println(nbr+" : "+p.getNom());
							nbr++;
						}
						System.out.println(nbr+" : Annuler");
						choix = InOut.getInt("Taper le num�ro de la personne � ajouter � l'�quipe : ");
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
		inscrits.add(new Option("Afficher la liste des �quipes", "q", new Action() {
			public void optionSelected() {
				int nbr = 0;
				for(Equipe e : inscriptions.getEquipes()) {
					System.out.println(nbr+" : "+e.getNom());
					System.out.println("Constitu�e de "+e.getMembres());
					nbr++;
				}
			}
		}));
		inscrits.addBack("b");
		return inscrits;
	}

	public Option optionsCompetition(String nom, Inscriptions inscriptions) {
		for(Competition c : inscriptions.getCompetitions()) {
			if(c.getNom() == nom) {
				compTmp = c;
			}
		}
		ArrayList<String> options = new ArrayList<String>();
		options.add("Ajouter une "+(compTmp.estEnEquipe() ? "une �quipe" : "une personne")+" parmis celles inscrites");
		options.add("Retirer "+(compTmp.estEnEquipe() ? "une �quipe" : "une personne")+" de la comp�tition");
		options.add("Afficher les participants � la comp�tition");
		List<String> menu = new List<String>(nom+" "+(compTmp.estEnEquipe() ? "(en �quipe)" : "(pas en �quipe)"), 
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
									System.out.println("Inscriptions ferm�es.");
								}
								else {
									if(!compTmp.estEnEquipe()) {
										int nbr = 0;
										for(Personne c : inscriptions.getPersonnes()) {
											System.out.println(nbr+" : "+c.getNom());
											nbr++;
										}
										System.out.println(nbr+" : Annuler");
										int choix = InOut.getInt("Taper le num�ro de la personne � ajouter : ");
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
										int choix = InOut.getInt("Taper le num�ro de l'�quipe � ajouter : ");
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
								int choix = InOut.getInt("Taper le num�ro de "+(compTmp.estEnEquipe() ? "l'�quipe" : "la personne")+" � supprimer : ");
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
								System.out.println("Liste des "+(compTmp.estEnEquipe() ? "�quipes" : "participants")+" :");
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
			System.out.println("Ajout effectu�");
		}
		else {
			System.out.println("Echec de l'ajout");
		}
	}
	
	public void afficherSuppression(boolean s) {
		if(s) {
			System.out.println("Suppression effectu�e");
		}
		else {
			System.out.println("Echec de la suppression");
		}
	}
}

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TraitementNotes {

    // Les attributs
    private String nomFichierEM;
    private String nomFichierEtudiants;
    private String nomFichierNotes;

    // Constructeur
    public TraitementNotes(String nomFichierEM, String nomFichierEtudiants, String nomFichierNotes) {
        this.nomFichierEM = nomFichierEM;
        this.nomFichierEtudiants = nomFichierEtudiants;
        this.nomFichierNotes = nomFichierNotes;
    }

    /* Créer une méthode (fonction) nomEtudiant qui prend comme paramètre le
       matricule d’un étudiant et retourne son nom complet (prénom et nom). */
    
    
    public String nomEtudiant(int matricule) throws IOException { //it may throw an IOException
    	// Using try to automatically close the BufferedReader after use
        try (BufferedReader reader = new BufferedReader(new FileReader(nomFichierEtudiants))) { 
            String line;
            while ((line = reader.readLine()) != null) { 
                String[] infoEtudiant = line.split(": "); //here it split the lines by : and tableau 
                // w  resultat tableau is stored in infoEtudiant
                if (infoEtudiant.length >= 3 && infoEtudiant[0].trim().equals(String.valueOf(matricule))) {
                	//trim() is used to remove leading and trailing whitespaces
                    return infoEtudiant[2].trim() + " " + infoEtudiant[1].trim();
                    //lin returni matricule apres return nom w prenom
                }
            }
        }
        return "Etudiant non trouvé";
        //si ma 3eddl return matricule 
    }

    /* Créer une méthode coefficient qui renvoie le coefficient d’un élément de
       module dont le code est passé en paramètre.    */
    
    public int coefficient(String code) throws IOException {
    	//read the contents of the file
        try (BufferedReader reader = new BufferedReader(new FileReader(nomFichierEM))) {
            String line;
            //read each line
            while ((line = reader.readLine()) != null) {
                String[] infoModule = line.split(": ");
                if (infoModule.length >= 4 && infoModule[0].trim().equals(code.trim())) {
                	 //it returns the coefficient (parsed as an integer) from the fourth element of the array.
                    return Integer.parseInt(infoModule[3].trim());
                }
            }
        }
        return -1; 
    }

    /* Créer une méthode nbrElevesR qui prend comme paramètres un code
       d’un élément de module et retourne le nombre d’étudiants ayant une
       moyenne supérieure ou égale à 10 dans l’élément donné */ 
    
    public int nbrElevesR(String code) throws IOException {
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(nomFichierNotes))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] infoNote = line.split(": ");
                if (infoNote.length >= 3 && infoNote[0].trim().equals(code.trim())) {
                    int moyenne = Integer.parseInt(infoNote[2].trim());
                    //parsing is used to convert the string moyenne into int
                    if (moyenne >= 10) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /* Créer une méthode moyenne qui prend comme paramètre un matricule d’un 
       étudiant et retourne sa moyenne générale.*/
    public double Moyenne(int matriculeCherche) throws IOException {
        double totalMoyennes = 0.0;
        int totalModules = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(nomFichierNotes))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] infoNote = line.split(": ");
                if (infoNote.length >= 3 && infoNote[1].trim().equals(String.valueOf(matriculeCherche))) {
                    int moyenne = Integer.parseInt(infoNote[2].trim());
                    totalMoyennes += moyenne;
                    totalModules++;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (totalModules > 0) {
            return totalMoyennes / totalModules;
        } else {
            return 0;
        }
    }


    /* Créer une méthode sauvegarderResultats qui crée un fichier resultats.txt
       où chaque ligne de ce fichier contient le matricule, le nom, le prénom, le
       titre de l’élément de module et la note de l’étudiant dans l’élément.  */
    public void sauvegarderResultats() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(nomFichierNotes));
             FileWriter writer = new FileWriter("resultats.txt")) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] infoNote = line.split(": ");
                if (infoNote.length >= 3) {
                    int matricule = Integer.parseInt(infoNote[1].trim());
                    //retrieve le nom from the other methodes
                    String nomComplet = nomEtudiant(matricule);
                    String codeModule = infoNote[0].trim();
                    int moyene = Integer.parseInt(infoNote[2].trim());
                    String titreModule = "";
                    try (BufferedReader moduleReader = new BufferedReader(new FileReader(nomFichierEM))) {
                        String moduleLine;
                        while ((moduleLine = moduleReader.readLine()) != null) {
                            String[] infoModule = moduleLine.split(": ");
                            if (infoModule.length >= 2 && infoModule[0].trim().equals(codeModule.trim())) {
                                titreModule = infoModule[1].trim();
                                break;
                            }
                        }
                    }
                    writer.write(matricule + ": " + nomComplet + ": " + titreModule + ": " + moyene + "\n");
                }
            }
        }
    }

    public static void main(String[] args) {
        // Chemins des fichiers à lire (read files)
        String fichierEtudiants = "C://Java/Etudiants.txt";
        String fichierNotes = "C:/Java/Notes.txt";
        String fichierElementsModule = "C:/Java/ElementsModule.txt";

        // Matricule de recherche exemple
        int matriculeRecherche = 1701; 
        //instance de notes
        TraitementNotes traitementNotes = new TraitementNotes(fichierElementsModule, fichierEtudiants, fichierNotes);

        try (
                BufferedReader etudiantsReader = new BufferedReader(new FileReader(fichierEtudiants));
                BufferedReader notesReader = new BufferedReader(new FileReader(fichierNotes));
                BufferedReader elementsModuleReader = new BufferedReader(new FileReader(fichierElementsModule));
        ) {

            System.out.println("Fichier des étudiants :");
            String line;
            while ((line = etudiantsReader.readLine()) != null) {
                String[] parties = line.split(":");
                if (parties.length >= 3 && parties[0].trim().equals(String.valueOf(matriculeRecherche))) {
                	
                	
                    // Appeler la méthode nomEtudiant
                    String nomComplet = traitementNotes.nomEtudiant(matriculeRecherche);
                    System.out.println("Nom complet de l'étudiant avec matricule " + matriculeRecherche + " : " + nomComplet);
                }
            }

            // Appeler la méthode coefficient
            String codeModule = "TC113";
            int coefficientModule = traitementNotes.coefficient(codeModule);
            System.out.println("Coefficient pour le module " + codeModule + " : " + coefficientModule);

            // Appeler la méthode nbrElevesR
            int etudiantsAuDessusDeDix = traitementNotes.nbrElevesR(codeModule);
            System.out.println("Nombre d'étudiants ayant une note supérieure à 10 dans le module " + codeModule + " : " + etudiantsAuDessusDeDix);

            // Appeler la méthode moyenne
            double moyenneEtudiant = traitementNotes.Moyenne(matriculeRecherche);
            System.out.println("Note moyenne de l'étudiant avec matricule " + matriculeRecherche + " : " + moyenneEtudiant);

            // Appeler la méthode sauvegarderResultats
            traitementNotes.sauvegarderResultats();
            System.out.println("Résultats sauvegardés dans le fichier resultats.txt");

            System.out.println("\nFichier des notes : ");
            while ((line = notesReader.readLine()) != null) {
                System.out.println(line);
            }

            System.out.println("\nFichier des éléments de module : ");
            while ((line = elementsModuleReader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture d'un des fichiers");
            e.printStackTrace();
        }
    }
}

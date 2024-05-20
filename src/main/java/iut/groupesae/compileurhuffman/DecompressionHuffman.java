package iut.groupesae.compileurhuffman;

import iut.groupesae.compileurhuffman.objets.ArbreHuffman;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;

import static java.lang.System.out;

public class DecompressionHuffman {
    private static String cheminFichierADecompiler;
    private static String dossierDestination;
    private static String cheminFichierDecompile;
    private static String nomFichierDecompile;

    /**
     * Demande à l'utilisateur de spécifier le fichier à décompresser.
     * @param args les arguments de la ligne de commande (optionnelle)
     */
    public static void demanderFichierADecompiler(String[] args) {
        boolean continuer = true;
        while (continuer) {
            try {
                out.print("""
                      Entrez le chemin du fichier à décompresser (URL) :
                      Exemple de chemin valide : "dossier1\\dossier2\\monFichier.bin"
                                                  dossier1/dossier2/monFichier.bin
                      ==>\s""");
                cheminFichierADecompiler = GestionFichier.getCheminFichierSource(args);

                ApplicationLigneCommande.afficherSeparateur();
                out.println("Chemin du fichier spécifié : " + cheminFichierADecompiler);
                ApplicationLigneCommande.afficherSeparateur();

                out.println("Voulez-vous sélectionner ce fichier ? (oui/non)");
                Scanner scanner = new Scanner(System.in);
                String choix = scanner.nextLine();
                if (choix.equalsIgnoreCase("oui")) {
                    traiterFichierADecompresser(scanner);
                    continuer = demanderRecommencer();
                } else {
                    continuer = demanderRecommencer();
                }
            } catch (IOException e) {
                out.println("Erreur lors de la lecture du fichier à décompresser !\nErreur :" + e);
                continuer = demanderRecommencer();
            }
        }
    }

    /**
     * Traite le fichier à décompresser en demandant l'emplacement de destination et en générant le fichier décompressé.
     * @param scanner le scanner utilisé pour lire les entrées de l'utilisateur
     * @throws IOException si une erreur survient lors de la manipulation des fichiers
     */
    private static void traiterFichierADecompresser(Scanner scanner) throws IOException {
        ApplicationLigneCommande.afficherSeparateur();

        out.println("Où voulez-vous enregistrer le fichier une fois décompressé ? (Entrez le chemin complet du répertoire)");
        String cheminFichierDestination = GestionFichier.nettoyerChemin(GestionFichier.getCheminDestination(scanner));
        GestionFichier.verifierRepertoireValide(scanner, cheminFichierDestination);
        dossierDestination = cheminFichierDestination;

        out.println("Sous quel nom voulez-vous enregistrer le fichier une fois décompressé ? (Entrez le nom du fichier)");
        nomFichierDecompile = GestionFichier.getNomFichierDestinationUnique(scanner, cheminFichierDestination);

        ApplicationLigneCommande.afficherSeparateur();
        long tempsDeCompression = System.currentTimeMillis();
        creerFichierDecomprime(tempsDeCompression);
        ApplicationLigneCommande.afficherSeparateur();
    }

    /**
     * Crée le fichier décompressé à partir du contenu et du répertoire de destination spécifiés.
     * @throws IOException si une erreur survient lors de l'écriture du fichier
     */
    private static void creerFichierDecomprime(long tempsDeCompression) throws IOException {
        cheminFichierDecompile = dossierDestination + "\\" + nomFichierDecompile + ".txt";
        String arbreHuffman = cheminFichierADecompiler.substring(0, cheminFichierADecompiler.lastIndexOf("\\")) + "\\arbreHuffman.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cheminFichierDecompile))) {
            writer.write(decoderFichier(arbreHuffman));
        }
        out.println("Le fichier a bien été décompressé et enregistré à l'emplacement suivant : " + cheminFichierDecompile);
        resumeDecompression(tempsDeCompression);
    }

    private static String decoderFichier(String arbreHuffman) throws IOException {
        ArbreHuffman arbre = new ArbreHuffman(arbreHuffman);
        /*
         * Expliquer readAllBytes et Path.of
         */
        byte[] bytes = Files.readAllBytes(Path.of(cheminFichierADecompiler));
        return arbre.decoderFichier(bytes);
    }

    /**
     * Affiche un résumé de la compression en indiquant la taille des fichiers et le taux de compression.
     * @param tempsDeCompression Temps de décompression total
     */
    private static void resumeDecompression(long tempsDeCompression) {
        File fichierOriginal = new File(cheminFichierADecompiler);
        File fichierDecompile = new File(cheminFichierDecompile);
        long tailleFichierOriginal = fichierOriginal.length();
        long tailleFichierDecompile = fichierDecompile.length();

        out.println("Taille du fichier compressé : " + tailleFichierOriginal + " octets");
        out.println("Taille du fichier décompressé : " + tailleFichierDecompile + " octets");

        double tauxCompression = ((double) tailleFichierOriginal / tailleFichierDecompile) * 100;
        out.println("Taux de compression : " + String.format("%.2f", tauxCompression) + "%");

        long tempsDeDecompression = System.currentTimeMillis() - tempsDeCompression;
        out.println("Temps de décompression : " + tempsDeDecompression + " millisecondes");
    }

    /**
     * Demande à l'utilisateur s'il veut réessayer.
     * @return true si l'utilisateur veut réessayer, sinon false
     */
    private static boolean demanderRecommencer() {
        out.println("Voulez-vous recommencer ? (oui/non)");
        Scanner scanner = new Scanner(System.in);
        String choix = scanner.nextLine();
        return choix.equalsIgnoreCase("oui");
    }
}
package fr.iutrodez.compilateurhuffman.objets;

/**
 * Cette classe représente un nœud dans un arbre de Huffman utilisé pour
 * la compression et la décompression de données.
 */
public class NoeudHuffman implements Comparable<NoeudHuffman> {
    private final double frequence;
    private NoeudHuffman gauche;
    private NoeudHuffman droite;
    private NoeudHuffman parent;
    private String codeHuffman;
    private final char caractere;

    /**
     * Constructeur qui crée un nœud de Huffman avec un caractère et sa fréquence d'apparition.
     * @param caractere le caractère.
     * @param frequence la fréquence d'apparition du caractère.
     */
    public NoeudHuffman(char caractere, double frequence) {
        this.caractere = caractere;
        this.frequence = frequence;
    }

    public char getCaractere() {
        return caractere;
    }

    public double getFrequence() {
        return frequence;
    }

    public NoeudHuffman getGauche() {
        return gauche;
    }

    public NoeudHuffman getDroite() {
        return droite;
    }

    public String getCodeHuffman() {
        return codeHuffman;
    }

    public void setGauche(NoeudHuffman gauche) {
        this.gauche = gauche;
        if (gauche != null) {
            gauche.setParent(this);
        }
    }

    public void setDroite(NoeudHuffman droite) {
        this.droite = droite;
        if (droite != null) {
            droite.setParent(this);
        }
    }

    public void setCodeHuffman(String codeHuffman) {
        this.codeHuffman = codeHuffman;
    }

    public NoeudHuffman getParent() {
        return parent;
    }

    public void setParent(NoeudHuffman parent) {
        this.parent = parent;
    }

    /**
     * Renvoie une représentation textuelle du nœud, incluant
     * son code Huffman,
     * son encodage binaire UTF-8,
     * et son caractère.
     * @return La représentation du nœud.
     */
    @Override
    public String toString() {
        return "codeHuffman = " + getCodeHuffman() + " ; encode = " + getUTF8Binary(getCaractere()) + " ; symbole = " + getCaractere();
    }

    /**
     * Renvoie la représentation binaire UTF-8 d'un caractère.
     * @param caractere le caractère à convertir.
     * @return la chaîne de caractères représentant la représentation binaire UTF-8 du caractère.
     */
    public static String getUTF8Binary(char caractere) {
        byte[] utf8Bytes = String.valueOf(caractere).getBytes(java.nio.charset.StandardCharsets.UTF_8);
        StringBuilder binaryRepresentation = new StringBuilder();
        for (byte b : utf8Bytes) {
            String binary = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            binaryRepresentation.append(binary);
        }
        return binaryRepresentation.toString();
    }

    /**
     * Cette méthode compare deux nœuds de Huffman en fonction de leur fréquence d'apparition.
     * @param autre le nœud Huffman à comparer avec le nœud actuel.
     * @return un entier qui caractérise la différence entre les deux nœuds.
     */
    @Override
    public int compareTo(NoeudHuffman autre) {
        return Double.compare(this.frequence, autre.frequence);
    }
}
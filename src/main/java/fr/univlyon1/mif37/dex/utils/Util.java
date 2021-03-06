package fr.univlyon1.mif37.dex.utils;

import fr.univlyon1.mif37.dex.mapping.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Amaia Nazábal
 * @version 1.0
 * @since 1.0 4/21/17.
 */
public class Util {
    private static final int ONE = 1;

    private Util() {
        /* On ajoute un constructeur privé étant donnée qu'il s'agit d'une classe util */
    }

    /**
     * La méthode retourne la liste de constants dans le EDB
     *
     * @param factsByRule tous les faits du programme + ça qu'on a inferer
     * @return la liste de constants
     */
    static List<String> getAllConstants(List<Relation> factsByRule) {
        Set<String> constants = new HashSet<>();
        factsByRule.forEach(edb ->
                constants.addAll(Arrays.asList(edb.getAttributes()))
        );

        return constants.stream().collect(Collectors.toList());
    }

    /**
     * Cette méthode crée un HashMap avec la valeur '1' pour indiquer l'utilisation du règle au moins une fois.
     *
     * @param edbs les faits dans le programme
     * @param idbs le nom des regles
     * @return le hashMap avec la valeur 1
     */
    static Map<String, Integer> createStratum(Collection<Relation> edbs, Collection<AbstractRelation> idbs) {
        HashMap<String, Integer> stratum = new HashMap<>();
        for (Relation edb : edbs) {
            stratum.put(edb.getName(), ONE);
        }

        for (AbstractRelation idb : idbs) {
            stratum.put(idb.getName(), ONE);
        }
        return stratum;
    }

    /**
     * Symbol Definitions. The definition def(s) of a program symbol s is the set of all program clauses that
     * have the symbol p in their head atom.
     *
     * @param edbs tous les faits du programme
     * @param tgds  toutes les règles du programme
     * @param ruleHead le nom de la règle
     * @return la liste de clauses dans laquelle le symbol est présent
     */
    static List<Object> getSymbols(Collection<Relation> edbs, Collection<Tgd> tgds, String ruleHead) {
        List<Object> symbols = new ArrayList<>();

        tgds.forEach(tgd -> {
            if (tgd.getRight().getName().equals(ruleHead))
                symbols.add(tgd);
        });

        edbs.forEach(edb -> {
            if (edb.getName().equals(ruleHead))
                symbols.add(edb);
        });

        return symbols;
    }

    /**
     * Cette méthode retourne une chaîne de caracteres pour imprimer un fait dans le format datalog
     *
     * @param edb un fait du programme
     * @return la chaîne de caractères
     */
    public static String getEDBString(Relation edb) {
        StringBuilder toString = new StringBuilder(edb.getName() + "(");

        AtomicInteger counter = new AtomicInteger();
        counter.set(-1);

        Arrays.asList(edb.getAttributes()).forEach(v -> {
            toString.append(v);
            if (counter.incrementAndGet() < edb.getAttributes().length - 1)
                toString.append(", ");
        });
        toString.append(")");

        return toString.toString();
    }

    /**
     * Cette méthode retourne une chaîne de caracteres pour imprimer une règle dans le format datalog
     *
     * @param tgd le règle
     * @return la chaîne de caractères formatee
     */
    public static String getTgdString(Tgd tgd) {
        StringBuilder toString = new StringBuilder(tgd.getRight().getName() + "(");

        tgd.getRight().getVars().forEach(v -> toString.append(v.getName() + ", "));
        toString.append(") :- ");

        tgd.getLeft().forEach(l -> {
            toString.append(l.getFlag() + " " + l.getAtom().getName() + " (");
            l.getAtom().getVars().forEach(v -> toString.append(v.getName() + ", "));
            toString.append("), ");
        });

        return toString.toString();
    }


    /**
     * Cette méthode vérifie que les relations sont égaux avec les même attributes.
     * Cette méthode ne gére pas les attributes nulls.
     *
     * @param edb les edb
     * @param possibleFact un autre fait
     * @return si sont ou pas égaux
     */
    public static boolean equalsRelation(Relation edb, Relation possibleFact) {
        return Arrays.equals(new List[]{Arrays.asList(edb.getAttributes())},
                new List[]{Arrays.asList(possibleFact.getAttributes())})
                && edb.getName().equals(possibleFact.getName());
    }

    /**
     * Cette méthode vérifie que les attributes soient dans le même ordre dans chaque liste.
     * Le méthode gére les attributes pas remplis encore.
     *
     * @param attributes1 liste des attributes
     * @param relation la relation
     * @return si les listes ont les mêmes attributes ou pas.
     * @return
     */
    public static boolean sameOrderAttributes(String[] attributes1, Relation relation) {
        if (relation != null ) {
            String[] attributes2 = relation.getAttributes();
            return sameOrderAttributes(Arrays.asList(attributes1), Arrays.asList(attributes2));
        }

        return false;
    }

    /**
     * Cette méthode vérifie que les attributes soient dans le même ordre dans chaque liste.
     * Le méthode gére les attributes pas remplis encore.
     *
     * @param relation la relation
     * @param attributes2 liste des attributes
     * @return si les listes ont les mêmes attributes ou pas.
     */
    public static boolean sameOrderAttributes(Relation relation, List<String> attributes2) {
        if (relation != null ) {
            String[] attributes1 = relation.getAttributes();

            return sameOrderAttributes(Arrays.asList(attributes1), attributes2);
        }

        return false;
    }

    /**
     * Cette méthode vérifie que les attributes soient dans le même ordre dans chaque liste.
     * Le méthode gére les attributes pas remplis encore.
     *
     * @param attributes1 liste des attributes
     * @param attributes2 liste des attributes
     * @return si les listes ont les mêmes attributes ou pas.
     */
    public static boolean sameOrderAttributes(String[] attributes1, String[] attributes2) {
        return sameOrderAttributes(Arrays.asList(attributes1), Arrays.asList(attributes2));
    }

    /**
     * Cette méthode vérifie que les attributes soient dans le même ordre dans chaque liste.
     * Le méthode gére les attributes pas remplis encore.
     *
     * @param attributes1 liste des attributes
     * @param attributes2 liste des attributes
     * @return si les listes ont les mêmes attributes ou pas.
     */
    public static boolean sameOrderAttributes(String[] attributes1, List<String> attributes2) {
        return sameOrderAttributes(Arrays.asList(attributes1), attributes2);
    }

    /**
     * Cette méthode vérifie que les attributes soient dans le même ordre dans chaque liste.
     * Le méthode gére les attributes pas remplis encore.
     *
     * @param attributes1 liste des attributes
     * @param attributes2 liste des attributes
     * @return si les listes ont les mêmes attributes ou pas.
     */
    public static boolean sameOrderAttributes(List<Object> attributes1, List<String> attributes2) {
        Iterator<Object> iteratorRelation1 = attributes1.iterator();
        Iterator<String> iteratorRelation2 = attributes2.iterator();

        while (iteratorRelation1.hasNext()) {
            String attribute1 = (String) iteratorRelation1.next();
            String attribute2;

            if (iteratorRelation2.hasNext())
                attribute2 = iteratorRelation2.next();
            else
                return true;

            if (attribute1 == null) {
                return false;
            }

            if (attribute2 != null && !attribute1.equals(attribute2)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Cette méthode enlève les doublants
     *
     * @param facts tous les faits
     * @return la liste de faits sans doublants
     */
    static List<Relation> removeDuplicates(List<Relation> facts) {
        List<Relation> uniqueList = new ArrayList<>();
        for (Relation edb: facts) {
            if (uniqueList.stream().noneMatch(f -> equalsRelation(edb, f)))
                uniqueList.add(edb);
        }

        return uniqueList;
    }

    /**
     *  Methode pour imprimer les TGDB dans la console.
     * @param tgds les TGDB du programme
     */
    public static void printTgds(Collection<Tgd> tgds) {
        /* get TGBS*/
        System.out.println("TGBS:");
        for (Tgd tgd : tgds) {
            System.out.println();
            System.out.print(tgd.getRight().getName() + "(");
            tgd.getRight().getVars().forEach(v -> System.out.print(v.getName() + ", "));
            System.out.print(") :- ");

            tgd.getLeft().forEach(l -> {
                System.out.print(l.getFlag() + " " + l.getAtom().getName() + " (");
                l.getAtom().getVars().forEach(v -> System.out.print(v.getName() + ", "));
                System.out.print("), ");
            });
        }
    }

    /**
     * Methode pour imprimer les IDB dans la console.
     * @param idbs les idb du programme
     */
    public static void printIdbs(Collection<AbstractRelation> idbs) {
        /* get IDBS*/
        System.out.println("IDB: ");
        for (AbstractRelation idb : idbs) {
            System.out.println();
            System.out.print(idb.getName() + "(");

            List<AbstractArgument> attributs = Arrays.asList(idb.getAttributes());
            attributs.forEach(p -> System.out.print(p.getVar().getName() + ","));
            System.out.print(")");
        }
    }

    /**
     * Methode pour imprimer les EDB dans la console.
     * @param edbs les edb du programme
     */
    public static void printEDBs(Collection<Relation> edbs){
        System.out.println("EDB: ");
        for (Relation edb : edbs) {
            System.out.println();
            System.out.print(edb.getName() + "(");
            List<String> attributs = Arrays.asList(edb.getAttributes());
            attributs.forEach(p -> System.out.print(p + ", "));
            System.out.print(")");
        }}
}

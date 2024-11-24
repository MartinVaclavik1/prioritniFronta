package com.example.prioritnifronta.abstrTable;

import com.example.prioritnifronta.FIFO.AbstrFIFO;
import com.example.prioritnifronta.FIFO.IAbstrFIFO;
import com.example.prioritnifronta.LIFO.AbstrLIFO;
import com.example.prioritnifronta.LIFO.IAbstrLIFO;
import com.example.prioritnifronta.enumy.eTypProhl;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class AbstrTable<K extends Comparable<K>, V> implements IAbstrTable<K, V> {

    private Prvek<K, V> koren;
    private Prvek<K, V> aktualni;
    private int pocet = 0;


    private static class Prvek<K, V> {
        private Prvek<K, V> rodic;
        private final K key;
        private final V value;
        private Prvek<K, V> synL;
        private Prvek<K, V> synP;

        private Prvek(Prvek<K, V> rodic, K key, V value, Prvek<K, V> synL, Prvek<K, V> synP) {
            this.rodic = rodic;
            this.key = key;
            this.value = value;
            this.synL = synL;
            this.synP = synP;
        }
    }

    @Override
    public void zrus() {
        koren = aktualni = null;
        pocet = 0;
    }

    @Override
    public boolean jePrazdny() {
        return koren == null;
    }

    //porovnání, jestli K (název obce) je menší, nebo větší, než aktuální a takhle traverzovat dokud se nenajde,
    // nebo dokud nebude slepá ulice
    @Override
    public V najdi(K key) throws AbstrTableException {
        if (jePrazdny()) {
            throw new AbstrTableException("prázdné pole");
        }
        aktualni = koren;
        while (true) {

            int comparator = aktualni.key.compareTo(key);
            if (comparator < 0) {

                if (aktualni.synP == null) {
                    throw new AbstrTableException("prvek " + key + " nenalezen");
                } else {
                    aktualni = aktualni.synP;
                }
            } else if (comparator > 0) {

                if (aktualni.synL == null) {
                    throw new AbstrTableException("prvek " + key + " nenalezen");
                } else {
                    aktualni = aktualni.synL;
                }
            } else {
                return aktualni.value;
            }
        }
    }

    //travertuje po listu a hledá velné místo, kam by seděl prvek. když najde stejný prvek,
    //vyhodí chybu, že už existuje
    @Override
    public void vloz(K key, V value) throws AbstrTableException {
        if (key == null || value == null) {
            throw new AbstrTableException("??");
        }
        if (jePrazdny()) {
            koren = new Prvek<>(null, key, value, null, null);
            pocet++;
        } else {
            Prvek<K, V> novyPrvek = new Prvek<>(null, key, value, null, null);
            aktualni = koren;

            while (true) {
                int comparator = aktualni.key.compareTo(novyPrvek.key);
                if (comparator < 0) {

                    if (aktualni.synP == null) {
                        aktualni.synP = novyPrvek;
                        novyPrvek.rodic = aktualni;
                        pocet++;
                        return;
                    } else {
                        aktualni = aktualni.synP;
                    }
                } else if (comparator > 0) {

                    if (aktualni.synL == null) {
                        aktualni.synL = novyPrvek;
                        novyPrvek.rodic = aktualni;
                        pocet++;
                        return;
                    } else {
                        aktualni = aktualni.synL;
                    }
                } else {
                    throw new AbstrTableException("prvek " + key + " již existuje v seznamu!!");
                }
            }
        }
    }

    //najde prvek metodou najdi. tam zůstane nastavený aktuální. z aktuálního vezme V do proměnné.
    //odebere se prvek a jestli má potomky, tak se najde prvek, který je nejvíce podobný odebranému
    //a ten se nastaví místo odebraného a rozředí se mu prvky dále ve struktuře
    @Override
    public V odeber(K key) throws AbstrTableException {
        //pro nastavení aktuálního na danou pozici
        try {
            najdi(key);
        } catch (AbstrTableException e) {
            throw new AbstrTableException(e.getMessage());
        }
        V value = aktualni.value;


        //najde největší-menší prvek a nejmenší-větší prvek a pak je porovná s aktuálním a kterej bude blíž,
        //(nezapomenout to dát jako abs value) tak se nastaví jako náhradník

        // když má prvek potomky
        if (aktualni.synL != null || aktualni.synP != null) {

            Prvek<K, V> nejblizsiNejmensi = aktualni.synL;
            Prvek<K, V> nejblizsiNejvetsi = aktualni.synP;
            Prvek<K, V> nastupce = null;

            //může mít potomka co je vlevo
            if (nejblizsiNejmensi != null) {
                while (nejblizsiNejmensi.synP != null) {
                    nejblizsiNejmensi = nejblizsiNejmensi.synP;
                }
                nastupce = nejblizsiNejmensi;
            }

            //může mít potomka, co je vpravo
            if (nejblizsiNejvetsi != null) {
                while (nejblizsiNejvetsi.synL != null) {
                    nejblizsiNejvetsi = nejblizsiNejvetsi.synL;
                }
                nastupce = nejblizsiNejvetsi;
            }

            //když oba existují, tak se jako nástupce nastaví buď nejblizsiNejmensi, nebo zůsatne nastavený největší
            if (nejblizsiNejvetsi != null && nejblizsiNejmensi != null) {
                if (rozdilPrvku(aktualni, nejblizsiNejmensi, nejblizsiNejvetsi) < 0) {
                    nastupce = nejblizsiNejmensi;
                }
            }

            //propojí zbývající prvky po náhradníkovi odebraného s předkem
            if (nastupce == nejblizsiNejmensi) {
                if (nastupce.synL != null) {
                    if (nastupce.rodic != aktualni) {
                        nastupce.synL.rodic = nastupce.rodic;
                    }
                }
                nastupce.rodic.synP = nastupce.synL;

            } else {
                if (nastupce.synP != null) {
                    if (nastupce.rodic != aktualni) {
                        nastupce.synP.rodic = nastupce.rodic;
                    }
                }
                nastupce.rodic.synL = nastupce.synP;

            }
            //zjištění jaký syn je v rodiči pomocí comparatoru
            if (aktualni != koren) {
                switch (aktualni.key.compareTo(aktualni.rodic.key)) {
                    //pravý (aktuální [odebraný] je větší, než rodič)
                    case 1 -> aktualni.rodic.synP = nastupce;
                    //levý (aktuální [odebraný] je menší, než rodič)
                    case -1 -> aktualni.rodic.synL = nastupce;
                }
                nastupce.rodic = aktualni.rodic;
                aktualni.rodic = null;
            } else {
                koren = nastupce;
            }

            if (aktualni.synP != null && aktualni.synP != nastupce) {
                aktualni.synP.rodic = nastupce;
                nastupce.synP = aktualni.synP;
            }
            if (aktualni.synL != null && aktualni.synL != nastupce) {
                aktualni.synL.rodic = nastupce;
                nastupce.synL = aktualni.synL;
            }

            //když prvek nemá žádné potomky
        } else {
            //když prvek není kořenový prvek, tak se odebere link z rodiče na něj
            if (aktualni.rodic != null) {
                //zjištění jaký syn je v rodiči
                if (aktualni.rodic.synL == aktualni) {
                    aktualni.rodic.synL = null;
                } else if (aktualni.rodic.synP == aktualni) {
                    aktualni.rodic.synP = null;
                }
                //odebrání linku z aktuálního na rodiče
                aktualni.rodic = null;
            } else {
                //když nemá potomky a je kořenový prvek, tak je jen 1 prvek v seznamu a může se zrušit celý
                zrus();
            }
        }
        aktualni = null;
        pocet--;
        return value;
    }

    //vrátí číslo, prvek 2 bliz = -1
    private Integer rozdilPrvku(Prvek<K, V> prvek1, Prvek<K, V> prvek2, Prvek<K, V> prvek3) throws AbstrTableException {

        char[] prvek1Char = prvek1.key.toString().toCharArray();
        char[] prvek2Char = prvek2.key.toString().toCharArray();
        char[] prvek3Char = prvek3.key.toString().toCharArray();
        int pocetOpakovani = Math.min(Math.min(prvek1Char.length, prvek2Char.length), prvek3Char.length);

        for (int i = 0; i < pocetOpakovani; i++) {
            if (prvek2Char[i] != prvek3Char[i]) {
                return Math.abs(prvek1Char[i] - prvek2Char[i]) < Math.abs(prvek1Char[i] - prvek3Char[i]) ? -1 : 1;
            }
        }
        if (prvek2Char.length != prvek3Char.length) {
            return prvek2Char.length < prvek3Char.length ? -1 : 1;
        }

        throw new AbstrTableException("Chyba při odečítání prvků");
    }

    @Override
    public Iterator<V> vytvorIterator(eTypProhl typ) {
        return new Iterator<>() {
            final IAbstrFIFO<Prvek<K, V>> fifo = new AbstrFIFO<>();
            final IAbstrLIFO<Prvek<K, V>> lifo = new AbstrLIFO<>();
            private Prvek<K, V> dalsi;
            int zobrazenyPocet = 0;

            @Override
            public boolean hasNext() {
                return zobrazenyPocet < pocet && !jePrazdny();
            }

            @Override
            public V next() {
                V odebrany = null;
                if (hasNext()) {

                    if (zobrazenyPocet == 0) {
                        fifo.vloz(koren);
                        lifo.vloz(koren);

                        dalsi = koren;

                        while (dalsi.synL != null) {
                            dalsi = dalsi.synL;
                        }

                    }

                    switch (typ) {
                        case DO_SIRKY -> {
                            Prvek<K, V> odebranyPrvek = fifo.odeber();

                            if (odebranyPrvek.synL != null) {
                                fifo.vloz(odebranyPrvek.synL);
                            }
                            if (odebranyPrvek.synP != null) {
                                fifo.vloz(odebranyPrvek.synP);
                            }

                            odebrany = odebranyPrvek.value;
                        }
                        case DO_HLOUBKY -> {
                            Prvek<K, V> odebranyPrvek = lifo.odeber();

                            if (odebranyPrvek.synP != null) {
                                lifo.vloz(odebranyPrvek.synP);
                            }
                            if (odebranyPrvek.synL != null) {
                                lifo.vloz(odebranyPrvek.synL);
                            }

                            odebrany = odebranyPrvek.value;
                        }
                        case IN_ORDER -> {
                            //dostat se co nejvíc doleva a odsud začínat. když je možnost jít doleva, tak jít,
                            // ale jít hned co nejvíc doleva
                            Prvek<K, V> odebranyPrvek = dalsi;

                            if (dalsi.synP != null) {
                                dalsi = dalsi.synP;
                                while (dalsi.synL != null)
                                    dalsi = dalsi.synL;
                                odebrany = odebranyPrvek.value;
                                break;
                            }

                            while (true) {
                                if (dalsi.rodic == null) {
                                    dalsi = null;
                                    odebrany = odebranyPrvek.value;
                                    break;
                                }
                                if (dalsi.rodic.synL == dalsi) {
                                    dalsi = dalsi.rodic;
                                    odebrany = odebranyPrvek.value;
                                    break;
                                }
                                dalsi = dalsi.rodic;
                            }
                        }
                    }
                } else {
                    throw new NoSuchElementException();
                }
                zobrazenyPocet++;
                return odebrany;
            }
        };
    }


    public int getPocet() {
        return pocet;
    }
}

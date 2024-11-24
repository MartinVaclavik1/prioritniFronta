package com.example.prioritnifronta.abstrHeap;

import com.example.prioritnifronta.Obec;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class AbstrHeap implements IAbstrHeap {

    private List<Prvek> pole = new ArrayList<>();
    private int pocet = 0;
    private boolean jeVybranyPocet = true;
    private final int startIndex = 1;

    public AbstrHeap() {
        pole.add(0, null);  //pro počítání od 1 - ulehčení počítání předka a potomků
    }

    private record Prvek(int pocet, String nazev, Obec value) {
    }

    @Override
    public void vybuduj(Obec[] obce) {
        //stup bude pole prvků?
        if(obce.length == 0){
            //TODO vlastni hlaska
            throw new NoSuchElementException();
        }
        zrus();
        for (Obec obec : obce) {
            //TODO zjistit jak to z toho vytáhnout
            vloz(obec.getPocetCelkem(), obec.getObec(), obec);
        }
    }

    @Override
    public void reorganizace() {
        jeVybranyPocet = !jeVybranyPocet;
        Iterator<Obec> it = vytvorIterator();
        Obec[] poleObci = new Obec[pocet];

        if (pocet > 0) {
            for (int i = 0; i < pocet; i++) {
                poleObci[i] = it.next();
            }

            vybuduj(poleObci);
        }
    }

    @Override
    public void zrus() {
        pole = new ArrayList<>();
    }

    @Override
    public boolean jePrazdny() {
        return pole.isEmpty();
    }

    //do pole prvků se vloží na další volný místo prvek -
    @Override
    public void vloz(int pocetObcanu, String nazev, Obec prvek) {
        if (pocetObcanu < 0 || nazev == null || prvek == null) {
            //TODO dodělat hlášku
            throw new NoSuchElementException();
        }

        pole.add(startIndex + pocet++, new Prvek(pocetObcanu, nazev, prvek));

        porovnejSPredkem(pocet); //index 2, index predka 1
    }

    private void porovnejSPredkem(int indexPotomka) {
        //TODO podle priority nastavit < nebo > u compareTo?
        int indexPredka = indexPotomka / 2;
        if (indexPotomka > pocet || indexPredka > pocet || indexPredka < startIndex) {
            return;
        }

        if (jeVybranyPocet && pole.get(indexPotomka).pocet > pole.get(indexPredka).pocet ||
                !jeVybranyPocet && pole.get(indexPotomka).nazev.compareTo(pole.get(indexPredka).nazev) > 0) {
            //prohodí potomka a předka
            prohod(indexPredka, indexPotomka);

            //kontola předka(prohozený potomek prvek) s jeho předkem
            if (indexPredka > startIndex) {
                porovnejSPredkem(indexPredka);
            }

            zkontrolujDolu(indexPotomka);
        }

    }

    private void prohod(int indexPredka, int indexPotomka) {
        Prvek pomocna = pole.get(indexPredka);
        pole.set(indexPredka, pole.get(indexPotomka));
        pole.set(indexPotomka, pomocna);
    }

    private void zkontrolujDolu(int indexRodice) {
        int indexPotomkaL = indexRodice * 2; //např 1 = 2, 2 = 4, 3 = 6...
        int indexPotomkaR = indexRodice * 2 + 1; //např 1 = 3, 2 = 5, 3 = 7...

        //když je indexL a indexR v poli, tak se zjistí větší z nich a porovná se s předkem
        if (indexPotomkaL <= pocet && indexPotomkaR <= pocet) {
            if (jeVybranyPocet) {
                //jestli indexL je větší
                int porovnani = Integer.compare(pole.get(indexPotomkaL).pocet, pole.get(indexPotomkaR).pocet);
                if (porovnani > 0) {
                    if (pole.get(indexPotomkaL).pocet > pole.get(indexRodice).pocet) {
                        prohod(indexPotomkaL, indexRodice);
                        porovnejSPredkem(indexRodice);
                        zkontrolujDolu(indexPotomkaL);
                    }
                } else if (porovnani < 0) {
                    if (pole.get(indexPotomkaR).pocet > pole.get(indexRodice).pocet) {
                        prohod(indexPotomkaR, indexRodice);
                        porovnejSPredkem(indexRodice);
                        zkontrolujDolu(indexPotomkaR);
                    }
                }
            } else {
                int porovnani = pole.get(indexPotomkaL).nazev.compareTo(pole.get(indexPotomkaR).nazev);
                if (porovnani > 0) {
                    if (pole.get(indexPotomkaL).nazev.compareTo(pole.get(indexRodice).nazev) > 0) {
                        prohod(indexPotomkaL, indexRodice);
                        porovnejSPredkem(indexRodice);
                        zkontrolujDolu(indexPotomkaL);
                    }
                } else if (porovnani < 0) {
                    if (pole.get(indexPotomkaR).nazev.compareTo(pole.get(indexRodice).nazev) > 0) {
                        prohod(indexPotomkaR, indexRodice);
                        porovnejSPredkem(indexRodice);
                        zkontrolujDolu(indexPotomkaR);
                    }
                }
            }
            //když je jen indexL v poli, tak se porovná s předkem
        } else if (indexPotomkaL <= pocet) {
            if (jeVybranyPocet) {
                if (pole.get(indexPotomkaL).pocet > pole.get(indexRodice).pocet) {
                    prohod(indexPotomkaL, indexRodice);
                    porovnejSPredkem(indexRodice);
                    zkontrolujDolu(indexPotomkaL);
                }

            } else {
                if (pole.get(indexPotomkaL).nazev.compareTo(pole.get(indexRodice).nazev) > 0) {
                    prohod(indexPotomkaL, indexRodice);
                    porovnejSPredkem(indexRodice);
                    zkontrolujDolu(indexPotomkaL);
                }
            }
        }
    }

    @Override
    public Obec odeberMax() {
        if (!jePrazdny()) {
            Obec prvek = pole.get(startIndex).value;
            pole.set(startIndex, pole.remove(pocet));
            pocet--;

            zkontrolujDolu(startIndex);
            return prvek;
        } else {
            //TODO udělat vlastní chybovou hlášku
            throw new NoSuchElementException("nelze odebrat z pole - prázdné pole");
        }
    }

    @Override
    public Obec zpristupniMax() {

        if (!jePrazdny()) {
            return pole.get(startIndex).value;
        } else {
            //TODO udělat vlastní chybovou hlášku
            throw new NoSuchElementException("nelze odebrat z pole - prázdné pole");
        }
    }

    @Override
    public Iterator<Obec> vytvorIterator() {
        return new Iterator<>() {
            int vypsany = 0;

            @Override
            public boolean hasNext() {
                return vypsany < pocet;
            }

            @Override
            public Obec next() {
                if (hasNext()) {
                    return pole.get(startIndex + vypsany++).value;
                } else {
                    //TODO dodělat chybovou hlášku
                    throw new NoSuchElementException();
                }
            }
        };
    }
}

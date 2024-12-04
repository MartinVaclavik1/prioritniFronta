package com.example.prioritnifronta.abstrHeap;

import com.example.prioritnifronta.LIFO.AbstrLIFO;
import com.example.prioritnifronta.LIFO.IAbstrLIFO;

import java.util.*;

public class AbstrHeap<O extends Comparable<O>> implements IAbstrHeap<O> {

    private List<O> pole = new ArrayList<>();
    private int pocet = 0;
    private final int startIndex = 1;

    public AbstrHeap() {
        pole.add(0, null);  //pro počítání od 1 - ulehčení počítání předka a potomků
    }

    @Override
    public void vybuduj(List <O> obce) throws AbstrHeapException {
        //stup bude pole prvků?
        if (obce.isEmpty()) {
            throw new AbstrHeapException("Pole obcí musí být delší, než 0 znaků");
        }
        zrus();
        for(int i = 1; i < obce.size(); i++){
            vloz(obce.get(i));
        }
    }

    @Override
    public void reorganizace() throws AbstrHeapException {

        List<O> listObci = new ArrayList<>();
        listObci.add(0, null);

        if (pocet > 0) {
            for (int i = 1; i < pole.size(); i++) {
                listObci.add(i, pole.get(i));
            }

            vybuduj(listObci);
        }
    }

    @Override
    public void zrus() {
        pole = new ArrayList<>();
        pole.add(0, null);
        pocet = 0;
    }

    @Override
    public boolean jePrazdny() {
        return pole.isEmpty();
    }

    //do pole prvků se vloží na další volný místo prvek -
    @Override
    public void vloz(O obec) throws AbstrHeapException {
        if (obec == null) {
            throw new AbstrHeapException("Neplatné vstupní hodnoty");
        }

        pole.add(startIndex + pocet++, obec);

        porovnejSPredkem(pocet); //index 2, index predka 1
    }

    private void porovnejSPredkem(int indexPotomka) {
        //TODO podle priority nastavit < nebo > u compareTo?
        int indexPredka = indexPotomka / 2;
        if (indexPotomka > pocet || indexPredka > pocet || indexPredka < startIndex) {
            return;
        }

        if (pole.get(indexPotomka).compareTo(pole.get(indexPredka)) > 0) {
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
        O pomocna = pole.get(indexPredka);
        pole.set(indexPredka, pole.get(indexPotomka));
        pole.set(indexPotomka, pomocna);
    }

    private void zkontrolujDolu(int indexRodice) {
        int indexPotomkaL = indexRodice * 2; //např 1 = 2, 2 = 4, 3 = 6...
        int indexPotomkaR = indexRodice * 2 + 1; //např 1 = 3, 2 = 5, 3 = 7...

        //když je indexL a indexR v poli, tak se zjistí větší z nich a porovná se s předkem
        if (indexPotomkaL <= pocet && indexPotomkaR <= pocet) {

                //jestli indexL je větší
                int porovnani = pole.get(indexPotomkaL).compareTo(pole.get(indexPotomkaR));
                if (porovnani > 0) {
                    if (pole.get(indexPotomkaL).compareTo(pole.get(indexRodice)) > 0) {
                        prohod(indexPotomkaL, indexRodice);
                        porovnejSPredkem(indexRodice);
                        zkontrolujDolu(indexPotomkaL);
                    }
                } else if (porovnani < 0) {
                    if (pole.get(indexPotomkaR).compareTo(pole.get(indexRodice))>0) {
                        prohod(indexPotomkaR, indexRodice);
                        porovnejSPredkem(indexRodice);
                        zkontrolujDolu(indexPotomkaR);
                    }
                }
            //když je jen indexL v poli, tak se porovná s předkem
        } else if (indexPotomkaL <= pocet) {
                if (pole.get(indexPotomkaL).compareTo(pole.get(indexRodice))>0) {
                    prohod(indexPotomkaL, indexRodice);
                    porovnejSPredkem(indexRodice);
                    zkontrolujDolu(indexPotomkaL);
                }
        }
    }

    @Override
    public O odeberMax() throws AbstrHeapException {
        if (!jePrazdny()) {
            O prvek = pole.get(startIndex);
            pole.set(startIndex, pole.remove(pocet));
            pocet--;

            zkontrolujDolu(startIndex);
            return prvek;
        } else {
            throw new AbstrHeapException("nelze odebrat z pole - prázdné pole");
        }
    }

    @Override
    public O zpristupniMax() throws AbstrHeapException {

        if (!jePrazdny()) {
            return pole.get(startIndex);
        } else {
            throw new AbstrHeapException("nelze zpřístupnit z pole - prázdné pole");
        }
    }

    @Override
    public Iterator<O> vytvorIterator(eTypProhl typ) {
        return new Iterator<>() {
            private int vypsany = 0;
            private final IAbstrLIFO<Integer> lifo = new AbstrLIFO<>();
            private boolean prvniPruchod = true;

            @Override
            public boolean hasNext() {
                return vypsany < pocet;
            }

            @Override
            public O next() {
                if (hasNext()) {
                    if (prvniPruchod) {
                        prvniPruchod = false;
                        lifo.vloz(startIndex);
                    }
                    switch (typ) {
                        case DO_SIRKY -> {
                            return pole.get(startIndex + vypsany++);
                        }
                        case DO_HLOUBKY -> {
                            int odebranyPrvek = lifo.odeber();

                            if (odebranyPrvek * 2 + 1 <= pocet) {
                                lifo.vloz(odebranyPrvek * 2 + 1);
                            }
                            if (odebranyPrvek * 2 <= pocet) {
                                lifo.vloz(odebranyPrvek * 2);
                            }
                            vypsany++;
                            return pole.get(odebranyPrvek);
                        }
                    }
                    return null;

                } else {
                    throw new NoSuchElementException();
                }
            }
        };
    }
}

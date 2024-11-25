package com.example.prioritnifronta.abstrHeap;

import com.example.prioritnifronta.LIFO.AbstrLIFO;
import com.example.prioritnifronta.LIFO.IAbstrLIFO;
import com.example.prioritnifronta.Obec;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class AbstrHeap implements IAbstrHeap {

    private List<Obec> pole = new ArrayList<>();
    private int pocet = 0;
    private boolean jeVybranyPocet = true;
    private final int startIndex = 1;

    public AbstrHeap() {
        pole.add(0, null);  //pro počítání od 1 - ulehčení počítání předka a potomků
    }

    @Override
    public void vybuduj(Obec[] obce) throws AbstrHeapException {
        //stup bude pole prvků?
        if (obce.length == 0) {
            throw new AbstrHeapException("Pole obcí musí být delší, než 0 znaků");
        }
        zrus();
        for (Obec obec : obce) {
            vloz(obec);
        }
    }

    @Override
    public void reorganizace() throws AbstrHeapException {
        jeVybranyPocet = !jeVybranyPocet;
        Iterator<Obec> it = vytvorIterator(eTypProhl.DO_SIRKY);
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
        pole.add(0, null);
        pocet = 0;
    }

    @Override
    public boolean jePrazdny() {
        return pole.isEmpty();
    }

    //do pole prvků se vloží na další volný místo prvek -
    @Override
    public void vloz(Obec prvek) throws AbstrHeapException {
        if (prvek == null || prvek.getPocetCelkem() < 0 || prvek.getObec() == null) {
            throw new AbstrHeapException("Neplatné vstupní hodnoty");
        }

        pole.add(startIndex + pocet++, prvek);

        porovnejSPredkem(pocet); //index 2, index predka 1
    }

    private void porovnejSPredkem(int indexPotomka) {
        //TODO podle priority nastavit < nebo > u compareTo?
        int indexPredka = indexPotomka / 2;
        if (indexPotomka > pocet || indexPredka > pocet || indexPredka < startIndex) {
            return;
        }

        if (jeVybranyPocet && pole.get(indexPotomka).getPocetCelkem() > pole.get(indexPredka).getPocetCelkem() ||
                !jeVybranyPocet && pole.get(indexPotomka).getObec().compareTo(pole.get(indexPredka).getObec()) > 0) {
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
        Obec pomocna = pole.get(indexPredka);
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
                int porovnani = Integer.compare(pole.get(indexPotomkaL).getPocetCelkem(), pole.get(indexPotomkaR).getPocetCelkem());
                if (porovnani > 0) {
                    if (pole.get(indexPotomkaL).getPocetCelkem() > pole.get(indexRodice).getPocetCelkem()) {
                        prohod(indexPotomkaL, indexRodice);
                        porovnejSPredkem(indexRodice);
                        zkontrolujDolu(indexPotomkaL);
                    }
                } else if (porovnani < 0) {
                    if (pole.get(indexPotomkaR).getPocetCelkem() > pole.get(indexRodice).getPocetCelkem()) {
                        prohod(indexPotomkaR, indexRodice);
                        porovnejSPredkem(indexRodice);
                        zkontrolujDolu(indexPotomkaR);
                    }
                }
            } else {
                int porovnani = pole.get(indexPotomkaL).getObec().compareTo(pole.get(indexPotomkaR).getObec());
                if (porovnani > 0) {
                    if (pole.get(indexPotomkaL).getObec().compareTo(pole.get(indexRodice).getObec()) > 0) {
                        prohod(indexPotomkaL, indexRodice);
                        porovnejSPredkem(indexRodice);
                        zkontrolujDolu(indexPotomkaL);
                    }
                } else if (porovnani < 0) {
                    if (pole.get(indexPotomkaR).getObec().compareTo(pole.get(indexRodice).getObec()) > 0) {
                        prohod(indexPotomkaR, indexRodice);
                        porovnejSPredkem(indexRodice);
                        zkontrolujDolu(indexPotomkaR);
                    }
                }
            }
            //když je jen indexL v poli, tak se porovná s předkem
        } else if (indexPotomkaL <= pocet) {
            if (jeVybranyPocet) {
                if (pole.get(indexPotomkaL).getPocetCelkem() > pole.get(indexRodice).getPocetCelkem()) {
                    prohod(indexPotomkaL, indexRodice);
                    porovnejSPredkem(indexRodice);
                    zkontrolujDolu(indexPotomkaL);
                }

            } else {
                if (pole.get(indexPotomkaL).getObec().compareTo(pole.get(indexRodice).getObec()) > 0) {
                    prohod(indexPotomkaL, indexRodice);
                    porovnejSPredkem(indexRodice);
                    zkontrolujDolu(indexPotomkaL);
                }
            }
        }
    }

    @Override
    public Obec odeberMax() throws AbstrHeapException {
        if (!jePrazdny()) {
            Obec prvek = pole.get(startIndex);
            pole.set(startIndex, pole.remove(pocet));
            pocet--;

            zkontrolujDolu(startIndex);
            return prvek;
        } else {
            throw new AbstrHeapException("nelze odebrat z pole - prázdné pole");
        }
    }

    @Override
    public Obec zpristupniMax() throws AbstrHeapException {

        if (!jePrazdny()) {
            return pole.get(startIndex);
        } else {
            throw new AbstrHeapException("nelze zpřístupnit z pole - prázdné pole");
        }
    }

    @Override
    public Iterator<Obec> vytvorIterator(eTypProhl typ) {
        return new Iterator<>() {
            private int vypsany = 0;
            private final IAbstrLIFO<Integer> lifo = new AbstrLIFO<>();
            private boolean prvniPruchod = true;

            @Override
            public boolean hasNext() {
                return vypsany < pocet;
            }

            @Override
            public Obec next() {
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

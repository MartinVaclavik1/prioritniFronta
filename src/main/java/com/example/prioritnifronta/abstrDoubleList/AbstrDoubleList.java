package com.example.prioritnifronta.abstrDoubleList;

import java.util.Iterator;

public class AbstrDoubleList<T> implements IAbstrDoubleList<T> {
    private Prvek<T> prvni;
    private Prvek<T> aktualni;

    private static class Prvek<T> {
        private Prvek<T> predchozi;
        private final T prvek;
        private Prvek<T> nasledujici;

        public Prvek(Prvek<T> predchozi, T prvek, Prvek<T> nasledujici) {
            this.predchozi = predchozi;
            this.prvek = prvek;
            this.nasledujici = nasledujici;
        }
    }

    @Override
    public void zrus() {
        prvni = null;
        aktualni = null;
    }

    @Override
    public boolean jePrazdny() {
        return prvni == null;
    }

    @Override
    public void vlozPrvni(T data) throws AbstrDoubleListException {

        if (data == null) {
            throw new AbstrDoubleListException("Nelze vložit null");
        }

        if (!jePrazdny()) {
            //vložení prvního při počtu 1 a více
            Prvek<T> novyPrvek = new Prvek<>(prvni.predchozi, data, prvni);
            Prvek<T> posledni = prvni.predchozi;
            prvni.predchozi = novyPrvek;
            posledni.nasledujici = novyPrvek;

            prvni = novyPrvek;
        } else {
            //vložení, když je jen jeden prvek.
            //nemůže se při vytváření vložit rovnou první, jako předchozí/následující, protože první je zatím null
            prvni = new Prvek<>(null, data, null);
            prvni.predchozi = prvni;
            prvni.nasledujici = prvni;
        }
    }

    @Override
    public void vlozPosledni(T data) throws AbstrDoubleListException {

        if (data == null) {
            throw new AbstrDoubleListException("Nelze vložit null");
        }

        if (prvni != null) {
            //když není list prázdný
            Prvek<T> novyPrvek = new Prvek<>(prvni.predchozi, data, prvni);
            Prvek<T> posledni = prvni.predchozi; //teoreticky jde napsat prvni.predchozi.nasledujici, ale to je moc matoucí
            posledni.nasledujici = novyPrvek;
            prvni.predchozi = novyPrvek;
        } else {
            //vlozPrvni(data); - teoreticky pomalejší alternativa, ale bez žádné duplikace
            prvni = new Prvek<>(null, data, null);
            prvni.predchozi = prvni;
            prvni.nasledujici = prvni;
        }
    }

    @Override
    public void vlozNaslednika(T data) throws AbstrDoubleListException {
        if (data == null) {
            throw new AbstrDoubleListException("Nelze vkládat null");
        }else if(aktualni == null){
            throw new AbstrDoubleListException("Není nastaven aktuální prvek");
        }

        if (prvni.predchozi == aktualni) {
            vlozPosledni(data);
            return;
        }

        Prvek<T> zaVlozenymPrvkem = aktualni.nasledujici;
        Prvek<T> vlozenyPrvek = new Prvek<>(aktualni, data, zaVlozenymPrvkem);

        aktualni.nasledujici = vlozenyPrvek;
        zaVlozenymPrvkem.predchozi = vlozenyPrvek;
    }

    @Override
    public void vlozPredchudce(T data) throws AbstrDoubleListException{
        if (data == null) {
            throw new AbstrDoubleListException("Nelze vkládat null");
        }else if(aktualni == null){
            throw new AbstrDoubleListException("Není nastaven aktuální prvek");
        }

        if (aktualni == prvni) {
            vlozPrvni(data);
            return;
        }

        Prvek<T> predVlozenymPrvkem = aktualni.nasledujici;
        Prvek<T> vlozenyPrvek = new Prvek<>(predVlozenymPrvkem, data, aktualni);

        aktualni.predchozi = vlozenyPrvek;
        predVlozenymPrvkem.nasledujici = vlozenyPrvek;
    }

    @Override
    public T zpristupniAktualni() throws AbstrDoubleListException {
        if (jePrazdny()) {
            throw new AbstrDoubleListException("Seznam je prázdný a tím pádem aktuální není nastaven");
        }
        return aktualni.prvek;
    }

    @Override
    public T zpristupniPrvni() throws AbstrDoubleListException {
        if (jePrazdny()) {
            throw new AbstrDoubleListException("Seznam je prázdný - nelze zpřístupnit první");
        }
        aktualni = prvni;
        return aktualni.prvek;
    }

    @Override
    public T zpristupniPosledni() throws AbstrDoubleListException {
        if (jePrazdny()) {
            throw new AbstrDoubleListException("Seznam je prázdný - nelze zpřístupnit první");

        }
        aktualni = prvni.predchozi;
        return aktualni.prvek;
    }

    @Override
    public T zpristupniNaslednika() throws AbstrDoubleListException {
        if (jePrazdny()) {
            throw new AbstrDoubleListException("Seznam je prázdný - nelze zpřístupnit následníka");
        } else if (aktualni == null) {
            throw new AbstrDoubleListException("Není nastaven aktuální prvek");
        }
        aktualni = aktualni.nasledujici;
        return aktualni.prvek;
    }

    @Override
    public T zpristupniPredchudce() throws AbstrDoubleListException {
        if (jePrazdny()) {
            throw new AbstrDoubleListException("Seznam je prázdný - nelze zpřístupnit předchůdce");
        } else if (aktualni == null) {
            throw new AbstrDoubleListException("Není nastaven aktuální prvek");
        }
        aktualni = aktualni.predchozi;
        return aktualni.prvek;
    }

    /**
     * odebere prvek mezi dalšími prvky, které spojí a vátí data z prvku
     */
    private T odeber(Prvek<T> odebrany) {
        T odebranyPrvek = odebrany.prvek;

        Prvek<T> predOdebranym = odebrany.predchozi;
        Prvek<T> zaOdebranym = odebrany.nasledujici;

        predOdebranym.nasledujici = zaOdebranym;
        zaOdebranym.predchozi = predOdebranym;

        odebrany.predchozi = null;
        odebrany.nasledujici = null;

        return odebranyPrvek;
    }

    @Override
    public T odeberAktualni() throws AbstrDoubleListException {

        if (jePrazdny()) {
            throw new AbstrDoubleListException("Seznam je prázdný - nelze odebrat aktuální");
        } else if (aktualni == null) {
            throw new AbstrDoubleListException("Není nastaven aktuální prvek");
        }

        if (aktualni == prvni) {
            return odeberPrvni();
        }

        T odebrany = odeber(aktualni);
        aktualni = prvni;
//
        if (prvni == prvni.predchozi) {
            zrus();
        }
//        T odebrany = aktualni.prvek;
//
//        Prvek<T> predAktualnim = aktualni.predchozi;
//        Prvek<T> zaAktualnim = aktualni.nasledujici;
//
//        aktualni = null;
//
//        predAktualnim.nasledujici = zaAktualnim;
//        zaAktualnim.predchozi = predAktualnim;


        return odebrany;
    }

    @Override
    public T odeberPrvni() throws AbstrDoubleListException {
        if (jePrazdny()) {
            throw new AbstrDoubleListException("Seznam je prázdný - nelze odebrat první");
        } else if (prvni == prvni.predchozi) {
            T odebranyPrvek = prvni.prvek;
            zrus();
            return odebranyPrvek;
        }

        Prvek<T> novyPrvni = prvni.nasledujici;
        T odebranyPrvek = odeber(prvni);


        prvni = novyPrvni;

        return odebranyPrvek;
    }

    @Override
    public T odeberPosledni() throws AbstrDoubleListException {
        if (jePrazdny()) {
            throw new AbstrDoubleListException("Seznam je prázdný - nelze odebrat poslední");
        }
        if (prvni == prvni.predchozi) {
            T odebranyPrvek = prvni.prvek;
            prvni = null;
            return odebranyPrvek;
        }

        Prvek<T> odebrany = prvni.predchozi;

        return odeber(odebrany);
    }

    @Override
    public T odeberNaslednika() throws AbstrDoubleListException {
        if (jePrazdny()) {
            throw new AbstrDoubleListException("Seznam je prázdný - nelze odebrat následníka");
        } else if (aktualni == null) {
            throw new AbstrDoubleListException("Není nastaven aktuální prvek");
        }
        if (prvni == prvni.predchozi) {
            T odebranyPrvek = prvni.prvek;
            prvni = null;
            return odebranyPrvek;
        }

        return odeber(aktualni.nasledujici);
    }

    @Override
    public T odeberPredchudce() throws AbstrDoubleListException {
        if (jePrazdny()) {
            throw new AbstrDoubleListException("Seznam je prázdný - nelze odebrat předchůdce");
        } else if (aktualni == null) {
            throw new AbstrDoubleListException("Není nastaven aktuální prvek");
        }
        if (prvni == prvni.predchozi) {
            T odebranyPrvek = prvni.prvek;
            prvni = null;
            return odebranyPrvek;
        }

        return odeber(aktualni.predchozi);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            Prvek<T> nastaveny = prvni;
            boolean jePrvniIterace = true;

            @Override
            public boolean hasNext() {
                return jePrazdny()? false: nastaveny.nasledujici != prvni;
            }


            @Override
            public T next() {
                if (jePrazdny()) {
                    System.err.println("prázdné pole");
                    return null;
                }

                if (jePrvniIterace) {
                    jePrvniIterace = false;
                    return nastaveny.prvek;
                }
                if (hasNext()) {
                    nastaveny = nastaveny.nasledujici;
                    return nastaveny.prvek;
                } else {
                    System.err.println("konec seznamu");
                    return null;
                }
            }
        };
    }
}

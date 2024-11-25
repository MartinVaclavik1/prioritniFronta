package com.example.prioritnifronta.abstrDoubleList;

import java.util.Iterator;

public interface IAbstrDoubleList<T> extends Iterable<T>{
    void zrus();//zrušení celého seznamu,
    boolean jePrazdny();//test naplněnosti seznamu,
    void vlozPrvni(T data) throws AbstrDoubleListException;//vložení prvku do seznamu na první místo
    void vlozPosledni(T data) throws AbstrDoubleListException;//vložení prvku do seznamu na poslední místo,
    void vlozNaslednika(T data) throws AbstrDoubleListException;//vložení prvku do seznamu jakožto následníka aktuálního prvku

    void vlozPredchudce(T data) throws AbstrDoubleListException;//vložení prvku do seznamu jakožto předchůdce aktuálního prvku

    T zpristupniAktualni() throws AbstrDoubleListException;//zpřístupnění aktuálního prvku seznamu,
    T zpristupniPrvni() throws AbstrDoubleListException;//zpřístupnění prvního prvku seznamu,
    T zpristupniPosledni() throws AbstrDoubleListException;//zpřístupnění posledního prvku seznamu,
    T zpristupniNaslednika() throws AbstrDoubleListException;//zpřístupnění následníka aktuálního prvku,
    T zpristupniPredchudce() throws AbstrDoubleListException;//zpřístupnění předchůdce aktuálního prvku, Pozn. Operace typu zpřístupni, přenastavují pozici aktuálního prvku

    T odeberAktualni() throws AbstrDoubleListException;//odebrání (vyjmutí) aktuálního prvku ze seznamu poté je aktuální prvek nastaven na první prvek

    T odeberPrvni() throws AbstrDoubleListException;//odebrání prvního prvku ze seznamu,
    T odeberPosledni() throws AbstrDoubleListException;//odebrání posledního prvku ze seznamu,
    T odeberNaslednika() throws AbstrDoubleListException;//odebrání následníka aktuálního prvku ze seznamu,
    T odeberPredchudce() throws AbstrDoubleListException;//odebrání předchůdce aktuálního prvku ze seznamu,
    Iterator<T> iterator(); //vytvoří iterátor (dle rozhraní Iterable)

}

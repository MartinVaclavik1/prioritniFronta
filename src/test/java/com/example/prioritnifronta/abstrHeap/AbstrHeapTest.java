package com.example.prioritnifronta.abstrHeap;

import com.example.prioritnifronta.Obec;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AbstrHeapTest {

    private final Obec T1 = new Obec(3322, "aaaa", 21, 43);
    private final Obec T2 = new Obec(3322, "bbb", 21, 43);
    private final Obec T3 = new Obec(3322, "ccc", 21, 43);
    private final Obec T4 = new Obec(3322, "ddd", 21, 43);
    private final Obec T5 = new Obec(3322, "eee", 21, 43);
    private final Obec T6 = new Obec(3322, "fff", 21, 43);


    @Test
    void test() {
        System.out.println(1 / 2);
        System.out.println(2 / 2);
        System.out.println(3 / 2);
        System.out.println(4 / 2);
        System.out.println(9 / 2);
    }

    @Test
    void vloz001() throws AbstrHeapException {
        IAbstrHeap<Obec> prioritniFronta = new AbstrHeap<>();
        prioritniFronta.vloz(T1);
        prioritniFronta.vloz(T2);
    }

    @Test
    void vloz002() throws AbstrHeapException {
        IAbstrHeap<Obec> prioritniFronta = new AbstrHeap<>();
        prioritniFronta.vloz(T2);
        prioritniFronta.vloz(T1);
        prioritniFronta.vloz(T5);
        prioritniFronta.vloz(T3);
    }

    @Test
    void vloz003() throws AbstrHeapException {
        IAbstrHeap<Obec> prioritniFronta = new AbstrHeap<>();
        prioritniFronta.vloz(T2);
        prioritniFronta.vloz(T1);
        prioritniFronta.vloz(T3);
        prioritniFronta.vloz(T5);
    }

    @Test
    void vloz004() throws AbstrHeapException {
        IAbstrHeap<Obec> prioritniFronta = new AbstrHeap<>();
        Obec.zmenPrioritu();
        prioritniFronta.reorganizace();
        prioritniFronta.vloz(T2);
        prioritniFronta.vloz(T1);
        prioritniFronta.vloz(T3);
        prioritniFronta.vloz(T5);
    }

    @Test
    void vloz005() throws AbstrHeapException {
        IAbstrHeap<Obec> prioritniFronta = new AbstrHeap<>();
        Obec.zmenPrioritu();
        prioritniFronta.reorganizace();
        prioritniFronta.vloz(T2);
        prioritniFronta.vloz(T1);
        prioritniFronta.vloz(T3);
        prioritniFronta.vloz(T5);
    }

    @Test
    void vloz006() throws AbstrHeapException {
        IAbstrHeap<Obec> prioritniFronta = new AbstrHeap<>();
        Obec.zmenPrioritu();
        prioritniFronta.reorganizace();

        List<Obec> list = new ArrayList<>();
        list.add(0,null);
        list.add(T2);
        list.add(T1);
        list.add(T3);
        list.add(T5);
        prioritniFronta.vybuduj(list);
        //T1 má jednoho potomka T5, který je vyšší, tak se prohodí
        //T2 bude mít potomka T5 a T3. T5 je nejvyšší ze dvojice a vyšší, než T2, tak se prohodí a pořadí je 5,2,3,1

        List<Obec> list2 = new ArrayList<>();
        list2.add(T5);
        list2.add(T2);
        list2.add(T3);
        list2.add(T1);

        Iterator<Obec> it = prioritniFronta.vytvorIterator(eTypProhl.DO_SIRKY);

        for (Obec obec: list2){
            assertEquals(obec,it.next());
        }
    }
    @Test
    void odeber001() throws AbstrHeapException {
        IAbstrHeap<Obec> prioritniFronta = new AbstrHeap<>();
        prioritniFronta.reorganizace();
        prioritniFronta.vloz(T2);
        prioritniFronta.vloz(T1);
        prioritniFronta.vloz(T3);
        prioritniFronta.vloz(T5);
        assertEquals(prioritniFronta.odeberMax(), T5);
    }

    @Test
    void iterator001() throws AbstrHeapException {
        IAbstrHeap<Obec> prioritniFronta = new AbstrHeap<>();
        prioritniFronta.reorganizace();
        prioritniFronta.vloz(T2);
        prioritniFronta.vloz(T1);
        prioritniFronta.vloz(T3);
        prioritniFronta.vloz(T5);

        Iterator<Obec> it = prioritniFronta.vytvorIterator(eTypProhl.DO_HLOUBKY);
        Obec[] test = new Obec[4];
        int i = 0;
        while (it.hasNext()) {
            test[i++] = it.next();
        }
        assertArrayEquals(test, new Obec[]{T5, T3, T2, T1});
    }
}
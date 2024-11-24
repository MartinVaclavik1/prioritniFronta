package com.example.prioritnifronta.abstrHeap;

import com.example.prioritnifronta.Obec;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class AbstrHeapTest {
    
    private final Obec T1 = new Obec(3322, "aaaa", 21,43);
    private final Obec T2 = new Obec(3322, "bbb", 21,43);
    private final Obec T3 = new Obec(3322, "ccc", 21,43);
    private final Obec T4 = new Obec(3322, "ddd", 21,43);
    private final Obec T5 = new Obec(3322, "eee", 21,43);
    private final Obec T6 = new Obec(3322, "fff", 21,43);


    @Test
    void test(){
        System.out.println(1/2);
        System.out.println(2/2);
        System.out.println(3/2);
        System.out.println(4/2);
        System.out.println(9/2);
    }

    @Test
    void vloz001(){
        IAbstrHeap prioritniFronta = new AbstrHeap();
        prioritniFronta.vloz(1, T1.toString(), T1);
        prioritniFronta.vloz(2, T2.toString(), T2);
    }

    @Test
    void vloz002(){
        IAbstrHeap prioritniFronta = new AbstrHeap();
        prioritniFronta.vloz(2, T2.toString(), T2);
        prioritniFronta.vloz(1, T1.toString(), T1);
        prioritniFronta.vloz(5, T5.toString(), T5);
        prioritniFronta.vloz(3, T3.toString(), T3);
    }

    @Test
    void vloz003(){
        IAbstrHeap prioritniFronta = new AbstrHeap();
        prioritniFronta.vloz(2, T2.toString(), T2);
        prioritniFronta.vloz(1, T1.toString(), T1);
        prioritniFronta.vloz(3, T3.toString(), T3);
        prioritniFronta.vloz(5, T5.toString(), T5);
    }

    @Test
    void vloz004(){
        IAbstrHeap prioritniFronta = new AbstrHeap();
        prioritniFronta.reorganizace();
        prioritniFronta.vloz(2, T2.toString(), T2);
        prioritniFronta.vloz(1, T1.toString(), T1);
        prioritniFronta.vloz(3, T3.toString(), T3);
        prioritniFronta.vloz(5, T5.toString(), T5);
    }

    @Test
    void vloz005(){
        IAbstrHeap prioritniFronta = new AbstrHeap();
        prioritniFronta.reorganizace();
        prioritniFronta.vloz(2, T2.toString(), T2);
        prioritniFronta.vloz(1, T1.toString(), T1);
        prioritniFronta.vloz(3, T3.toString(), T3);
        prioritniFronta.vloz(5, T5.toString(), T5);
    }

    @Test
    void odeber001(){
        IAbstrHeap prioritniFronta = new AbstrHeap();
        prioritniFronta.reorganizace();
        prioritniFronta.vloz(2, T2.toString(), T2);
        prioritniFronta.vloz(1, T1.toString(), T1);
        prioritniFronta.vloz(3, T3.toString(), T3);
        prioritniFronta.vloz(5, T5.toString(), T5);
        assertEquals(prioritniFronta.odeberMax(),T5);
    }

    @Test
    void iterator001(){
        IAbstrHeap prioritniFronta = new AbstrHeap();
        prioritniFronta.reorganizace();
        prioritniFronta.vloz(2, T2.toString(), T2);
        prioritniFronta.vloz(1, T1.toString(), T1);
        prioritniFronta.vloz(3, T3.toString(), T3);
        prioritniFronta.vloz(5, T5.toString(), T5);

        Iterator<Obec> it = prioritniFronta.vytvorIterator();
        Obec[] test = new Obec[4];
        int i = 0;
        while (it.hasNext()){
            test[i++] = it.next();
        }
        assertArrayEquals(test, new Obec[] {T5, T3, T2, T1});
    }
}
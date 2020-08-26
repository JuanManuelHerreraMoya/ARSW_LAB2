package eci.arsw.highlandersim;

import edu.eci.arsw.highlandersim.ControlFrame;
import edu.eci.arsw.highlandersim.Immortal;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.util.List;

import static org.junit.Assert.*;

public class test {
    ControlFrame frame;
    Immortal im;
    List<Immortal> lis;

    @Before
    public void inicio() {
        frame = new ControlFrame();
    }

    @Test
    public void pruebaInvariante() throws InterruptedException {
        lis = frame.setupInmortals();
        int healt=0;
        for(Immortal i : lis){
            i.start();
        }

        Thread.sleep(2000);

        for(Immortal i : lis){
            i.pausarPelea();
        }

        for(Immortal i : lis){
            healt+=i.getHealth();
        }

        assertEquals(300,healt);
    }

    @Test
    public void pruebaEliminacionImmortal() throws InterruptedException {
        lis = frame.setupInmortals();
        int res = lis.size();
        for(Immortal i : lis){
            i.start();
        }

        Thread.sleep(3000);

        for(Immortal i : lis){
            i.pausarPelea();
        }

        assertNotEquals(res,lis.size());
    }

    @Test
    public void pruebaDetenerHilos(){
        lis = frame.setupInmortals();
        boolean res=false;
        for(Immortal i : lis){
            i.start();
        }

        for(Immortal i : lis){
            i.pausarPelea();
        }

        for(Immortal i : lis){
            res=i.getEnJuego();
            if (res){
                break;
            }
        }

        assertFalse(res);
    }

    @Test
    public void pruebaReanudarHilos(){
        lis = frame.setupInmortals();
        boolean res=false;
        for(Immortal i : lis){
            i.start();
        }

        for(Immortal i : lis){
            i.pausarPelea();
        }

        for(Immortal i : lis){
            i.renudarJuego();
        }

        for(Immortal i : lis){
            res=i.getEnJuego();
            if (!res){
                break;
            }
        }

        assertTrue(res);
    }

}

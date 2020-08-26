package edu.eci.arsw.highlandersim;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Immortal extends Thread {

    private ImmortalUpdateReportCallback updateCallback=null;
    
    private AtomicInteger health;
    
    private int defaultDamageValue;

    private final List<Immortal> immortalsPopulation;

    private final String name;

    private final Random r = new Random(System.currentTimeMillis());

    private boolean peleando;

    private boolean alive;

    public Immortal(String name, List<Immortal> immortalsPopulation, int health, int defaultDamageValue, ImmortalUpdateReportCallback ucb) {
        super(name);
        this.updateCallback=ucb;
        this.name = name;
        this.immortalsPopulation = immortalsPopulation;
        this.health = new AtomicInteger(health);
        this.defaultDamageValue=defaultDamageValue;
        peleando = true ;
        this.alive = true;
    }

    public void run() {
        while (alive) {
            while(!getEnJuego()){
                synchronized (this){
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            Immortal im;
            if (immortalsPopulation.size() != 1) {
                int myIndex = immortalsPopulation.indexOf(this);
                int nextFighterIndex = r.nextInt(immortalsPopulation.size());
                //avoid self-fight
                if (nextFighterIndex == myIndex) {
                    nextFighterIndex = ((nextFighterIndex + 1) % immortalsPopulation.size());
                }
                im = immortalsPopulation.get(nextFighterIndex);
                this.fight(im);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{break;}
        }
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void fight(Immortal i2) {

        synchronized (i2){
            if (i2.getHealth() > 0 && this.getHealth() > 0 ) {
                i2.changeHealth(-1*defaultDamageValue);
                this.changeHealth(defaultDamageValue);
                updateCallback.processReport("Fight: " + this + " vs " + i2+"\n");
            } if(i2.getHealth() <= 0) {
                immortalsPopulation.remove(i2);
                i2.pausarPelea();
                i2.setAlive(false);
            } if(this.getHealth() <= 0){
                immortalsPopulation.remove(this);
                this.pausarPelea();
                this.setAlive(false);
            }
        }
    }

    public void changeHealth(int v) {
        health.addAndGet(v);
    }

    public int getHealth() {
        return health.intValue();
    }

    @Override
    public String toString() {

        return name + "[" + health + "]";
    }

    public synchronized void pausarPelea() {
        peleando = false;
        this.notifyAll();
    }

    public boolean getEnJuego(){
        return peleando;
    }

    public synchronized void renudarJuego() {
        peleando = true;
        this.notifyAll();
    }

}

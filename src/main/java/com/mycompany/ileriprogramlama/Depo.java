
package com.mycompany.ileriprogramlama;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
/**
 *
 * @author yasin
 */
public class Depo {
    public String urunAdi;
    public int stok;
    public int kapasite;
    public int minimumStok;
    public int eklenecekUrun;

    public Depo(String urunAdi, int kapasite, int minimumStok, int eklenecekUrun) {
        this.urunAdi = urunAdi;
        this.kapasite = kapasite;
        this.minimumStok = minimumStok;
        this.eklenecekUrun = eklenecekUrun;
        this.stok = 0;
    }

    public synchronized void urunEkle() throws InterruptedException {
        while (stok + eklenecekUrun > kapasite) {
            System.out.println("Depo dolu! urun eklenemiyor.");
            wait();
        }
        stok += eklenecekUrun;
        System.out.println(eklenecekUrun + " adet urun eklendi++++++++++. Guncel stok: " + stok);
        notifyAll();
    }

    public synchronized void urunCek(int miktar) throws InterruptedException {
        while (stok < miktar) {
            System.out.println("Stok yetersiz! Bekleniyor...");
            wait();
        }
        stok -= miktar;
        System.out.println(miktar + " adet urun cekildi------. Guncel stok: " + stok);
        if (stok <= minimumStok) {
            System.out.println("Minimum stoga ulasildi! urun ekleniyor...");
            urunEkle();
        }
        notifyAll();
    }

    public int getStok() {
        return stok;
    }

    public void setMinimumStok(int minimumStok) {
        this.minimumStok = minimumStok;
    }

    public void setEklenecekUrun(int eklenecekUrun) {
        this.eklenecekUrun = eklenecekUrun;
    }
}
package com.mycompany.ileriprogramlama;

/**
 *
 * @author yasin
 */
public class Uretici extends Thread {
    private Depo depo;

    public Uretici(Depo depo) {
        this.depo = depo;
    }

    @Override
    public void run() {
        try {
            while (true) {
                depo.urunEkle();
                Thread.sleep(2000); // Üretici işlemi için bekleme süresi
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

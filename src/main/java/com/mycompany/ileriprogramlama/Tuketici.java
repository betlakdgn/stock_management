
package com.mycompany.ileriprogramlama;

/**
 *
 * @author yasin
 */
public class Tuketici extends Thread {
    private Depo depo;

    public Tuketici(Depo depo) {
        this.depo = depo;
    }

    @Override
    public void run() {
        try {
            while (true) {
                int miktar = (int) (Math.random() * 5) + 1; // 1-5 arası rasgele ürün çek
                depo.urunCek(miktar);
                Thread.sleep(1000); // Tüketici işlemi için bekleme süresi
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

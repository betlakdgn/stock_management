package com.mycompany.ileriprogramlama;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 *
 * @author yasin
 */
public class DepoYonetimGUI {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Depo Yönetimi");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel urunLabel = new JLabel("Ürün Adı:");
        JTextField urunField = new JTextField(10);

        JLabel kapasiteLabel = new JLabel("Depo Kapasitesi:");
        JTextField kapasiteField = new JTextField(5);

        JLabel minimumLabel = new JLabel("Minimum Stok:");
        JTextField minimumField = new JTextField(5);

        JLabel eklenecekLabel = new JLabel("Eklenecek Ürün Adedi:");
        JTextField eklenecekField = new JTextField(5);

        JButton ekleButton = new JButton("Depoyu Başlat");

        JPanel panel = new JPanel();
        panel.add(urunLabel);
        panel.add(urunField);
        panel.add(kapasiteLabel);
        panel.add(kapasiteField);
        panel.add(minimumLabel);
        panel.add(minimumField);
        panel.add(eklenecekLabel);
        panel.add(eklenecekField);
        panel.add(ekleButton);

        frame.add(panel);
        frame.setVisible(true);

        ekleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String urunAdi = urunField.getText();
                int kapasite = Integer.parseInt(kapasiteField.getText());
                int minimumStok = Integer.parseInt(minimumField.getText());
                int eklenecekUrun = Integer.parseInt(eklenecekField.getText());

                Depo depo = new Depo(urunAdi, kapasite, minimumStok, eklenecekUrun);

                Uretici uretici = new Uretici(depo);
                Tuketici tuketici1 = new Tuketici(depo);
                Tuketici tuketici2 = new Tuketici(depo);

                uretici.start();
                tuketici1.start();
                tuketici2.start();
            }
        });
    }
}

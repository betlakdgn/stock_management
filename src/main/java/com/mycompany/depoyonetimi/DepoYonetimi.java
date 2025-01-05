package com.mycompany.depoyonetimi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.concurrent.*;

public class DepoYonetimi {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> tedarikciComboBox;
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ExecutorService executor = Executors.newFixedThreadPool(4);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DepoYonetimi().initialize());
    }

    public void initialize() {
        // Ana pencere oluşturuluyor
        frame = new JFrame("Depo Yönetimi");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Tablo modelini oluştur
        tableModel = new DefaultTableModel(new Object[]{"Ürün", "Stok", "Min Stok", "Eklenecek Stok", "Alınacak Adet"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // "Tedarikçi Ekle" ve "Ürün Ekle" butonları
        JButton btnTedarikciEkle = new JButton("Tedarikçi Ekle");
        JButton btnUrunEkle = new JButton("Ürün Ekle");
        JButton btnSatinAl = new JButton("Satın Al");

        // Tedarikçi ComboBox'ı
        tedarikciComboBox = new JComboBox<>();
        tedarikciComboBox.addItem("Seçiniz"); // Varsayılan

        // Butonların bulunduğu panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnTedarikciEkle);
        buttonPanel.add(btnUrunEkle);
        buttonPanel.add(btnSatinAl);

        // Ana pencereyi düzenle
        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Tedarikçi ekleme işlevi
        btnTedarikciEkle.addActionListener(e -> {
            String tedarikci = JOptionPane.showInputDialog(frame, "Tedarikçi Adını Girin:");
            if (tedarikci != null && !tedarikci.isEmpty()) {
                tedarikciComboBox.addItem(tedarikci);
                JOptionPane.showMessageDialog(frame, "Tedarikçi Eklendi: " + tedarikci);
            } else {
                JOptionPane.showMessageDialog(frame, "Tedarikçi adı boş olamaz!", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Ürün ekleme işlevi
        btnUrunEkle.addActionListener(e -> {
            JPanel panel = new JPanel(new GridLayout(5, 2));
            JTextField urunAdiField = new JTextField();
            JTextField stokField = new JTextField();
            JTextField minStokField = new JTextField();
            JTextField ekStokField = new JTextField();

            // Tedarikçi seçim alanı
            panel.add(new JLabel("Ürün Adı:"));
            panel.add(urunAdiField);
            panel.add(new JLabel("Stok:"));
            panel.add(stokField);
            panel.add(new JLabel("Min Stok:"));
            panel.add(minStokField);
            panel.add(new JLabel("Eklenecek Stok:"));
            panel.add(ekStokField);
            panel.add(new JLabel("Tedarikçi:"));
            panel.add(tedarikciComboBox);

            int result = JOptionPane.showConfirmDialog(frame, panel, "Ürün Ekle", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String urunAdi = urunAdiField.getText();
                int stok = Integer.parseInt(stokField.getText());
                int minStok = Integer.parseInt(minStokField.getText());
                int ekStok = Integer.parseInt(ekStokField.getText());
                String tedarikci = tedarikciComboBox.getSelectedItem().toString();

                if (!urunAdi.isEmpty() && !tedarikci.equals("Seçiniz")) {
                    tableModel.addRow(new Object[]{urunAdi, stok, minStok, ekStok, 0});
                    // Her ürün için stok kontrol thread'i başlat
                    executor.submit(() -> stokKontrolVeEkle(urunAdi, minStok, ekStok));
                    JOptionPane.showMessageDialog(frame, "Ürün Eklendi: " + urunAdi);
                } else {
                    JOptionPane.showMessageDialog(frame, "Ürün adı veya tedarikçi seçimi eksik!", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Satın alma işlevi
        btnSatinAl.addActionListener(e -> {
            executor.submit(this::satinAlIslemi); // İşlemi paralel olarak çalıştır
        });

        // Otomatik stok güncellemeyi başlat
        frame.setVisible(true);
    }

    // "Satın Al" işlemi
    private void satinAlIslemi() {
        synchronized (tableModel) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                try {
                    int mevcutStok = Integer.parseInt(tableModel.getValueAt(i, 1).toString());
                    int alinacakAdet = Integer.parseInt(tableModel.getValueAt(i, 4).toString());

                    if (alinacakAdet > 0) {
                        if (alinacakAdet <= mevcutStok) {
                            mevcutStok -= alinacakAdet;
                            tableModel.setValueAt(mevcutStok, i, 1);
                            tableModel.setValueAt(0, i, 4);
                        } else {
                            JOptionPane.showMessageDialog(frame,
                                    "Stok yetersiz: " + tableModel.getValueAt(i, 0),
                                    "Hata",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame,
                            "Hatalı veya boş 'Alınacak Adet' değeri! Satır: " + (i + 1),
                            "Hata",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // Stok kontrol ve ekleme işlemi
    private void stokKontrolVeEkle(String urunAdi, int minStok, int ekStok) {
        while (true) {
            synchronized (tableModel) {
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    String mevcutUrun = tableModel.getValueAt(i, 0).toString();
                    if (mevcutUrun.equals(urunAdi)) {
                        int mevcutStok = Integer.parseInt(tableModel.getValueAt(i, 1).toString());
                        if (mevcutStok <= minStok) {
                            mevcutStok += ekStok;
                            tableModel.setValueAt(mevcutStok, i, 1);
                            System.out.println("Stok güncellendi: " + mevcutUrun + " - Yeni Stok: " + mevcutStok);
                        }
                    }
                }
            }
            try {
                Thread.sleep(1000); // saniyede bir kontrol et
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}

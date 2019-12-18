package com.sunflower.nabung;

public class Wishlist {
    private String dataID, Judul, jangkaWaktu, totalSaldo, saldoTerkumpul, saldoRekomendasi, tanggalAwal;

    public Wishlist() {
    }

    public Wishlist(String dataID, String judul, String jangkaWaktu, String totalSaldo, String saldoTerkumpul, String saldoRekomendasi, String tanggalAwal) {
        this.dataID = dataID;
        Judul = judul;
        this.jangkaWaktu = jangkaWaktu;
        this.totalSaldo = totalSaldo;
        this.saldoTerkumpul = saldoTerkumpul;
        this.saldoRekomendasi = saldoRekomendasi;
        this.tanggalAwal = tanggalAwal;
    }

    public String getDataID() {
        return dataID;
    }

    public void setDataID(String dataID) {
        this.dataID = dataID;
    }

    public String getJudul() {
        return Judul;
    }

    public void setJudul(String judul) {
        Judul = judul;
    }

    public String getJangkaWaktu() {
        return jangkaWaktu;
    }

    public void setJangkaWaktu(String jangkaWaktu) {
        this.jangkaWaktu = jangkaWaktu;
    }

    public String getTotalSaldo() {
        return totalSaldo;
    }

    public void setTotalSaldo(String totalSaldo) {
        this.totalSaldo = totalSaldo;
    }

    public String getSaldoTerkumpul() {
        return saldoTerkumpul;
    }

    public void setSaldoTerkumpul(String saldoTerkumpul) {
        this.saldoTerkumpul = saldoTerkumpul;
    }

    public String getSaldoRekomendasi() {
        return saldoRekomendasi;
    }

    public void setSaldoRekomendasi(String saldoRekomendasi) {
        this.saldoRekomendasi = saldoRekomendasi;
    }

    public String getTanggalAwal() {
        return tanggalAwal;
    }

    public void setTanggalAwal(String tanggalAwal) {
        this.tanggalAwal = tanggalAwal;
    }
}

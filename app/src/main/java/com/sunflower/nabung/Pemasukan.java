package com.sunflower.nabung;

public class Pemasukan {
    private String Catatan,Tanggal,SaldoPemasukan;
    private String dataID;

    public Pemasukan() {
    }

    public Pemasukan(String dataID, String catatan, String tanggal, String saldoPemasukan) {
        this.dataID = dataID;
        Catatan = catatan;
        Tanggal = tanggal;
        SaldoPemasukan = saldoPemasukan;
    }

    public String getDataID() {
        return dataID;
    }

    public void setDataID(String dataID) {
        this.dataID = dataID;
    }

    public String getCatatan() {
        return Catatan;
    }

    public void setCatatan(String catatan) {
        Catatan = catatan;
    }

    public String getTanggal() {
        return Tanggal;
    }

    public void setTanggal(String tanggal) {
        Tanggal = tanggal;
    }

    public String getSaldoPemasukan() {
        return SaldoPemasukan;
    }

    public void setSaldoPemasukan(String saldoPemasukan) {
        SaldoPemasukan = saldoPemasukan;
    }
}

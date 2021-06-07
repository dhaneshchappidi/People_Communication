package com.example.dhaneshchappidi.pc;

public class Modelchat {
    String name,idno,image,uid,type;
    public Modelchat() {
    }

    public Modelchat(String name, String idno, String image, String uid, String type) {
        this.name = name;
        this.idno = idno;
        this.image = image;
        this.uid = uid;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}


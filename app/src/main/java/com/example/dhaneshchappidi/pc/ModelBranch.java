package com.example.dhaneshchappidi.pc;

public class ModelBranch {
    String uid,uname,uemail,bid,btitle,bdesc,byear,branch;

    public ModelBranch() {
    }

    public ModelBranch(String uid, String uname, String uemail, String bid, String btitle, String bdesc, String byear, String branch) {
        this.uid = uid;
        this.uname = uname;
        this.uemail = uemail;
        this.bid = bid;
        this.btitle = btitle;
        this.bdesc = bdesc;
        this.byear = byear;
        this.branch = branch;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUemail() {
        return uemail;
    }

    public void setUemail(String uemail) {
        this.uemail = uemail;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getBtitle() {
        return btitle;
    }

    public void setBtitle(String btitle) {
        this.btitle = btitle;
    }

    public String getBdesc() {
        return bdesc;
    }

    public void setBdesc(String bdesc) {
        this.bdesc = bdesc;
    }

    public String getByear() {
        return byear;
    }

    public void setByear(String byear) {
        this.byear = byear;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}

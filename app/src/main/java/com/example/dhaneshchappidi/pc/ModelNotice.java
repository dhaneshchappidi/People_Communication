package com.example.dhaneshchappidi.pc;

public class ModelNotice {
        String ntitle,ndesc,nyear,nbranch,nid;

    public ModelNotice() {

    }

    public ModelNotice(String ntitle, String ndesc, String nyear, String nbranch,String nid) {
        this.ntitle = ntitle;
        this.ndesc = ndesc;
        this.nyear = nyear;
        this.nbranch = nbranch;
        this.nid=nid;
    }

    public String getNtitle() {
        return ntitle;
    }

    public void setNtitle(String ntitle) {
        this.ntitle = ntitle;
    }

    public String getNdesc() {
        return ndesc;
    }

    public void setNdesc(String ndesc) {
        this.ndesc = ndesc;
    }

    public String getNyear() {
        return nyear;
    }

    public void setNyear(String nyear) {
        this.nyear = nyear;
    }

    public String getNbranch() {
        return nbranch;
    }

    public void setNbranch(String nbranch) {
        this.nbranch = nbranch;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }
}

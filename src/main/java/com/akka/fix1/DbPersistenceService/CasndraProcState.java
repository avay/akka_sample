package com.akka.fix1.DbPersistenceService;

import java.io.Serializable;
import java.util.ArrayList;

public class CasndraProcState implements Serializable {
    private final ArrayList<String> usrDetails;

    public CasndraProcState() {
        this(new ArrayList<String>());
    }

    public CasndraProcState(ArrayList<String> events) {
        this.usrDetails = events;
    }

    public CasndraProcState copy() {
        return new CasndraProcState(new ArrayList<String>(usrDetails));
    }

    public void update(UsrDetail arg0) {
        usrDetails.add(arg0.toString());
    }

    @Override
    public String toString() {
        return usrDetails.toString();
    }
}

package com.example.emanuele.gino;

import java.util.List;

/**
 * Created by emanuele on 03/12/17.
 */

public class EsportaNodo {

    List<Nodi> nodi;
    String nodes;
    public EsportaNodo(List<Nodi> items) {
        this.nodi = items;

    }

    public static class  Nodi{
        String id;
        int group;
        public Nodi(String id, int group) {
            this.id=id;
            this.group=group;
        }
    }


}

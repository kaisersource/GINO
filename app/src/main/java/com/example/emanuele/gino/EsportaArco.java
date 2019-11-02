package com.example.emanuele.gino;

import java.util.List;

/**
 * Created by emanuele on 04/12/17.
 */


    public class EsportaArco {

        List<Archi> archi;

        public EsportaArco(List<Archi> items) {
            this.archi = items;

        }

        public static class Archi{
            String source;
            String target;
            int value;
            public Archi(String source, String target, int value) {
                this.source=source;
                this.target=target;
                this.value=value;
            }
        }


    }


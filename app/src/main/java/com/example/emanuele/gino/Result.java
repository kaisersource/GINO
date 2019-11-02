package com.example.emanuele.gino;

import android.app.Activity;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emanuele.gino.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import io.github.kexanie.library.MathView;

/**
 * Created by emanuele on 03/10/17.
 */

public class Result extends Activity {
    protected int[] B;
    protected int[][] A;
    protected TextView edges, grado, regolare, isolato, connesso, euleriano;
    public int sizeA;
    protected View resultlayout;
    MathView definizioni;







    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        B = getIntent().getIntArrayExtra("array");
        setContentView(R.layout.result);

        sizeA = (int) Math.sqrt(B.length);
        A = new int[sizeA][sizeA];


        //get xml resources
        edges = (TextView) findViewById(R.id.edges);
        grado = (TextView) findViewById(R.id.grado);
        regolare = (TextView) findViewById(R.id.regolare);
        isolato = (TextView) findViewById(R.id.isolato);
        connesso = (TextView) findViewById(R.id.connesso);
        euleriano = (TextView) findViewById(R.id.euleriano);
        resultlayout = (View) findViewById(R.id.resultlayout);
        definizioni = (MathView) findViewById(R.id.definizioni);

        //assegnamento gradienti
        GradientDrawable Gresultlayout = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{0xFFACDBC9, 0xFFF8B195});
        Gresultlayout.setCornerRadius(0f);
        resultlayout.setBackground(Gresultlayout);


        creaMatrice();

        stampaArchi();
        stampaGrado();

        if (stampaRegolare() == 0)
            regolare.setText("Grafo regolare");
        else regolare.setText("Grafo non regolare");
        detectIsolatedNodes();
        //   connesso();

        if (COMP_CONNESSE(sizeA) == 1)
            connesso.setText("Grafo connesso");
        else connesso.setText("Componenti connesse: " + COMP_CONNESSE(0));
        euleriano();



        edges.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                definizioni.setText("Un grafo \\(G\\) è una coppia \\((V, E)\\) " +
                        "dove \\(V\\) è un insieme e  \\(E\\subseteq V\\times V\\) " +
                        "è un sottoinsieme del prodotto cartesiano di \\(V\\) per se stesso. " +
                        "Gli elementi di \\(V\\) sono detti nodi e quelli di \\(E\\) sono detti archi. " +
                        "I nodi sono spesso chiamati anche vertici. " +
                        "Gli archi sono detti anche lati o spigoli.");
            }
        });

        grado.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                definizioni.setText("Il numero di archi incidenti in un vertice \\(v\\in V\\) " +
                        "(cioè il numero di archi che si connettono ad esso) " +
                        "prende il nome di grado del vertice \\(v\\). " +
                        "Un arco che si connette al vertice ad entrambe le estremità (un cappio) " +
                        "è contato due volte.");
            }
        });

        regolare.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                definizioni.setText("Nella teoria dei grafi, un grafo regolare è un grafo " +
                        "in cui ogni vertice ha lo stesso numero di vicini, " +
                        "cioè ogni vertice ha lo stesso grado.");
            }
        });

        isolato.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                definizioni.setText("Un nodo isolato è un vertice che non è connesso " +
                        "a nessun altro vertice. Un nodo isolato ha grado 0.");
            }
        });

        connesso.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                definizioni.setText("Un grafo \\(G = (V, E)\\) è detto connesso se, " +
                        "per ogni coppia di vertici \\(u, v \\in V\\), " +
                        "esiste un cammino che collega \\(u\\) a \\(v\\). Un sottografo connesso massimale " +
                        "di un grafo non orientato è detto componente connessa di tale grafo. " +
                        "Di conseguenza, un grafo è connesso se esso è composto da una sola componente connessa.");
            }
        });

        euleriano.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                definizioni.setText("Un grafo \\(G\\) connesso ammette un cammino euleriano " +
                        "se ha esattamente due vertici di grado dispari. Ammette circuito euleriano invece se " +
                        "tutti i suoi vertici hanno grado pari.");
            }
        });


        // connesso.append("" + mark[i]);
         /*\
               connesso.append("" + i);
           else connesso.append("\n");

       }*/

    }

    void creaMatrice() {
        for (int i = 0; i < sizeA; i++) {
            for (int j = 0; j < sizeA; j++) {
                A[i][j] = B[j + sizeA * i];
            }
        }
    }

    void stampaMatrice() {
        String s = "";
        for (int i = 0; i < sizeA; i++) {
            for (int j = 0; j < sizeA; j++) {
                s = s + " " + A[i][j] + " ";
            }
        }
        edges.setText(s);

    }

    void stampaArchi() {
        edges.setText("Archi: ");
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A.length; j++) {
                if (A[i][j] == 1)
                    edges.append("(" + (i + 1) + "," + (j + 1) + ")");
            }
        }
    }

    void stampaGrado() {
        //stampa il vertice con più archi
        //elemento con piu archi.
        int[] Carry = new int[sizeA];//vettore d'appoggio per contare le ocorrenze
        for (int i = 0; i < sizeA; i++) {
            for (int j = 0; j < sizeA; j++) {
                //se l'arco esiste incrementa il grado di 1, se è un cappio incrementa di 2
                if (A[i][j] == 1 && i == j)
                    Carry[i] = Carry[i] + 2;
                else if (A[i][j] == 1)
                    Carry[i]++;
            }
        }
        int degmaxind[] = new int[sizeA]; //array dei vertici col massimo grado
        int max = Carry[0];//salvo l'elemento di partenza da confrontare

        for (int i = 0; i < sizeA; i++) {
            if (max <= Carry[i]) {//verifico l'elemento che ha avuto il maggior numero di archi(lo scopro in base all'indice i)
                max = Carry[i];//nuovo massimo

            }
        }

        for (int i = 0; i < sizeA; i++)
            if (max == Carry[i])
                degmaxind[i] = i + 1;

        grado.setText("max grado: " + max + "\n");
        grado.append("vertici max grado: ");

        if (degmaxind[0] > 0)
            grado.append("" + degmaxind[0]);
        for (int i = 1; i < sizeA; i++) {
            if (degmaxind[i] > 0) //quelli con zero li evito
                grado.append(" " + degmaxind[i]);
        }

    }

    //verifica la k-regolarità del grafo
    int stampaRegolare() {
        int[] C = new int[sizeA];
        for (int i = 0; i < sizeA; i++) {
            for (int j = 0; j < sizeA; j++) {
                if (A[i][j] == 1)//se esiste l'arco
                    C[i]++;//incremento il grado
            }
        }
        for (int i = 0; i < sizeA - 1; i++) {
            if (C[i] != C[i + 1]) //Se sono uguali non entra nella condizione e ritorna 1 (Regolare). Altrimenti 0 (non regolare).
                return 1;
        }
        return 0;
    }

    void detectIsolatedNodes() {
        //trovare nodi isolati (quindi grado 0)
        int[] C = new int[sizeA]; //array accumulatore il cui indice corrisponde al nodo mentre i suoi valori alla presenza di archi


        for (int i = 0; i < sizeA; i++) {
            for (int j = 0; j < sizeA; j++) {
                if (A[i][j] == 0)//se e' un nodo isolato allora
                    C[i]++; //incrementa. Intuitivamente il nodo i isolato e' quello la cui C[i]=dim.
            }
        }
        isolato.setText("Nodi isolati =>");
        for (int i = 0; i < sizeA; i++) {
            if (C[i] == sizeA)
                isolato.append(" " + (i + 1));//stampo l'indice che equivale al nodo isolato
        }
    }


    //DFS per trovare il numero di componenti connesse (ricorsiva)
    int COMP_CONNESSE(int n) {
        int i;
        int numComp = 0;
        int comp[] = new int[sizeA];
      /*  for (i = 0; i < sizeA; i++) {
            comp[i] = 0;
        }*/
        for (i = 0; i < sizeA; i++) {
            if (comp[i] == 0) {
                numComp++;
                comp[i] = 1;
                //DFS_CONNESSE(i,numComp, comp);
                DFS_CONNESSE(i, comp);
            }
        }
        return numComp;
    }

    void DFS_CONNESSE(int u, int comp[]) {
        int v;
        // comp[u] = numComp;
        for (v = 0; v < sizeA; v++) {
            if (A[u][v] == 1) {
                if (comp[v] == 0) {
                    comp[v] = 1;
                    DFS_CONNESSE(v, comp);
                }

            }
        }

    }

    //euleriano sfrutta il codice di stampagrado per rilevare il grado di ogni vertice
    //CN euleriano è che G sia connesso, dunque invoco la dfs
    void euleriano() {
        int oddegree = 0;


        int[] Carry = new int[sizeA];//vettore d'appoggio per contare le ocorrenze
        for (int i = 0; i < sizeA; i++) {
            for (int j = 0; j < sizeA; j++) {
                if (A[i][j] == 1 && i == j)
                    Carry[i] = Carry[i] + 2;
                else if (A[i][j] == 1)
                    Carry[i]++;
            }
            if (Carry[i] % 2 == 1) oddegree++; //grado dispari ->incrementa
        }

        if (oddegree == 0 && COMP_CONNESSE(sizeA) == 1)
            euleriano.setText("Ammette circuito euleriano");
        else if (oddegree == 2 && COMP_CONNESSE(sizeA) == 1)
            euleriano.setText("Ammette cammino euleriano");
        else euleriano.setText("grafo non  euleriano");


    }


}



    //creare una funzione che trasforma un array di interi in json














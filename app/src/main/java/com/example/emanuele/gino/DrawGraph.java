package com.example.emanuele.gino;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by emanuele on 04/12/17.
 */

public class DrawGraph extends Activity {
    protected int[] B;
    protected int[][] A;
    public int sizeA;
    protected WebView wv_graph;


    String tree="{ \"nodes\": ";
    String startLink=",\"links\": ";
    String endLink="}";
    String filename = "grafo.json";
    FileOutputStream deleteContent;
    FileOutputStream outputStream;

    private final static String TAG = DrawGraph.class.getSimpleName();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        B = getIntent().getIntArrayExtra("array");
        setContentView(R.layout.draw_graph);

        sizeA = (int) Math.sqrt(B.length);
        A = new int[sizeA][sizeA];

        creaMatrice();
        disegnaMatrice();
    }

    void creaMatrice() {
        for (int i = 0; i < sizeA; i++) {
            for (int j = 0; j < sizeA; j++) {
                A[i][j] = B[j + sizeA * i];
            }
        }
    }

    void disegnaMatrice() {
        List<EsportaNodo.Nodi> esportaNodi = new ArrayList<>();
        List<EsportaArco.Archi> esportaArchi = new ArrayList<>();

        for (int i = 0; i < sizeA; i++)
            esportaNodi.add(new EsportaNodo.Nodi(""+(i+1),(i+1)));

        for (int i = 0; i < sizeA; i++) {
            for (int j = 0; j < sizeA; j++) {
                if(A[i][j]==1)
                    esportaArchi.add(new EsportaArco.Archi(""+(i+1),""+(j+1),1));
            }

        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json0= gson.toJson(esportaNodi);
        String json1= gson.toJson(esportaArchi);

        System.out.print(tree);
        System.out.println(json0);
        System.out.print(startLink);
        System.out.print(json1);
        System.out.print(endLink);
        String grafojson= Environment.getExternalStorageDirectory()+"/GINO/graph.json";
        String indexhtml= "file:///android_asset/index.html";


        //creazione cartella GINO
        final File f = new File(Environment.getExternalStorageDirectory(), "GINO");

        if (!f.exists()) {
            Log.d(TAG, "Folder doesn't exist, creating it...");
            boolean rv = f.mkdir();
            Log.d(TAG, "Folder creation " + ( rv ? "success" : "failed"));
        } else {
            Log.d(TAG, "Folder already exists.");
        }


        try {
            //Eliminazione contenuto: 32 è il backspace, quindi scrive il nulla
            deleteContent =new FileOutputStream(grafojson);
            deleteContent.write(32);
            deleteContent.close();

            //scrittura su file
            outputStream =new FileOutputStream(grafojson,true);
            outputStream.write(tree.getBytes());
            outputStream.write(json0.getBytes());
            outputStream.write(startLink.getBytes());
            outputStream.write(json1.getBytes());
            outputStream.write(endLink.getBytes());

            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //gestione della webview
        wv_graph=(WebView)findViewById(R.id.wv_graph);

        final WebSettings ws = wv_graph.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setPluginState(WebSettings.PluginState.ON);
        ws.setAllowFileAccess(true);
        ws.setDomStorageEnabled(true);
        ws.setAllowContentAccess(true);
        ws.setAllowFileAccessFromFileURLs(true);
        ws.setAllowUniversalAccessFromFileURLs(true);
        ws.setBuiltInZoomControls(true);

        wv_graph.setWebViewClient(new WebViewClient());
        wv_graph.setWebChromeClient(new WebChromeClient());
        wv_graph.loadUrl(indexhtml); //stringa dell'asset

    }

}

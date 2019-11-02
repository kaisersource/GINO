package com.example.emanuele.gino;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.emanuele.gino.R;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

public class MainActivity extends Activity {


    int[] A;
    int i = 0, j = 0,size;
    ImageButton verifica,disegna;
    Button[][] buttonarray;
    int temp = 0;
    TextView indici_alti[];
    //TextView indici_lati[];
    TableRow tableRow;
    TableLayout tableContent;
    TableLayout tableHeader;

    protected RelativeLayout mainlayout;
    protected RelativeLayout buttons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mainlayout=(RelativeLayout) findViewById(R.id.ml);
        GradientDrawable Gmainlayout = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {0xFFACDBC9,0xFFF8B195});
        Gmainlayout.setCornerRadius(0f);
        mainlayout.setBackground(Gmainlayout);

        //passing size from previous activity
        size=getIntent().getIntExtra("size",0);


        buttonarray = new Button[size][size];
        indici_alti= new TextView[size];
        buttons=findViewById(R.id.pulsanti);
        //indici_lati= new TextView[size+1];


        tableHeader = findViewById(R.id.tbl_header);
        tableContent = findViewById(R.id.tfb);



        //inserisco l'array degli indici
        populatehighindex();
        populateButtons();
        bottoneVerifica();
        bottoneDisegna();
        //Intent per lanciare l'activity dei risultati
        A = new int[size*size];

        //linearlayout pulsanti

 //configurazione verifica e disegna imagebuttons





        //listeners
        verifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(int i=0; i<size; i++) {
                    for (int j = 0; j<size; j++) {
                        A[j + size*i] = Integer.parseInt(buttonarray[i][j].getText().toString());
                    }
                }
                Intent intent = new Intent(MainActivity.this, Result.class);
                intent.putExtra("array", A);
                startActivity(intent);

            }
        });



        disegna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(int i=0; i<size; i++) {
                    for (int j = 0; j<size; j++) {
                        A[j + size*i] = Integer.parseInt(buttonarray[i][j].getText().toString());
                    }
                }
                Intent intent = new Intent(MainActivity.this, DrawGraph.class);
                intent.putExtra("array", A);
                startActivity(intent);

            }
        });

    }


    private void populateButtons() {

        //gradiente degli zeri
        final GradientDrawable shape0 = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{0xFFF69D3D, 0xFFF05053});
        shape0.setShape(GradientDrawable.RECTANGLE);
        //shape0.setCornerRadii(new float[] { 8, 8, 8, 8, 0, 0, 0, 0 });
        shape0.setStroke(15, Color.parseColor("#040009"));

        //gradiente degli uni
        final GradientDrawable shape1 = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{0xFF93FF00, 0xFF0055EB});
        shape1.setShape(GradientDrawable.RECTANGLE);
        //shape1.setCornerRadii(new float[] { 8, 8, 8, 8, 0, 0, 0, 0 });
        shape1.setStroke(15, Color.parseColor("#040009"));

        for (i = 0; i < size; i++) {

            tableRow = new TableRow(this);

            /*lambda tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f)); */
            TableLayout.LayoutParams tableRowParams= new TableLayout.LayoutParams
                    (TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.MATCH_PARENT); //nuovo

            tableRow.setLayoutParams(tableRowParams);
            tableContent.setColumnShrinkable(i,true);



            for (j = 0; j < size; j++) {

                buttonarray[i][j] = (Button) new Button(this);
              /*lambda  buttonarray[i][j].setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f));
                */
                TableRow.LayoutParams buttonParams= new TableRow.LayoutParams
                        (TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT); //nuovo

                buttonParams.setMargins(10,10,10,0);

                buttonarray[i][j].setLayoutParams(buttonParams);
                buttonarray[i][j].setTextSize(COMPLEX_UNIT_SP,25);
                buttonarray[i][j].setText("0");
                buttonarray[i][j].setBackground(shape0);
                buttonarray[i][j].setPadding(0, 0, 0, 0);
                buttonarray[i][j].setHeight(buttonarray[i][j].getMeasuredWidth());
                buttonarray[i][j].setOnClickListener(new View.OnClickListener() {
                   //trick per bloccare gli indici, altrimenti l'inserimento viene fatto solo all'ultimo elemento
                    final int i1 = i;
                    final int j1 = j;

                    @Override
                    public void onClick(View v) {

                        if (buttonarray[i1][j1].getText().toString().equals("0")) {
                            buttonarray[i1][j1].setText("1");
                            buttonarray[j1][i1].setText("1");
                            buttonarray[i1][j1].setBackground(shape1);
                            buttonarray[j1][i1].setBackground(shape1);
                        } else {

                            buttonarray[i1][j1].setText("0");
                            buttonarray[j1][i1].setText("0");
                            buttonarray[i1][j1].setBackground(shape0);
                            buttonarray[j1][i1].setBackground(shape0);

                        }
                    }
                });
                buttonarray[i][j].setWidth(buttonarray[i][j].getHeight());
                tableRow.addView(buttonarray[i][j]);

            }
            tableContent.addView(tableRow);
        }
    }

    //popolare gli indici delle colonne
    private void populatehighindex(){

        //indici fixati su un tablelayout tableheader sopra la scrollview

        tableRow = new TableRow(this);
        tableRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT,
                1.0f));


        for(int i=0;i<size;i++){
            indici_alti[i] = new TextView(this);
        indici_alti[i].setLayoutParams(new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.MATCH_PARENT,
                1.0f));

            indici_alti[i].setText(""+(i+1));
            indici_alti[i].setGravity(Gravity.CENTER);

            tableRow.addView(indici_alti[i]);
        }
        tableHeader.addView(tableRow);
    }

    private void bottoneVerifica(){
        //parametri del bottone e dell'indice
        RelativeLayout.LayoutParams verParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        verParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        verParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        verifica = new ImageButton(this);
        verifica.setImageResource(R.drawable.check);

        verifica.setBackgroundColor(Color.TRANSPARENT);
        verifica.setPadding(15,0,0,15);
        verifica.setLayoutParams(verParams);


        buttons.addView(verifica);
    }
    private void bottoneDisegna(){

        disegna = new ImageButton(this);
        disegna.setImageResource(R.drawable.draw);
        RelativeLayout.LayoutParams disParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        disParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        disParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        disegna.setBackgroundColor(Color.TRANSPARENT);
        disegna.setPadding(0,0,15,15);
        disegna.setLayoutParams(disParams);

        buttons.addView(disegna);
    }




}

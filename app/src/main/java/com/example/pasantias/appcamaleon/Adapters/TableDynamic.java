package com.example.pasantias.appcamaleon.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.pasantias.appcamaleon.Pojos.Item;
import com.example.pasantias.appcamaleon.R;
import com.example.pasantias.appcamaleon.Util.Cart;

import java.util.List;

public class TableDynamic {
    private TableLayout tableLayout;
    private Context context;
    private String[] header;
    private List<Item> productos;

    private TableRow tableRow;
    private TextView textCell;

    private int indexR;
    private int indexC;

    public TableDynamic(TableLayout tableLayout, Context context) {
        this.tableLayout = tableLayout;
        this.context = context;
    }

    public void addHeader(String[] header) {
        this.header = header;
        createHeader();
    }

    public void addData(List<Item> lis) {
        this.productos = lis;
        createDataTable();
    }

    private void newRow() {
        tableRow = new TableRow(context);
    }

    private void newCell() {
        textCell = new TextView(context);
        textCell.setBackgroundColor(Color.WHITE);
        textCell.setTextColor(Color.BLACK);
        textCell.setPadding(7,15,0,15);
        //textCell.setGravity(Gravity.CENTER);
        textCell.setTextSize(15);
    }


    private void createHeader() {
        indexC = 0;
        newRow();
        while (indexC < header.length) {
            newCell();
            textCell.setText(header[indexC++]);
            tableRow.addView(textCell, newTableRowParams());
        }
        tableLayout.addView(tableRow);
    }

    private void createDataTable() {
        //   String info;
        for (indexR = 0; indexR < productos.size(); indexR++) {
            newRow();
            setDataCell(productos.get(indexR).getProducto().getProductoNombre(),indexR);
            setDataCell(productos.get(indexR).getProducto().getProductoPresentacion(),indexR);
            setDataCell(productos.get(indexR).getProducto().getProductoPrecio().toString(),indexR);
            setDataCell(productos.get(indexR).getCantidad().toString(),indexR);
            Double value=productos.get(indexR).getCantidad()*productos.get(indexR).getProducto().getProductoPrecio();
            setDataCell(Cart.subTotalItem(productos.get(indexR).getProducto().getProductoPrecio(),productos.get(indexR).getCantidad()),indexR);

            tableLayout.addView(tableRow);
//            for(indexC=0;indexC<header.length;indexC++){
//                newCell();
//                Strin[] colums=data.get(indexR-1);
//                info=(indexC<colums.length)?colums[indexC]:"";
//                textCell.setText(info);
//                tableRow.addView(textCell,newTableRowParams());
//
//            }
        }
    }

    private void setDataCell(String value,int index) {
        newCell();
        textCell.setText(value);
        if (index%2 == 0){
            textCell.setBackgroundColor(Color.LTGRAY);
        }
        tableRow.addView(textCell, newTableCellParams());
    }


    private TableRow.LayoutParams newTableRowParams() {
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.setMargins(1, 1, 1, 1);
        params.weight = 1;
        return params;
    }
    private TableRow.LayoutParams newTableCellParams() {
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.setMargins(1, 0, 1, 0);
        params.weight = 1;
        return params;
    }

}

package com.debuggerx.pidilitesaleshandler.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.debuggerx.pidilitesaleshandler.R;

import java.util.ArrayList;
import java.util.Arrays;

import static com.debuggerx.pidilitesaleshandler.R.id.NewEditText;

public class BigItemListActivity extends AppCompatActivity {

    String[] items;
    ArrayList<String> listitemsBI;
    ArrayAdapter<String> adapter;
    ListView listVwBI;
    EditText editTextBI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_item_list);
        listVwBI = (ListView) findViewById(R.id.Bigitemlistview);
        editTextBI= (EditText) findViewById(R.id.shppngtxtSearch);

        initList();
        removeItem();


        //to refill the list with selected items
        editTextBI.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().isEmpty()) {
                    //refresh listView
                    initList();
                    removeItem();

                } else {
                    //search item
                    initList();
                    searchItem(charSequence.toString());
                    removeItem();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });


        listVwBI.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                String itmnm = String.valueOf(adapterView.getItemAtPosition(pos));
                setItemAmount(itmnm);
            }
        });
    }

    public void initList(){
        items = callItems();
        listitemsBI = new ArrayList<>(Arrays.asList(items));
        adapter=new ArrayAdapter<>(BigItemListActivity.this,R.layout.layout_list_item_big_items,R.id.txtitemBI,listitemsBI);
        listVwBI.setAdapter(adapter);
    }

    private String[] callItems() {
        //Reading from SD BigItemList.txt
        //String ItemAllList = ReadFromSD ( "/PidiliteSH/Appdata" , "BigItemList.txt" );
        //return ItemAllList.split("\n");

        String[] ItemAllList = {"Fevikwik ","" +
                                "MSeal Small Packs ",
                                "Steelgrip ",
                                "Motomax ",
                                "Solvent Cement ",
                                "PTFE ",
                                "FK Bulk Packs ",
                                "Mseal Bulk Pack ",
                                "Motomax",
                                "RTV",
                                "Fevitite Rapid",
                                "Zorrik"    };
        return ItemAllList;
    }

    private void removeItem() {
        String[] z =  getSharedPreferences("Temp_list_variable", Context.MODE_PRIVATE).getString("Itemlist", "There is no Item selected").split("\n");
        for (int i =0 ; i<z.length ;i++){
            String p = z[i].split("\t:\t")[0];
            for(String item: items){
                if(item.matches(p)){
                    listitemsBI.remove(item);
                }
            }
        }


        adapter.notifyDataSetChanged();
    }

    public void setItemAmount(final String Itmesname){
        LayoutInflater inflater = LayoutInflater.from(BigItemListActivity.this);
        View subView = inflater.inflate(R.layout.layout_edittext,null);
        final EditText etAddAmountET=(EditText) subView.findViewById(NewEditText);
        etAddAmountET.setHint("Amount");
        etAddAmountET.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Amount");
        builder.setMessage("Enter Total Amount of Items\n\t(" + Itmesname + ") :\n\t\t(in numbers)");
        builder.setView(subView);
        AlertDialog alertDialog = builder.create();

        final String[] x = new String[1];
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (etAddAmountET.getText().toString().isEmpty()) {       //checks if empty
                    dialog.cancel();
                    Toast.makeText(BigItemListActivity.this, "Enter Amount", Toast.LENGTH_SHORT).show();
                } else {
                    if((etAddAmountET.getText().toString().matches("\\d+")) &&(!etAddAmountET.getText().toString().matches("0"))){
                        if (getSharedPreferences("Temp_list_variable", Context.MODE_PRIVATE).getString("Itemlist", "There is no Item selected").matches("There is no Item selected")) {
                            getSharedPreferences("Temp_list_variable", Context.MODE_PRIVATE).edit().putString("Itemlist", Itmesname + "\t:\t" + etAddAmountET.getText().toString()).apply();
                        } else {
                            String x = getSharedPreferences("Temp_list_variable", Context.MODE_PRIVATE).getString("Itemlist", "There is no Item selected");
                            getSharedPreferences("Temp_list_variable", Context.MODE_PRIVATE).edit().putString("Itemlist", x + "\n" + Itmesname + "\t:\t" + etAddAmountET.getText().toString()).apply();
                        }
                        finish();
                    }else{
                        dialog.cancel();
                        Toast.makeText(BigItemListActivity.this, "Enter Valid Numeric characters only", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
        //alertDialog.show();
    }


    private void searchItem(String s) {
        String s1 = s.substring(0, 1).toUpperCase();//convertss String's first letter to upperCase
        String s2 = s1 + s.substring(1);
        for(String item: items){
            if((!item.contains(s))&&(!item.contains(s2))){
                listitemsBI.remove(item);
            }
        }
        adapter.notifyDataSetChanged();
    }
}

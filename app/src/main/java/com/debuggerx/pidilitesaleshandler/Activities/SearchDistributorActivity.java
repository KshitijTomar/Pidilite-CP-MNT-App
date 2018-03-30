package com.debuggerx.pidilitesaleshandler.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
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

public class SearchDistributorActivity extends AppCompatActivity {

    String[] items;
    ArrayList<String> listitems;
    ArrayAdapter<String> adapter;
    ListView listVw;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_distributor);
        listVw = (ListView) findViewById(R.id.listview);
        editText = (EditText) findViewById(R.id.txtSearch);
        getSharedPreferences("Temp_list_variable", Context.MODE_PRIVATE).edit().putString("Distributor", "There is no distributor saved").apply();

        initList();

        //to refill the list with selected items
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")) {
                    //refresh listView
                    initList();
                } else {
                    //search item
                    initList();
                    searchItem(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        listVw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                String dis = String.valueOf(adapterView.getItemAtPosition(pos));
                if (dis.matches("There is no distributor saved")) {     //if clicked entry matches with "There is no distributor saved",  noting happens
                    Toast.makeText(SearchDistributorActivity.this, "Select a valid Distributor or\nAdd a new Distributor", Toast.LENGTH_SHORT).show();
                } else {
                    //item stored in TEMP variable via SharedPreferences
                    getSharedPreferences("Temp_list_ariable", Context.MODE_PRIVATE).edit().putString("Distributor", dis).apply();;
                    Intent intent = new Intent(new Intent(SearchDistributorActivity.this,SearchLocationActivity.class));
                    startActivity(intent);
                    finish();
                }
            }
        });

        //to del a particular item on the list
        listVw.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
                String dis = String.valueOf(adapterView.getItemAtPosition(pos));
                if (!dis.matches("There is no distributor saved")){
                    DelDis((int) id);
                }
                return true;
            }
        });
    }


    //method used to refresh ListView
    public void initList(){
        //call items
        items = callItems();
        listitems = new ArrayList<>(Arrays.asList(items));
        adapter=new ArrayAdapter<>(this,R.layout.layout_list_item_distributor,R.id.txtitemLO,listitems);
        listVw.setAdapter(adapter);
    }

    private String[] callItems() {
        //using Shared Preference to call the complete string of List of distributor
        SharedPreferences sharedPref = getSharedPreferences("DistributorList", Context.MODE_PRIVATE);
        String DistributorAllList= sharedPref.getString("DistributorString","There is no distributor saved");
        String[] DistributorListItems = DistributorAllList.split("\n");
        return DistributorListItems;
    }

    private void searchItem(String s) {
        String s1 = s.substring(0, 1).toUpperCase();//convertss String's first letter to upperCase
        String s2 = s1 + s.substring(1);
        for(String item: items){
            if((!item.contains(s))&&(!item.contains(s2))){
                listitems.remove(item);
            }
        }
        adapter.notifyDataSetChanged();
    }

    //Creates a Alert-Dialog Box for a New Distributor to enter
    public void addDistributor(View v){
        LayoutInflater inflater = LayoutInflater.from(SearchDistributorActivity.this);
        View subView = inflater.inflate(R.layout.layout_edittext,  null);
        final EditText etNewDistributorET=(EditText) subView.findViewById(NewEditText);
        etNewDistributorET.setHint("Distributor's Name");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Distributor");
        builder.setMessage("Add a new Distributor");
        builder.setView(subView);
        AlertDialog alertDialog = builder.create();

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (etNewDistributorET.getText().toString().isEmpty()) {       //checks if empty
                    dialog.cancel();
                } else if(etNewDistributorET.getText().toString().contains(",")){
                    Toast.makeText(SearchDistributorActivity.this, "Do not use Comma(,)", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }else{
                    String preDistributors = getSharedPreferences("DistributorList", Context.MODE_PRIVATE).getString("DistributorString", "There is no distributor saved");
                    String nwLoc = etNewDistributorET.getText().toString();        //first letter Capital
                    String s1 = nwLoc.substring(0, 1).toUpperCase();
                    String nwCLOC = s1 + nwLoc.substring(1);                    //capitalized variable
                    String finalDistributors = preDistributors + "\n" + nwCLOC ;
                    if (preDistributors.matches("There is no distributor saved")) {
                        getSharedPreferences("DistributorList", Context.MODE_PRIVATE).edit().
                                putString("DistributorString", nwCLOC).apply();
                    } else {
                        getSharedPreferences("DistributorList", Context.MODE_PRIVATE).edit().
                                putString("DistributorString", finalDistributors).apply();
                    }
                    initList();
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


    // if long pressed on an entry, crats a dialog box, asking to del this Distributor
    public void DelDis(int id){
        final AlertDialog.Builder builderAl = new AlertDialog.Builder(SearchDistributorActivity.this);
        SharedPreferences sharedPref = getSharedPreferences("DistributorList", Context.MODE_PRIVATE);
        String DistributorAllList= sharedPref.getString("DistributorString","There is no Distributor saved");
        String[] DistributorListItems = DistributorAllList.split("\n");

        String Strmsg = "Do you want to delete : "+ DistributorListItems[id];
        builderAl.setMessage(Strmsg);
        builderAl.setCancelable(true);

        String DistributorListnew = "";
        int i=0, l=DistributorListItems.length;
        while( i < l ){
            if (i != id){
                if (i == 0){
                    DistributorListnew = DistributorListItems[i];
                }else{
                    if(DistributorListnew.matches(""))
                    {
                        DistributorListnew = DistributorListItems[i];
                    }else{
                        DistributorListnew = DistributorListnew + "\n" + DistributorListItems[i];
                    }
                }
            }
            i=i+1;
        }

        //Positive Button For Interface
        final String finalDistributorListnew;
        if (DistributorListnew.matches("")){
            finalDistributorListnew = "There is no distributor saved";
        }else {
            finalDistributorListnew = DistributorListnew;
        }
        final String finalfinalDistributorListnew = finalDistributorListnew;
        builderAl.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getSharedPreferences("DistributorList", Context.MODE_PRIVATE).edit().
                        putString("DistributorString", finalfinalDistributorListnew).apply();
                initList();
            }
        });

        //Negative button for interface
        builderAl.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog= builderAl.create();
        alertDialog.show();
    }

}

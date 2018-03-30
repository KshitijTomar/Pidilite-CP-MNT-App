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

public class SearchLocationActivity extends AppCompatActivity {

    String[] items;
    ArrayList<String> listitems;
    ArrayAdapter<String> adapter;
    ListView listVw;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);
        listVw = (ListView) findViewById(R.id.listview);
        editText = (EditText) findViewById(R.id.txtSearch);
        getSharedPreferences("Temp_list_variable", Context.MODE_PRIVATE).edit().putString("Location", "There is no location saved").apply();

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
                String loc = String.valueOf(adapterView.getItemAtPosition(pos));
                if (loc.matches("There is no location saved")) {
                    getSharedPreferences("Temp_list_variable", Context.MODE_PRIVATE).edit().putString("Location", "There is no location saved").apply();
                    Toast.makeText(SearchLocationActivity.this, "Select a valid Location or Add a new location", Toast.LENGTH_SHORT).show();
                } else {
                    //item stored in TEMP variable via SharedPreferences
                    getSharedPreferences("Temp_list_variable", Context.MODE_PRIVATE).edit().putString("Location", loc).apply();
                    Intent intent = new Intent(new Intent(SearchLocationActivity.this,SearchVendorActivity.class));
                    startActivity(intent);
                    finish();
                }
            }
        });

        //to del a particular item on the list
        listVw.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
                String loc = String.valueOf(adapterView.getItemAtPosition(pos));
                if (!loc.matches("There is no location saved")){
                    DelLoc((int) id);
                    return true;
                }else{
                    return false;
                }

            }
        });
    }


    //method used to refresh ListView
    public void initList(){
        //call items
        items = callItems();
        listitems = new ArrayList<>(Arrays.asList(items));
        adapter=new ArrayAdapter<>(this,R.layout.layout_list_item_location,R.id.txtitemLO,listitems);
        listVw.setAdapter(adapter);
    }

    private String[] callItems() {
        //using Shared Preference to call the complete string of List of Locations
        SharedPreferences sharedPref = getSharedPreferences("LocationList", Context.MODE_PRIVATE);
        String LocationAllList= sharedPref.getString("LocationString","There is no location saved");
        String[] LocationListItems = LocationAllList.split("\n");
        return LocationListItems;
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

    //Creates a Alert-Dialog Box for a New Location to enter
    public void addLocation(View v){
        LayoutInflater inflater = LayoutInflater.from(SearchLocationActivity.this);
        View subView = inflater.inflate(R.layout.layout_edittext,  null);
        final EditText etNewLocationET=(EditText) subView.findViewById(NewEditText);
        etNewLocationET.setHint("Route/Location");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Route/Location");
        builder.setMessage("Add Route/location of the shop");
        builder.setView(subView);
        AlertDialog alertDialog = builder.create();

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (etNewLocationET.getText().toString().isEmpty()) {       //checks if empty
                    dialog.cancel();
                } else if(etNewLocationET.getText().toString().contains(",")){
                    Toast.makeText(SearchLocationActivity.this, "Do not use Comma(,)", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }else{
                    String preLocations = getSharedPreferences("LocationList", Context.MODE_PRIVATE).getString("LocationString", "There is no location saved");
                    String nwLoc = etNewLocationET.getText().toString();        //first letter Capital
                    String s1 = nwLoc.substring(0, 1).toUpperCase();
                    String nwCLOC = s1 + nwLoc.substring(1);                    //capitalized variable
                    String finalLocations = preLocations + "\n" + nwCLOC ;
                    if (preLocations.matches("There is no location saved")) {
                        getSharedPreferences("LocationList", Context.MODE_PRIVATE).edit().
                                putString("LocationString", nwCLOC).apply();
                    } else {
                        getSharedPreferences("LocationList", Context.MODE_PRIVATE).edit().
                                putString("LocationString", finalLocations).apply();
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


    public void DelLoc(int id){
        final AlertDialog.Builder builderAl = new AlertDialog.Builder(SearchLocationActivity.this);
        SharedPreferences sharedPref = getSharedPreferences("LocationList", Context.MODE_PRIVATE);
        String LocationAllList= sharedPref.getString("LocationString","There is no location saved");
        String[] LocationListItems = LocationAllList.split("\n");

        String Strmsg = "Do you want to delete : "+ LocationListItems[id];
        builderAl.setMessage(Strmsg);
        builderAl.setCancelable(true);

        String LocationListnew = "";
        int i=0, l=LocationListItems.length;
        while( i < l ){
            if (i != id){
                if (i == 0){
                    LocationListnew = LocationListItems[i];
                }else{
                    if(LocationListnew.matches(""))
                    {
                        LocationListnew = LocationListItems[i];
                    }else{
                        LocationListnew = LocationListnew + "\n" + LocationListItems[i];
                    }
                }
            }
            i=i+1;
        }

        //Positive Button For Interface
        final String finalLocationListnew;
        if (LocationListnew.matches("")){
            finalLocationListnew = "There is no location saved";
        }else {
            finalLocationListnew = LocationListnew;
        }
        final String finalfinalLocationListnew = finalLocationListnew;
        builderAl.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getSharedPreferences("LocationList", Context.MODE_PRIVATE).edit().
                        putString("LocationString", finalfinalLocationListnew).apply();
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

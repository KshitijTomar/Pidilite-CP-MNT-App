package com.debuggerx.pidilitesaleshandler.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.debuggerx.pidilitesaleshandler.R;

import java.util.ArrayList;
import java.util.Arrays;

import static com.debuggerx.pidilitesaleshandler.R.id.NewEditText1;
import static com.debuggerx.pidilitesaleshandler.R.id.NewEditText2;


public class SearchVendorActivity extends AppCompatActivity {

    String[] items;
    ArrayList<String> listitemsVn;
    ArrayAdapter<String> adapter;
    ListView listVwVn;
    EditText editTextVn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_vendor);
        listVwVn = (ListView) findViewById(R.id.vendorlistview);
        editTextVn = (EditText) findViewById(R.id.vendortxtSearch);
        getSharedPreferences("Temp_list_variable", Context.MODE_PRIVATE).edit().putString("Vendor", "There is no Vendor saved").apply();

        initList();

        //to refill the list with selected items
        editTextVn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {}
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
        });


        listVwVn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                String ven = String.valueOf(adapterView.getItemAtPosition(pos));
                if (ven.matches("There is no Vendor saved")) {
                    Toast.makeText(SearchVendorActivity.this, "Select a valid Vendor or\nAdd a new Vendor", Toast.LENGTH_SHORT).show();
                } else {
                    //item stored in TEMP vairable via SharedPreferences
                    getSharedPreferences("Temp_list_variable", Context.MODE_PRIVATE).edit().putString("Vendor", ven).apply();
                    Intent intent = new Intent(new Intent(SearchVendorActivity.this, FinalListActivity.class));
                    startActivity(intent);
                    finish();
                }
            }
        });

        //to del a particular item on the list
        listVwVn.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
                String ven = String.valueOf(adapterView.getItemAtPosition(pos));
                if (!ven.matches("There is no Vendor saved")) {
                    DelVen(ven);
                    initList();
                    return true;
                }else{
                    return false;
                }
            }
        });
    }

    //method used to refresh ListView
    public void initList() {
        items = callItems();
        listitemsVn = new ArrayList<>(Arrays.asList(items));
        adapter = new ArrayAdapter<>(SearchVendorActivity.this, R.layout.layout_list_item_vendor, R.id.txtitemVE, listitemsVn);
        listVwVn.setAdapter(adapter);

        //To display specific vendors
        String Temploc = getSharedPreferences("Temp_list_variable", Context.MODE_PRIVATE).getString("Location", "There is no Location saved");
        searchItem(Temploc);
        if (listitemsVn.isEmpty()) {
            listitemsVn.add("There is no Vendor saved");
        }
    }

    private String[] callItems() {
        String VendorAllList = getSharedPreferences("VendorList", Context.MODE_PRIVATE).getString("VendorString","There is no Vendor saved" );
        return VendorAllList.split("\n");
    }

    private void searchItem(String s) {
        for (String item : items) {
            if (!item.contains(s)) {
                listitemsVn.remove(item);
            }
        }
        adapter.notifyDataSetChanged();
    }

    //Creates a Alert-Dialog Box for a New Vendor to enter
    public void addVendor(View v) {
        //add layout of addvendor(alert)
        LayoutInflater inflater = LayoutInflater.from(SearchVendorActivity.this);
        View subView = inflater.inflate(R.layout.layout_edittexts2, null);

        //initialize editText in layout
        final EditText etNewVendornameET = (EditText) subView.findViewById(NewEditText1);
        final EditText etNewVendormobET = (EditText) subView.findViewById(NewEditText2);
        etNewVendornameET.setHint("Vendor's Name");
        etNewVendormobET.setHint("Vendor's Contact Number");
        //Alert
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Vendor");
        builder.setMessage("Add Vendor's Name and \nContact number (10-Digits) :");
        builder.setView(subView);
        AlertDialog alertDialog = builder.create();

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if ( (etNewVendornameET.getText().toString().isEmpty()) || (etNewVendormobET.getText().toString().isEmpty()) ) {       //checks if empty
                    dialog.cancel();
                    Toast.makeText(SearchVendorActivity.this, "Enter data in both the boxes", Toast.LENGTH_SHORT).show();
                } else if( (etNewVendormobET.getText().toString().length()==10) && (etNewVendormobET.getText().toString().matches("\\d+")) ) {
                    if((etNewVendornameET.getText().toString().contains(","))||(etNewVendormobET.getText().toString().contains(","))){
                        Toast.makeText(SearchVendorActivity.this, "Do not use Comma(,)", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }else {
                        String Temploc = getSharedPreferences("Temp_list_variable", Context.MODE_PRIVATE).getString("Location", "There is no location saved");
                        String preVendors = getSharedPreferences("VendorList", Context.MODE_PRIVATE).getString("VendorString", "There is no Vendor saved");
                        //String preVendors = ReadFromSD("/PidiliteSH/Appdata", "LOCVEND.txt");
                        String nwVen = etNewVendornameET.getText().toString() + ", " + Temploc + ", (" + etNewVendormobET.getText().toString() + ")";        //first letter Capital
                        String s1 = nwVen.substring(0, 1).toUpperCase();
                        String nwCVEN = s1 + nwVen.substring(1);                    //capitalized variable
                        String finalVendors = preVendors + "\n" + nwCVEN;
                        if (preVendors.matches("There is no vendor saved")) {
                            getSharedPreferences("VendorList", Context.MODE_PRIVATE).edit().putString("VendorString", nwCVEN).apply();
//                        SaveToSD(nwCVEN, "/PidiliteSH/Appdata", "LOCVEND.txt");
                        } else {
                            getSharedPreferences("VendorList", Context.MODE_PRIVATE).edit().putString("VendorString", finalVendors).apply();
//                        SaveToSD(finalVendors, "/PidiliteSH/Appdata", "LOCVEND.txt");
                        }
                        initList();
                    }
                }else {
                    dialog.cancel();
                    Toast.makeText(SearchVendorActivity.this, "Enter a valid contact number \n(10 digit number)", Toast.LENGTH_SHORT).show();
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


    public void DelVen(String ven) {
        final AlertDialog.Builder builderAl = new AlertDialog.Builder(SearchVendorActivity.this);
        
        
        String VendorAllList = getSharedPreferences("VendorList", Context.MODE_PRIVATE).getString("VendorString","There is no Vendor saved" );
        String[] VendorListItems = VendorAllList.split("\n");

        String Strmsg = "Do you want to delete :\n\t" + ven;
        builderAl.setMessage(Strmsg);
        builderAl.setCancelable(true);

        String VendorListnew = "";
        int i = 0, l = VendorListItems.length;
        while (i < l) {
            if (!VendorListItems[i].split(",")[0].matches(ven.split(",")[0])) {
                if (i == 0) {
                    VendorListnew = VendorListItems[i];
                } else {
                    VendorListnew = VendorListnew + "\n" + VendorListItems[i];
                }
            }
            i++;
        }
        final String finalVendorListnew = VendorListnew;
        
        //Positive Button For Interface
        builderAl.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getSharedPreferences("VendorList", Context.MODE_PRIVATE).edit().putString("VendorString", finalVendorListnew).apply();
                initList();
                Toast.makeText(SearchVendorActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
            }
        });

        //Negative button for interface
        builderAl.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = builderAl.create();
        alertDialog.show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SearchVendorActivity.this, SearchLocationActivity.class);
        startActivity(intent);
        finish();
    }
}
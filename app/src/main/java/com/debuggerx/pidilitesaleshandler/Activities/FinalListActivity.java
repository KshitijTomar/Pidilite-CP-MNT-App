package com.debuggerx.pidilitesaleshandler.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.debuggerx.pidilitesaleshandler.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.Arrays;


import static com.debuggerx.pidilitesaleshandler.R.id.NewEditText;


public class FinalListActivity extends AppCompatActivity {

    String[] items;
    ArrayList<String> listitemsFL;
    ArrayAdapter<String> adapter;
    ListView listVwFL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_list);
        listVwFL = (ListView) findViewById(R.id.Finallistview);

        TextView vnnam=(TextView) findViewById(R.id.VnNametxt);
        TextView vnloca=(TextView) findViewById(R.id.VnLoctxt);
        TextView disname=(TextView) findViewById(R.id.DisNametxt);

        disname.setText(getSharedPreferences("Temp_list_variable", Context.MODE_PRIVATE).getString("Distributor", "There is no distributor saved"));
        vnloca.setText(getSharedPreferences("Temp_list_variable", Context.MODE_PRIVATE).getString("Location", "There is no location saved"));
        vnnam.setText(getSharedPreferences("Temp_list_variable", Context.MODE_PRIVATE).getString("Vendor", "There is no location saved").split(",")[0]);

        getSharedPreferences("Temp_list_variable", Context.MODE_PRIVATE).edit().putString("Itemlist", "There is no Item selected").apply();

        initList();

        listVwFL.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                String itemselected = String.valueOf(adapterView.getItemAtPosition(pos));
                if (!itemselected.matches( "There is no Item selected")) {
                    String name_of_item_selected = itemselected.split("\t:\t")[0];
                    changeItemQuantity( name_of_item_selected , pos);
                }
            }
        });

        listVwFL.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
                String itemselected = String.valueOf(adapterView.getItemAtPosition(pos));
                Deleteentry(itemselected,pos);
                return true;
            }
        });
    }

    public void changeItemQuantity(final String name_of_item_selected, final int pos){
        LayoutInflater inflater = LayoutInflater.from(FinalListActivity.this);
        View subView = inflater.inflate(R.layout.layout_edittext,null);
        final EditText etAddQuantityET=(EditText) subView.findViewById(NewEditText);
        etAddQuantityET.setHint("Quantity");
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Quantity");
        builder.setMessage("Enter Quantity of Items(" + name_of_item_selected + ") :\n(in numbers)");
        builder.setView(subView);
        AlertDialog alertDialog = builder.create();

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (etAddQuantityET.getText().toString().isEmpty()) {       //checks if empty
                    dialog.cancel();
                    Toast.makeText(FinalListActivity.this, "Enter Quantity", Toast.LENGTH_SHORT).show();
                } else {
                    if(etAddQuantityET.getText().toString().matches("0")){
                        removeItem(pos);
                    }else{
                        if( etAddQuantityET.getText().toString().matches("\\d+") ) {
                            listitemsFL.remove(pos);                                                                         //removing the to be changed listitem
                            listitemsFL.add(name_of_item_selected + "\t:\t"+ etAddQuantityET.getText().toString());             //adding the changd item
                            String fi = ListToString();                                                                   //saving to ItemlistShardepref
                            getSharedPreferences("Temp_list_variable", Context.MODE_PRIVATE).edit().putString("Itemlist", fi).apply();
                        }else{
                            dialog.cancel();
                            Toast.makeText(FinalListActivity.this, "Enter Numeric characters only", Toast.LENGTH_SHORT).show();
                        }
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

    public void initList(){
        items=  getSharedPreferences("Temp_list_variable", Context.MODE_PRIVATE).getString("Itemlist", "There is no Item selected").split("\n");;
        listitemsFL = new ArrayList<>(Arrays.asList(items));
        adapter=new ArrayAdapter<>(FinalListActivity.this,R.layout.layout_list_item_final_list,R.id.txtitemFL,listitemsFL);
        listVwFL.setAdapter(adapter);
    }


    public void removeItem(int pos) {
        listitemsFL.remove(pos);
        if(listitemsFL.isEmpty()){
            listitemsFL.add("There is no Item selected");
        }
        String fi = ListToString();
        getSharedPreferences("Temp_list_variable", Context.MODE_PRIVATE).edit().putString("Itemlist", fi).apply();
        initList();
    }

    public String ListToString() {
        final String[] x = {"" };
        for (int k=0;k<listitemsFL.size();k++){
            if (k==0){
                x[0] = listitemsFL.get(k);
            }else{
                x[0] = x[0] + "\n" + listitemsFL.get(k);
            }
        }
        return x[0];
    }


    @Override
    protected void onResume() {
        super.onResume();
        initList();
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(FinalListActivity.this);
        builder.setMessage("Are you sure you want to Discard?");
        builder.setCancelable(true);
        //Positive Button For Interface
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(FinalListActivity.this,Main2Activity.class);
                startActivity(intent);
                finish();
            }
        });
        //Negative button for interface
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog= builder.create();
        alertDialog.show();
    }



    public void AddMoreItems(View view){
        Intent intent = new Intent(FinalListActivity.this,BigItemListActivity.class);
        startActivity(intent);
    }

//    public void SubmitFinalList(View view){
//        if (listitemsFL.get(0).matches("There is no Item selected")){
//            Toast.makeText(this, "Check/Fill the Quantity of Items ", Toast.LENGTH_SHORT).show();
//        }else{
//            int i =0;
//            SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
//            String usrname= sharedPref.getString("username","Name");
//            String usrid= sharedPref.getString("userid","ID");
//            String usrcontact= sharedPref.getString("usercontact","Contact");
//            String usremail= sharedPref.getString("useremail","E-mail");
//
//            String userDetails= usrname +","+ usrid +","+ usrcontact +","+ usremail;
//
//            String date = new SimpleDateFormat("yyyy-MM-dd,hh-mm-ss").format(new Date());
//
//            String disname = getSharedPreferences("Temp_list_variable", Context.MODE_PRIVATE).getString("Distributor", "There is no distributor saved");
//            String vnnam = getSharedPreferences("Temp_list_variable", Context.MODE_PRIVATE).getString("Vendor", "There is no location saved");
//            String datafirst= date + "," +disname+"," + vnnam ;
//
//            for(int k=0;k<listitemsFL.size();k++){
//                String datatobestored = datafirst + "," + listitemsFL.get(k).split("\t:\t")[0] + "," + listitemsFL.get(k).split("\t:\t")[1] + "," + userDetails +"\n";
//                String x = Readfromsd("/PidiliteSH/Database" , "DB-"+usrname+".txt");
//                if (x.matches("File Does not Exist")){
//                    SaveToSD(datatobestored ,"" , "DB-"+usrname+".csv");
//                    SaveToSD(datatobestored ,"" , "DB-"+usrname+".txt");
//                }else{
//                    SaveToSD(datatobestored + "\n" + x,"" , "DB-"+usrname+".csv" );
//                    SaveToSD(datatobestored + "\n" + x,"" , "DB-"+usrname+".txt" );
//                }
//
//            }
//            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
//            finish();
//        }
//    }


    public void SubmitFinalList(View view){
        if (listitemsFL.get(0).matches("There is no Item selected")){
            Toast.makeText(this, "Check/Fill the Quantity of Items ", Toast.LENGTH_SHORT).show();
        }else{
            SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            String usrname= sharedPref.getString("username","Name");

            String disname = getSharedPreferences("Temp_list_variable", Context.MODE_PRIVATE).getString("Distributor", "There is no distributor saved");
            String vnnam = getSharedPreferences("Temp_list_variable", Context.MODE_PRIVATE).getString("Vendor", "There is no location saved");
            String datafirst= disname+ "," + vnnam ;

            //to get item and qualtity from the list in a dataentry
            String dataentry = listitemsFL.get(0).split("\t:\t")[0] + "," + listitemsFL.get(0).split("\t:\t")[1];        // gives Item:Quantity
            for(int k=1;k<listitemsFL.size();k++) {
                dataentry = dataentry +","+listitemsFL.get(k).split("\t:\t")[0] + "," + listitemsFL.get(k).split("\t:\t")[1] + "\n";
            }

            int i=0;
            String x = Readfromsd("/PidiliteSH/Database" , "DB-"+usrname+".txt");           //Reading previous data from file
            boolean matchdone = false;
            if (x.matches("File Does not Exist")){                                          //if file not exists, save data directly to the file
                SaveToSD(datafirst+","+dataentry ,"" , "DB-"+usrname+".csv");
                SaveToSD(datafirst+","+dataentry ,"" , "DB-"+usrname+".txt");
            }else{                                                                          //if exists then
                String[] y=x.split("\n");                                                   //seperate the lines into an array
                String New_String = null;
                for(i =0;i<y.length;i++){                                                   //traverse all
                    if(     ((y[i].split(",")[0]).matches(disname)) &&                      //if Dist matches
                            ((y[i].split(",")[1]).contains(vnnam.split(",")[0])) &&         //if location matches
                            ((y[i].split(",")[2]).contains(vnnam.split(",")[1])) &&         //if vendors name matches
                            ((y[i].split(",")[3]).contains(vnnam.split(",")[2])) ){         //if vendo's contact number matches
                        if(New_String==null) {    New_String = y[i] + "," + dataentry; }
                        else{     New_String = New_String + "\n" + y[i] + "," + dataentry ; }
                        matchdone=true;
                    }else{
                        if(New_String==null) {    New_String = y[i]  ;     }
                        else{       New_String= New_String+ "\n" + y[i] ;     }
                    }
                }
                if(i==y.length && !matchdone){
                    if(New_String==null) {      New_String = datafirst + "," + dataentry ;    }
                    else{   New_String = New_String + "\n" + datafirst + "," + dataentry ;    }
                }
                SaveToSD(New_String , "" , "DB-"+usrname+".txt" );
                SaveToSD(New_String , "" , "DB-"+usrname+".csv" );
            }


            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    public void Deleteentry(String items_name, final int position) {
        final AlertDialog.Builder builderAl = new AlertDialog.Builder(FinalListActivity.this);
        String Strmsg = "Do you want to delete : " + items_name;
        builderAl.setMessage(Strmsg);
        builderAl.setCancelable(true);

        //Positive Button For Interface
        builderAl.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                removeItem(position);
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



//    public void SaveToSD (String data_to_be_saved, String Folder_name, String file_name){
//        String state;
//        state= Environment.getExternalStorageState();
//        if(Environment.MEDIA_MOUNTED.equals(state)){
//            File Root= Environment.getExternalStorageDirectory();
//            File dir=new File(Root.getAbsolutePath()+Folder_name);
//            boolean isDirectoryCreated=dir.exists();
//            if (!isDirectoryCreated) {
//                isDirectoryCreated= dir.mkdir();
//            }
//            if(isDirectoryCreated) {
//                File file =new File(dir,file_name);
//                String Message=data_to_be_saved;
//                try{
//                    FileWriter fileOutputStream= new FileWriter(file);
//                    fileOutputStream.write(Message);
//                    fileOutputStream.flush();
//                    fileOutputStream.close();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                    Toast.makeText(this, "Data not saved (FileNotFoundException)", Toast.LENGTH_SHORT).show();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Toast.makeText(this, "Data not saved (IOException)", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//        else{
//            Toast.makeText(getApplicationContext(), "Sd card not Found", Toast.LENGTH_SHORT).show();
//        }
//    }


    public void SaveToSD (String data_to_be_saved, String Folder_name, String file_name){
        String state;
        state= Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            File Root= Environment.getExternalStorageDirectory();
            File file =new File(Root,file_name);
            String Message=data_to_be_saved;
            try{
                FileWriter fileOutputStream= new FileWriter(file);
                fileOutputStream.write(Message);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Data not saved (FileNotFoundException)", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Data not saved (IOException)", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "Sd card not Found", Toast.LENGTH_SHORT).show();
        }
    }

    public String Readfromsd(String Folder_name, String file_name){
//        File Root= Environment.getExternalStorageDirectory();
//        File dir=new File(Root.getAbsolutePath()+"/"+Folder_name);
//        File file =new File(dir,file_name);
        File file =new File(Environment.getExternalStorageDirectory(),file_name);
        String Message;
        try {
            FileInputStream fileinputstream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileinputstream);
            BufferedReader bufferReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            while ((Message=bufferReader.readLine())!=null){
                stringBuffer.append(Message + "\n");
            }
            return stringBuffer.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "File Does not Exist" ;
        } catch (IOException e) {
            e.printStackTrace();
            return "File Does not Exist" ;
        }
    }

}


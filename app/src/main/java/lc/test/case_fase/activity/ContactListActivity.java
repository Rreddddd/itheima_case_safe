package lc.test.case_fase.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lc.test.case_fase.R;

public class ContactListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_list);

        List<Map<String, String>> contactRows = getContactRows();
        if(contactRows!=null && contactRows.size()>0){
            ListView lv_contract_item = findViewById(R.id.lv_contract_item);
            lv_contract_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent();
                    intent.putExtra("number",contactRows.get(position).get("number"));
                    setResult(0,intent);
                    finish();
                }
            });
            lv_contract_item.setAdapter(new BaseAdapter() {
                @Override
                public int getCount() {
                    return contactRows.size();
                }

                @Override
                public Map<String, String> getItem(int position) {
                    return contactRows.get(position);
                }

                @Override
                public long getItemId(int position) {
                    return 0;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    Map<String, String> item = getItem(position);
                    View view = View.inflate(getApplicationContext(), R.layout.item_contact_item, null);
                    ((TextView)view.findViewById(R.id.tv_name)).setText(item.get("name"));
                    ((TextView)view.findViewById(R.id.tv_number)).setText(item.get("number"));
                    return view;
                }
            });
        }
    }

    private List<Map<String,String>> getContactRows(){
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(Uri.parse("content://com.android.contacts/raw_contacts"),
                new String[]{"contact_id"},
                null,
                null,
                null);
        if(cursor!=null){
            List<Map<String,String>> rows=new ArrayList<Map<String,String>>();
            Map<String,String> row;
            String contactId;
            Cursor cursor1;
            while (cursor.moveToNext()){
                contactId=cursor.getString(0);
                cursor1 = contentResolver.query(Uri.parse("content://com.android.contacts/data"),
                        new String[]{"mimetype","data1"},
                        "raw_contact_id=?",
                        new String[]{contactId},
                        null);
                if(cursor1!=null){
                    row=new HashMap<String,String>();
                    while (cursor1.moveToNext()){
                        switch (cursor1.getString(0)){
                            case "vnd.android.cursor.item/phone_v2":
                                row.put("number",cursor1.getString(1));
                                break;
                            case "vnd.android.cursor.item/name":
                                row.put("name",cursor1.getString(1));
                                break;
                        }
                    }
                    cursor1.close();
                    rows.add(row);
                }
            }
            cursor.close();
            return rows;
        }
        return null;
    }
}

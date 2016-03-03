package com.nsu.app.dropdownlistviewdemo;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;

/**
 * Create By Anthony on 2016/2/15
 * demo of dropDownListView
 */
public class DropDownListViewDemo extends Activity {
    private LinkedList<String> listItems           = null;
    private DropDownListView listView            = null;
    private ArrayAdapter<String> adapter;

    private String[]             mStrings            = {"11111", "2222", "3333",
            "4444", "5555", "6666", "77777", "8888", "99999",
            "aaaaa", "bbbb", "cccc", "dddd", "eeee",};
    public static final int      MORE_DATA_MAX_COUNT = 3;
    public int                   moreDataCount       = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_down_list_view_demo);

        listView = (DropDownListView)findViewById(R.id.list_view);
        // set drop down listener
        listView.setOnDropDownListener(new DropDownListView.OnDropDownListener() {

            @Override
            public void onDropDown() {
                new GetDataTask(true).execute();
            }
        });

        // set on bottom listener
        listView.setOnBottomListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new GetDataTask(false).execute();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ToastUtils.show(context, R.string.drop_down_tip);
                Toast.makeText(DropDownListViewDemo.this,R.string.drop_down_tip,Toast.LENGTH_LONG).show();
            }
        });
        // listView.setShowFooterWhenNoMore(true);

        listItems = new LinkedList<String>();
        listItems.addAll(Arrays.asList(mStrings));
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);
    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        private boolean isDropDown;

        public GetDataTask(boolean isDropDown) {
            this.isDropDown = isDropDown;
        }

        @Override
        protected String[] doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
            return mStrings;
        }

        @Override
        protected void onPostExecute(String[] result) {

            if (isDropDown) {
                listItems.addFirst("Added after drop down");
                adapter.notifyDataSetChanged();

                // should call onDropDownComplete function of DropDownListView at end of drop down complete.
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
                listView.onDropDownComplete(getString(R.string.update_at) + dateFormat.format(new Date()));
            } else {
                moreDataCount++;
                listItems.add("Added after on bottom");
                adapter.notifyDataSetChanged();

                if (moreDataCount >= MORE_DATA_MAX_COUNT) {
                    listView.setHasMore(false);
                }

                // should call onBottomComplete function of DropDownListView at end of on bottom complete.
                listView.onBottomComplete();
            }

            super.onPostExecute(result);
        }
    }

}

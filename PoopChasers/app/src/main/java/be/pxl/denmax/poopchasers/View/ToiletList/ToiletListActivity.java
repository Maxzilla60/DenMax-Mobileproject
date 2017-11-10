package be.pxl.denmax.poopchasers.View.ToiletList;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import be.pxl.denmax.poopchasers.Model.ToiletAndDistance;
import be.pxl.denmax.poopchasers.Model.ToiletComment;
import be.pxl.denmax.poopchasers.R;
import be.pxl.denmax.poopchasers.View.ToiletDetail.ToiletDetailActivity;
import be.pxl.denmax.poopchasers.View.ToiletDetail.ToiletDetailAdapter;

public class ToiletListActivity extends AppCompatActivity {

    public class ToiletInfo{
        public int id;
        public String name;
        public float distance;

        public ToiletInfo(int id, String name, float distance){
            this.id = id;
            this.name = name;
            this.distance = distance;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toilet_list);

        setList();
    }

    private void setList() {
        ArrayList<Integer> ids = getIntent().getIntegerArrayListExtra("ids");
        ArrayList<String> names = getIntent().getStringArrayListExtra("names");
        ArrayList<Integer> distances = getIntent().getIntegerArrayListExtra("distances");

        ArrayList<ToiletInfo> toiletInfoList = new ArrayList<>();

        for(int i = 0; i < names.size() || i < distances.size(); i++){
            toiletInfoList.add(new ToiletInfo(ids.get(i), names.get(i), distances.get(i)));
        }

        ToiletListAdapter adapter = new ToiletListAdapter(toiletInfoList, getApplicationContext());
        ListView listView = (ListView) findViewById(R.id.toiletList);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ToiletInfo toiletInfo = (ToiletInfo) adapterView.getItemAtPosition(i);

                int id = toiletInfo.id;

                Intent intent = new Intent(getBaseContext(), ToiletDetailActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
    }
}

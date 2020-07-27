package net.along.fragonflyfm.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.along.fragonflyfm.R;
import net.along.fragonflyfm.adapter.SearchesAdapter;
import net.along.fragonflyfm.base.BaseFragment;
import net.along.fragonflyfm.base.DownloadData;
import net.along.fragonflyfm.entity.SearchesData;
import net.along.fragonflyfm.util.JSONUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 创建者 by:陈泰龙
 * <p>
 * 2020/7/1
 **/

public class SearchesFragment extends BaseFragment {
    private TextView tv_location;
    private View mRootView;
    private SearchView sv_location;
    private final static String TAG = "SearchesFragment";
    private List<SearchesData> mList;
    private SearchesAdapter mAdapter;
    private RecyclerView fmRecyclerView;
    private PopupWindow popupWindow;
    private ListView Region;
    private View choiceDialog;
    private JSONArray jsonData;
    private RelativeLayout clickChart;


    @Override
    protected View onSubViewLoaded(LayoutInflater inflater, ViewGroup container) {
        mRootView = inflater.inflate(R.layout.fragment_searches, container, false);
        initView();
        inDialog();
        showFM();
        return mRootView;
    }

    /**
     * 显示地区电台
     */
    private void showFM() {
        JSONArray fmItemJson= DownloadData.GetJSON();
        if (fmItemJson==null){
            Toast.makeText(getActivity(), "获取电台数据失败", Toast.LENGTH_SHORT).show();
            return;
        }
        Gson gson=new Gson();
        List<SearchesData> list=
                gson.fromJson(fmItemJson.toString(), new TypeToken<List<SearchesData>>(){}.getType());
        mAdapter=new SearchesAdapter(getContext(),list);
        GridLayoutManager manager=new GridLayoutManager(getActivity(),3);
        fmRecyclerView.setLayoutManager(manager);
        fmRecyclerView.setAdapter(mAdapter);
    }

    private void updateFM() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.upData();
                    }
                });
            }
        }, 1000);

    }


    /**
     * 地址选择
     */
    private void inDialog() {
        popupWindow = new PopupWindow();
        choiceDialog = View.inflate(getContext(), R.layout.choose_region_dialog, null);
        popupWindow.setContentView(choiceDialog);
        Region = choiceDialog.findViewById(R.id.dialog_list_view);
        List<String> province = new ArrayList<>();
        //获取地区数据
        jsonData = JSONUtils.getDistrict();
        if (jsonData == null) {
            Toast.makeText(getContext(), "获取地区数据失败", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "inDialog: 获取地区数据失败");
            return;
        }
        for (int i = 0; i < jsonData.length(); i++) {
            try {
                JSONObject data = jsonData.getJSONObject(i);
                province.add(data.getString("title"));
                Log.d(TAG, "inDialog: " + data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, province);
        Region.setAdapter(adapter);
        Region.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int i, long id) {
                tv_location.setText(province.get(i));
                Intent intent = new Intent(getActivity(), DownloadData.class);
                try {
                    JSONObject district = jsonData.getJSONObject(i);
                    intent.putExtra("provinceId", district.getInt("id"));
                    getActivity().startService(intent);
                    updateFM();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 组件的初始化
     */
    private void initView() {
        tv_location = mRootView.findViewById(R.id.fragment_searches_location);
        sv_location = mRootView.findViewById(R.id.fragment_searches_search);
        fmRecyclerView=mRootView.findViewById(R.id.fm_recycler_view);
        clickChart = mRootView.findViewById(R.id.fragment_searches_mask);
        tv_location.setOnClickListener(mOnClickListener);
        clickChart.setOnClickListener(mOnClickListener);
        mList = new ArrayList<>();
    }


    /**
     * 点击事件
     */
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fragment_searches_location:
                    showChooseProvinceWindow();
                    Log.d(TAG, "onClick: 你点击了选择地区事件");
                    break;
                case R.id.fragment_searches_mask:
                    shutDialog();
                    break;
            }
        }
    };

    /**
     * 显示Dialog
     */
    public void showChooseProvinceWindow() {
        if (popupWindow.isShowing()) {
            return;
        }
        popupWindow.setWidth(mRootView.getLayoutParams().width);
        popupWindow.setHeight(mRootView.getHeight() / 2);
        popupWindow.showAtLocation(mRootView, Gravity.BOTTOM,0,0);
        choiceDialog.findViewById(R.id.close_choose_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shutDialog();
            }
        });
    }

    /**
     * 关闭Dialog
     */
    public void shutDialog() {
        popupWindow.dismiss();
    }
}
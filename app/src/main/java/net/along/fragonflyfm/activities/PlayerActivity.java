package net.along.fragonflyfm.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import net.along.fragonflyfm.R;

/**
 * 创建者 by:陈泰龙
 * <p>
 * 2020/7/29
 **/

public class PlayerActivity extends AppCompatActivity {
    private ImageView returnImageView;
    private TextView returnName;
    private ImageView fmCoverImg;
    private TextView title;
    private TextView userName;
    private TextView start;
    private TextView end;
    private SeekBar mSeekBar;
    private ImageView previous; //上一首
    private ImageView next;  //下一首
    private ImageView play;
    private static String ChannelName;
    private static String CoverUrl;
    private static String Title;
    private static String Broadcaster;
    private static String Start_Time;
    private static String Ent_Time;
    private static int Duration;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initData();
        initView();
    }

    /**
     * 数据显示
     */
    private void initData() {
        CoverUrl = getIntent().getStringExtra("cover");
        ChannelName = getIntent().getStringExtra("channelName");
        Broadcaster = getIntent().getStringExtra("broadcaster");
        Title = getIntent().getStringExtra("title");
        Start_Time = getIntent().getStringExtra("start_time");
        Ent_Time = getIntent().getStringExtra("end_time");
        Duration = getIntent().getIntExtra("duration", 0) / 60;
    }

    /**
     * 初始化组件
     */
    private void initView() {
        this.findViewById(R.id.activity_player_return).setOnClickListener(mOnClickListener); //返回
        returnImageView = this.findViewById(R.id.activity_player_return);
        returnName = this.findViewById(R.id.activity_station_name);
        returnName.setOnClickListener(mOnClickListener);
        fmCoverImg = this.findViewById(R.id.activity_player_radio_pictures);
        title = this.findViewById(R.id.activity_play_title);
        userName = this.findViewById(R.id.activity_player_anchor);
        start = this.findViewById(R.id.activity_start_position);
        end = this.findViewById(R.id.activity_end_positions);
        mSeekBar = this.findViewById(R.id.activity_track_seek_bar);
        previous = this.findViewById(R.id.activity_play_left);
        next = this.findViewById(R.id.activity_play_right);
        play = this.findViewById(R.id.activity_play_pause_playback);

        //数据显示
        start.setText(Start_Time);
        end.setText(Ent_Time);
        title.setText(Title);
        userName.setText("主播："+Broadcaster);
        returnName.setText(ChannelName);
        mSeekBar.setMax(Duration);
        returnImageView.setImageResource(R.drawable.ic_retuenimage);
        previous.setImageResource(R.drawable.qh2);
        next.setImageResource(R.drawable.qh1);
        play.setImageResource(R.drawable.zt);
        Glide.with(this).load(CoverUrl).into(fmCoverImg);

    }

    /**
     * 返回上一层
     */
    private View.OnClickListener mOnClickListener = v -> {
        switch (v.getId()) {
            case R.id.activity_player_return:
            case R.id.activity_station_name:
                finish();
                break;
        }
    };
}
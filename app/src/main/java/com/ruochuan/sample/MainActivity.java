package com.ruochuan.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;

import com.ruochuan.bubbleview.BubbleImageView;

public class MainActivity extends AppCompatActivity {
    SeekBar radius;
    SeekBar triangleHeight;
    SeekBar triangleWidth;
    SeekBar offset;
    BubbleImageView bubbleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bubbleView = (BubbleImageView) findViewById(R.id.bubble);

        radius = (SeekBar) findViewById(R.id.radius);
        offset = (SeekBar) findViewById(R.id.offset);
        triangleHeight = (SeekBar) findViewById(R.id.height);
        triangleWidth = (SeekBar) findViewById(R.id.width);

        final int curRadius = bubbleView.getRadius();
        final int curOffset = bubbleView.getOffset();
        final int curTriangleWidth = bubbleView.getTriangleWidth();
        final int curTriangleHeight = bubbleView.getTriangleHeight();

        radius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bubbleView.setRadius(curRadius + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        offset.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bubbleView.setOffset(curOffset + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        triangleHeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bubbleView.setTriangleHeight(curTriangleHeight + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        triangleWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bubbleView.setTriangleWidth(curTriangleWidth + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}

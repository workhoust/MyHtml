package com.szdogen.myhtml;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = findViewById(R.id.tv);
        String str_s = "<myfont size='80px' color='#00FF00'>硬件：FFFF-0000</myfont>" +
                "<myfont size='100px' color='#FF0000'>检测成功！可以升级</myfont>"+"这个是原始字体";

        tv.setText(Html.fromHtml(str_s,null,new HtmlTagHandler("myfont")));
    }
}
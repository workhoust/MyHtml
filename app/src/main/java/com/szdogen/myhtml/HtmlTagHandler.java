package com.szdogen.myhtml;

import android.graphics.Color;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import org.xml.sax.XMLReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Author : myl
 * Date : 2020/11/17 21:55
 * Company : http://www.szdogen.com/
 * <p>
 * 放下狭隘，心宽，天地就宽。
 * <p>
 *     QQ:593021743
 * Desc : 自定义HTML标签解析
 */
public class HtmlTagHandler implements Html.TagHandler {
    // 自定义标签名称
    private String tagName;

    // 存放标签所有属性键值对
    final List<HashMap<String, String>> list_attributes = new ArrayList();
    final List<Integer> countTag = new ArrayList<>();//保存所有标签

    public HtmlTagHandler(String tagName) {
        this.tagName = tagName;
    }

    private int start_count = 0;
    private int end_count = 0;

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        // 判断是否是当前需要的tag
        if (tag.equalsIgnoreCase(tagName)) {
            int tag_len = output.length();
            countTag.add(tag_len);
            if (opening) {//开始标签
                HashMap<String, String> map = new HashMap<>();
                list_attributes.add(map);
                parseAttributes(xmlReader, map);
                start_count++;//计算开始标签有多少个
            } else {//结束标签
                end_count++;//计算结束标签
                if (start_count == end_count) {//表示文本标签查询完成
                    if ((countTag.size() % 2) != 0) {
                        return;
                    }
                    for (int i = 0; i < countTag.size() / 2; i += 1) {
                        setText(output, list_attributes.get(i), countTag.get(i), countTag.get(i + 1));
                    }

                }
            }
        }
    }

    private void setText(Editable output, HashMap<String, String> attributes, int startIndex, int endIndex) {
        // 获取对应的属性值
        String color = attributes.get("color");
        String size = attributes.get("size");
        size = size.split("px")[0];

        // 设置颜色
        if (!TextUtils.isEmpty(color)) {
            output.setSpan(new ForegroundColorSpan(Color.parseColor(color)), startIndex, endIndex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        // 设置字体大小
        if (!TextUtils.isEmpty(size)) {
            output.setSpan(new AbsoluteSizeSpan(Integer.parseInt(size)), startIndex, endIndex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    /**
     * 解析所有属性值
     *
     * @param xmlReader
     */
    private void parseAttributes(final XMLReader xmlReader, HashMap<String, String> attributes) {
        try {
            Field elementField = xmlReader.getClass().getDeclaredField("theNewElement");
            elementField.setAccessible(true);
            Object element = elementField.get(xmlReader);
            Field attsField = element.getClass().getDeclaredField("theAtts");
            attsField.setAccessible(true);
            Object atts = attsField.get(element);
            Field dataField = atts.getClass().getDeclaredField("data");
            dataField.setAccessible(true);
            String[] data = (String[]) dataField.get(atts);
            Field lengthField = atts.getClass().getDeclaredField("length");
            lengthField.setAccessible(true);
            int len = (Integer) lengthField.get(atts);

            for (int i = 0; i < len; i++) {
                attributes.put(data[i * 5 + 1], data[i * 5 + 4]);
            }
        } catch (Exception e) {

        }
    }
}

package com.example.geyiyang.eric_x_music.Model;

import java.text.Collator;
import java.util.Comparator;

/**
 * Created by geyiyang on 2017/10/18.
 */

public class MusicInfoComparator implements Comparator<MusicInfo> {

    @Override
    public int compare(MusicInfo o1, MusicInfo o2) {
        String str1 = o1.getTitle();
        String str2 = o2.getTitle();
        char[] c={str1.toLowerCase().charAt(0),str2.toLowerCase().charAt(0)};//首字母
        String[] str={str1.substring(0, 1),str2.substring(0, 1)};
        int type[]={1,1};
        for(int i=0;i<2;i++)
        {
            if(str[i].matches("[\\u4e00-\\u9fbb]+"))//中文字符
                type[i]=1;
            else if(c[i]>='a' && c[i]<='z')
                type[i]=2;
            else if(c[i]>='1' && c[i]<='9')
                type[i]=3;
            else
                type[i]=4;
        }
        if(type[0]==1 && type[1]==1)
            return Collator.getInstance(java.util.Locale.CHINESE).compare(str1, str2);
        if(type[0]==type[1]) //同一类
            return str1.compareTo(str2);
        return type[1]-type[0];
    }
}

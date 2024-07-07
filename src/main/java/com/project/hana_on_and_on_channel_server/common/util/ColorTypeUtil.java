package com.project.hana_on_and_on_channel_server.common.util;

import com.project.hana_on_and_on_channel_server.owner.domain.enumType.ColorType;

public class ColorTypeUtil {
    private static ColorType[] types = ColorType.values();

    public static ColorType getColorType(int index){
        return types[index];
    }

    public static ColorType getRandomColorType(){
        int random = (int)(Math.random() * (types.length));
        return types[random];
    }


}

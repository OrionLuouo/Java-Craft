package OrionLuouo.Craft.data;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StringUtil {
    public static final JLabel LABEL = new JLabel();
    public static final Set<Character> SKIN_BLANKS = new HashSet<>(Arrays.asList(new Character[] {' ' , '\r' , '\n' , '\t'}));

    /**
     * 计算一条字符串在参数font下的像素长度。
     * Return the pixel length of the string in this font.
     */
    public static int getStringWidth(String text, Font font) {
        FontMetrics metrics = LABEL.getFontMetrics(font);
        return metrics.stringWidth(text);
    }

    /**
     * 计算字符串在给定字体与像素宽度的情况下会被分成几行。
     * Return the row count of the string in this pixel width and font.
     */
    public static int getRowCount(String text, int width, Font font) {
        String[] rows = text.split("\n");
        int rowCount = 0;
        FontMetrics metrics = LABEL.getFontMetrics(font);
        for (String row : rows) {
            char[] chars = row.toCharArray();
            int i = 0;
            int w = 0;
            for (; i < chars.length; i++) {
                w += metrics.stringWidth("" + chars[i]);
                if (w >= width) {
                    if (w > width)
                        i--;
                    rowCount++;
                    w = 0;
                }
            }
            if (w != 0)
                rowCount++;
            else if (i == 0)
                rowCount++;
        }
        return rowCount;
    }

    /**
     * Get the string out of prefix and suffix.
     * @param text
     * @return
     */
    public static String peel(String text) {
        return peel(text , SKIN_BLANKS);
    }

    /**
     *
     */
    public static String peel(String text , Set<Character> skin) {
        StringBuilder builder = new StringBuilder();
        int index;
        while ((index = builder.length() - 1) != -1 && skin.contains(builder.charAt(index))) {
            builder.deleteCharAt(index);
        }
        while (builder.length() != 0 && skin.contains(builder.charAt(0))) {
            builder.deleteCharAt(0);
        }
        return builder.toString();
    }
}

package ru.netology.graphics.image;


public class ColorSchema implements TextColorSchema {
    @Override
    public char convert(int color) {
        char[] arrayChars = new char[]{'▇', '●', '◉', '◍', '◎', '○', '☉', '◌', '-'};
        int i = (int) Math.floor((double) color / 256 * 9);
        return arrayChars[i];
    }
}

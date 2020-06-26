package com.huzong.utils;

import java.io.File;

public interface IImage {
    int[] getImgWidthHeight(File file);
    void reduceImg(File file, String imgdist, Float rate);
}

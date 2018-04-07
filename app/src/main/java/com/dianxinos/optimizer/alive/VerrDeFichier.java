package com.dianxinos.optimizer.alive;

import wake.tim.mimic.com.mimictim.Info;

/**
 * Created by Wang Junkai on 11/22/2017.
 * 文件锁获取Native实现
 */
public final class VerrDeFichier {
    static {
        System.loadLibrary("file-lock");
        Info.y("V: load ok!");
    }

    /**
     * 获取文件锁
     *
     * @param p 文件路径
     * @return 成功获取文件锁引用的descriptor, 用于后续释放使用
     * @throws IllegalStateException
     */
    static native int acquerir(String p) throws IllegalStateException;

    /**
     * 释放文件锁
     *
     * @param d 对应释放的Descriptor
     * @throws IllegalStateException
     */
    static native void liberer(int d) throws IllegalStateException;
}
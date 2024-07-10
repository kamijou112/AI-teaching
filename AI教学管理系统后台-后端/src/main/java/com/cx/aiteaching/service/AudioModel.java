package com.cx.aiteaching.service;

import java.io.IOException;
import java.io.File;


//语音大模型
public interface AudioModel {
    String  AM(File file) throws IOException;
}

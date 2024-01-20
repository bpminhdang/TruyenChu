package com.example.truyenchu._interface;

import java.util.List;

public interface DataListener
{
    void onDataReceived(String data);
    void onBooleanListReceived(List<Boolean> readList, List<Boolean> favList);
}


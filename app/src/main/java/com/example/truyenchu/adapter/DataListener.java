package com.example.truyenchu.adapter;

import java.util.List;

public interface DataListener
{
    void onDataReceived(String data);
    void onBooleanListReceived(List<Boolean> readList, List<Boolean> favList);
}


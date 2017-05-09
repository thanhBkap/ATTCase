package com.att.attcase;

import android.content.Context;

import com.android.volley.Request;

import java.util.ArrayList;

/**
 * Created by admin on 3/24/2017.
 */

public class MyCommand<T> {
    Context context;
    ArrayList<Request<T>> listRequest=new ArrayList<>();

    public MyCommand(Context context) {
        this.context = context;
    }

    public void add(Request<T> request){
        listRequest.add(request);
    }
    public void remove(Request<T> request){
        listRequest.remove(request);
    }

    public void execute(){
        for (Request<T> request:listRequest){
            MySingleton.getInstance(context).addToRequestQueue(request);
        }
    }
}

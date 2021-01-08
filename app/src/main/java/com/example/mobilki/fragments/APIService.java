package com.example.mobilki.fragments;

import com.example.mobilki.notifications.MyResponse;
import com.example.mobilki.notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAfqjTVkY:APA91bEjbxFfDLy3kwQww1jYbSeIVQxssvc_7PMA1jHOKa5dIsVyrT-w3h1w9vd8fBrf4tPnZXKsG0VM-a-IxOQR0L9FT2MYqZt5VLGX3re7YZwtlAW2Sjf5BAgJxRHzSefo0a2A5TTx"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);


}

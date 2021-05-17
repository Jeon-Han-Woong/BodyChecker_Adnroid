package org.ict.bodychecker.retrofit;

import org.ict.bodychecker.ValueObject.Example;
import org.ict.bodychecker.ValueObject.ExerciseVO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitInterface {

    @GET("http://10.0.2.2:8181/exer/")
    Call<Example> getDailyExer(@Query("edate") String edate);
}

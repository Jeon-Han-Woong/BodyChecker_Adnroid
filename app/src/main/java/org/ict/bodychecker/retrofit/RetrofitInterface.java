package org.ict.bodychecker.retrofit;

import org.ict.bodychecker.ValueObject.ExerciseData;
import org.ict.bodychecker.ValueObject.ExerciseVO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitInterface {

    @GET("exer/{edate}")
    Call<List<ExerciseVO>> getDailyExer(@Query("edate") String edate);
}

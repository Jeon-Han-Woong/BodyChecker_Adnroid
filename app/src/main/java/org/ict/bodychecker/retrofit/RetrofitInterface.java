package org.ict.bodychecker.retrofit;

import org.ict.bodychecker.ValueObject.ExerciseVO;
import org.ict.bodychecker.ValueObject.MealVO;
import org.ict.bodychecker.ValueObject.MemberVO;

import java.util.List;

import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RetrofitInterface {

    /*================== Exercise ====================*/
    @GET("exer/{edate}.json")
    Call<List<ExerciseVO>> getDailyExer(@Path("edate") String edate);

    @GET("exer/neweno")
    Call<Integer> getNewEno();

    @POST("exer/new")
    Call<ExerciseVO> registerExer(@Body ExerciseVO exer);

    @PUT("exer/modify/{eno}")
    Call<ExerciseVO> modifyExer(@Path("eno") int eno, @Body ExerciseVO exer);

    @DELETE("exer/remove/{eno}")
    Call<Void> removeExer(@Path("eno") int eno);

    /*================== Meal ====================*/
    @GET("meal/getList/{fdate}")
    Call<List<MealVO>> getDailyMeal(@Path("fdate") String fdate);

    @POST("meal/addFoods")
    Call<String> addFoods(@Body MealVO mealVO);

    @DELETE("meal/remove/{fdate}/{ftime}")
    Call<String> removeFoods(@Path("fdate") String fdate, @Path("ftime") String ftime);

    /*================== Member ====================*/
    @POST("member/check/{mid}")
    Call<Integer> check(@Path("mid") String mid);

    @POST("member/join")
    Call<String> join(@Body MemberVO memberVO);

    @POST("member/login")
    Call<MemberVO> login(@Body MemberVO memberVO);

    @PUT("member/modify")
    Call<String> modify(@Body MemberVO memberVO);

    @DELETE("member/remove/{mno}")
    Call<String> remove(@Path("mno") int mno);
}

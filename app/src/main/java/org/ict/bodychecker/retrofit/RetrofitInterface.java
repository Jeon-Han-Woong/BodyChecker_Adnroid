package org.ict.bodychecker.retrofit;

import org.ict.bodychecker.ValueObject.ExerciseVO;
import org.ict.bodychecker.ValueObject.GoalVO;
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

    /*================== Daily ====================*/
    @GET("daily/water/get/{ddate}/{mno}")
    Call<Integer> getDailyWater(@Path("ddate") String ddate, @Path("mno") int mno);

    @GET("daily/water/plus/{ddate}/{mno}")
    Call<Integer> plusWater(@Path("ddate") String ddate, @Path("mno") int mno);

    @GET("daily/water/minus/{ddate}/{mno}")
    Call<Integer> minusWater(@Path("ddate") String ddate, @Path("mno") int mno);


    /*================== Exercise ====================*/
    @GET("exer/{edate}")
    Call<List<ExerciseVO>> getDailyExer(@Path("edate") String edate);

    @GET("exer/kcal/{edate}")
    Call<Integer> getSumKcal(@Path("edate") String edate);

    @GET("exer/neweno")
    Call<Integer> getNewEno();

    @POST("exer/new")
    Call<String> registerExer(@Body ExerciseVO exer);

    @PUT("exer/modify/{eno}")
    Call<String> modifyExer(@Path("eno") int eno, @Body ExerciseVO exer);

    @DELETE("exer/remove/{eno}")
    Call<Void> removeExer(@Path("eno") int eno);

    /*================== Goal ====================*/
    @GET("goal/doing/{fin_date}.json")
    Call<List<GoalVO>> getDoing(@Path("fin_date") String fin_date);

    @GET("goal/newgno.json")
    Call<Integer> getNewGno();

    @POST("goal/new")
    Call<GoalVO> registerGoal(@Body GoalVO goal);

    @PUT("goal/modify/{gno}")
    Call<GoalVO> modifyGoal(@Path("gno") int gno, @Body GoalVO goal);

    @DELETE("goal/remove/{gno}")
    Call<Void> removeGoal(@Path("gno") int gno);

    @GET("goal/finish/{fin_date}.json")
    Call<List<GoalVO>> getFinish(@Path("fin_date") String fin_date);

    @PUT("goal/finish/success/{gno}")
    Call<GoalVO> selectSuccess(@Path("gno") int gno, @Body GoalVO goal);

    /*================== Meal ====================*/
    @GET("meal/getlist/{fdate}")
    Call<List<MealVO>> getDailyMeal(@Path("fdate") String fdate);

    @POST("meal/addFoods")
    Call<String> addFoods(@Body MealVO mealVO);

    @DELETE("meal/remove/{fdate}/{ftime}")
    Call<String> removeFoods(@Path("fdate") String fdate, @Path("ftime") String ftime);

    /*================== Member ====================*/
    @GET("member/getinfo/{mno}")
    Call<MemberVO> getInfo(@Path("mno") int mno);

    @POST("member/check")
    Call<String> check(@Body String mid);

    @POST("member/join")
    Call<String> join(@Body MemberVO memberVO);

    @GET("member/login/{mid}/{pwd}")
    Call<Integer> login(@Path("mid") String mid, @Path("pwd") String pwd);

    @PUT("member/modify")
    Call<String> modifyInfo(@Body MemberVO memberVO);

    @DELETE("member/remove/{mno}")
    Call<String> remove(@Path("mno") int mno);
}

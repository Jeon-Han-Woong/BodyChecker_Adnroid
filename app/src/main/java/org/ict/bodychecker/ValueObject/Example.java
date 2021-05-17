package org.ict.bodychecker.ValueObject;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Example {

    @SerializedName("ExerciseVO")
    @Expose
    private List<ExerciseVO> ExerciseVO = null;

    public List<ExerciseVO> getExerciseVO() {
        return ExerciseVO;
    }

    public void setExerciseVO(List<ExerciseVO> ExerciseVO) {
        this.ExerciseVO = ExerciseVO;
    }

}
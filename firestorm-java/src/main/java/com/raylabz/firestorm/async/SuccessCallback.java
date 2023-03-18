package com.raylabz.firestorm.async;

public interface SuccessCallback<ResultType> {

    void execute(ResultType result);

}

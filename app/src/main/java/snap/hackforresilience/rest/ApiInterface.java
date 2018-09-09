package snap.hackforresilience.rest;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import snap.hackforresilience.Count;


public interface ApiInterface {

    @GET("result")
    Call<Count> getCounter();
}
package com.mobisquid.mobicash.utils;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.mobisquid.mobicash.model.TransactionObj;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by mobicash on 9/9/15.
 */
public class ConnectionClass {
    static Vars vars;
    static String finalstring;
    static Gson gson;

    public static String ConnectionClass(final Context context, String URL_FEED, final String[] parameters
            , final String[] values, final String tag, final VolleyCallback callback) {
        vars = new Vars(context);
        gson = new Gson();

        CustomRequest postRequest = new CustomRequest(Request.Method.POST, URL_FEED, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    VolleyLog.v("Response:", response.toString(4));
                    if (response != null) {
                        vars.log("respo==we have==" + response.toString());
                        finalstring = response.toString();
                        callback.onSuccess(finalstring);

                        AppController.getInstance().cancelPendingRequests(tag);
                    } else {

                        vars.log("null==" + response.toString());

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                TransactionObj transerror = new TransactionObj();
                String errormsg;
                vars.log("==================ERROR============");
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    vars.log("error TimeoutError");
                    transerror.setResult("Failed");
                    transerror.setLocation("neterror");
                    transerror.setMessage("Failed");

                    transerror.setError("No connection could be established");
                    errormsg = gson.toJson(transerror);
                    finalstring = errormsg;
                    callback.onSuccess(finalstring);
                    // AppController.getInstance(context).cancelPendingRequests(tag);
                } else if (error instanceof NetworkError) {
                    vars.log("error NetworkError");
                    transerror.setResult("Failed");
                    transerror.setMessage("Failed");
                    transerror.setLocation("neterror");
                    transerror.setError("Network error check internet connection");
                    errormsg = gson.toJson(transerror);
                    finalstring = errormsg;
                    callback.onSuccess(finalstring);
                    //   AppController.getInstance(context).cancelPendingRequests(tag);
                } else if (error instanceof ServerError) {
                    vars.log("error ServerError");
                    transerror.setResult("Failed");
                    transerror.setMessage("Failed");
                    transerror.setLocation("neterror");
                    transerror.setError("Server error please contact our help center");
                    errormsg = gson.toJson(transerror);
                    finalstring = errormsg;
                    callback.onSuccess(finalstring);
                    //    AppController.getInstance(context).cancelPendingRequests(tag);
                } else if (error instanceof ParseError) {
                    vars.log("error ServerError");
                    transerror.setResult("Failed");
                    transerror.setMessage("Failed");
                    transerror.setLocation("neterror");
                    transerror.setError("Network error check internet connection");
                    errormsg = gson.toJson(transerror);
                    finalstring = errormsg;
                    callback.onSuccess(finalstring);
                    //    AppController.getInstance(context).cancelPendingRequests(tag);
                } else if (error instanceof AuthFailureError) {
                    vars.log("error ServerError");
                    transerror.setResult("Failed");
                    transerror.setMessage("Failed");
                    transerror.setLocation("neterror");
                    transerror.setError("Network error check internet connection");
                    errormsg = gson.toJson(transerror);
                    finalstring = errormsg;
                    callback.onSuccess(finalstring);
                    //    AppController.getInstance(context).cancelPendingRequests(tag);
                } else {
                    vars.log("error Unknown error====");
                    transerror.setResult("Failed");
                    transerror.setLocation("neterror");
                    transerror.setMessage("Failed");
                    transerror.setError("Please check your internet connection");
                    errormsg = gson.toJson(transerror);
                    finalstring = errormsg;
                    callback.onSuccess(finalstring);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                for (int y = 0; y < parameters.length; y++) {
                    params.put(parameters[y], values[y]);
                    vars.log("para " + y + "===" + parameters[y] + "====and ==" + "values " + y + "===" + values[y]);
                }

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(postRequest);

        return URL_FEED;
    }

    public static void returnString(final Context context, String URL_FEED, final String tag, final VolleyCallback callback) {
        vars = new Vars(context);
        gson = new Gson();
        StringRequest postRequeststring = new StringRequest(Request.Method.GET, URL_FEED,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String respo) {
                        // TODO Auto-generated method stub

                        if (respo != null) {
                            TransactionObj transerror = new TransactionObj();
                            String errormsg;
                            transerror.setResult("Success");
                            transerror.setError("no error");
                            transerror.setMessage(respo);
                            errormsg = gson.toJson(transerror);
                            finalstring = errormsg;
                            callback.onSuccess(finalstring);

                            AppController.getInstance().cancelPendingRequests(tag);
                        } else {

                            vars.log("null==" + respo);

                        }

                    }

                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        TransactionObj transerror = new TransactionObj();
                        String errormsg;
                        vars.log("==================ERROR============");
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            transerror.setResult("Failed");
                            transerror.setError("No connection could be established");
                            transerror.setMessage(null);
                            errormsg = gson.toJson(transerror);
                            finalstring = errormsg;
                            callback.onSuccess(finalstring);
                            AppController.getInstance().cancelPendingRequests(tag);
                        }


                    }

                });

       /* {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };*/


        postRequeststring.setRetryPolicy(new DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(postRequeststring);
    }
    public static void FreeString(final Context context, String URL_FEED, final String tag, final VolleyCallback callback) {
        vars = new Vars(context);
        gson = new Gson();
        StringRequest postRequeststring = new StringRequest(Request.Method.GET, URL_FEED,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String respo) {
                        // TODO Auto-generated method stub

                        if (respo != null) {

                            finalstring = respo;
                            callback.onSuccess(finalstring);

                            AppController.getInstance().cancelPendingRequests(tag);
                        } else {

                            vars.log("null==" + respo);

                        }

                    }

                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        vars.log("==================ERROR============");
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            callback.onSuccess("{\"message\":\"Connection failed\",\"transType\":\"yes\"," +
                                    "\"sellvalue\":10,\"costprice\":10,\"__hashCodeCalc\":false}");
                            AppController.getInstance().cancelPendingRequests(tag);
                        }


                    }

                });

        postRequeststring.setRetryPolicy(new DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(postRequeststring);
    }

    public interface VolleyCallback {
        void onSuccess(String result);
    }


    public static String JsonString(int type, final Context context, String URL_FEED, final JSONObject body, final String tag,
                                    final VolleyCallback callback) {
        vars = new Vars(context);
        gson = new Gson();

        StringRequest postRequeststring = new StringRequest(type, URL_FEED,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String respo) {
                        // TODO Auto-generated method stub
                        vars.log("+++++++++resp-=+++++++"+respo);
                        if (respo != null) {
                            finalstring = respo;
                            callback.onSuccess(finalstring);

                            AppController.getInstance().cancelPendingRequests(tag);
                        } else {

                            vars.log("null==" + respo);

                        }

                    }

                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        TransactionObj transerror = new TransactionObj();
                        String errormsg;
                        vars.log("==================ERROR============");
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            transerror.setResult("Failed");
                            transerror.setError("Connection failure");
                            transerror.setMessage("Connection failure");
                            errormsg = gson.toJson(transerror);
                            finalstring = errormsg;
                            callback.onSuccess(finalstring);
                            AppController.getInstance().cancelPendingRequests(tag);
                        }else if(error instanceof ServerError ){
                            transerror.setResult("Failed");
                            transerror.setError("Error");
                            transerror.setMessage("Server error 500");
                            errormsg = gson.toJson(transerror);
                            finalstring = errormsg;
                            callback.onSuccess(finalstring);
                            AppController.getInstance().cancelPendingRequests(tag);
                        }


                    }

                })

        {


             @Override
                        public byte[] getBody() throws AuthFailureError {
                            return body.toString().getBytes();
                        }


            @Override
            public String getBodyContentType() {
                return "application/json";
            }

        };


        postRequeststring.setRetryPolicy(new DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(postRequeststring);


       /* System.out.println("JSON : " + json);
        try {

            jsonObject = new JSONObject(json);

            StringRequest postRequest = new StringRequest(Request.Method.POST,URL_FEED, jsonObject,
                    new Response.Listener<JSONObject>(){
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                VolleyLog.v("Response:", response.toString(4));
                                if (response != null) {
                                    vars.log("respo==we have==" + response.toString());
                                    finalstring = response.toString();
                                    callback.onSuccess(finalstring);

                                    AppController.getInstance(context).cancelPendingRequests(tag);
                                } else {

                                    vars.log("null==" + response.toString());

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    TransactionObj transerror = new TransactionObj();
                    String errormsg;
                    vars.log("==================ERROR============"+error);
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        vars.log("error TimeoutError");
                        transerror.setResult("Failed");
                      //  transerror.setEmail("failed");
                        transerror.setMessage("Failed");
                        transerror.setError("No connection could be established");
                        errormsg = gson.toJson(transerror);
                        finalstring = errormsg;
                        callback.onSuccess(finalstring);
                        // AppController.getInstance(context).cancelPendingRequests(tag);
                    } else if (error instanceof NetworkError) {
                        vars.log("error NetworkError");
                       // transerror.setEmail("failed");
                        transerror.setResult("Failed");
                        transerror.setMessage("Failed");
                        transerror.setError("Network error check internet connection");
                        errormsg = gson.toJson(transerror);
                        finalstring = errormsg;
                        callback.onSuccess(finalstring);
                        //   AppController.getInstance(context).cancelPendingRequests(tag);
                    } else if (error instanceof ServerError) {
                        vars.log("error ServerError");
                        transerror.setResult("Failed");
                        transerror.setMessage("Failed");
                        transerror.setError("Server error please contact our help center");
                        errormsg = gson.toJson(transerror);
                        finalstring = errormsg;
                        callback.onSuccess(finalstring);
                        //    AppController.getInstance(context).cancelPendingRequests(tag);
                    } else if (error instanceof ParseError) {
                        vars.log("error ServerError");
                        transerror.setResult("Failed");
                        transerror.setMessage("Failed");
                      //  transerror.setEmail("failed");
                        transerror.setError("Network error check internet connection");
                        errormsg = gson.toJson(transerror);
                        finalstring = errormsg;
                        callback.onSuccess(finalstring);
                        //    AppController.getInstance(context).cancelPendingRequests(tag);
                    } else if (error instanceof AuthFailureError) {
                        vars.log("error ServerError");
                        transerror.setResult("Failed");
                        transerror.setMessage("Failed");
                      //  transerror.setEmail("failed");
                        transerror.setError("Network error check internet connection");
                        errormsg = gson.toJson(transerror);
                        finalstring = errormsg;
                        callback.onSuccess(finalstring);
                        //    AppController.getInstance(context).cancelPendingRequests(tag);
                    }
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json");
                    params.put("charset", "utf-8");

                    return params;
                }
            };
            postRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance(context).addToRequestQueue(postRequest);

        }catch (Exception e){}
        return URL_FEED;
    }
*/
        return URL_FEED;

    }
    public static void  stringRequest(int type,String URL_FEED, final String[] parameters
            ,final String[] values, final VolleyCallback callback){

        StringRequest strReq = new StringRequest(type,
                URL_FEED, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("response========"+response);
                callback.onSuccess(response);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error========"+error);
                NetworkResponse networkResponse = error.networkResponse;
                callback.onSuccess("{\n" +
                        "  \"error\": true,\n" +
                        "  \"message\": \"No connection found\"\n" +
                        "}");

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                for(int y =0;y<parameters.length;y++){
                    params.put(parameters[y], values[y]);
                    //  vars.log("para "+y+"==="+parameters[y]+"====and =="+"values "+y+"==="+values[y]);
                }


                return params;
            }
        };

        //Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);

    }

}
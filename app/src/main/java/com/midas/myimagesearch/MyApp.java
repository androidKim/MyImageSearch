package com.midas.myimagesearch;

import android.app.Application;
import android.content.Context;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.midas.myimagesearch.core.APIClient;
import com.midas.myimagesearch.core.APIInterface;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MyApp extends Application
{
    /******************** Define ********************/

    /******************** Member ********************/
    public APIInterface m_APIInterface = null;
    public boolean m_bInit = false;
    /******************** Cotroller ********************/

    /******************** User function ********************/
    //------------------------------------------------
    //
    public MyApp()
    {

    }
    //------------------------------------------------
    //
    public MyApp(Context pContext)
    {
        if(m_bInit == false)
        {
            if(pContext == null)
                return;

            //getKeyHash(pContext);

            m_APIInterface = APIClient.getClient().create(APIInterface.class);//web request ctrl
            m_bInit = true;
        }
    }

    //------------------------------------------------
    //
    public void getKeyHash(final Context context)
    {
        String keyHash = "";
        try
        {
            PackageInfo info = context.getPackageManager().getPackageInfo("com.midas.myimagesearch", PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("keyHash",keyHash);
            }
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
    }
}

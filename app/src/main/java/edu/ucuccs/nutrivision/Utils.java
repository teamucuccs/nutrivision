/**
 * Author: Aljohn De Guzman on 09/07/2016.
 * Website: http://ajdeguzman.x10.mx
 * E-mail: aljohndeguzman@gmail.com
 */

package edu.ucuccs.nutrivision;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

public class Utils {


    private static Context mContext;
    private static View mView;

    private Utils(){
        throw new AssertionError();
    }
    public Utils(Context mContext){
        this.mContext = mContext;
    }
    public Utils(View view){
        this.mView = view;
    }
    public static void showToast(String msg, int length){
        Toast.makeText(mContext, msg, length).show();
    }
    public static void showSnackBar(String msg, CoordinatorLayout layout, int length){
        Snackbar.make(layout, msg, length).show();
    }
    public static void showSnackBar(int msg, CoordinatorLayout layout, int length){
        Snackbar.make(layout, msg, length).show();
    }
}

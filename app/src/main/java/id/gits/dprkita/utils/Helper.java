package id.gits.dprkita.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.PopupMenu;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import id.gits.dprkita.R;
import id.gits.dprkita.activity.LoginActivity;
import id.gits.dprkita.fragment.MainDashboardFragment;
import id.gits.wplib.apis.LoginApi;
import id.gits.wplib.utils.WPKey;

/**
 * Created by yatnosudar on 1/28/15.
 */
public class Helper {

    private static MixpanelAPI mixpanel = null;

    public static ActionBar getActionBarFragment(Activity activity) {
        return ((ActionBarActivity) activity).getSupportActionBar();
    }

    public static FragmentManager getFragmentManager(Activity activity) {
        return ((ActionBarActivity) activity).getSupportFragmentManager();
    }

    public static void addFragment(Activity activity, Fragment fragment, int layout_id, FragmentManager fragmentManager) {
        String backStateName = fragment.getClass().getName();
        String fragmentTag = backStateName;
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(layout_id, fragment, fragmentTag);
        ft.commit();
    }

    /*Activity activity, Fragment fragment, int layout_id, FragmentManager fragmentManager*/
    public static void replaceFragment( Activity activity, Fragment fragment, int layout_id, FragmentManager fragmentManager) {
        String backStateName = fragment.getClass().getName();
        String fragmentTag = backStateName;
        boolean fragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 0);
        if (!fragmentPopped) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.add(layout_id, fragment, fragmentTag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            if (fragment instanceof MainDashboardFragment == false)
                ft.addToBackStack(fragmentTag);
            else{
                dialogCloseApps(activity);
            }
            ft.commit();
        }
    }

    public static void dialogCloseApps(final Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(activity.getString(R.string.alert_logout));
        builder.setPositiveButton("Ya",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finish();
            }
        });
        builder.setNegativeButton("Batal",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    /*Activity activity, Fragment fragment, int layout_id, FragmentManager fragmentManager*/
    public static void replaceFragmentR(Activity activity, Fragment fragment, int layout_id, FragmentManager fragmentManager) {
        String backStateName = fragment.getClass().getName();
        String fragmentTag = backStateName;
        boolean fragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 0);
        if (!fragmentPopped) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(layout_id, fragment, fragmentTag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            if (fragment instanceof MainDashboardFragment == false)
                ft.addToBackStack(fragmentTag);
            ft.commit();
        }
    }

    public static String dateParse(String dateInput, String old_format, String formatDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(old_format);
        Date date = sdf.parse(dateInput);
        Locale id = new Locale("in", "ID");
        SimpleDateFormat format = new SimpleDateFormat(formatDate, id);
        String result = format.format(date);
        return result;
    }

    public static boolean isOnline(Activity activity) {
        ConnectivityManager cm =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static List<HashMap<String, Object>> customFields(HashMap<String, Object> data) {
        List<HashMap<String, Object>> customField = new ArrayList<>();
        customField.add(data);
        return customField;
    }

    public static HashMap<String, String> valueCustomField(String key, String val) {
        HashMap<String, String> data = new HashMap<>();
        data.put(WPKey.KEY.KEY, key);
        data.put(WPKey.KEY.VALUE, val);
        return data;
    }

    public static IconDrawable makeOwnIcon(Activity activity, int color, Iconify.IconValue icon) {
        return new IconDrawable(activity, icon).colorRes(color).actionBarSize();
    }

    public static IconDrawable makeOwnIconWithoutSize(Activity activity, int color, Iconify.IconValue icon) {
        return new IconDrawable(activity, icon).colorRes(color);
    }

    public static String splitKomisi(String delimeter, String content) {
        String[] komisi = content.split(delimeter);
        return komisi[0];
    }

    public static void dialogReport(final Activity activity, final String username) {
        AlertDialog.Builder laporkan = new AlertDialog.Builder(activity);
        laporkan.setMessage(activity.getResources().getString(R.string.lapor));
        laporkan.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        laporkan.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {

                    Intent gmail = new Intent(Intent.ACTION_VIEW);
                    gmail.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                    gmail.putExtra(Intent.EXTRA_EMAIL, new String[]{activity.getResources().getString(R.string.email_receive)});
                    gmail.setData(Uri.parse(activity.getResources().getString(R.string.email_receive)));
                    gmail.putExtra(Intent.EXTRA_SUBJECT, activity.getResources().getString(R.string.laporkan_subject) + username);
                    gmail.setType("plain/text");
                    gmail.putExtra(Intent.EXTRA_TEXT, activity.getResources().getString(R.string.body_email));
                    activity.startActivity(gmail);
                    dialog.cancel();
                } catch (Exception e) {
                    dialog.cancel();
                }
            }
        });
        laporkan.show();
    }


    public static void showMenu(final Activity activity, View v, final String message, final String username,final int type) {
        final LoginApi.ApiDao sessionLogin = SharePreferences.sessionLogin(activity);
        PopupMenu popup = new PopupMenu(activity, v);
        popup.inflate(R.menu.menu_laporkan);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.action_lapor:
                        if (sessionLogin != null) {
                            try {

                                Intent gmail = new Intent(Intent.ACTION_VIEW);
                                gmail.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                                gmail.putExtra(Intent.EXTRA_EMAIL, new String[]{activity.getResources().getString(R.string.email_receive)});
                                gmail.setData(Uri.parse(activity.getResources().getString(R.string.email_receive)));
                                gmail.putExtra(Intent.EXTRA_SUBJECT, activity.getResources().getString(R.string.laporkan_subject) + " " + username);
                                gmail.setType("plain/text");
                                gmail.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(message) + "\n" + activity.getResources().getString(R.string.body_email));
                                activity.startActivity(gmail);
                            } catch (Exception e) {
                            }
                        } else {
                            LoginActivity.startActivity(activity, 1);
                        }
                        break;
                    case R.id.action_share:

                        String textAspirasi = "";
                        //aspirasi
                        if (type==0){
                            textAspirasi = " aspirasi ";
                        }
                        //podium
                        if (type==1){
                            textAspirasi = " podium ";
                        }
                        //comment
                        if (type==2){
                            textAspirasi = " komentar ";
                        }
                        Document js = Jsoup.parse(message);

                        final String share_to = "Saya membagikan"+textAspirasi+username+", "+js.text()+" di aplikasi DPR KITA"+" http://dprkita.org/web/public/";
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, share_to);
                        sendIntent.setType("text/plain");
                        activity.startActivity(sendIntent);
                        break;
                }
                return false;
            }
        });

        popup.show();

    }

    public static String loadHtmlBody(String title, String body, String date) {
        String loadHtml = "\n" +
                "<!DOCTYPE HTML>\n" +
                "<head>\n" +
                "    <title>" + title + "</title>\n" +
                "\n" +
                "    <style type=\"text/css\">\n" +
                "        body {\n" +

                "            font-family: Arial;\n" +
                "            font-size: 15px;\n" +
                "            line-height: 22px;\n" +
                "            padding: 0px;\n" +
                "            margin:0px;\n" +
                "        }\n" +
                "        .photo {\n" +
                "            position: relative;\n" +
                "            display: inline-block;\n" +
                "        }\n" +
                "        .photo img {\n" +
                "            width: 100%;\n" +
                "            vertical-align: middle;\n" +
                "        }\n" +
                "        .photo:before {\n" +
                "            content: \"\";\n" +
                "            position: absolute;\n" +
                "            top: 0; right: 0; bottom: 0; left: 0;\n" +
                "            background: linear-gradient(\n" +
                "                to bottom, \n" +
                "                rgba(150,35,110,1) 0%, \n" +
                "                rgba(150,35,110,0) 0%, \n" +
                "                rgba(150,35,110,0) 0%, \n" +
                "                rgba(150,35,110,1) 100%\n" +
                "            ); \n" +
                "        }\n" +
                "        .content {\n" +
                "            padding: 0px 5%;\n" +
                "        }\n" +
                "        .content h1 {\n" +
                "            color: #212121;\n" +
                "            line-height: 28px;\n" +
                "        }\n" +
                "        .content .headline {\n" +
                "            color: #9E9E9E;\n" +
                "            font-size: 16px;\n" +
                "            text-transform: uppercase;\n" +
                "        }\n" +
                "        .content .headline span {\n" +
                "            font-size:11px;\n" +
                "            text-transform: capitalize;\n" +
                "        }\n" +
                "        .content p {\n" +
                "            color: #9E9E9E;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"content\">\n" +
                "    <h1>" + title + "</h1>\n" +
                "    <div class=\"headline\">Headline <span>" + date + "</span></div>\n" +
                body +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
        return loadHtml;
    }

    public static boolean checkCamera(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    public static byte[] getBytesFromBitmap(String photoPath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }


    public static String toBinary(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);
        for (int i = 0; i < Byte.SIZE * bytes.length; i++)
            sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
        return sb.toString();
    }

    public static byte[] fromBinary(String s) {
        int sLen = s.length();
        byte[] toReturn = new byte[(sLen + Byte.SIZE - 1) / Byte.SIZE];
        char c;
        for (int i = 0; i < sLen; i++)
            if ((c = s.charAt(i)) == '1')
                toReturn[i / Byte.SIZE] = (byte) (toReturn[i / Byte.SIZE] | (0x80 >>> (i % Byte.SIZE)));
            else if (c != '0')
                throw new IllegalArgumentException();
        return toReturn;
    }


    public static boolean isAnggotaDPR(String role) {
        if (role.toLowerCase().equals(Constant.TAG.ROLE_ANGGOTADPR)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name

            if (ipAddr.equals("")) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            return false;
        }
    }


    public static MixpanelAPI mixpanel(Context context) {
        if (mixpanel == null) {
            mixpanel = MixpanelAPI.getInstance(context, Constant.KEY.MIXPANEL);
            LoginApi.ApiDao dao = SharePreferences.sessionLogin(context);
            if (dao != null) {
                mixpanel.identify(dao.getUser_id());
            }
        }
        return mixpanel;
    }

    public static void addActionMixpanel(Context context, String track, HashMap<String, String> data) {

        JSONObject props = new JSONObject();
        try {
            if (data != null) {
                String json = new Gson().toJson(data);
                props = new JSONObject(json);
            } else {
                props = new JSONObject();
            }
        } catch (JSONException e) {
        }
        Helper.mixpanel(context).track(track, props);
    }

    public static List<Fragment> getFragmentFromViewPager(Fragment fragment) {
        List<Fragment> f = fragment.getChildFragmentManager().getFragments();
        return f;
    }


    public static String MD5(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte byteData[] = md.digest();
        //convert the byte to hex format method 2
        StringBuffer hexString = new StringBuffer();
        for (int i=0;i<byteData.length;i++) {
            String hex=Integer.toHexString(0xff & byteData[i]);
            if(hex.length()==1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }

}

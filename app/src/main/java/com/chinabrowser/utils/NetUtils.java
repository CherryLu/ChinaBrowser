package com.chinabrowser.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;


import com.chinabrowser.APP;
import com.chinabrowser.bean.AnyRadio_ItemPayload;

import org.apache.http.util.EncodingUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Administrator on 2018/3/26.
 */

public class NetUtils {


    // 协议测试开关
    public static boolean PROTOCOL_TEST = true;
    // 使用Socket方式开关
    public static boolean USE_SOCKET = false;

    public static boolean isBreak = false;// 上传专用取消,其他的时候不要动
    private static boolean READ_BUFFER = false;
    private static final int DefaultReadBufferSize = 1000;


    /**
     * 判断是否有网络连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static byte[] getHttpDataUsePostForAction(String url, String action, String param, String noEncryptParam, boolean useSingleAppserv) {
        byte[] pResultBuf = null;
        StringBuffer sb = new StringBuffer();
        LogUtils.d("NET", "url " + url + "\n" + "action" + action + "\n" + "param" + param);
        if (NetUtils.PROTOCOL_TEST) {
            // 如果配置为动态接口方式，自己拼参数
            if (TextUtils.isEmpty(url)) {
                // 如果没有url，自己拼参数
                sb.append("http://");
                sb.append(ServerUtils.getInstance().getServerIpFromServer(useSingleAppserv));
                // sb.append(":8080");
                sb.append("/");
                sb.append(ServerUtils.ServiceName);
            } else {
                sb.append(url);
            }

            StringBuffer postData = new StringBuffer();
            addParam(postData, "action", action);
            addParam(postData, "format", "json");
            postData.append("&");

            postData.append(CommUtils.GetCommonParameterAndEncrypt(param));

            if (!TextUtils.isEmpty(noEncryptParam)) {
                postData.append("&");
                postData.append(noEncryptParam);
            }
            pResultBuf = getHttpDataUsePost(sb.toString(), postData.toString(), 30 * 1000, 1);
        } else {
            // 如果是静态页面，用get方式

            if (TextUtils.isEmpty(url)) {
                // 如果没有url，自己拼参数
                sb.append("http://");
                sb.append(ServerUtils.getInstance().getServerIpFromServer(useSingleAppserv));
                sb.append("/");
                sb.append(ServerUtils.ServiceName);
            } else {
                sb.append(url);
            }

            if (!sb.toString().contains("?")) {
                sb.append("?");
            } else {
                sb.append("&");
            }
           addParam(sb, "action", action);
            addParam(sb, "format", "json");
            sb.append("&");
            sb.append(CommUtils.GetCommonParameterAndEncrypt(param));

            pResultBuf = GetHttpData(sb.toString(), true);
        }
        return pResultBuf;
    }

    public static String byteToString(byte[] response) {
        String result = "";
        try {
            result = EncodingUtils.getString(response, APP.ENCODE_UTF);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            result = "";
        }
        return result;
    }

    public static void setThreadPriorityLow() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
    }

    // 添加参数
    public static void addParam(StringBuffer s, String key, String value) {
        String v = ToEncoder(value);
        String lastChar = null;
        String str = s.toString();
        int len = str.length();
        if (len > 0)
            lastChar = str.substring(len - 1, len);
        if (lastChar != null && !lastChar.equals("&"))
            s.append("&");
        s.append(key);
        s.append("=");
        s.append(v);
    }

    /*
	 * 通用参数方法
	 */
    public static String ToEncoder(String para) {
        String p = "";
        if (TextUtils.isEmpty(para)) {
            return p;
        } else {
            try {

                p = Uri.encode(para, APP.ENCODE_UTF);
                if (TextUtils.isEmpty(p))
                    return "";
                else
                    return p;
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                // LogUtils.ShowToast(AnyRadioApplication.mContext,
                // "oom ToEncoder",
                // Toast.LENGTH_SHORT);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return "";
    }



    public static byte[] getHttpDataUsePost(String url, String InputParam, int timerout, int repeatCount) {
        return getHttpDataUsePost(url, InputParam, timerout, repeatCount, false);
    }



    // post方式获取url对应的数据，可以传入参数，超时时间，重试次数
    public static byte[] getHttpDataUsePost(String url, String InputParam, int timerout, int repeatCount, boolean test) {
        boolean isMainThread = CommUtils.isMainThread();
        LogUtils.d("NET","isMainThread="+isMainThread);
        if(isMainThread){
            LogUtils.d("NET","主线程调用网络请求");
            return null;
        }
        LogUtils.d("netUtils getHttpDataUsePost action " + InputParam.substring(7, InputParam.indexOf("&")) + " ", url + "?" + InputParam);
        LogUtils.d("NET","netUtils getHttpDataUsePost " + url + "?" + InputParam);
        byte[] pResultBuf = null;
        int reConnectHttp = 0;
        boolean done = false;
        while (true) {
            if (USE_SOCKET) {
                pResultBuf = getHttpDataUseSocket(url, InputParam, timerout,
                        test);
                if (pResultBuf != null) {
                    done = true;
                    break;
                }
            } else {
                HttpURLConnection con = null;
                OutputStream os = null;
                DataOutputStream dos = null;
                long startTime = System.currentTimeMillis();
                try {
                    URL dataUrl = new URL(url);
                    con = (HttpURLConnection) dataUrl.openConnection();
                    con.setUseCaches(false);
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type",
                            "application/x-www-form-urlencoded");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.setConnectTimeout(timerout);
                    con.setReadTimeout(timerout);
                    os = con.getOutputStream();
                    dos = new DataOutputStream(os);
                    byte[] totalData = InputParam.getBytes();
                    ByteArrayInputStream bais = new ByteArrayInputStream(
                            totalData);
                    int total = totalData.length;
                    int cur = 0;
                    byte[] buff = new byte[10 * 1024];
                    int length;
                    isBreak = false;
                    while (!isBreak && (length = bais.read(buff)) != -1) {
                        cur += length;
                        dos.write(buff, 0, length);
                        dos.flush();
                        notifyAll(cur, total);
                    }
                    notifyAll(1, 1);
                    isBreak = false;
                    // dos.write(InputParam.getBytes());
                    int code = con.getResponseCode();

                    LogUtils.d("getBytesFromStream con.getContentLength(): ", con.getContentLength()+"");
                    // 由于服务器返回的content-length并不准确，忽略此参数，以实际收到为准。
                    int len = -1;

                    LogUtils.d("NetUtils getposturl code: " , code + " url: " + url + "?" + InputParam);

                    if ((code == 200)) {
                        InputStream is = con.getInputStream();
                        pResultBuf = getBytesFromStream(is, len);
                        done = true;
                        //addOffTime(System.currentTimeMillis() - startTime, 0, test, url, InputParam, "");
                        break;
                    }
                    //addOffTime(System.currentTimeMillis() - startTime, code, test, url, InputParam, "");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    //addOffTime(System.currentTimeMillis() - startTime, -1, test, url, InputParam, ex.toString());
                } finally {
                    if (dos != null)
                        try {
                            dos.close();
                        } catch (IOException e) {
                        }
                    if (os != null)
                        try {
                            os.close();
                        } catch (IOException e) {
                        }
                    if (con != null)
                        con.disconnect();

                }
            }
            reConnectHttp++;
            if (done || reConnectHttp >= repeatCount) {
                break;
            } else {
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                }
            }
        }

        return pResultBuf;
    }
    private static ArrayList<Handler> handlers;

    // public static boolean USE_PROXY = false;
    public static void addPostListner(Handler handler) {
        if (handlers == null) {
            handlers = new ArrayList<Handler>();
        }
        if (!handlers.contains(handler)) {
            handlers.add(handler);
        }
    }

    public static void removePostLisenter(Handler handler) {
        if (handlers == null) {
            return;
        }
        if (handlers.contains(handler)) {
            handlers.remove(handler);
        }
        if (handlers.size() <= 0) {
            handlers = null;
        }
    }
    public static final int MSG_POST_RATE = 95159;

    private static void notifyAll(int cur, int total) {
        if (handlers != null) {
            Message msg = new Message();
            msg.what = MSG_POST_RATE;
            msg.arg1 = cur;
            msg.arg2 = total;
            for (Handler h : handlers) {
                if (h != null) {
                    h.sendMessage(msg);
                }
            }
        }
    }

    // 获取url地址对应的数据，返回byte数组，默认超时时间30秒
    public static byte[] GetHttpData(String s, boolean isReplease) {
        return GetHttpData(s, 30000, false);
    }

    public static byte[] GetHttpData(String s, int timeout, boolean test) {
        AnyRadio_ItemPayload hd = GetHttpDataRange(s, timeout, false, -1, -1, test);
        if (hd != null)
            return hd.data;
        return null;
    }

    public static AnyRadio_ItemPayload GetHttpDataRange(String s, int timeout, boolean useRange, int rangeStart, int rangeLen, boolean test) {

        AnyRadio_ItemPayload ret = null;
        byte[] bs = null;
        int rangeSize = -1;
        HttpURLConnection hc = null;
        long startTime = System.currentTimeMillis();

        try {
            URL url = new URL(s);

            hc = (HttpURLConnection) url.openConnection();
            if (useRange) {
                String range = "bytes=" + rangeStart + "-";
                if (rangeLen > 0) {
                    range += rangeLen;
                }
                hc.setRequestProperty("Range", range);
            }
            hc.setConnectTimeout(timeout);
            hc.setReadTimeout(timeout);
            hc.setDoInput(true);
            hc.setInstanceFollowRedirects(true);
            int code = hc.getResponseCode();
            int len = hc.getContentLength();
            if (code == HttpURLConnection.HTTP_OK) {
                InputStream is = hc.getInputStream();
                bs = getBytesFromStream(is, len);
                is.close();
                //addOffTime(System.currentTimeMillis() - startTime, bs == null ? 1 : 0, test, s, "", "");
            } else if (code == HttpURLConnection.HTTP_PARTIAL) {
                if (rangeLen >= 0) {
                    String range = hc.getHeaderField("Content-Range");
                    if (!TextUtils.isEmpty(range)) {
                        String v[] = range.split("/");
                        if (v.length >= 2) {
                            rangeSize = CommUtils.convert2int(v[1]);
                        }
                    }
                }
                InputStream is = hc.getInputStream();
                bs = getBytesFromStream(is, len);
                is.close();
                //addOffTime(System.currentTimeMillis() - startTime,bs == null ? 1 : 0, test, s, "", "");
            } else {
                //addOffTime(System.currentTimeMillis() - startTime, code, test,s, "", "");
            }

        } catch (Exception e) {
            e.printStackTrace();
           // addOffTime(System.currentTimeMillis() - startTime, -1, test, s, "",e.toString());
        } finally {
            if (hc != null)
                hc.disconnect();
        }

        if (bs != null) {
            ret = new AnyRadio_ItemPayload();
            ret.data = bs;
            ret.rangeSize = rangeSize;
        }

        return ret;
    }

    public static byte[] getBytesFromStream(InputStream is, int len) {
        byte[] ret = null;
        int readSize = 0;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            if (READ_BUFFER) {
                int ch = 0;
                while ((ch = is.read()) != -1) {
                    readSize++;
                    baos.write(ch);
                    if (len > 0 && readSize >= len) {
                        LogUtils.d("getBytesFromStream ","readSize : "  + readSize + " len " + len);
                        break;
                    }
                }
            } else {
                byte[] buf = new byte[DefaultReadBufferSize];
                while (true) {
                    int readLen = is.read(buf, 0, buf.length);
                    if (readLen == -1) {
                        break;
                    }
                    readSize += readLen;
                    baos.write(buf, 0, readLen);
                    if (len > 0 && readSize >= len) {
                        LogUtils.d("getBytesFromStream ","readSize : "  + readSize + " len " + len);
                        break;
                    }
                }
                buf = null;
            }
            ret = baos.toByteArray();
        } catch (Exception e) {
            LogUtils.d("getBytesFromStream ","readSize : "  + readSize + " len " + len);
        } catch (OutOfMemoryError e) {
            LogUtils.d("getBytesFromStream ","readSize : "  + readSize + " len " + len);
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
            }
        }

        if (ret != null) {
            LogUtils.d("getBytesFromStream " , byteToString(ret));
        }

        LogUtils.d("getBytesFromStream ","len :" + len + " readSize " + readSize + " ret: " + ret.length);
        return ret;
    }

    public static byte[] getHttpDataUseSocket(String url, String postData, int timerout, boolean test) {
        byte[] ret = null;
        Socket socket = null;
        OutputStream os = null;
        InputStream ins = null;
        long startTime = System.currentTimeMillis();
        try {
            URL u = new URL(url);
            int port = u.getPort();
            if (port < 0)
                port = 80;
            String host = u.getHost();
            String path = u.getPath();
            String query = u.getQuery();

            InetAddress2 serverAddr = null;
            serverAddr = InetAddress2.getByName(host);
            socket = new Socket();
            InetSocketAddress isa = null;
            isa = new InetSocketAddress(serverAddr.getHostAddress(), port);

            LogUtils.d("getHttpDataUseSocket socket.connect: " , url + postData);
            socket.connect(isa, timerout);

            socket.setSoTimeout(timerout);
            socket.setKeepAlive(true);

            boolean usePost = !TextUtils.isEmpty(postData);

            os = socket.getOutputStream();
            ins = socket.getInputStream();

            StringBuffer sb = new StringBuffer();
            if (usePost)
                sb.append("POST ");
            else
                sb.append("GET ");
            if (TextUtils.isEmpty(path)) {
                sb.append("/");
            } else {
                sb.append(path);
            }
            if (!TextUtils.isEmpty(query)) {
                sb.append("?");
                sb.append(query);
            }

            sb.append(" HTTP/1.0\r\n");
            sb.append("Host:");
            sb.append(host);
            sb.append(":");
            sb.append(port);
            sb.append("\r\n");
            sb.append("Accept: */*");
            sb.append("\r\n");
            sb.append("Accept-Language: zh-cn");
            sb.append("\r\n");
            sb.append("User-Agent: Mozilla/4.0 (compatible; MSIE 5.00; Windows 98)");
            sb.append("\r\n");
            sb.append("Content-Type: application/x-www-form-urlencoded");
            sb.append("\r\n");

            sb.append("Connection: Keep-Alive\r\n");
            if (usePost) {
                sb.append("Content-Length: ");
                sb.append(postData.length());
                sb.append("\r\n");
            }
            sb.append("\r\n");
            if (usePost)
                sb.append(postData);

            LogUtils.d("getHttpDataUseSocket sendData: ", sb.toString());

            os.write(sb.toString().getBytes());
            os.flush();

            ret = getBytesFromStreamUseSocket(ins);

            //addOffTime(System.currentTimeMillis() - startTime, ret == null ? 1 : 0, test, url, postData, "");

        } catch (Exception e) {
            e.printStackTrace();
            //addOffTime(System.currentTimeMillis() - startTime, -1, test, url, postData, e.toString());
        } finally {
            try {
                if (ins != null)
                    ins.close();
                if (os != null)
                    os.close();
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
            }
        }
        return ret;
    }

    private static byte[] getBytesFromStreamUseSocket(InputStream is) {
        byte[] ret = null;

        int readSize = 0;
        int len = -1;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            int headIndex = -1;
            int resCode = -1;
            byte[] buf = new byte[DefaultReadBufferSize];
            int readLen = 0;
            while (true) {
                if (READ_BUFFER) {
                    int ch = 0;
                    if ((ch = is.read()) != -1) {
                        readSize++;
                        baos.write(ch);

                    } else {
                        break;
                    }
                } else {
                    readLen = is.read(buf, 0, buf.length);
                    if (readLen == -1) {
                        break;
                    }
                    readSize += readLen;
                    baos.write(buf, 0, readLen);
                }



                if (headIndex == -1) {
                    if (readSize > 20) {
                        String s = new String(baos.toByteArray());
                        headIndex = checkHeaderEndPos(s);
                    }
                }
                if (resCode == -1 && headIndex > 0) {
                    String s = new String(baos.toByteArray());
                    resCode = getResCode(s);
                }
                if (len == -1 && headIndex > 0) {
                    String s = new String(baos.toByteArray());
                    len = getContentLength(s);
                }

                if (len > 0 && readSize >= len + headIndex) {
                    break;
                }
            }

            byte[] data = baos.toByteArray();


            int bodyLen = readSize - headIndex;
            if (headIndex > 0 && bodyLen > 0 && resCode == 200) {
                ret = new byte[bodyLen];
                System.arraycopy(data, headIndex, ret, 0, bodyLen);
            }

            baos.close();
            baos = null;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    private static int checkHeaderEndPos(String s) {
        int ret = -1;
        int off = 0;
        int i = s.indexOf("\r\n\r\n");
        if (i < 0) {
            i = s.indexOf("\n\n");
            if (i < 0) {
                i = s.indexOf("\r\r");
            }
            off = 2;
        } else {
            off = 4;
        }

        if (i > 0) {
            ret = i + off;
            LogUtils.d("getHttpDataUseSocket"," checkHeaderEndPos "+ret);
        }

        return ret;
    }

    private static int getResCode(String s) {
        int ret = -1;
        if (s.length() > 0) {
            String str = s.toLowerCase();
            int i = str.indexOf("http/1.1");
            if (i == 0) {
                String v[] = str.split(" ");
                if (v.length >= 2) {
                    ret = CommUtils.convert2int(v[1]);
                    LogUtils.d("getHttpDataUseSocket ","getResCode " + ret);
                }
            }
        }
        return ret;
    }

    private static int getContentLength(String s) {
        int ret = -1;
        if (s.length() > 0) {
            String str = s.toLowerCase();
            String line[] = str.split("\n");
            for (int i = 0; i < line.length; i++) {
                if (line[i].contains("content-length")) {
                    String nv[] = line[i].split(":");
                    if (nv.length > 1) {
                        ret = CommUtils.convert2int(nv[1].trim());
                        LogUtils.d("getHttpDataUseSocket","getContentLength " + ret);
                    }
                }
            }
        }
        return ret;
    }



}

package cn.anyradio.utils;

oneway interface IRemotePlayServiceCallback {
    void playStateChanged(int what, int arg1, int arg2);
    void playInfoChanged(int what, String value);
}

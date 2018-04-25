package cn.anyradio.utils;

import cn.anyradio.utils.IRemotePlayServiceCallback;

interface IRemotePlayService {
    void registerCallback(IRemotePlayServiceCallback cb);
    void unregisterCallback(IRemotePlayServiceCallback cb);
}

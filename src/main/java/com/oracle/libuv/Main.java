package com.oracle.libuv;

import static com.oracle.libuv.handles.DefaultHandleFactory.newFactory;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.oracle.libuv.cb.AsyncCallback;
import com.oracle.libuv.cb.StreamReadCallback;
import com.oracle.libuv.handles.AsyncHandle;
import com.oracle.libuv.handles.HandleFactory;
import com.oracle.libuv.handles.LoopHandle;
import com.oracle.libuv.handles.PipeHandle;
import com.oracle.libuv.handles.ProcessHandle;
import com.oracle.libuv.handles.StdioOptions;

public class Main {

    public static void main(String[] args) throws Throwable {
        LibUV.loadJni();
        final HandleFactory handleFactory = newFactory();
        final LoopHandle loop = handleFactory.getLoopHandle();

        ProcessHandle process = handleFactory.newProcessHandle();
        
        PipeHandle inPipe = handleFactory.newPipeHandle(false);
        PipeHandle outPipe = handleFactory.newPipeHandle(false);

        outPipe.setReadCallback(new StreamReadCallback() {

            @Override
            public void onRead(ByteBuffer data) throws Exception {
                byte[] arr = new byte[data.remaining()];
                data.get(arr);
                System.out.println(new String(arr));
            }
        });

        AsyncHandle async = handleFactory.newAsyncHandle();
        async.setAsyncCallback(new AsyncCallback() {

            @Override
            public void onSend(int status) throws Exception {
                String cmd = "{\"id\":1,\"method\":\"Target.setDiscoverTargets\",\"params\":{\"discover\":true}}\0";
                inPipe.write(cmd);
                async.close();
            }
        });

        ScheduledExecutorService timer = new ScheduledThreadPoolExecutor(1);

        timer.schedule(new Runnable() {
            @Override
            public void run() {
                outPipe.readStart();
                System.out.println(outPipe.isClosing());
                System.out.println(outPipe.isClosing());
                async.send();
            }
        }, 2000, TimeUnit.MILLISECONDS);


        final StdioOptions[] stdio = new StdioOptions[5];
        stdio[0] = new StdioOptions(StdioOptions.StdioType.INHERIT_FD, null, 0);
        stdio[1] = new StdioOptions(StdioOptions.StdioType.INHERIT_FD, null, 1);
        stdio[2] = new StdioOptions(StdioOptions.StdioType.INHERIT_FD, null, 2);
        stdio[3] = new StdioOptions(StdioOptions.StdioType.CREATE_PIPE, inPipe, 3);
        stdio[4] = new StdioOptions(StdioOptions.StdioType.CREATE_PIPE, outPipe, 4);

        final EnumSet<ProcessHandle.ProcessFlags> flags = EnumSet.noneOf(ProcessHandle.ProcessFlags.class);
        flags.add(ProcessHandle.ProcessFlags.WINDOWS_VERBATIM_ARGUMENTS);

        process.spawn("chrome.exe", new String[] { "chrome.exe", "--remote-debugging-pipe" }, new String[] { }, "C:\\Program Files (x86)\\Google\\Chrome\\Application", flags, stdio, -1, -1);

        loop.run();
    }
}

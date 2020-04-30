package com.edu.vip.mynio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author zhangxiaofeng
 * @Describetion
 * @date 2020/4/3017:15
 */
public class NIOServerDemo {
    private int port;
    // 轮询器
    private Selector selector;
    // 缓冲区
    private ByteBuffer buffer;
    public NIOServerDemo(int port) {
        try {
            this.port = port;
            ServerSocketChannel server = ServerSocketChannel.open();
            server.bind(new InetSocketAddress(this.port));
            // 默认采用阻塞式，设置为非阻塞
            server.configureBlocking(false);
            selector = Selector.open();
            server.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        System.out.println("listen on "+this.port);

        try {
            while (true) {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                // 迭代，同步体现在这里，因为每次只能拿一个key，每次只能处理一种状态
                while (iterator.hasNext()) {
                    SelectionKey next = iterator.next();
                    iterator.remove();
                    process(next);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void process(SelectionKey key) throws IOException {
        //针对每一种状态给一个反应
        if (key.isAcceptable()) {
            ServerSocketChannel channel = (ServerSocketChannel) key.channel();
            channel.accept();
            channel.configureBlocking(false);
            // 当数据准备就绪的时候，将状态改为可读
            key = channel.register(selector, SelectionKey.OP_READ);

        } else if (key.isReadable()) {
            SocketChannel channel = (SocketChannel) key.channel();
            int len = channel.read(buffer);
            if (len > 0) {
                buffer.flip();
                String s = new String(buffer.array(), 0,len);
                key = channel.register(selector, SelectionKey.OP_WRITE);
                // 在key上携带一个附件，一会再写出去
                key.attach(s);
                System.out.println("接收到的结果是：" + s);
            }
        } else if (key.isWritable()) {
            SocketChannel channel = (SocketChannel) key.channel();
            String attachment = (String) key.attachment();
            channel.write(ByteBuffer.wrap(("输出：" + attachment).getBytes()));
            channel.close();
        }
    }

    public static void main(String[] args) {
        new NIOServerDemo(8080).listen();
    }
}

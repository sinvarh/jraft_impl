/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package main.rpc;

import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.RpcClient;
import main.model.rpc.common.RaftRpcReq;

/**
 * a demo for rpc client, you can just run the main method after started rpc server of {@link }
 *
 * @author tsui
 * @version $Id: RpcClientDemoByMain.java, v 0.1 2018-04-10 10:39 tsui Exp $
 */
public class RaftRpcClient {

    static RpcClient client;

    static String             addr                      = "127.0.0.1:8999";

    RaftServerUsersProcessor raftServerUsersProcessor = new RaftServerUsersProcessor();

    public RaftRpcClient() {
        // 1. create a rpc client
        client = new RpcClient();
        // 2. add processor for connect and close event if you need
        client.registerUserProcessor(raftServerUsersProcessor);
        // 3. do init
        client.startup();
    }

    public static void main(String[] args) throws RemotingException, InterruptedException {
        new RaftRpcClient();
        RaftRpcReq<String > req = new RaftRpcReq<>("hello world sync");

        String res = (String) client.invokeSync(addr, req, 3000);
        System.out.println("invoke sync result = [" + res + "]");

        client.shutdown();
    }
}

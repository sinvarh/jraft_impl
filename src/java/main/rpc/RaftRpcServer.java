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


/**
 * a demo for rpc server, you can just run the main method to start a server
 *
 * @author tsui
 * @version $Id: RpcServerDemoByMain.java, v 0.1 2018-04-10 10:37 tsui Exp $
 */
public class RaftRpcServer {

    BoltServer                server;
    int                       port                      = 8999;
    RaftServerUsersProcessor raftServerUsersProcessor = new RaftServerUsersProcessor();

    public RaftRpcServer() {
        // 1. create a Rpc server with port assigned
        server = new BoltServer(port);
        // 3. register user processor for client request
        server.registerUserProcessor(raftServerUsersProcessor);
        // 4. server start
        if (server.start()) {
            System.out.println("server start ok!");
        } else {
            System.out.println("server start failed!");
        }
        // server.getRpcServer().stop();
    }

    public static void main(String[] args) {
        new RaftRpcServer();
    }
}

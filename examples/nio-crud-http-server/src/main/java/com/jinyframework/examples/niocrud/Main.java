package com.jinyframework.examples.niocrud;

import com.google.gson.Gson;
import com.jinyframework.NIOHttpServer;
import com.jinyframework.core.RequestBinderBase.HttpResponse;
import com.jinyframework.examples.niocrud.router.CatRouter;
import lombok.val;

import java.io.IOException;
import java.util.Random;

public final class Main {
    public static void main(String[] args) throws IOException {
        val server = NIOHttpServer.port(1234);
        server.useTransformer(res -> new Gson().toJson(res));

        server.get("/", ctx -> HttpResponse.ofAsync("Hello World!"));
        server.get("/random", ctx -> HttpResponse.ofAsync(new Random().nextInt()));
        val catRouter = CatRouter.getRouter();
        server.use("/cat", catRouter);

        server.start();
    }
}
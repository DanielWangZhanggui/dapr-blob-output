package org.example;

import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import io.dapr.client.domain.InvokeBindingRequest;
import io.dapr.utils.TypeRef;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@EnableAutoConfiguration
public class SpringBlobTest {

    private String BINDING_NAME = "blobstoragebingding";
    private String BINDING_OPERATION = "create";
    private String STORE_NAME = "blobstoragestore";
    private DaprClient client = new DaprClientBuilder().build();
    @RequestMapping("/bindings")
    public String createBindings()
    {
        createBlobBingding();
        return "success";
    }
    @RequestMapping("/states")
    public String index()
    {
        saveBlobtoStore();
        return "success";
    }
    public boolean createBlobBingding(){
        InvokeBindingRequest invokeBindingRequest = new InvokeBindingRequest(BINDING_NAME, BINDING_OPERATION);
        Map<String, String> metaData = new HashMap<String, String>();
        metaData.put("blobName", "file1");
        invokeBindingRequest.setMetadata(metaData);
        invokeBindingRequest.setData("test");
        client.invokeBinding(BINDING_NAME, BINDING_OPERATION, "test").block();
        client.invokeBinding(invokeBindingRequest, TypeRef.BYTE_ARRAY).block();
        return true;
    }
    public boolean saveBlobtoStore(){
        client.saveState(STORE_NAME, "Test", "Test1").block();
        return true;
    }
    public static void main(String [] args)
    {
        SpringApplication.run(SpringBlobTest.class, args);
    }
}

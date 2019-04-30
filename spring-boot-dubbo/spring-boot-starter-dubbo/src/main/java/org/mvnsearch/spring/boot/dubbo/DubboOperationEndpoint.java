package org.mvnsearch.spring.boot.dubbo;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.registry.Registry;
import com.alibaba.dubbo.registry.RegistryFactory;
import org.mvnsearch.spring.boot.dubbo.listener.ProviderExportListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.boot.actuate.endpoint.mvc.MvcEndpoint;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * dubbo operation endpoint
 *
 * @author alex
 */
@RestController
public class DubboOperationEndpoint implements MvcEndpoint {
    @Autowired
    private DubboProperties properties;
    private Registry registry;
    public static Boolean OFFLINE=false;

    @PostConstruct
    public void init() {
        ExtensionLoader<RegistryFactory> extensionLoader = ExtensionLoader.getExtensionLoader(RegistryFactory.class);
        URL url = URL.valueOf(properties.getRegistry());
        RegistryFactory registryFactory = extensionLoader.getExtension(url.getProtocol());
        registry = registryFactory.getRegistry(url);
    }

    @RequestMapping("/offline")
    public String offline() {
        ProtocolConfig.destroyAll();
        OFFLINE = true;
        return "sucess";
    }

    public String getPath() {
        return "dubbo";
    }

    public boolean isSensitive() {
        return false;
    }

    public Class<? extends Endpoint> getEndpointType() {
        return null;
    }
}

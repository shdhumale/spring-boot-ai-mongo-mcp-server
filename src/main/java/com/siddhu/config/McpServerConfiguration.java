package com.siddhu.config;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.siddhu.service.MCPServerMongoServiceClient;

@Configuration
public class McpServerConfiguration {

    @Bean
    public ToolCallbackProvider mongoTools(MCPServerMongoServiceClient mongoServiceClient) {
        return MethodToolCallbackProvider.builder().toolObjects(mongoServiceClient).build();
    }
}
